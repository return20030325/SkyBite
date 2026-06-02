package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Api(tags = "套餐相关接口")
@RequestMapping("/admin/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        PageResult pageResult = setmealService.page(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增套餐")
    @CacheEvict(cacheNames = "setmealCache",key = "#setmealDTO.categoryId")
    public Result save(@RequestBody SetmealDTO setmealDTO){
        setmealService.save(setmealDTO);
        return Result.success("套餐添加成功");
    }

    /**
     * 批量删除套餐
     * @param ids
     * @return
     */
    @ApiOperation("批量删除套餐")
    @DeleteMapping
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result delete(@RequestParam  List<Long> ids){
        log.info("批量删除套餐,{}",ids);
        setmealService.delete(ids);
        return Result.success();

    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @ApiOperation("根据id查询套餐")
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id){
        log.info("根据id查询套餐,{}",id);
        SetmealVO setmealVO = setmealService.getById(id);
        return Result.success(setmealVO);
    }

    /**
     * 更新套餐
     * @param setmealDTO
     * @return
     */
    @PutMapping
    @ApiOperation("更新套餐")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result update(@RequestBody SetmealDTO setmealDTO){
        log.info("更新套餐,{}",setmealDTO);
        setmealService.udpate(setmealDTO);
        return Result.success("更新套餐成功");
    }

    /**
     * 起售、停售套餐
     * @param status
     * @param id
     * @return
     */
    @ApiOperation("起售、停售套餐")
    @PostMapping("/status/{status}")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result startOrStop(@PathVariable Integer status ,Long id){
        SetmealDTO setmealDTO = new SetmealDTO()   ;
        setmealDTO.setId(id);
        setmealDTO.setStatus(status);
        setmealService.startOrStop(setmealDTO);
        return Result.success("状态更改成功");

    }


}
