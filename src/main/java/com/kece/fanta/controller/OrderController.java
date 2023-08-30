package com.kece.fanta.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kece.fanta.common.BaseContext;
import com.kece.fanta.common.R;
import com.kece.fanta.entity.Orders;
import com.kece.fanta.service.OrderDetailService;
import com.kece.fanta.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        log.info("订单数据：{}", orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }

    @GetMapping("/userPage")
    public R<Page> userPage(int page, int pageSize) {

        log.info("page = {},pageSize = {}", page, pageSize);

        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper();
        //添加过滤条件
        queryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        //添加排序条件
        queryWrapper.orderByDesc(Orders::getOrderTime);

        //执行查询
        orderService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {

        log.info("page = {},pageSize = {}", page, pageSize);

        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);

        //执行查询
        orderDetailService.page(pageInfo);

        return R.success(pageInfo);
    }
}
