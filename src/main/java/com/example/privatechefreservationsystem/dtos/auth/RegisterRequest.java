package com.example.privatechefreservationsystem.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 註冊請求 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    /** 帳號 */
    @NotBlank(message = "帳號不能為空")
    @Size(min = 6, max = 20, message = "帳號長度必須在 6 到 20 個字元之間")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "帳號只能包含英文字母與數字")
    @Schema(description = "帳號", example = "user123")
    private String username;

    /** 電子郵件 */
    @NotBlank(message = "電子郵件不能為空")
    @Email(message = "電子郵件格式不正確")
    @Schema(description = "電子郵件", example = "123@example.com")
    private String email;

    /** 密碼 */
    @NotBlank(message = "密碼不能為空")
    @Schema(description = "密碼", example = "P@ssw0rd!")
    private String password;
}

