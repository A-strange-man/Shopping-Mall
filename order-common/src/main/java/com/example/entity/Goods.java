package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author CaoJing
 * @date 2021/07/26 14:08
 *
 * 商品 - 实体类
 */
@Data
@ToString
public class Goods implements Serializable {
    // 主键
    @TableId(type = IdType.AUTO)
    private BigInteger id;

    // 卖家id
    private BigInteger sellerId;

    // 商品名称
    private String goodsName;

    // 价格
    private BigDecimal goodsPrice;

    // 库存
    private Integer goodsStockNumber;

    // 备注
    private String goodsDescription;
}
