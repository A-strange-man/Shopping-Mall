package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.MyOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author CaoJing
 * @date 2021/07/27 16:37
 */
@Mapper
public interface OrderMapper extends BaseMapper<MyOrder> {
}
