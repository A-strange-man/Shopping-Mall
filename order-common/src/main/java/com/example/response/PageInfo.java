package com.example.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author CaoJing
 * @date 2021/07/27 14:02
 *
 * 查询结果 - 页面信息
 */
@Data
public class PageInfo<T> implements Serializable {
    // 页码
    private Long pageIndex;

    // 商品列表
    private List<T> records;

    // 页面商品数量
    private Long totalNum;

    // 总页面数
    private Long pageTotal;
}
