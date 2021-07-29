package com.example.service;

import com.example.request.ListGoodsRequest;
import com.example.response.JsonData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigInteger;

/**
 * @author CaoJing
 * @date 2021/07/28 8:51
 */
@FeignClient(name = "order-admin")
public interface CallMethod {

    @PostMapping(value = "/api/goods/list/v1/admin")
    JsonData findByName(@RequestBody ListGoodsRequest request);

    @GetMapping(value = "/api/goods/getById/v1/admin")
    JsonData findById(@RequestParam("goodsId")BigInteger goodsId);
}
