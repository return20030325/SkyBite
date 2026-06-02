package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShopCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/user/shoppingCart")
@Api(tags = "C端购物车相关接口")
@Slf4j
public class ShoppingCartController {

    public static final String KEY = "SHOP_STATUS";

    @Autowired
    private ShopCartService shopCartService;

    @Autowired
    private RedisTemplate redisTemplate;



    /**
     * 获取购物车列表
     * @return
     */
    @ApiOperation("获取购物车列表")
    @GetMapping("/list")
    public Result<List<ShoppingCart>> list(){
        List<ShoppingCart> shoppingCartList =  shopCartService.list();
        return Result.success(shoppingCartList);
    }

    /**
     * 添加购物车
     * @param shoppingCartDTO
     * @return
     */
    @ApiOperation("添加购物车")
    @PostMapping("/add")
    public Result save(@RequestBody ShoppingCartDTO shoppingCartDTO){
        shopCartService.save(shoppingCartDTO);
        return Result.success("添加购物车成功");

    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    @ApiOperation("清空购物车")
    public Result clean(){
        shopCartService.clean();
        return Result.success("清空购物车成功");

    }

    /**
     * 删除购物车中一个商品
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/sub")
    @ApiOperation("删除购物车中一个商品")
    public Result delete(@RequestBody ShoppingCartDTO shoppingCartDTO){
        shopCartService.delete(shoppingCartDTO);
        return Result.success("删除成功");
    }

}