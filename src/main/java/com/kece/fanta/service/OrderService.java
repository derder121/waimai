package com.kece.fanta.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kece.fanta.entity.Orders;
import org.springframework.core.annotation.Order;

public interface OrderService extends IService<Orders> {

    /**
     * 用户下单
     *
     * @param orders
     */
    void submit(Orders orders);
}
