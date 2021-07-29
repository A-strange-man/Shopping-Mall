package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Goods;
import com.example.entity.ShoppingCart;
import com.example.mapper.GoodsMapper;
import com.example.mapper.ShoppingCartMapper;
import com.example.request.AddShoppingCartRequest;
import com.example.request.ListShoppingCartRequest;
import com.example.response.JsonData;
import com.example.response.PageInfo;
import com.example.service.ShoppingCartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * @author CaoJing
 * @date 2021/07/28 13:47
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private static final Logger log = LoggerFactory.getLogger(ShoppingCartServiceImpl.class);

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Override
    @Transactional
    public JsonData addShoppingCart(AddShoppingCartRequest request) {

        BigInteger buyerId = request.getBuyerId();
        BigInteger goodsId = request.getGoodsId();
        Integer buyNumber = request.getBuyNumber();

        // 根据商品id获取商品信息
        Goods goods = goodsMapper.selectById(goodsId);
        if (goods == null) {
            return JsonData.buildError(5000, "未找到该商品");
        }

        // 先查询购物车中是否已经存在该商品
        QueryWrapper<ShoppingCart> wrapper = new QueryWrapper<>();
        wrapper.eq("goods_id", goodsId).eq("buy_id", buyerId);
        ShoppingCart shoppingCart = shoppingCartMapper.selectOne(wrapper);

        if (shoppingCart == null) {
            // 购物车中没有该商品,添加到购物车
            shoppingCart = new ShoppingCart();
            shoppingCart.setBuyId(buyerId);
            shoppingCart.setGoodsId(goodsId);
            shoppingCart.setGoodsName(goods.getGoodsName());
            shoppingCart.setGoodsNumber(buyNumber);
            // 计算总价格
            BigDecimal totalPrice = goods.getGoodsPrice().multiply(BigDecimal.valueOf(buyNumber));
            shoppingCart.setGoodsPrice(totalPrice);
            shoppingCart.setDescription("");
            int rows = shoppingCartMapper.insert(shoppingCart);
            if (rows != 1) {
                log.error("-添加购物车 - 出现异常 - 商品：{}", shoppingCart.toString());
                return JsonData.buildError(5000, "添加购物车出现异常");
            }

        } else {
            // 购物车中已存在该商品，则数量 +1
            BigInteger id = shoppingCart.getId();
            UpdateWrapper<ShoppingCart> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", id);

            int buyNum = shoppingCart.getGoodsNumber();
            buyNum += 1;
            // 重新计算价格
            BigDecimal totalPrice = goods.getGoodsPrice().multiply(BigDecimal.valueOf(buyNum));
            shoppingCart.setGoodsNumber(buyNum);
            shoppingCart.setGoodsPrice(totalPrice);

            int rows = shoppingCartMapper.update(shoppingCart, updateWrapper);
            if (rows != 1) {
                log.error("-添加购物车 - 增加商品数量 - 出现异常 - 商品：{}", shoppingCart.toString());
                return JsonData.buildError(5000, "添加购物车出现异常");
            }
        }

        return JsonData.buildSuccess();
    }


    @Override
    public JsonData listShoppingCart(ListShoppingCartRequest request) {

        BigInteger buyerId = request.getBuyerId();
        Integer index = request.getPageIndex();
        Integer size = request.getPageSize();

        Page<ShoppingCart> page = new Page<>(index, size);
        QueryWrapper<ShoppingCart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("buy_id", buyerId);

        IPage<ShoppingCart> iPage = shoppingCartMapper.selectPage(page, queryWrapper);
        if (iPage == null) {
            return JsonData.buildError(5000, "未查询到购物车信息");
        }

        List<ShoppingCart> cartList = iPage.getRecords();
        PageInfo<ShoppingCart> pageInfo = new PageInfo<>();
        // 获取-设置 商品列表
        pageInfo.setRecords(cartList);
        // 获取-设置 总页数
        pageInfo.setPageTotal(iPage.getPages());
        // 获取-设置 数据总条数
        pageInfo.setTotalNum(iPage.getTotal());
        // 设置 - 当前页码
        pageInfo.setPageIndex(iPage.getCurrent());

        return JsonData.buildSuccess(pageInfo);
    }
}
