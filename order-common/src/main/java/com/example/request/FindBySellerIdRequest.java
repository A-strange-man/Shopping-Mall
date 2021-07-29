package com.example.request;

import lombok.Data;

import java.math.BigInteger;

/**
 * @author CaoJing
 * @date 2021/07/28 17:29
 */
@Data
public class FindBySellerIdRequest {
    // 商家ID
    private BigInteger sellerId;
    // 页码
    private Integer pageIndex;
    // 每页显示的条数
    private Integer pageSize;
}
