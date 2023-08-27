package com.kece.fanta.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kece.fanta.entity.Dish;
import com.kece.fanta.mapper.DishMapper;
import com.kece.fanta.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
