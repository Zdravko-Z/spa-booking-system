package com.fundamentals.spa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "spa_booking_treatments",
uniqueConstraints = @UniqueConstraint(columnNames = {"booking_id", "treatment_id"}))
public class SpaBookingTreatment {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;

    @Column(name = "price_at_booking", precision = 10, scale = 2, nullable = false)
    private BigDecimal priceAtBooking;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private SpaBooking booking;

    @ManyToOne
    @JoinColumn(name = "treatment_id", nullable = false)
    private SpaTreatment treatment;
}
