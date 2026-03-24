package com.example.demo.dto;

import com.example.demo.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "登录响应结果")
public class LoginResponse {
    @Schema(description = "JWT Token，后续请求需在 Header 中携带")
    private String token;
    @Schema(description = "用户信息")
    private User user;
    public LoginResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }
}
