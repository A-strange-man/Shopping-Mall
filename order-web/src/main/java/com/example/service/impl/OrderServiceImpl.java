package com.example.service.impl;

import com.example.entity.Goods;
import com.example.entity.MyOrder;
import com.example.entity.ShoppingCart;
import com.example.mapper.GoodsMapper;
import com.example.mapper.OrderMapper;
import com.example.mapper.ShoppingCartMapper;
import com.example.response.JsonData;
import com.example.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author CaoJing
 * @date 2021/07/28 15:45
 */
@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    @Transactional
    public JsonData addOrder(Integer id) {

        // 从购物车查询到对应的商品及数量
        ShoppingCart shoppingCart = shoppingCartMapper.selectById(id);
        if (shoppingCart == null) {
            return JsonData.buildError(5000, "未查询到该购物车");
        }

        // 根据商品id从商品表中查询商品
        Goods goods = goodsMapper.selectById(shoppingCart.getGoodsId());
        // 库存小于购买量，下单失败
        if (goods.getGoodsStockNumber() < shoppingCart.getGoodsNumber()) {
            return JsonData.buildError(5000, "库存不足");
        }

        // 创建订单
        MyOrder newMyOrder = new MyOrder();
        newMyOrder.setBuyId(shoppingCart.getBuyId());
        newMyOrder.setSellerId(goods.getSellerId());
        newMyOrder.setGoodsId(goods.getId());
        newMyOrder.setGoodsName(goods.getGoodsName());
        newMyOrder.setGoodsPrice(shoppingCart.getGoodsPrice());
        newMyOrder.setGoodsNumber(shoppingCart.getGoodsNumber());
        newMyOrder.setPayStatus(1);

        int rows = orderMapper.insert(newMyOrder);
        if (rows != 1) {
            return JsonData.buildError(5000, "创建订单失败");
        }

        // 删除购物车
        shoppingCartMapper.deleteById(id);

        return JsonData.buildSuccess();
    }
}
