package com.kece.fanta.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kece.fanta.dto.SetmealDto;
import com.kece.fanta.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    public void saveWithDish(SetmealDto setmealDto);

    //删除套餐及套餐关联数据
    public void removeWithDish(List<Long> ids);
}
