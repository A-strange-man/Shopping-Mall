package com.example.controller;

import com.example.response.JsonData;
import com.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author CaoJing
 * @date 2021/07/28 15:41
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 提交订单
     * @param id 购物车ID
     * @return
     */
    @RequestMapping("/add/v1")
    public JsonData addOrder(@RequestParam("id") Integer id) {

        return orderService.addOrder(id);
    }
}
