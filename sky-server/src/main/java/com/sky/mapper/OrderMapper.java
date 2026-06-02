package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    void insert(Orders orders);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    /**
     * 历史订单查询
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> select(OrdersPageQueryDTO ordersPageQueryDTO);

    void delete(Orders orders);

    @Select("select * from orders where id=#{id}")
    Orders getById(Long id);

    @Select("select * from orders where status=#{status} and order_time<#{orderTime}")
    List<Orders> getByStatusAndOrderTimeLT(Integer status, LocalDateTime orderTime);

    Integer countByMap(Map map);

    /**
     * 查询条件营业额
     * @param map
     * @return
     */
    Double sumByMap(Map map);

    /**
     * 查询商品销量排名
     * @param begin
     * @param end
     */
    List<GoodsSalesDTO> getSalesTop10(LocalDateTime begin, LocalDateTime end);

}
