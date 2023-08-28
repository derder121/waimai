package com.kece.fanta.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kece.fanta.common.R;
import com.kece.fanta.dto.DishDto;
import com.kece.fanta.entity.Dish;
import com.kece.fanta.entity.DishFlavor;
import com.kece.fanta.mapper.DishMapper;
import com.kece.fanta.service.DishFlavorService;
import com.kece.fanta.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        // 保存菜品的基本信息到菜品表
        this.save(dishDto);

        Long dishId = dishDto.getId(); //菜品id

        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item)->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
        // 查询菜品信息
        Dish dish = this.getById(id);

        //查询菜品口味信息
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> dishFlavor = dishFlavorService.list(lambdaQueryWrapper);


        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        dishDto.setFlavors(dishFlavor);
        return dishDto;
    }

    @Override
    public void updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);  //更新菜品信息

        // 删除菜品对应的口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        // 获取dishDto中的菜品信息
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).toList();

        dishFlavorService.saveBatch(flavors);
    }
}
