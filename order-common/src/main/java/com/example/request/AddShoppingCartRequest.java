package com.example.request;

import lombok.Data;

import java.math.BigInteger;

/**
 * @author CaoJing
 * @date 2021/07/28 13:38
 */
@Data
public class AddShoppingCartRequest {
    // 商品id
    private BigInteger goodsId;
    // 购买数量
    private Integer buyNumber;
    // 购买者id
    private BigInteger buyerId;
}
