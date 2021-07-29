package com.example.controller;

import com.example.request.AddShoppingCartRequest;
import com.example.request.ListShoppingCartRequest;
import com.example.response.JsonData;
import com.example.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author CaoJing
 * @date 2021/07/28 13:36
 */
@RestController
@RequestMapping("/api/shopping/cart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 用户添加商品到购物车
     * @param request
     * @return
     */
    @RequestMapping("/add/v1")
    public JsonData addShoppingCart(@RequestBody AddShoppingCartRequest request) {

        return shoppingCartService.addShoppingCart(request);
    }

    /**
     * 用户查询购物车信息
     * @param request
     * @return
     */
    @RequestMapping("/list/v1")
    public JsonData listShoppingCart(@RequestBody ListShoppingCartRequest request) {

        return shoppingCartService.listShoppingCart(request);
    }
}
