package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Goods;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author CaoJing
 * @date 2021/07/27 16:33
 */
@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {

}
