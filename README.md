# 私廚預約系統 (Private Chef Reservation System)

## 專案簡介

這是一個基於 Spring Boot 開發的私廚預約管理系統，提供使用者註冊、登入以及訂位管理功能。系統採用 JWT 進行身份驗證，並整合 Spring Security 提供安全保護。

## 主要功能

### 🔐 認證功能
- **使用者註冊**：支援新用戶註冊，密碼採用 Base64 加密傳輸
- **使用者登入**：JWT Token 認證機制，有效期 30 分鐘
- **密碼安全**：使用 BCrypt 進行密碼加密存儲

### 📅 訂位管理
- **建立訂位**：使用者可以預約指定時間的餐點服務
- **取消訂位**：透過姓名和電話號碼取消已建立的訂位
- **訂位驗證**：自動驗證人數、時間等必要欄位

### 📊 API 文檔
- 整合 Swagger UI，提供互動式 API 文檔
- 統一的 API 回應格式（rtnCode、rtnMsg、data）

## 技術棧

### 後端框架
- **Spring Boot 3.5.8**
- **Spring Security**：安全認證與授權
- **Spring Data JPA**：資料持久層
- **Spring AOP**：日誌記錄切面

### 資料庫
- **MySQL**：主要資料庫
- **Hibernate**：ORM 框架

### 安全與認證
- **JWT (JSON Web Token)**：Token 驗證
- **JJWT 0.12.6**：JWT 處理庫
- **BCrypt**：密碼加密

### API 文檔
- **Springdoc OpenAPI 2.7.0**：自動生成 API 文檔
- **Swagger UI**：API 測試介面

### 其他工具
- **Lombok**：簡化 Java 程式碼
- **Jackson**：JSON 處理
- **Jakarta Validation**：參數驗證

## 環境需求

- **JDK**: 21 或以上
- **Maven**: 3.6 或以上
- **MySQL**: 8.0 或以上

## 安裝與設定

### 1. 克隆專案

```bash
git clone <repository-url>
cd PrivateChefReservationSystem
```

### 2. 設定資料庫

創建 MySQL 資料庫：

```sql
CREATE DATABASE restaurant CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. 配置 application.properties

修改 `src/main/resources/application.properties`：

```properties
# MySQL 配置
spring.datasource.url=jdbc:mysql://localhost:3306/restaurant
spring.datasource.username=your_username
spring.datasource.password=your_password

# JWT 配置（請更換為您自己的密鑰）
jwt.secret=your_secret_key_here
jwt.expiration=1800000
```

### 4. 編譯與運行

使用 Maven 編譯：

```bash
./mvnw clean install
```

運行應用程式：

```bash
./mvnw spring-boot:run
```

或者使用 IntelliJ IDEA 直接運行 `PrivateChefReservationSystemApplication` 類。

## API 文檔

應用程式啟動後，可以透過以下網址存取 API 文檔：

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs (JSON)**: http://localhost:8080/v3/api-docs

## API 端點

### 認證相關 API

#### 註冊
```
POST /api/auth/register
Content-Type: application/json

{
  "username": "testuser",
  "email": "test@example.com",
  "password": "cGFzc3dvcmQxMjM="  // Base64 編碼後的密碼
}
```

#### 登入
```
POST /api/auth/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "cGFzc3dvcmQxMjM="  // Base64 編碼後的密碼
}
```

回應範例：
```json
{
  "rtnCode": "0000",
  "rtnMsg": "成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "username": "testuser"
  }
}
```

#### 測試受保護端點
```
GET /api/auth/test
Authorization: Bearer <your_jwt_token>
```

### 訂位管理 API

#### 建立訂位
```
POST /api/reservation/create
Authorization: Bearer <your_jwt_token>
Content-Type: application/json

{
  "guestName": "王小明",
  "phoneNumber": "0912345678",
  "count": 4,
  "bookingTime": "2025-12-20T18:00:00",
  "note": "靠窗座位"
}
```

#### 取消訂位
```
POST /api/reservation/cancel
Authorization: Bearer <your_jwt_token>
Content-Type: application/json

