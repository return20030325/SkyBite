package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {




    Page<Category> select(CategoryPageQueryDTO categoryPageQueryDTO);

    void save(Category category);

    void update(Category category);

    @Delete("delete from category where id =#{id}")
    void delete(Long id);

    List<CategoryDTO> getListByType(Integer type);
}
