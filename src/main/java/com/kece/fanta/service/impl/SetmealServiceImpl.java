package com.kece.fanta.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kece.fanta.common.CustomException;
import com.kece.fanta.dto.SetmealDto;
import com.kece.fanta.entity.Setmeal;
import com.kece.fanta.entity.SetmealDish;
import com.kece.fanta.mapper.SetmealMapper;
import com.kece.fanta.service.SetmealDishService;
import com.kece.fanta.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        // 保存套餐基本信息  Setmeal表
        this.save(setmealDto);

        // 保存套餐和菜品的关联信息   SetmealDish
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        Long id = setmealDto.getId();
        // 套餐的id一一赋值到套餐关系表中的setmealid
        setmealDishes = setmealDishes.stream().map((item)->{
            item.setSetmealId(id);
            return item;
                }).toList();
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public void removeWithDish(List<Long> ids) {
        // 查询套餐的状态，确认是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(queryWrapper);
        if (count > 0 ){
            throw new CustomException("套餐正在售卖中，不能删除");
        }


        this.removeByIds(ids);    //删除套餐表数据

        // 删除套餐关系表中的数据
        LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(queryWrapper1);

    }

    @Override
    public void updateWithDish(SetmealDto setmealDto) {
        this.updateById(setmealDto);    // 先保存套餐基本信息

        // 移除套餐关联菜品信息
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishService.remove(queryWrapper);

        // 获取用户发来的套餐菜品信息
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        // 将用户发来的套餐菜品信息补全
        setmealDishes = setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).toList();

        // 保存套餐菜品信息
        setmealDishService.saveBatch(setmealDishes);
    }
}
