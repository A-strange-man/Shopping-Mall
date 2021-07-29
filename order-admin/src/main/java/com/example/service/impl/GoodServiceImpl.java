package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Goods;
import com.example.mapper.GoodsMapper;
import com.example.request.AddGoodsRequest;
import com.example.request.FindBySellerIdRequest;
import com.example.request.ListGoodsRequest;
import com.example.response.JsonData;
import com.example.response.PageInfo;
import com.example.service.GoodsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author CaoJing
 * @date 2021/07/28 9:48
 */
@Service
public class GoodServiceImpl implements GoodsService {

    // 日志工具
    private static final Logger log = LoggerFactory.getLogger(GoodServiceImpl.class);

    // 商品插入redis的统一前缀, 最后拼接商品id组成完整key
    private static final String keyPrefix = "goods:tec_14536:";

    // 商家id 的 key 前缀
    private static final String sellerKeyPrefix = "seller:tec_14536:";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Environment env;

    /**
     * 根据商品关键字 匹配商品名 进行模糊查询
     * 对查询结果进行分页处理
     * @param request
     * @return
     * @throws JsonProcessingException
     */
    @Override
    public JsonData findGoodsByName(ListGoodsRequest request) throws JsonProcessingException {

        Integer index = request.getPageIndex();
        Integer size = request.getPageSize();
        String queryKey = request.getQueryKey();

        Page<Goods> page = new Page<>(index, size);
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("goods_name", queryKey);

        IPage<Goods> iPage = goodsMapper.selectPage(page, queryWrapper);

        PageInfo<Goods> pageInfo = new PageInfo<>();
        List<Goods> goodsList = iPage.getRecords();

        if (goodsList == null || goodsList.isEmpty()) {
            return JsonData.buildError(5000, "未查询到相关商品");
        }

        // 获取-设置 商品列表
        pageInfo.setRecords(goodsList);
        // 商品信息放入缓存
        for (Goods goods : goodsList) {
            String key = keyPrefix + goods.getId();
            storeInCache(key, goods);
        }
        // 获取-设置 总页数
        pageInfo.setPageTotal(iPage.getPages());
        // 获取-设置 数据总条数
        pageInfo.setTotalNum(iPage.getTotal());
        // 设置 - 当前页码
        pageInfo.setPageIndex(iPage.getCurrent());

        return JsonData.buildSuccess(pageInfo);
    }


    /**
     * 商品放入缓存 - 有效期 12h
     * @param key
     * @param goods
     */
    private void storeInCache(String key, Goods goods) throws JsonProcessingException {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(key, objectMapper.writeValueAsString(goods), 12, TimeUnit.HOURS);
    }


    /**
     * 根据商品id进行精确查找
     * @param goodsId
     * @return
     */
    @Override
    public JsonData findGoodsById(BigInteger goodsId) throws JsonProcessingException {
        Goods goods = null;
        goods = goodsMapper.selectById(goodsId);

        if (goods == null) {
            return JsonData.buildError(5000, "未查询到该商品");
        } else {
            // 放入缓存
            String key = keyPrefix + goodsId;
            storeInCache(key, goods);
            return JsonData.buildSuccess(goods);
        }
    }

    /**
     * 添加商品
     * @param request
     * @return
     */
    @Override
    public JsonData addGoods(AddGoodsRequest request) {

        Goods newGoods = new Goods();
        newGoods.setSellerId(request.getSellerId());
        newGoods.setGoodsName(request.getGoodsName());
        newGoods.setGoodsPrice(request.getGoodsPrice());
        newGoods.setGoodsStockNumber(request.getStockNumber());
        newGoods.setGoodsDescription(request.getDescription());

        int rows = goodsMapper.insert(newGoods);
        if (rows != 1) {
            return JsonData.buildError(5000, "添加商品失败");
        }

        return JsonData.buildSuccess();
    }

    /**
     * 根据商家id查找商品
     * @param request
     * @return
     */
    @Override
    public JsonData findGoodsBySellerId(FindBySellerIdRequest request) throws JsonProcessingException {

        String key = sellerKeyPrefix + request.getSellerId();

        List<Goods> goodsList = new ArrayList<>();

        Integer index = request.getPageIndex();
        Integer size = request.getPageSize();
        BigInteger sellerId = request.getSellerId();

        PageInfo<Goods> pageInfo = new PageInfo<>();

        // 先查询缓存
        ListOperations<String, Object> ops = redisTemplate.opsForList();
        Object res = ops.rightPop(key);
        Goods goods;
        while (res != null) {
            goods = objectMapper.readValue(res.toString(), Goods.class);
            goodsList.add(goods);
            res = ops.rightPop(key);
        }

        // 缓存中没有，则查询数据库
        if (goodsList.isEmpty()) {
            Page<Goods> page = new Page<>(index, size);
            QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("seller_id", sellerId);
            Page<Goods> goodsPage = goodsMapper.selectPage(page, queryWrapper);

            goodsList = goodsPage.getRecords();
            if (goodsList == null || goodsList.isEmpty()) {
                return JsonData.buildError(5000, "未查询到相关商品");
            }

            // 获取-设置 商品列表
            pageInfo.setRecords(goodsList);
            // 获取-设置 总页数
            pageInfo.setPageTotal(goodsPage.getPages());
            // 获取-设置 数据总条数
            pageInfo.setTotalNum(goodsPage.getTotal());
            // 设置 - 当前页码
            pageInfo.setPageIndex(goodsPage.getCurrent());

            // 卖家-商品 信息添加到缓存
            for (Goods g : goodsList) {
                String content = objectMapper.writeValueAsString(g);
                ops.leftPush(key, content);
            }

            return JsonData.buildSuccess(pageInfo);
        }

        return JsonData.buildError(5000, "未知错误");
    }

    /**
     * 根据商品id删除商品
     * @param id
     * @return
     */
    @Override
    public JsonData deleteByGoodsId(BigInteger id) {
        if (id == null) {
            return JsonData.buildError(5000, "数据错误");
        }

        try {
            rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
            rabbitTemplate.setExchange(env.getProperty("mq.direct.exchange.name"));
            rabbitTemplate.setRoutingKey(Objects.requireNonNull(env.getProperty("mq.direct.routing.key.one.name")));
            rabbitTemplate.convertAndSend(id);

        } catch (Exception e) {
            log.error("消息模型-DirectExchange-two-生产者-发送消息异常：{}", id, e.fillInStackTrace());
            return JsonData.buildError(5000, "删除失败，未知异常");
        }

        // 删除数据库中的商品信息
        int rows = goodsMapper.deleteById(id);
        if (rows != 1) {
            log.error("删除商品信息-出现异常-goodsId={}", id);
        }

        return JsonData.buildSuccess();
    }
}
