package com.kece.fanta.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kece.fanta.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
