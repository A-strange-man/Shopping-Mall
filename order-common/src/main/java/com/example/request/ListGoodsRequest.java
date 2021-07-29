package com.example.request;

import lombok.Data;

import java.math.BigInteger;

/**
 * @author CaoJing
 * @date 2021/07/26 14:45
 *
 * 商品查询请求体
 */
@Data
public class ListGoodsRequest {
    // 页码
    private Integer pageIndex;

    // 每页显示的条数
    private Integer pageSize;

    // 查询关键字    如果关键字为纯数字，将其视为goodsId进行精准查询，否则视为商品名称模糊查询，如果为空，表示查询所有商品
    private String queryKey;

    // 卖家id
    private BigInteger sellerId;
}
