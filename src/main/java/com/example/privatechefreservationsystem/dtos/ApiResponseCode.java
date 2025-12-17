package com.example.privatechefreservationsystem.dtos;

public class ApiResponseCode {
    /** 成功 */
    public static final  String SUCCESS = "0000";

    /** 註冊失敗 */
    public static final String REGISTER_FAILED = "1001";

    /** 登入失敗 */
    public static final String LOGIN_FAILED = "1002";

    /** 無效的令牌 */
    public static final String INVALID_TOKEN = "1003";

    /** 使用者不存在 */
    public static final String USER_NOT_FOUND = "1004";

    /** 使用者已存在 */
    public static final String USER_ALREADY_EXISTS = "1005";

    /** 密碼錯誤 */
    public static final String INCORRECT_PASSWORD = "1006";

    /** Token 已過期 */
    public static final String TOKEN_EXPIRED = "4001";

    /** Token 無效 */
    public static final String TOKEN_INVALID = "4002";

    /** 無權限訪問此資源 */
    public static final String ACCESS_DENIED = "4004";

    /** 認證失敗 */
    public static final String AUTHENTICATION_FAILED = "4005";

    /** 一般錯誤 */
    public static final String COMMON_ERROR = "5001";

    /** 參數錯誤 */
    public static final String PARAM_INVALID = "6001";

    /** 其他失敗 */
    public static final String SYSTEM_ERROR = "9999";
}
