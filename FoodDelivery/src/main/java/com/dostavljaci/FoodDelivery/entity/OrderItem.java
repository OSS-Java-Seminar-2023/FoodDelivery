package com.dostavljaci.FoodDelivery.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.util.UUID;

@Data
@Table(name = "orderitem", schema = "public")
@Entity
public class OrderItem {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, name = "quantity")
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "menu_item_id")
    private MenuItem menuItem;
}
