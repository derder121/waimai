package com.kece.fanta.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kece.fanta.entity.Setmeal;
import com.kece.fanta.mapper.SetmealMapper;
import com.kece.fanta.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
}
