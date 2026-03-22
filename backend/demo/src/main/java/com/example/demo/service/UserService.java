package com.example.demo.service;

import com.example.demo.dao.UserDao;
import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public User getUserByName(String username) {
        return userDao.getUserByName(username);
    }
    public User getUserById(Long id) {
        return userDao.getUserById(id);
    }
}
