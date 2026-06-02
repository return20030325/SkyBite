package com.sky.dto;

import io.swagger.annotations.Api;
import lombok.Data;

import java.io.Serializable;


@Api(tags = "员工分页查询传递的数据模型")
@Data
public class EmployeePageQueryDTO implements Serializable {

    //员工姓名
    private String name;

    //页码
    private int page;

    //每页显示记录数
    private int pageSize;

}
