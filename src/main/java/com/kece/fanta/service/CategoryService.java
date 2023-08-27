package com.kece.fanta.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kece.fanta.entity.Category;

public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
