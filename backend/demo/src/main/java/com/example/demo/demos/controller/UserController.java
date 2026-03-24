package com.example.demo.demos.controller;

import com.example.demo.config.Result;
import com.example.demo.dto.PasswordUpdateRequest;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.tools.Jwt;
import com.example.demo.tools.PasswordEncoder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    private Long getUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return Jwt.getUserId(token);
    }


    @PutMapping("/{id}/password")
    @Operation(summary = "修改密码")
    public Result<Void> updatePassword(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @RequestBody @Valid PasswordUpdateRequest request,
            HttpServletRequest httpRequest) {

        Long currentUserId = getUserId(httpRequest);
        if (!currentUserId.equals(id)) {
            return Result.error("无权修改他人密码");
        }

        User user = userService.getUserById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }

        if (!PasswordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return Result.error("原密码错误");
        }

        String encodedPassword = PasswordEncoder.encode(request.getNewPassword());
        userService.updatePassword(id, encodedPassword);

        return Result.success();
    }
}
