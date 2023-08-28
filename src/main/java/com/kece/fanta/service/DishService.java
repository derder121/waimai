package com.kece.fanta.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kece.fanta.dto.DishDto;
import com.kece.fanta.entity.Dish;

public interface DishService extends IService<Dish> {

    // 新增菜品，同时新增菜品对应的口味数据，需要操作两张表
    public void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品信息和口味信息，操作两张表
    public DishDto getByIdWithFlavor(Long id);

    //更新菜品表以及对应的口味
    public void updateWithFlavor(DishDto dishDto);
}
