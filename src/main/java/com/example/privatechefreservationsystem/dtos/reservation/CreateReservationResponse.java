package com.example.privatechefreservationsystem.dtos.reservation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateReservationResponse {
    /** 訂位人姓名 */
    @Schema(description = "訂位人姓名", example = "王小明")
    private String guestName;

    /** 聯絡電話 */
    @Schema(description = "聯絡電話", example = "0912345678")
    private String phoneNumber;

    /** 預約時間 */
    @Schema(description = "預約時間，格式為 yyyy-MM-dd'T'HH:mm:ss", example = "2024-12-31T19:00:00")
    private LocalDateTime bookingTime;

    /** 人數 */
    @Schema(description = "預約人數", example = "4")
    private Integer count;

    /** 備註 */
    @Schema(description = "備註", example = "請準備素食餐點")
    private String note;
}
