package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;

import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private DishMapper dishMapper;


    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);

        return new PageResult(page.getTotal(), page.getResult());

    }

    /**
     * 新增套餐
     *
     * @param setmealDTO
     */
    @Transactional
    @Override
    public void save(SetmealDTO setmealDTO) {
        //添加setmeal表
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmeal.setUpdateTime(LocalDateTime.now());
        setmeal.setCreateTime(LocalDateTime.now());
        setmeal.setCreateUser(BaseContext.getCurrentId());
        setmeal.setUpdateUser(BaseContext.getCurrentId());
        setmealMapper.save(setmeal);
        Long setmealId = setmeal.getId();
        //添加setmealDish表
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealId);
            setmealDishMapper.save(setmealDish);
        }

    }

    /**
     * 批量删除套餐
     *
     * @param ids
     */
    @Transactional
    @Override
    public void delete(List<Long> ids) {
        for (Long id : ids) {
            //处于起售中的套餐不能删除
            Setmeal setmeal = setmealMapper.getById(id);
            if (setmeal.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);

            }
            //删除套餐表
            setmealMapper.delete(id);
            setmealDishMapper.delete(id);
        }


    }


    /** 根据id查询套餐
     * @param id
     * @return
     */
    @Override
    public SetmealVO getById(Long id) {
        //查询套餐表
        Setmeal setmeal = setmealMapper.getById(id);
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        //查询菜品表
        List<SetmealDish> setmealDishList = setmealDishMapper.getsetmealDishById(id);
        setmealVO.setSetmealDishes(setmealDishList);
        return setmealVO;

    }

    /**
     * 更新套餐
     *
     * @param setmealDTO
     */
    @Transactional
    @Override
    public void udpate(SetmealDTO setmealDTO) {
        //更新setmeal表
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);
        //更新setmeal_dish表


        List<SetmealDish> setmealDishList = setmealDTO.getSetmealDishes();
        //2、删除套餐和菜品的关联关系，操作setmeal_dish表，执行delete
        setmealDishMapper.delete(setmealDTO.getId());
        //3、重新插入套餐和菜品的关联关系，操作setmeal_dish表，执行insert
        for (SetmealDish setmealDish : setmealDishList) {
            setmealDish.setSetmealId(setmealDTO.getId());
            setmealDishMapper.save(setmealDish);
        }


    }

    /**
     * 起售、停售套餐
     * @param setmealDTO
     */
    @Override
    public void startOrStop(SetmealDTO setmealDTO) {
        //起售套餐时，判断套餐内是否有停售菜品，有停售菜品提示"套餐内包含未启售菜品，无法启售"
        Integer status = setmealDTO.getStatus();
        if (status==StatusConstant.ENABLE){
            List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
            for (SetmealDish setmealDish : setmealDishes) {
                Long dishId = setmealDish.getDishId();
                Dish dish = dishMapper.getById(dishId);
                if (dish.getStatus()==StatusConstant.DISABLE){
                    throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                }
            }
        }
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.update(setmeal);

    }


    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }


    /**
     * 根据套餐id查询包含的菜品
     * @param id
     * @return
     */
    @Override
    public List<DishItemVO> getDishItemById(Long id) {
        List<DishItemVO> dishItemVOList = setmealMapper.getDishItemById(id);
        return dishItemVOList;
    }



}
