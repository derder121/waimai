package com.kece.fanta.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kece.fanta.common.R;
import com.kece.fanta.dto.DishDto;
import com.kece.fanta.entity.Category;
import com.kece.fanta.entity.Dish;
import com.kece.fanta.entity.DishFlavor;
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
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }


    // 菜品信息分页查询
    @GetMapping("/page")  // 分页查询
    public R<Page> page(int page, int pageSize,String name){
        Page<Dish> pageInfo = new Page<>(page, pageSize);     // 初始分页构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();   // 条件构造器

        // 如果是搜索查询的话，根据名字查询
        queryWrapper.like(name!=null,Dish::getName,name);

        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo,queryWrapper);

        Page<DishDto> dishDtoPage = new Page<>();   // 最终分页构造器
        // 1、将不需要改的属性先赋值给dishDtoPage
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records = pageInfo.getRecords();  // 获取Records属性
        List<DishDto> dishDtos = records.stream().map((item)->{      // 遍历Records属性的集合
            DishDto dishDto = new DishDto();
            // 1、、先把item中的属性赋值给dishDto
            BeanUtils.copyProperties(item,dishDto);

            // 2、、后面把dishDto中缺失的categoryName属性补充
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category!=null){
                // 若id对应的菜品分类存在，则将菜品分类名称赋值上
                dishDto.setCategoryName(category.getName());
            }else {
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
    public R<DishDto> get(@PathVariable("id") Long id){
        DishDto byIdWithFlavor = dishService.getByIdWithFlavor(id);
        return R.success(byIdWithFlavor);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);
        return R.success("更新菜品成功");
    }

    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable("status") Integer status,@RequestParam("ids") List<Long> ids){
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId,ids);
        List<Dish> dishs = dishService.listByIds(ids);
        for (Dish dish : dishs) {
            dish.setStatus(status);
        }
        dishService.removeByIds(ids);
        dishService.saveBatch(dishs);
        return R.success("修改成功");
    }

    @DeleteMapping
    public R<String> deleteDish(@RequestParam("ids") List<Long> ids){
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DishFlavor::getDishId,ids);
        dishFlavorService.remove(queryWrapper);
        dishService.removeByIds(ids);
        return R.success("删除成功");
    }

}
