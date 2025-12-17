package com.example.privatechefreservationsystem.controllers.auth;

import com.example.privatechefreservationsystem.dtos.ApiResponse;
import com.example.privatechefreservationsystem.dtos.ApiResponseCode;
import com.example.privatechefreservationsystem.dtos.auth.AuthResponse;
import com.example.privatechefreservationsystem.dtos.auth.LoginRequest;
import com.example.privatechefreservationsystem.dtos.auth.RegisterRequest;
import com.example.privatechefreservationsystem.exceptions.BusinessException;
import com.example.privatechefreservationsystem.services.auth.AuthService;
import com.example.privatechefreservationsystem.utils.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 認證控制器
 * 處理註冊和登入請求
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "登入、註冊相關", description = "提供用戶註冊與登入功能")
public class AuthController {
    private final AuthService authService;

    /**
     * 用戶註冊
     */
    @PostMapping("/register")
    @Operation(
            summary = "用戶註冊",
            description = "用戶提供用戶名、電子郵件和密碼進行註冊。"
    )
    public ResponseEntity<ApiResponse<AuthResponse>> register(@RequestBody @Valid RegisterRequest request) {
        //檢測密碼是否為base64加密，如是則解密
        String password = "";
        try {
            password = AuthUtil.decryptPassword(request.getPassword());

            if (password.length() < 6 || password.length() > 20) {
                throw new BusinessException(ApiResponseCode.PARAM_INVALID, "密碼長度必須在6到20個字元之間");
            }
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ApiResponseCode.REGISTER_FAILED, "密碼格式錯誤");
        }

        AuthResponse response = authService.register(request, password);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 用戶登入
     */
    @PostMapping("/login")
    @Operation(
            summary = "用戶登入",
            description = "用戶提供用戶名和密碼進行登入。"
    )
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody @Valid LoginRequest request) {
        //檢測密碼是否為base64加密，如是則解密
        String password = "";
        try {
            password = AuthUtil.decryptPassword(request.getPassword());

            if (password.length() < 6 || password.length() > 20) {
                throw new BusinessException(ApiResponseCode.PARAM_INVALID, "密碼長度必須在6到20個字元之間");
            }
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ApiResponseCode.LOGIN_FAILED, "密碼格式錯誤");
        }

        AuthResponse response = authService.login(request, password);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 測試受保護的端點
     */
    @GetMapping("/test")
    public ResponseEntity<ApiResponse<String>> test() {
        return ResponseEntity.ok(ApiResponse.success());
    }
}

