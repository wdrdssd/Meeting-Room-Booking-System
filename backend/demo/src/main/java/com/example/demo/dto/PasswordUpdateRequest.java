package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Schema(description = "密码修改请求参数")
public class PasswordUpdateRequest {

    @Schema(description = "原密码", required = true)
    private String oldPassword;

    @Schema(description = "新密码", required = true)
    private String newPassword;
}
