package com.kece.fanta.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kece.fanta.dto.DishDto;
import com.kece.fanta.entity.Dish;

public interface DishService extends IService<Dish> {

    // 新增菜品，同时新增菜品对应的口味数据，需要操作两张表
    public void saveWithFlavor(DishDto dishDto);
}
