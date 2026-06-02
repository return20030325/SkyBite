package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShopCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShopCartServiceImpl implements ShopCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 获取购物车列表
     * @return
     */
    @Override
    public List<ShoppingCart> list() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(BaseContext.getCurrentId());
        return shoppingCartMapper.list(shoppingCart);


    }

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    @Override
    public void save(ShoppingCartDTO shoppingCartDTO) {
        //一个用户一个购物车
        Long dishId = shoppingCartDTO.getDishId();
        Long setmealId = shoppingCartDTO.getSetmealId();
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        //获取购物车列表
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        //判断当前商品是否在购物车中
        if(shoppingCartList !=null && shoppingCartList.size()==1 ){
            shoppingCart = shoppingCartList.get(0);
            shoppingCart.setNumber(shoppingCart.getNumber()+1);
            shoppingCartMapper.updateNumberById(shoppingCart);
        //如果不在购物车中
        }else {
            //通过dishId从dish表中获取详细信息
            Dish dish = dishMapper.getById(dishId);
            if (dish!=null){
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            }else {
                //通过setmeal从setmeal表中获取详细信息
                Setmeal setmeal = setmealMapper.getById(shoppingCartDTO.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());

            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.save(shoppingCart);


        }


    }

    /**
     * 清空购物车
     */
    @Override
    public void clean() {
        ShoppingCart shoppingCart = ShoppingCart.builder().userId(BaseContext.getCurrentId()).build();
        shoppingCartMapper.clean(shoppingCart);

    }

    /**
     * 删除购物车中一个商品
     * @param shoppingCartDTO
     */
    @Override
    public void delete(ShoppingCartDTO shoppingCartDTO) {
        Long dishId = shoppingCartDTO.getDishId();
        if(dishId!=null){
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
            List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
            if(list!=null && list.size()==1){
                ShoppingCart cart = list.get(0);
                if(cart.getNumber()>1){
                    cart.setNumber(cart.getNumber()-1);
                    shoppingCartMapper.updateNumberById(cart);
                }else {
                    shoppingCartMapper.delete(cart);
                }

            }




        }
    }
}
