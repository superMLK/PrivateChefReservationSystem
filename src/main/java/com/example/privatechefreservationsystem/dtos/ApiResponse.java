package com.example.privatechefreservationsystem.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "統一回應格式")
public class ApiResponse<T> {
    @Schema(description = "回應代碼", example = "0000")
    private String rtnCode;

    @Schema(description = "回應訊息", example = "成功")
    private String rtnMsg;

    @Schema(description = "回應資料")
    private T data;

    /** 成功的回應，不帶資料 */
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(ApiResponseCode.SUCCESS, "成功", null);
    }

    /** 成功的回應，帶有資料 */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ApiResponseCode.SUCCESS, "成功", data);
    }

    /** 成功的回應，帶有自訂訊息和資料 */
    public static <T> ApiResponse<T> success(String rtnMsg, T data) {
        return new ApiResponse<>(ApiResponseCode.SUCCESS, rtnMsg, data);
    }

    /** 失敗的回應，帶有錯誤訊息 */
    public static <T> ApiResponse<T> failure(String rtnCode, String rtnMsg) {
        return new ApiResponse<>(rtnCode, rtnMsg, null);
    }

    /** 失敗的回應，帶有錯誤訊息和資料 */
    public static <T> ApiResponse<T> failure(String rtnCode, String rtnMsg, T data) {
        return new ApiResponse<>(rtnCode, rtnMsg, data);
    }
}
