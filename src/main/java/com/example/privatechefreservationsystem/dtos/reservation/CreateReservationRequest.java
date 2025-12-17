package com.example.privatechefreservationsystem.dtos.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateReservationRequest {
    /** 訂位人姓名 */
    @NotBlank(message = "訂位人姓名不能為空")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z ]+$", message = "訂位人姓名格式不正確，僅允許中文、英文及空格")
    @Schema(description = "訂位人姓名", example = "王小明")
    private String guestName;

    /** 聯絡電話 */
    @NotBlank(message = "聯絡電話不能為空")
    @Size(min = 10, max = 10, message = "聯絡電話長度必須為 10 個字元")
    @Pattern(regexp = "09\\d{8}", message = "聯絡電話格式不正確，必須以 09 開頭並包含 10 位數字")
    @Schema(description = "聯絡電話", example = "0912345678")
    private String phoneNumber;

    /** 預約時間 */
    @NotNull(message = "預約時間不能為空")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Future(message = "預約時間必須是未來的時間")
    @Schema(description = "預約時間，格式為 yyyy-MM-dd'T'HH:mm:ss", example = "2024-12-31T19:00:00")
    private LocalDateTime bookingTime;

    /** 預約人數 */
    @NotNull(message = "預約人數不能為空")
    @Max(value = 8, message = "預約人數不能超過 8 人")
    @Min(value = 1, message = "預約人數必須至少 1 人")
    @Schema(description = "預約人數", example = "4")
    private Integer count;

    /** 備註 */
    @Size(max = 200, message = "備註長度不能超過 200 個字元")
    @Schema(description = "備註", example = "請準備素食餐點")
    private String note;
}
