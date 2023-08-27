
package com.kece.fanta.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kece.fanta.common.R;
import com.kece.fanta.entity.Category;
import com.kece.fanta.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping  // 新增分类
    public R<String> save(@RequestBody Category category){
        log.info("category:{}",category);
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    @GetMapping("/page")  // 分页查询
    public R<Page> page(int page, int pageSize){
        Page<Category> pageInfo = new Page<>(page,pageSize);   //分页构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();  //条件构造器
        queryWrapper.orderByAsc(Category::getSort);  //添加排序条件
        categoryService.page(pageInfo,queryWrapper); // 进行分页查询
        return R.success(pageInfo);
    }

    @DeleteMapping  // 根据id删除分类
    public R<String> delete(@RequestParam("id") Integer id){
        System.out.println(id);
        log.info("删除分类，id为{}",id);


        categoryService.removeById(id);
        return R.success("分类信息删除成功");
    }
}
