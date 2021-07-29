package com.example.service;

import com.example.request.ListGoodsRequest;
import com.example.response.JsonData;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.math.BigInteger;

/**
 * @author CaoJing
 * @date 2021/07/27 17:21
 */
public interface GoodsService {

    /**
     * 根据查询关键字 queryKey 查询商品信息
     * 如果关键字为纯数字，将其视为goodsId进行精准查询，否则视为商品名称模糊查询，如果为空，表示查询所有商品
     * @param request
     * @return
     */
    JsonData findGoods(ListGoodsRequest request) throws JsonProcessingException;

    /**
     * 根据商品id查询商品
     * @param goodsId
     * @return
     */
    JsonData findGoodsById(BigInteger goodsId) throws JsonProcessingException;
}
