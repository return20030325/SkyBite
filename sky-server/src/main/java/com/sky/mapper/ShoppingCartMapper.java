package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {


    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 添加购物车
     */
    void save(ShoppingCart shoppingCart);

    /**
     * 更新商品数量
     *
     * @param shoppingCart
     */
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumberById(ShoppingCart shoppingCart);

    /**
     * 删除购物车中一个商品
     * @param shoppingCart
     */
    void delete(ShoppingCart shoppingCart);

    /**
     * 清空购物车
     * @param shoppingCart
     */
    @Delete("delete from shopping_cart where user_id=#{userId}")
    void clean(ShoppingCart shoppingCart);

    void insertBatch(List<ShoppingCart> shoppingCartList);
}
