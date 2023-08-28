package com.kece.fanta.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kece.fanta.common.R;
import com.kece.fanta.dto.SetmealDto;
import com.kece.fanta.entity.Category;
import com.kece.fanta.entity.Dish;
import com.kece.fanta.entity.Setmeal;
import com.kece.fanta.service.CategoryService;
import com.kece.fanta.service.SetmealDishService;
import com.kece.fanta.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;


    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("套餐信息：{}", setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("添加成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        //获取套餐表中的数据副本
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo, queryWrapper);

        // 创建套餐表数据的主本
        Page<SetmealDto> setmealDtoPage = new Page<>();
        // 将副本的数据除了“records”全部赋值到主本中
        BeanUtils.copyProperties(pageInfo, setmealDtoPage, "records");

        // 从副本中获取“records”数据
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> setmealDtos = records.stream().map((item) -> {
            // 创建一个setmealDto对象，用于封装数据
            SetmealDto setmealDto = new SetmealDto();
            // 将records中的数据赋值到setmealDto中
            BeanUtils.copyProperties(item, setmealDto);

            // 找到records数据中对应的套餐分类id，通过分类id找到套餐分类名字
            Long id = item.getCategoryId();
            Category category = categoryService.getById(id);
            if (category != null) {
                // 将套餐分类名字封装到setmealDto中
                setmealDto.setCategoryName(category.getName());
            } else {
                setmealDto.setCategoryName("无");
            }
            return setmealDto;
        }).toList();

        setmealDtoPage.setRecords(setmealDtos);
        return R.success(setmealDtoPage);
    }

    @DeleteMapping
    public R<String> delete(@RequestParam("ids") List<Long> ids) {
        log.info("ids:{}", ids);
        setmealService.removeWithDish(ids);
        return R.success("删除成功");
    }

    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable("status") Integer status, @RequestParam("ids") List<Long> ids) {
        //  获取要修改的套餐菜品数据
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        List<Setmeal> setmeals = setmealService.listByIds(ids);

        // 对其逐一修改status值
        for (Setmeal setmeal : setmeals) {
            setmeal.setStatus(status);
        }

        // 删除之前的套餐菜品数据
        setmealService.removeByIds(ids);
        // 添加新构建的setmeal数据
        setmealService.saveBatch(setmeals);
        return R.success("修改成功");
    }

    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus());  //查询指定状态的套餐

        //按updateTime降序排序
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        //SQL:select * from setmeal where category_id = ? and status = ? order by update_time desc
        List<Setmeal> list = setmealService.list(queryWrapper);

        return R.success(list);
    }
}