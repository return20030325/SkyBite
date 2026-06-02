package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@Api("工作台相关接口")
@RestController
@RequestMapping("/admin/workspace")
public class WorkSpaceController {

    @Autowired
    private WorkspaceService workspaceService;

    @ApiOperation("查询今日运营数据")
    @GetMapping("businessData")
    public Result<BusinessDataVO> businessData() {
        //获得当天的开始时间
        LocalDateTime begin = LocalDateTime.now().with(LocalTime.MIN);
        //获得当天的结束时间
        LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);
        BusinessDataVO businessDataVO = workspaceService.getBusinessData(begin, end);
        return Result.success(businessDataVO);

    }

    @GetMapping("/overviewSetmeals")
    @ApiOperation("查询套餐总览")
    public Result<SetmealOverViewVO> setmealOverView() {
        SetmealOverViewVO setmealOverViewVO = workspaceService.getSetmealOverView();
        return Result.success(setmealOverViewVO);
    }

    @ApiOperation("查询菜品总览")
    @GetMapping("/overviewDishes")
    public Result<DishOverViewVO> overviewDishes(){
        DishOverViewVO dishOverViewVO = workspaceService.getoverviewDishes();
        return Result.success(dishOverViewVO);

    }

    @GetMapping("/overviewOrders")
    @ApiOperation("查询订单管理数据")
    public Result<OrderOverViewVO> orderOverView(){
        return Result.success(workspaceService.getOrderOverView());
    }

}
