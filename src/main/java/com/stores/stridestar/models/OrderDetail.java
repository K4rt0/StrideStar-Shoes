package com.stores.stridestar.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orderDetails")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity")
    @NotBlank(message = "Số lượng không được bỏ trống !")
    private int quantity;

    @Column(name = "price")
    @NotBlank(message = "Giá tiền không được bỏ trống !")
    private double price;

    @Column(name = "attributes")
    private String attributes;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}