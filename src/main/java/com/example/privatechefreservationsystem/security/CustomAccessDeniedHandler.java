package com.example.privatechefreservationsystem.security;

import com.example.privatechefreservationsystem.dtos.ApiResponse;
import com.example.privatechefreservationsystem.dtos.ApiResponseCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 自訂訪問拒絕處理器
 * 處理權限不足時的響應
 */
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request,
                      HttpServletResponse response,
                      AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        ApiResponse<Void> apiResponse = ApiResponse.failure(
            ApiResponseCode.ACCESS_DENIED,
            "無權限訪問此資源"
        );

        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}

