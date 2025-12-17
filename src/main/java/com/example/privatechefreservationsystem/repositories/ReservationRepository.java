package com.example.privatechefreservationsystem.repositories;

import com.example.privatechefreservationsystem.entities.ReservationEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<@NonNull ReservationEntity, @NonNull Long> {
    Optional<ReservationEntity> findByGuestName(String guestName);
    Optional<ReservationEntity> findByBookingTime(LocalDateTime bookingTime);
    Optional<ReservationEntity> findByCount(Integer count);
    Optional<ReservationEntity> findByNote(String note);
    Optional<ReservationEntity> findByGuestNameAndPhoneNumber(String guestName, String phoneNumber);
}
