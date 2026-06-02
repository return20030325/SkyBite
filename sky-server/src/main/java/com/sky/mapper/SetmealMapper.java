package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;
import java.util.Map;

@Mapper
public interface SetmealMapper {
    void update(Setmeal setmeal);


    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    void save(Setmeal setmeal);

    @Select("select * from setmeal where id=#{id}")
    Setmeal getById(Long id);

    void delete(Long id);

    /**
     * 动态条件查询套餐
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据套餐id查询菜品选项
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);


    Integer countByMap(Map map);
}
