package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author CaoJing
 * @date 2021/07/27 16:38
 */
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
