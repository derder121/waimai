package com.kece.fanta.controller;

import com.kece.fanta.common.BaseContext;
import com.kece.fanta.common.R;
import com.kece.fanta.entity.ShoppingCart;
import com.kece.fanta.service.AddressBookService;
import com.kece.fanta.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        List<ShoppingCart> shoppingCarts = new ArrayList<>();
        return R.success(shoppingCarts);
    }
}
