package com.example.privatechefreservationsystem.controllers.reservation;

import com.example.privatechefreservationsystem.dtos.ApiResponse;
import com.example.privatechefreservationsystem.dtos.reservation.CancelReservationRequest;
import com.example.privatechefreservationsystem.dtos.reservation.CancelReservationResponse;
import com.example.privatechefreservationsystem.dtos.reservation.CreateReservationRequest;
import com.example.privatechefreservationsystem.dtos.reservation.CreateReservationResponse;
import com.example.privatechefreservationsystem.services.reservation.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
@Tag(name = "訂位管理", description = "提供訂位的新增、取消與查詢功能")
public class ReservationController {
    private final ReservationService reservationService;

    /** 訂位 */
    @PostMapping("/create")
    @Operation(
            summary = "建立新訂位",
            description = "使用者輸入姓名、人數與時間進行訂位。注意：人數必須大於 0。"
    )
    public ResponseEntity<ApiResponse<CreateReservationResponse>> createReservation(@RequestBody @Valid CreateReservationRequest request) {
        CreateReservationResponse response = reservationService.createReservation(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /** 取消訂位 */
    @PostMapping("/cancel")
    @Operation(
            summary = "取消訂位",
            description = "使用者輸入姓名與聯絡電話以取消訂位。"
    )
    public ResponseEntity<ApiResponse<CancelReservationResponse>> cancelReservation(@RequestBody @Valid CancelReservationRequest request) {
        CancelReservationResponse response = reservationService.cancelReservation(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
