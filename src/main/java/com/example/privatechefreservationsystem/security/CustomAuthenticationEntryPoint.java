package com.example.privatechefreservationsystem.security;

import com.example.privatechefreservationsystem.dtos.ApiResponse;
import com.example.privatechefreservationsystem.dtos.ApiResponseCode;
import com.example.privatechefreservationsystem.exceptions.SecurityException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 自訂認證入口點
 * 處理認證失敗時的響應
 */
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                        HttpServletResponse response,
                        AuthenticationException authException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        // 檢查是否有自訂的 SecurityException
        SecurityException securityException =
            (SecurityException) request.getAttribute("securityException");

        ApiResponse<Void> apiResponse;

        if (securityException != null) {
            // 使用自訂的錯誤碼
            apiResponse = ApiResponse.failure(
                securityException.getErrorCode(),
                securityException.getErrorMessage()
            );
        } else {
            // 預設認證失敗
            apiResponse = ApiResponse.failure(
                ApiResponseCode.AUTHENTICATION_FAILED,
                "認證失敗"
            );
        }

        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}

