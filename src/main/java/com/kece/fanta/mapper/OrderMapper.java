package com.kece.fanta.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kece.fanta.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
