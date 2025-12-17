package com.example.privatechefreservationsystem.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString // 方便除錯用
public class ReservationEntity {
    /** 主鍵，自動跳號 */
    @Id // 對應 SQL: PRIMARY KEY
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 對應 SQL: AUTO_INCREMENT
    private Long id; // 對應 SQL: INT 或 BIGINT

    /** 訂位人姓名 */
    @Column(name = "guest_name", nullable = false) // 對應 SQL: VARCHAR(100) NOT NULL
    private String guestName; // 注意：Java習慣用駝峰式 (guestName)，SQL習慣用蛇式 (guest_name)

    @Column(name = "phone_number", nullable = false) // 對應 SQL: VARCHAR(15) NOT NULL
    private String phoneNumber;

    /** 預約時間 */
    @Column(name = "booking_time", nullable = false) // 對應 SQL: DATETIME NOT NULL
    private LocalDateTime bookingTime;

    /** 人數 */
    @Column(name = "count", nullable = false) // 對應 SQL: INT NOT NULL
    private Integer count;

    /** 備註 */
    @Column(name = "note", columnDefinition = "TEXT") // 對應 SQL: TEXT
    private String note;
}
