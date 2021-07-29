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
 * @date 2021/07/26 14:24
 *
 * 购物车 - 实体类
 */
@Data
@ToString
public class ShoppingCart implements Serializable {
    // 主键
    @TableId(type = IdType.AUTO)
    private BigInteger id;

    // 买家id
    private BigInteger buyId;

    // 商品id
    private BigInteger goodsId;

    // 商品名称
    private String goodsName;

    // 价格
    private BigDecimal goodsPrice;

    // 数量
    private Integer goodsNumber;

    // 备注/留言
    private String description;
}
