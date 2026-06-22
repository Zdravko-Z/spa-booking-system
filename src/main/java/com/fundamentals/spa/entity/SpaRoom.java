package com.fundamentals.spa.entity;

import com.fundamentals.spa.entity.enums.RoomStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "spa_rooms")
public class SpaRoom {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;

    @Column(name = "room_number", length = 20, nullable = false, unique = true)
    private String roomNumber;

    @Column(length = 100)
    private String name;

    @Column(nullable = false)
    private int capacity = 1;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus status = RoomStatus.AVAILABLE;
}
