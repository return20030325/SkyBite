package com.sky.controller.user;

import com.sky.dto.CategoryDTO;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userCategoryController")
@RequestMapping("/user/category")
@Api(tags = "C端-分类相关接口")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    /**
     * 条件查询
     * @param type
     * @return
     */
    @GetMapping("/list")
    public Result<List<CategoryDTO>> list(Integer type){
        List<CategoryDTO> categoryDTOList = categoryService.getListByType(type);
        return Result.success(categoryDTOList);
    }



}
