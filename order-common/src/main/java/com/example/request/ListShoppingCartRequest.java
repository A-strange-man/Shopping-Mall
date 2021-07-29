package com.example.request;

import lombok.Data;

import java.math.BigInteger;

/**
 * @author CaoJing
 * @date 2021/07/28 14:55
 */
@Data
public class ListShoppingCartRequest {
    // 需要查询的买家的用户Id
    private BigInteger buyerId;
    // 页码
    private Integer pageIndex;
    // 每页显示的条数
    private Integer pageSize;
}
