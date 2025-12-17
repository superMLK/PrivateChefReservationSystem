package com.example.privatechefreservationsystem.services.reservation;

import com.example.privatechefreservationsystem.dtos.ApiResponseCode;
import com.example.privatechefreservationsystem.dtos.reservation.CancelReservationRequest;
import com.example.privatechefreservationsystem.dtos.reservation.CancelReservationResponse;
import com.example.privatechefreservationsystem.dtos.reservation.CreateReservationRequest;
import com.example.privatechefreservationsystem.dtos.reservation.CreateReservationResponse;
import com.example.privatechefreservationsystem.entities.ReservationEntity;
import com.example.privatechefreservationsystem.repositories.ReservationRepository;
import com.example.privatechefreservationsystem.exceptions.BusinessException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    /** 訂位 */
    public CreateReservationResponse createReservation(CreateReservationRequest request) {
        // 檢查是否已存在相同訂位
        reservationRepository.findByGuestNameAndPhoneNumber(request.getGuestName(), request.getPhoneNumber())
                .ifPresent(i -> {
                    throw new BusinessException(ApiResponseCode.COMMON_ERROR, "已存在相同訂位");
                });

        ReservationEntity reservationEntity = ReservationEntity.builder()
                .guestName(request.getGuestName())
                .phoneNumber(request.getPhoneNumber())
                .bookingTime(request.getBookingTime())
                .count(request.getCount())
                .note(request.getNote())
                .build();

        ReservationEntity savedEntity = reservationRepository.save(reservationEntity);

        return new CreateReservationResponse(
                savedEntity.getGuestName(),
                savedEntity.getPhoneNumber(),
                savedEntity.getBookingTime(),
                savedEntity.getCount(),
                savedEntity.getNote()
        );
    }

    /** 取消訂位 */
    @Transactional
    public CancelReservationResponse cancelReservation(CancelReservationRequest request) {
        // 查找訂位
        ReservationEntity reservation = reservationRepository
                .findByGuestNameAndPhoneNumber(request.getGuestName(), request.getPhoneNumber())
                .orElseThrow(() -> new BusinessException(ApiResponseCode.COMMON_ERROR, "找不到該訂位"));

        reservationRepository.delete(reservation);

        return new CancelReservationResponse();
    }
}
