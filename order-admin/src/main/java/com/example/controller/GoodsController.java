package com.example.controller;

import com.example.request.AddGoodsRequest;
import com.example.request.FindBySellerIdRequest;
import com.example.request.ListGoodsRequest;
import com.example.response.JsonData;
import com.example.service.GoodsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

/**
 * @author CaoJing
 * @date 2021/07/27 16:17
 */
@RestController
@RequestMapping("/api/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 根据关键字 匹配商品名 进行模糊查询
     * @param request
     * @return
     * @throws JsonProcessingException
     */
    @RequestMapping("/list/v1")
    public JsonData listGoods(@RequestBody ListGoodsRequest request) throws JsonProcessingException {

        return goodsService.findGoodsByName(request);
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

    /**
     * 添加商品信息
     * @param request
     * @return
     */
    @RequestMapping("/add/v1")
    public JsonData addGoods(@RequestBody AddGoodsRequest request) {

        return goodsService.addGoods(request);
    }

    /**
     * 根据商家ID查找商品
     * @param request
     * @return
     */
    @RequestMapping("/list/bySellerId/v1")
    public JsonData findGoodsBySellerId(@RequestBody FindBySellerIdRequest request) throws JsonProcessingException {

        return goodsService.findGoodsBySellerId(request);
    }

    /**
     * 根据商品id删除商品信息
     * @param id
     * @return
     */
    @RequestMapping("/deleteById/v1")
    public JsonData deleteByGoodsId(@RequestParam("id") BigInteger id) {

        return goodsService.deleteByGoodsId(id);
    }
}
