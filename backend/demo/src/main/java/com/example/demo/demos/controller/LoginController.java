package com.example.demo.demos.controller;

import com.example.demo.config.Result;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.tools.Jwt;
import com.example.demo.tools.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

        if (loginRequest.getUsername() == null || loginRequest.getUsername().trim().isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
            return Result.error("密码不能为空");
        }

        User user = userService.getUserByName(loginRequest.getUsername());

        if (user == null) {
            System.out.println("用户不存在");
            return Result.error("用户名或密码错误");
        }


        boolean matched = PasswordEncoder.matches(loginRequest.getPassword(), user.getPassword());

        if (!matched) {
            return Result.error("用户名或密码错误");
        }

        String token = Jwt.generateToken(user.getId(), user.getRole());

        user.setPassword(null);

        return Result.success(new LoginResponse(token, user));
    }
}