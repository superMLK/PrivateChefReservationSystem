package com.example.privatechefreservationsystem.services.auth;

import com.example.privatechefreservationsystem.dtos.ApiResponseCode;
import com.example.privatechefreservationsystem.dtos.auth.AuthResponse;
import com.example.privatechefreservationsystem.dtos.auth.LoginRequest;
import com.example.privatechefreservationsystem.dtos.auth.RegisterRequest;
import com.example.privatechefreservationsystem.entities.UserEntity;
import com.example.privatechefreservationsystem.repositories.UserRepository;
import com.example.privatechefreservationsystem.exceptions.BusinessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 認證服務類
 * 處理用戶註冊和登入邏輯
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * 用戶註冊
     */
    public AuthResponse register(RegisterRequest request, String password) {
        // 檢查用戶是否已存在
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new BusinessException(ApiResponseCode.USER_ALREADY_EXISTS, "用戶名已存在");
        }

        // 檢查郵箱是否已存在
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BusinessException(ApiResponseCode.USER_ALREADY_EXISTS, "電子郵件已存在");
        }

        // 創建新用戶
        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(password))
                .build();

        // 保存用戶到數據庫
        UserEntity savedEntity = userRepository.save(user);

        // 生成 JWT token
        String jwtToken = jwtService.generateToken(user);

        return new AuthResponse(jwtToken, savedEntity.getUsername(), "註冊成功");
    }

    /**
     * 用戶登入
     */
    public AuthResponse login(LoginRequest request, String password) {
        // 使用 Spring Security 進行身份驗證
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            password
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BusinessException(ApiResponseCode.INCORRECT_PASSWORD, "用戶名或密碼錯誤");
        }


        // 從數據庫獲取用戶
        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException(ApiResponseCode.USER_NOT_FOUND, "用戶不存在"));

        // 生成 JWT token
        String jwtToken = jwtService.generateToken(user);

        return new AuthResponse(jwtToken, user.getUsername(), "登入成功");
    }
}