{
  "guestName": "王小明",
  "phoneNumber": "0912345678"
}
```

## API 回應格式

所有 API 回應都遵循統一格式：

```json
{
  "rtnCode": "0000",
  "rtnMsg": "成功",
  "data": { }
}
```

### 常見回應代碼

| 代碼 | 說明 |
|------|------|
| 0000 | 成功 |
| 1001 | 參數錯誤 |
| 2001 | 用戶已存在 |
| 2002 | 註冊失敗 |
| 2003 | 登入失敗 |
| 3001 | 訂位建立失敗 |
| 3002 | 找不到訂位資料 |
| 3003 | 取消訂位失敗 |
| 4001 | Token 已過期 |
| 4002 | Token 無效 |
| 4003 | 缺少 Token |
| 4004 | 無權限訪問此資源 |
| 4005 | 認證失敗 |
| 9999 | 系統錯誤 |

## 專案結構

```
src/main/java/com/example/privatechefreservationsystem/
├── aspects/              # AOP 切面（日誌記錄）
├── configs/              # 配置類（Security、Swagger、Jackson）
├── controllers/          # 控制器層
│   ├── auth/            # 認證相關 API
│   └── reservation/     # 訂位相關 API
├── dtos/                # 資料傳輸物件
│   ├── auth/           # 認證相關 DTO
│   └── reservation/    # 訂位相關 DTO
├── entities/            # 實體類（資料庫映射）
├── exceptions/          # 自訂例外處理
├── filters/             # 過濾器（JWT 認證）
├── repositories/        # 資料存取層
├── security/            # 安全相關配置
├── services/            # 業務邏輯層
│   ├── auth/           # 認證服務
│   └── reservation/    # 訂位服務
└── utils/              # 工具類
```

## 安全性考量

1. **密碼加密**：所有密碼使用 BCrypt 進行單向加密
2. **JWT Token**：採用 HS256 演算法簽名，有效期 30 分鐘
3. **CSRF 保護**：API 模式下已停用 CSRF
4. **CORS 配置**：可根據需求調整跨域設定
5. **密鑰管理**：建議將敏感配置移至環境變數或 Vault

## 開發建議

### Base64 密碼編碼（測試用）

前端可以使用 Base64 對密碼進行編碼後再傳送：

```javascript
// JavaScript 範例
const password = "password123";
const encodedPassword = btoa(password);
console.log(encodedPassword); // cGFzc3dvcmQxMjM=
```

### 使用 Postman 測試

1. 註冊或登入以獲取 JWT Token
2. 在 Postman 的 Authorization 標籤選擇 "Bearer Token"
3. 貼上獲得的 Token
4. 發送其他需要認證的請求

## 日誌記錄

系統使用 AOP 自動記錄以下資訊：

- 請求方法、類別、參數
- 執行時間
- 異常堆疊追蹤
- 回應結果

日誌級別可在 `application.properties` 中調整。

## 故障排除

### 端口被佔用
```bash
# 查找佔用 8080 端口的進程
lsof -i :8080

# 終止進程
kill -9 <PID>
```

### 資料庫連線失敗
- 確認 MySQL 服務已啟動
- 檢查資料庫名稱、使用者名稱和密碼是否正確
- 確認 MySQL 端口為 3306

### JWT Token 無效
- 確認 Token 沒有過期（30 分鐘有效期）
- 檢查 Authorization header 格式：`Bearer <token>`
- 確認 jwt.secret 配置正確

## 未來規劃

- [ ] 新增查詢訂位功能
- [ ] 新增修改訂位功能
- [ ] 實作訂位時間衝突檢查
- [ ] 新增使用者角色管理（管理員/一般用戶）
- [ ] 新增 Email 通知功能
- [ ] 實作 Refresh Token 機制
- [ ] 新增單元測試與整合測試
- [ ] Docker 容器化部署

## 授權

此專案僅供學習與練習使用。

## 聯絡方式

如有問題或建議，歡迎聯絡專案維護者。

---

**最後更新日期**: 2025-12-17

