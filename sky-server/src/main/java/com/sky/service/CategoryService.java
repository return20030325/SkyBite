package com.sky.service;


import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getListByType(Integer type);

    PageResult page(CategoryPageQueryDTO categoryPageQueryDTO);

    void save(CategoryDTO categoryDTO);

    void update(CategoryDTO categoryDTO);

    void startOrStop(Category category);

    void delete(Long id);
}
