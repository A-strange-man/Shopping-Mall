package com.example.service;

import com.example.request.AddGoodsRequest;
import com.example.request.FindBySellerIdRequest;
import com.example.request.ListGoodsRequest;
import com.example.response.JsonData;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.math.BigInteger;

/**
 * @author CaoJing
 * @date 2021/07/28 9:40
 */
public interface GoodsService {

    /**
     * 根据商品名字关键字进行模糊查询
     * @param request
     * @return
     */
    JsonData findGoodsByName(ListGoodsRequest request) throws JsonProcessingException;

    /**
     * 根据商品id进行精确查找
     * @param goodsId
     * @return
     */
    JsonData findGoodsById(BigInteger goodsId) throws JsonProcessingException;

    /**
     * 添加商品信息
     * @param request
     * @return
     */
    JsonData addGoods(AddGoodsRequest request);

    /**
     * 根据商家id查找商品
     * @param request
     * @return
     */
    JsonData findGoodsBySellerId(FindBySellerIdRequest request) throws JsonProcessingException;

    /**
     * 根据商品id删除商品信息
     * @param id
     * @return
     */
    JsonData deleteByGoodsId(BigInteger id);
}
