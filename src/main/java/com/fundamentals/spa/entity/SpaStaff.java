package com.fundamentals.spa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "spa_staff")
public class SpaStaff {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;

    @Column(length = 100)
    private String specialization;

    @Column(name = "first_name",length = 50, nullable = false)
    private String firstName;

    @Column(name = "last_name",length = 50, nullable = false)
    private String lastName;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}
