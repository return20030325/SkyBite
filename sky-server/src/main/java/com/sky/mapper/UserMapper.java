package com.sky.mapper;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface UserMapper {

    @Select("select * from user where code=#{code}")
    User login(UserLoginDTO userLoginDTO);

    /**
     * 根据openid查询用户
     * @param openid
     * @return
     */
    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openid);

    /**
     * 插入数据
     * @param user
     */
    void insert(User user);

    @Select("select * from user where id=#{userId}")
    User getById(Long userId);

    Integer countByMap(Map map);

    @Delete("delete from user where id=#{id}")
    void deleteById(Long id);

    @Delete("delete from user ")
    void deleteAll();


}
