package com.dostavljaci.FoodDelivery.entity;

import jakarta.persistence.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
@Table(name = "notification", schema = "public")
@Entity
public class Notification {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private Date timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Order order;

}
