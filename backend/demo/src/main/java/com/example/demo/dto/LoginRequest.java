package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "登录请求参数")
public class LoginRequest {
    @Schema(description = "用户名/账号", required = true)
    private String username;
    @Schema(description = "密码", required = true)
    private String password;
}
