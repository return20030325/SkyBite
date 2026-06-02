package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DishMapper {


    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    void save(Dish dish);

    @Select("select * from dish where id=#{id}")
    Dish getById(Long id);

    @Delete("delete from dish where id = #{id}")
    void deleteById(Long id);

    void update(Dish dish);

    @Select("select * from dish where category_id=#{categoryId}")
    List<Dish> getByCategoryId(Dish dish);

    Integer countByMap(Map map);

    /**
     * 动态条件查询菜品
     *
     * @param dish
     * @return
     */
    List<Dish> list(Dish dish);
}
