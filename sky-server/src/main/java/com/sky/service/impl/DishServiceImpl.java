package com.sky.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private SetmealMapper setmealMapper;


    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());

    }

    /**
     * 批量插入口味数据
     *
     * @param dishDTO
     */
    @Transactional
    @Override
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dish.setCreateTime(LocalDateTime.now());
        dish.setUpdateTime(LocalDateTime.now());
        dish.setCreateUser(BaseContext.getCurrentId());
        dish.setUpdateUser(BaseContext.getCurrentId());
        dishMapper.save(dish);
        Long dishId = dish.getId();
        //添加口味
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });

        }
        dishFlavorMapper.insertBatch(flavors);


    }

    /**
     * 批量删除菜品
     *
     * @param ids
     */
    @Transactional
    @Override
    public void deleteBatch(List<Long> ids) {
        for (Long id : ids) {
            //根据id查询菜品
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                //当前商品处于起售状态，不可删除
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);

            }
            //判断当前菜品是否能够删除---是否被套餐关联了？？
            List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishId(id);
            if (setmealIds != null && setmealIds.size() > 0) {
                throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);

            }
            //删除菜品口味表
            dishFlavorMapper.deleteById(id);


            //删除菜品表
            dishMapper.deleteById(id);

        }
    }

    @Override
    public DishVO getById(Long id) {
        //查询dish表
        Dish dish = dishMapper.getById(id);
        //查询dishflavor表
        List<DishFlavor> flavors = dishFlavorMapper.getFlavorByDishId(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(flavors);
        return dishVO;

    }

    /**
     * 修改菜品
     * @param dishDTO
     */
    @Transactional
    @Override
    public void update(DishDTO dishDTO) {
        Dish dish = new Dish();
        //修改dish表
        BeanUtils.copyProperties(dishDTO,dish);
        dish.setUpdateUser(BaseContext.getCurrentId());
        dish.setUpdateTime(LocalDateTime.now());
        dishMapper.update(dish);
        //修改dishFlavor表

        //删除原有的口味数据
        dishFlavorMapper.deleteById(dishDTO.getId());
        //重新插入口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishDTO.getId());
            });
            //向口味表插入n条数据
            dishFlavorMapper.insertBatch(flavors);
        }

        /*List<DishFlavor> flavors = dishDTO.getFlavors();
        for(DishFlavor flavor: flavors){
            dishFlavorMapper.update(flavor);
        }*/

    }

    @Transactional
    @Override
    public void startOrStop(Integer status, Long id) {
        //修改dish表
        Dish dish = Dish.builder().status(status).id(id).build();
        dishMapper.update(dish);
        //停售套餐表
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishId(id);
        for(Long setmealId:setmealIds){
            Setmeal setmeal = Setmeal.builder().status(status).id(setmealId).build();
            setmealMapper.update(setmeal);

        }


    }

    /**
     * 根据分类id查询菜品
     * @param dish
     * @return
     */
    @Override
    public List<DishVO> getByCategoryId(Dish dish) {
        List<Dish> dishList = dishMapper.getByCategoryId(dish);
        List<DishVO> dishVOList =new ArrayList<>();
        for(Dish d:dishList){
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);
            dishVOList.add(dishVO);

        }
        return dishVOList;
    }

    @Override
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }


}
