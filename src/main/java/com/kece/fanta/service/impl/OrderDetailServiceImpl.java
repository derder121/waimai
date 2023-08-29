package com.kece.fanta.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kece.fanta.entity.OrderDetail;
import com.kece.fanta.entity.Orders;
import com.kece.fanta.mapper.OrderDetailMapper;
import com.kece.fanta.mapper.OrderMapper;
import com.kece.fanta.service.OrderDetailService;
import com.kece.fanta.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
