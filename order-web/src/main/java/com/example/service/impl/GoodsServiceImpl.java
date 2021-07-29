package com.example.service.impl;

import com.example.entity.Goods;
import com.example.mapper.GoodsMapper;
import com.example.publicUtil.StrPares;
import com.example.request.ListGoodsRequest;
import com.example.response.JsonData;
import com.example.service.CallMethod;
import com.example.service.GoodsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

/**
 * @author CaoJing
 * @date 2021/07/27 17:42
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    // redis缓存中商品的 key 统一前缀
    private static final String keyPrefix = "goods:tec_14536:";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CallMethod callMethod;

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public JsonData findGoods(ListGoodsRequest request) throws JsonProcessingException {

        String queryKey = request.getQueryKey();
        // 判断是否是纯数字
        if (StrPares.isNum(queryKey)) {
            // 是纯数字，在redis中做查询
            String key = keyPrefix + queryKey;
            Object obj =  redisTemplate.opsForValue().get(key);
            if (obj != null) {
                Goods goods = objectMapper.readValue(obj.toString(), Goods.class);
                return JsonData.buildSuccess(goods);
            }

        } else {
            // 不是纯数字，调用admin服务接口做数据库查询, 根据名字模糊查询
            return callMethod.findByName(request);
        }

        // 是纯数字，但缓存中没有，调用admin服务接口做商品id查询
        BigInteger id = StrPares.getNum(queryKey);
        return callMethod.findById(id);
    }


    @Override
    public JsonData findGoodsById(BigInteger goodsId) throws JsonProcessingException {
        Goods goods = null;
        String key = keyPrefix + goodsId;
        // 先缓存查询, 查到则直接返回
        Object obj = redisTemplate.opsForValue().get(key);
        if (obj != null) {
            if (obj == "") {
                return JsonData.buildError(5000, "未查询到该商品");
            }
            goods = objectMapper.readValue(obj.toString(), Goods.class);
            return JsonData.buildSuccess(goods);
        }

        // 缓存中没有查询到，则查询数据库
        goods = goodsMapper.selectById(goodsId);
        if (goods == null) {
            // 防止缓存穿透，在缓存放个空串
            redisTemplate.opsForValue().set(key, "");
            return JsonData.buildError(5000, "未查询到该商品");
        } else {
            storeInCache(key, goods);
            return JsonData.buildSuccess(goods);
        }
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
}
