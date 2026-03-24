package com.example.demo.demos.controller;

import com.example.demo.config.Result;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.tools.Jwt;
import com.example.demo.tools.PasswordEncoder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "用户管理", description = "用户登录、注册等相关接口")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "使用用户名和密码登录，返回 JWT Token 和用户信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "登录成功，返回 token 和用户信息"),
            @ApiResponse(responseCode = "400", description = "用户名或密码错误，或参数为空")
    })
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