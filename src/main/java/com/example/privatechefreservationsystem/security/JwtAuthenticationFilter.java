package com.example.privatechefreservationsystem.security;

import com.example.privatechefreservationsystem.dtos.ApiResponseCode;
import com.example.privatechefreservationsystem.services.auth.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.privatechefreservationsystem.exceptions.SecurityException;

import java.io.IOException;

/**
 * JWT 認證過濾器
 * 攔截每個請求，檢查 JWT token 並設置 Spring Security 上下文
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // 1. 檢查 Header 格式
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            jwt = authHeader.substring(7);

            // 2. 檢查 Token 格式 (這裡若有問題也要拋出異常)
            if (jwt.trim().isEmpty() || jwt.split("\\.").length != 3) {
                throw new MalformedJwtException("Token 格式錯誤");
            }

            // 3. 解析 Token (這裡最常發生 ExpiredJwtException)
            username = jwtService.extractUsername(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            // 一切正常，放行
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            // 1. 定義你的自定義異常 (SecurityException)
            SecurityException customException = getSecurityException(e);

            // 3. 【關鍵動作】把這張寫著詳細原因的紙條，塞進 Request 口袋
            // 字串 "securityException" 必須跟 EntryPoint 裡 getAttribute 的 key 一模一樣
            request.setAttribute("securityException", customException);

            // 4. 【觸發警衛】拋出 Spring Security 專用的異常
            // 為什麼要這樣做？因為 Spring Security 的機制是：
            // 只有捕捉到 AuthenticationException 類型的錯誤，才會呼叫 AuthenticationEntryPoint
            throw new BadCredentialsException("Invalid Token");
        }
    }

    private static SecurityException getSecurityException(Exception e) {
        SecurityException customException;

        // 2. 判斷錯誤類型，封裝成不同的錯誤碼
        if (e instanceof ExpiredJwtException) {
            // 假設你的錯誤碼 Enum 叫 ApiResponseCode.TOKEN_EXPIRED
            customException = new SecurityException(ApiResponseCode.TOKEN_EXPIRED, "Token 已過期，請重新登入");
        } else if (e instanceof MalformedJwtException) {
            customException = new SecurityException(ApiResponseCode.TOKEN_INVALID, e.getMessage());
        } else {
            // 其他未知錯誤
            customException = new SecurityException(ApiResponseCode.AUTHENTICATION_FAILED, "認證失敗: " + e.getMessage());
        }
        return customException;
    }
}

