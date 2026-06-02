package com.sky.controller.user;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Slf4j
@Api(tags = "C端-订单相关接口")
public class OrderController {

    @Autowired
    private OrderService orderService;


    /**
     * 用户下单(新增订单)
     * @param ordersSubmitDTO
     * @return
     */
    @PostMapping("/submit")
    @ApiOperation("用户下单")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        OrderSubmitVO orderSubmitVO = orderService.submit(ordersSubmitDTO);
        return Result.success(orderSubmitVO);


    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        orderService.paySuccess(ordersPaymentDTO.getOrderNumber());
        return Result.success(orderPaymentVO);
    }

    /**
     *历史订单查询
     * @param ordersPageQueryDTO
     * @return
     */
    @ApiOperation("历史订单查询")
    @GetMapping("/historyOrders")
    public Result<PageResult> page(OrdersPageQueryDTO ordersPageQueryDTO){
        PageResult pageResult = orderService.page(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 取消订单
     * @param id
     * @return
     */
    @ApiOperation("取消订单")
    @PutMapping("/cancel/{id}")
    public Result cancel(@PathVariable Long id){
        orderService.cancel(id);
        return Result.success("订单取消成功");
    }

    /**
     * 查询订单详情
     * @param id
     * @return
     */
    @ApiOperation("查询订单详情")
    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> details(@PathVariable Long id){
        OrderVO orderVO = orderService.details(id);
        return Result.success(orderVO);


    }

    /**
     * 再来一单：将原订单中的商品重新加入到购物车中
     * @param id
     * @return
     */
    @ApiOperation("再来一单")
    @PostMapping("/repetition/{id}")
    public Result repetition(@PathVariable Long id){
        orderService.repetition(id);
        return Result.success();

    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/reminder/{id}")
    @ApiOperation("用户催单")
    public Result reminder(@PathVariable Long id){
        orderService.reminder(id);
        return Result.success();
    }


}
