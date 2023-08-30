package com.kece.fanta.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kece.fanta.entity.OrderDetail;
import com.kece.fanta.mapper.OrderDetailMapper;
import com.kece.fanta.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
