package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShopCartService {
    List<ShoppingCart> list();

    void save(ShoppingCartDTO shoppingCartDTO);

    void clean();

    void delete(ShoppingCartDTO shoppingCartDTO);
}
