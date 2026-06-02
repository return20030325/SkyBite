package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;


public interface DishService {
    PageResult page(DishPageQueryDTO pageQueryDTO);

    void saveWithFlavor(DishDTO dishDTO);

    void deleteBatch(List<Long> ids);

    DishVO getById(Long id);

    void update(DishDTO dishDTO);

    void startOrStop(Integer status, Long id);

    List<DishVO> getByCategoryId(Dish dish);

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);


}
