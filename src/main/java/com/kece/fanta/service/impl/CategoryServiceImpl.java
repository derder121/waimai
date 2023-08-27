package com.kece.fanta.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kece.fanta.common.CustomException;
import com.kece.fanta.entity.Category;
import com.kece.fanta.entity.Dish;
import com.kece.fanta.entity.Setmeal;
import com.kece.fanta.mapper.CategoryMapper;
import com.kece.fanta.service.CategoryService;
import com.kece.fanta.service.DishService;
import com.kece.fanta.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    //根据Id删除分类，删除之前判断是否有关联
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishwrapper= new LambdaQueryWrapper<>();
//        添加查询条件
        dishwrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishwrapper);


        LambdaQueryWrapper<Setmeal> setwrapper = new LambdaQueryWrapper<>();
//         添加查询条件
        setwrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setwrapper);

        // 查询是否关联菜品及套餐，若已关联，抛出异常

        if (count1>0){
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }
        if (count2>0){
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }
        super.removeById(id);
    }
}
