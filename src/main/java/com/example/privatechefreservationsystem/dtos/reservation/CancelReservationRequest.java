package com.example.privatechefreservationsystem.dtos.reservation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelReservationRequest {
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
}
