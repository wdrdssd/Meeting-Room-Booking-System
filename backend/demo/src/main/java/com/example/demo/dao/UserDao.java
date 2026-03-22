package com.example.demo.dao;

import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
public interface UserDao {
    @Select("SELECT * FROM user WHERE username = #{username}")
    User getUserByName(@Param("username") String username);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User getUserById(@Param("id") Long id);
}
