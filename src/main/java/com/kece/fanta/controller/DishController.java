package com.kece.fanta.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kece.fanta.common.CustomException;
import com.kece.fanta.common.R;
import com.kece.fanta.dto.DishDto;
import com.kece.fanta.entity.Category;
import com.kece.fanta.entity.Dish;
import com.kece.fanta.entity.DishFlavor;
import com.kece.fanta.entity.Setmeal;
import com.kece.fanta.service.CategoryService;
import com.kece.fanta.service.DishFlavorService;
import com.kece.fanta.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

//菜品管理
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;


    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }


    // 菜品信息分页查询
    @GetMapping("/page")  // 分页查询
    public R<Page> page(int page, int pageSize, String name) {
        Page<Dish> pageInfo = new Page<>(page, pageSize);     // 初始分页构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();   // 条件构造器

        // 如果是搜索查询的话，根据名字查询
        queryWrapper.like(name != null, Dish::getName, name);

        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo, queryWrapper);

        Page<DishDto> dishDtoPage = new Page<>();   // 最终分页构造器
        // 1、将不需要改的属性先赋值给dishDtoPage
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        List<Dish> records = pageInfo.getRecords();  // 获取Records属性
        List<DishDto> dishDtos = records.stream().map((item) -> {      // 遍历Records属性的集合
            DishDto dishDto = new DishDto();
            // 1、、先把item中的属性赋值给dishDto
            BeanUtils.copyProperties(item, dishDto);

            // 2、、后面把dishDto中缺失的categoryName属性补充
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                // 若id对应的菜品分类存在，则将菜品分类名称赋值上
                dishDto.setCategoryName(category.getName());
            } else {
                // 如菜品分类不存在，则显示“无”
                dishDto.setCategoryName("无");
            }
            return dishDto;
        }).toList();    //转换成list集合

//      2、、完成初始构造器到最终构造器的转化
        dishDtoPage.setRecords(dishDtos);

        return R.success(dishDtoPage);
    }

    //根据id查询菜品信息和菜品口味信息
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable("id") Long id) {
        DishDto byIdWithFlavor = dishService.getByIdWithFlavor(id);
        return R.success(byIdWithFlavor);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);
        return R.success("更新菜品成功");
    }

    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable("status") Integer status, @RequestParam("ids") List<Long> ids) {
        //  获取要修改的套餐菜品数据
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId, ids);
        List<Dish> dishs = dishService.listByIds(ids);

        // 对其逐一修改status值
        for (Dish dish : dishs) {
            dish.setStatus(status);
        }

        // 删除之前的套餐菜品数据
        dishService.removeByIds(ids);
        // 添加新构建的setmeal数据
        dishService.saveBatch(dishs);
        return R.success("修改成功");
    }

    @DeleteMapping
    public R<String> deleteDish(@RequestParam("ids") List<Long> ids) {
        // 查询套餐的状态，确认是否可以删除
        LambdaQueryWrapper<Dish> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(Dish::getId, ids);
        queryWrapper1.eq(Dish::getStatus, 1);
        int count = dishService.count(queryWrapper1);
        if (count > 0) {
            throw new CustomException("该菜品售卖中，无法删除");
        }

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DishFlavor::getDishId, ids);
        dishFlavorService.remove(queryWrapper);
        dishService.removeByIds(ids);
        return R.success("删除成功");
    }


    /*//根据添加查询相应菜品数据，用于套餐添加
    @GetMapping("/list")
    // dish内部只保存了CategoryId，用于查询对于菜品分类下的菜品
    public R<List<Dish>> list(Dish dish) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus, 1);  //查询起售状态的菜品
        // 添加排序条件
        queryWrapper.orderByDesc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        //SQL:select * from dish where id = ? and status = ? order by sort desc,update_time desc
        List<Dish> list = dishService.list(queryWrapper);
        return R.success(list);
    }*/

    //根据添加查询相应菜品数据，用于套餐添加
    @GetMapping("/list")
    // dish内部只保存了CategoryId，用于查询对于菜品分类下的菜品
    public R<List<DishDto>> list(Dish dish) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus, 1);  //查询起售状态的菜品
        // 添加排序条件
        queryWrapper.orderByDesc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        //SQL:select * from dish where id = ? and status = ? order by sort desc,update_time desc
        List<Dish> list = dishService.list(queryWrapper);

        //增加DishDto的flavors属性
        List<DishDto> dishDtoList = list.stream().map((item) -> {      // 遍历list集合
            DishDto dishDto = new DishDto();
            // 1、、先把item中的属性赋值给dishDto
            BeanUtils.copyProperties(item, dishDto);

            // 2、、后面把dishDto中缺失的categoryName属性补充
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                // 若id对应的菜品分类存在，则将菜品分类名称赋值上
                dishDto.setCategoryName(category.getName());
            } else {
                // 如菜品分类不存在，则显示“无”
                dishDto.setCategoryName("无");
            }

            //当前菜品id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId, dishId);
            //SQL:select * from dishFlavor where dish_id = ?
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).toList();    //转换成list集合

        return R.success(dishDtoList);
    }
}
