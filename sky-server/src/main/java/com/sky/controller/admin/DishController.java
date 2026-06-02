package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品相关接口")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 菜品分页查询
     * @param pageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO pageQueryDTO){
        PageResult pageResult = dishService.page(pageQueryDTO);
        return Result.success(pageResult);

    }


    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @ApiOperation("新增菜品")
    @PostMapping
    public Result save(@RequestBody DishDTO dishDTO){
        dishService.saveWithFlavor(dishDTO);
        //清理缓存数据
        String key = "dish_" + dishDTO.getCategoryId();
        cleanCache(key);
        return Result.success("新增菜品成功");

    }

    /**
     * 批量删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result delete(@RequestParam List<Long> ids){
        log.info("批量删除菜品id,{}",ids);
        dishService.deleteBatch(ids);
        //将所有的菜品缓存数据清理掉，所有以dish_开头的key
        cleanCache("dish_*");
        return  Result.success("菜品删除成功");
    }

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @ApiOperation("根据id查询菜品")
    @GetMapping("/{id}")
    public Result<DishVO> getById(@PathVariable Long id){
        log.info("根据id查询菜品,{}",id);
        DishVO dishVO = dishService.getById(id);
        return Result.success(dishVO);

    }


    /**
     * 修改菜品
     * @param dishDTO
     * @return
     */
    @ApiOperation("修改菜品")
    @PutMapping
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品",dishDTO);
        dishService.update(dishDTO);
        //将所有的菜品缓存数据清理掉，所有以dish_开头的key
        cleanCache("dish_*");
        return Result.success("修改菜品成功");
    }

    /**
     * 菜品停售起售
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status,Long id){
        log.info("菜品停售起售{},{}",status,id);
        dishService.startOrStop(status,id);
        //将所有的菜品缓存数据清理掉，所有以dish_开头的key
        cleanCache("dish_*");
        return Result.success("状态修改成功");

    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @ApiOperation("根据分类id查询菜品")
    @GetMapping("/list")
    public Result<List<DishVO>> list(Long categoryId){
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        List<DishVO> dishVoList = dishService.getByCategoryId(dish);
        return Result.success(dishVoList);
    }

    /**
     * 清理缓存数据
     * @param pattern
     */
    private void cleanCache(String pattern) {
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }

}
