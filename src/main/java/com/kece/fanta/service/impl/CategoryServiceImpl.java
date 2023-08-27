package com.kece.fanta.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kece.fanta.entity.Category;
import com.kece.fanta.mapper.CategoryMapper;
import com.kece.fanta.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
