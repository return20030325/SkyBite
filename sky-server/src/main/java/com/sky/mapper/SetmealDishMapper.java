package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    List<Long> getSetmealIdsByDishId(Long id);

    void save(SetmealDish setmealDish);

    void delete(Long id);

    @Select("select * from setmeal_dish where setmeal_id=#{id}")
    List<SetmealDish> getsetmealDishById(Long id);
}
