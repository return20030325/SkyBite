package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDetailMapper {
    void insertBatch(List<OrderDetail> orderDetailList);

    @Delete("delete from orderDetail where order_id=#{orderId}")
    void delete(Long orderId);

    @Select("select * from order_detail where order_id=#{orderId}")
    List<OrderDetail> getByOrderId(Long orderId);

    @Select("select count(*) from Orders where status=#{status}")
    Integer countStatus(Integer status);
}
