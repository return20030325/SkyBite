package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 条件查询
     * @param type
     * @return
     */
    @Override
    public List<CategoryDTO> getListByType(Integer type) {
        List<CategoryDTO> categoryDTOList = categoryMapper.getListByType(type);
        return categoryDTOList;
    }

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(CategoryPageQueryDTO categoryPageQueryDTO) {

        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        Page<Category> page = categoryMapper.select(categoryPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());


    }

    /**
     * 添加分类
     * @param categoryDTO
     */
    @Override
    public void save(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        category.setCreateUser(BaseContext.getCurrentId());
        category.setUpdateUser(BaseContext.getCurrentId());
        categoryMapper.save(category);
    }

    /**
     * 修改分类
     *
     * @param categoryDTO
     */
    @Override
    public void update(CategoryDTO categoryDTO) {
        Category category=  new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        category.setUpdateUser(BaseContext.getCurrentId());
        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.update(category);
    }

    /**
     * 启用、禁用分类
     * @param category
     */
    @Override
    public void startOrStop(Category category) {
        categoryMapper.update(category);
    }

    /**
     * 根据id删除分类
     * @param id
     */
    @Override
    public void delete(Long id) {
        categoryMapper.delete(id);

    }
}
