package com.kece.fanta.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kece.fanta.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
