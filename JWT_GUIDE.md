# JWT 認證機制使用指南

## 📋 概述

此專案已成功實現基於 Spring Security 和 JJWT 的 JWT 認證機制。

## 🏗️ 架構組件

### 1. **JwtService** - JWT 核心服務
- 生成 JWT token
- 驗證 token 有效性
- 提取用戶信息

### 2. **JwtAuthenticationFilter** - JWT 過濾器
- 攔截所有 HTTP 請求
- 從 Authorization header 提取 token
- 驗證 token 並設置 Spring Security 上下文

### 3. **SecurityConfig** - 安全配置
- 配置無狀態 Session (Stateless)
- 設置公開端點 `/api/auth/**`
- 集成 JWT 過濾器

### 4. **AuthService** - 認證業務邏輯
- 用戶註冊
- 用戶登入
- 密碼加密

### 5. **AuthController** - REST API 端點
- `POST /api/auth/register` - 註冊
- `POST /api/auth/login` - 登入
- `GET /api/auth/test` - 測試受保護端點

## 🚀 API 使用說明

### 1. 註冊新用戶

**請求:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john",
    "email": "john@example.com",
    "password": "password123"
  }'
```

**響應:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "john",
  "message": "註冊成功"
}
```

### 2. 用戶登入

**請求:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john",
    "password": "password123"
  }'
```

**響應:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "john",
  "message": "登入成功"
}
```

### 3. 訪問受保護資源

**請求:**
```bash
curl -X GET http://localhost:8080/api/auth/test \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

**響應:**
```
JWT 認證成功！你可以訪問受保護的資源。
```

## 🔧 配置說明

### application.properties

```properties
# JWT 密鑰 (建議在生產環境使用環境變數)
jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970

# JWT 過期時間 (毫秒) - 預設 24 小時
jwt.expiration=86400000
```

## 📝 使用 Postman 測試

### 步驟 1: 註冊用戶
1. 選擇 `POST` 方法
2. URL: `http://localhost:8080/api/auth/register`
3. Headers: `Content-Type: application/json`
4. Body (raw JSON):
```json
{
  "username": "testuser",
  "email": "test@example.com",
  "password": "test123"
}
```
5. 點擊 Send，複製返回的 token

### 步驟 2: 使用 Token 訪問受保護端點
1. 選擇 `GET` 方法
2. URL: `http://localhost:8080/api/auth/test`
3. Headers: 
   - Key: `Authorization`
   - Value: `Bearer <你的token>`
4. 點擊 Send

## 🔐 安全特性

1. ✅ **密碼加密** - 使用 BCrypt 加密存儲
2. ✅ **無狀態認證** - 不依賴 Session
3. ✅ **Token 過期機制** - 24小時自動過期
4. ✅ **用戶名唯一性** - 防止重複註冊
5. ✅ **CSRF 保護** - JWT 模式下已禁用

## 📂 項目結構

```
com.example.privatechefreservationsystem
├── configs/
│   └── SecurityConfig.java          # Spring Security 配置
├── controllers/
│   └── AuthController.java          # 認證 API
├── dtos/
│   ├── LoginRequest.java            # 登入請求 DTO
│   ├── RegisterRequest.java         # 註冊請求 DTO
│   └── AuthResponse.java            # 認證響應 DTO
├── entitys/
│   └── UserEntity.java              # 用戶實體
├── filters/
│   └── JwtAuthenticationFilter.java # JWT 過濾器
├── repositorys/
│   └── UserRepository.java          # 用戶數據訪問
└── services/
    ├── JwtService.java              # JWT 核心服務
    ├── AuthService.java             # 認證業務邏輯
    └── UserService.java             # 用戶服務
```

## 🎯 後續開發建議

1. **角色權限管理** - 添加 ROLE_ADMIN, ROLE_CHEF 等角色
2. **Token 刷新機制** - 實現 refresh token
3. **異常處理** - 統一的全局異常處理
4. **驗證碼功能** - 註冊/登入時的驗證碼
5. **日誌記錄** - 記錄認證相關操作
6. **密碼重置** - 郵箱找回密碼功能

## ⚠️ 注意事項

1. **生產環境配置**:
   - 將 `jwt.secret` 設置為強隨機字符串
   - 使用環境變數管理敏感配置
   - 設置 `spring.jpa.hibernate.ddl-auto=validate`

2. **數據庫初始化**:
   - 確保 MySQL 服務已啟動
   - 創建 `restaurant` 數據庫
   - UserEntity 會自動創建 `users` 表

3. **Token 管理**:
   - Token 存儲在客戶端 (LocalStorage/SessionStorage)
   - 每次請求都在 Header 中攜帶
   - Token 過期後需要重新登入

## 🚀 啟動應用

```bash
# 使用 Maven Wrapper
./mvnw spring-boot:run

# 或直接運行主類
PrivateChefReservationSystemApplication
```

應用將在 `http://localhost:8080` 啟動。

## 📞 測試範例

### 完整測試流程

```bash
# 1. 註冊用戶
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"chef1","email":"chef1@test.com","password":"chef123"}'

# 2. 登入 (獲取 token)
TOKEN=$(curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"chef1","password":"chef123"}' \
  | jq -r '.token')

# 3. 使用 token 訪問受保護資源
curl -X GET http://localhost:8080/api/auth/test \
  -H "Authorization: Bearer $TOKEN"
```

---

**實作完成！** 🎉

您的 Spring Boot 應用現在已經具備完整的 JWT 認證機制，可以開始開發其他業務功能了。

