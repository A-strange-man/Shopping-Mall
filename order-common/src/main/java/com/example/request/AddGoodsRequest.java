package com.example.request;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author CaoJing
 * @date 2021/07/28 17:16
 */
@Data
public class AddGoodsRequest {
    // 卖家id
    private BigInteger sellerId;
    // 商品名称
    private String goodsName;
    // 商品价格
    private BigDecimal goodsPrice;
    // 库存数量
    private Integer stockNumber;
    // 备注
    private String description;
}
