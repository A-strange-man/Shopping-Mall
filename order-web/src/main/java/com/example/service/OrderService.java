package com.example.service;

import com.example.response.JsonData;

/**
 * @author CaoJing
 * @date 2021/07/28 15:45
 */
public interface OrderService {

    /**
     * 提交订单
     * @param id
     * @return
     */
    JsonData addOrder(Integer id);
}
