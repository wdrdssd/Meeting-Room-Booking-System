package com.example.demo.dao;

import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Component
public interface UserDao {
    @Select("SELECT * FROM user WHERE username = #{username}")
    User getUserByName(@Param("username") String username);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User getUserById(@Param("id") Long id);

    @Update("UPDATE user SET password = #{password} WHERE id = #{id}")
    int updatePassword(@Param("id") Long id, @Param("password") String password);
}
