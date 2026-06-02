package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.entity.Orders;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 查询今日运营数据
     * @return
     */
    @Override
    public BusinessDataVO getBusinessData(LocalDateTime begin,LocalDateTime end) {
        Map map = new HashMap();
        map.put("begin",begin);
        map.put("end",end);
        //新增用户数
        Integer newUsers = userMapper.countByMap(map);
        //营业额
        Double turnover = orderMapper.sumByMap(map);
        //在没有营业额的情况下，应该为0，而不是null
        turnover = turnover==null ? 0.0 :turnover;
        //订单完成率
        Double orderCompletionRate = 0.0;
        //总订单数
        Integer totalOrderCount = orderMapper.countByMap(map);
        map.put("status", Orders.COMPLETED);
        //有效订单数
        Integer validOrderCount = orderMapper.countByMap(map);
        //平均客单价
        Double unitPrice = 0.0;
        if(totalOrderCount != 0 && validOrderCount != 0){

            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
            unitPrice = turnover / validOrderCount;
        }
        BusinessDataVO businessDataVO = BusinessDataVO.builder()
                .newUsers(newUsers)
                .orderCompletionRate(orderCompletionRate)
                .turnover(turnover)
                .unitPrice(unitPrice)
                .validOrderCount(validOrderCount)
                .build();

        return businessDataVO;
    }

    /**
     * 查询套餐总览
     * @return
     */
    @Override
    public SetmealOverViewVO getSetmealOverView() {
        Map map = new HashMap();
        map.put("status", StatusConstant.ENABLE);
        Integer sold = setmealMapper.countByMap(map);
        map.put("status",StatusConstant.DISABLE);
        Integer discontinued = setmealMapper.countByMap(map);

        SetmealOverViewVO setmealOverViewVO = SetmealOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
        return setmealOverViewVO;
    }

    /**
     * 查询菜品总览
     * @return
     */
    @Override
    public DishOverViewVO getoverviewDishes() {
        Map map = new HashMap();
        map.put("status", StatusConstant.ENABLE);
        Integer sold = dishMapper.countByMap(map);

        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = dishMapper.countByMap(map);

        return DishOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    @Override
    public OrderOverViewVO getOrderOverView() {

        Map map = new HashMap();
        map.put("begin", LocalDateTime.now().with(LocalTime.MIN));
        map.put("status", Orders.TO_BE_CONFIRMED);

        //待接单
        Integer waitingOrders = orderMapper.countByMap(map);

        //待派送
        map.put("status", Orders.CONFIRMED);
        Integer deliveredOrders = orderMapper.countByMap(map);

        //已完成
        map.put("status", Orders.COMPLETED);
        Integer completedOrders = orderMapper.countByMap(map);

        //已取消
        map.put("status", Orders.CANCELLED);
        Integer cancelledOrders = orderMapper.countByMap(map);

        //全部订单
        map.put("status", null);
        Integer allOrders = orderMapper.countByMap(map);

        return OrderOverViewVO.builder()
                .waitingOrders(waitingOrders)
                .deliveredOrders(deliveredOrders)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .allOrders(allOrders)
                .build();


    }
}
