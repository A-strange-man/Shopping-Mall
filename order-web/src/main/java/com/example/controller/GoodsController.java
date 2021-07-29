package com.example.controller;

import com.example.request.ListGoodsRequest;
import com.example.response.JsonData;
import com.example.service.GoodsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

/**
 * @author CaoJing
 * @date 2021/07/27 17:10
 */
@RestController
@RequestMapping("/api/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 根据查找关键字查找商品列表
     * @param request
     * @return
     * @throws JsonProcessingException
     */
    @GetMapping("/list/v1")
    public JsonData listGoods(@RequestBody ListGoodsRequest request) throws JsonProcessingException {

        return goodsService.findGoods(request);
    }


    /**
     * 根据商品id精准查找
     * @param goodsId
     * @return
     */
    @GetMapping("/getById/v1")
    public JsonData findById(@RequestParam("goodsId") BigInteger goodsId) throws JsonProcessingException {

        return goodsService.findGoodsById(goodsId);
    }
}
