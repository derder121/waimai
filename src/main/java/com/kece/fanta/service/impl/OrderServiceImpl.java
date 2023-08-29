package com.kece.fanta.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kece.fanta.common.BaseContext;
import com.kece.fanta.common.CustomException;
import com.kece.fanta.entity.AddressBook;
import com.kece.fanta.entity.Orders;
import com.kece.fanta.entity.ShoppingCart;
import com.kece.fanta.mapper.AddressBookMapper;
import com.kece.fanta.mapper.OrderMapper;
import com.kece.fanta.service.AddressBookService;
import com.kece.fanta.service.OrderService;
import com.kece.fanta.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserService userService;

    /**
     * 用户下单
     *
     * @param orders
     */
    @Override
    @Transactional
    public void submit(Orders orders) {
        //获得当前用户id
        Long userId = BaseContext.getCurrentId();

        //查询当前用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(wrapper);

        if (shoppingCarts == null || shoppingCarts.size() == 0) {
            throw new CustomException("购物车为空，不能下单");
        }

        //查询用户数据

        //查询地址数据

        //向订单表插入数据，一条数据

        //向订单明细表插入数据，多条数据

        //清空购物车数据
    }
}
