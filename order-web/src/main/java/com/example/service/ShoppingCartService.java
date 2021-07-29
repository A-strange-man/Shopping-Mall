package com.example.service;

import com.example.request.AddShoppingCartRequest;
import com.example.request.ListShoppingCartRequest;
import com.example.response.JsonData;

/**
 * @author CaoJing
 * @date 2021/07/28 13:46
 */
public interface ShoppingCartService {

    /**
     * 用户添加购物车
     * @param request
     * @return
     */
    JsonData addShoppingCart(AddShoppingCartRequest request);

    /**
     * 用户查询购物车
     * @param request
     * @return
     */
    JsonData listShoppingCart(ListShoppingCartRequest request);
}
