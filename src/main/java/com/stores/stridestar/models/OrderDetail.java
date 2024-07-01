package com.stores.stridestar.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
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

    @Column(name = "name")
    private String name;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "category")
    private String category;

    @Column(name = "quantity")
    @Min(value = 0, message = "Số lượng không được nhỏ hơn 0 !")
    private int quantity;

    @Column(name = "price")
    @Min(value = 0, message = "Giảm giá không được nhỏ hơn 0 !")
    private double price;

    @Column(name = "attributes")
    private String attributes;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnoreProperties("orderDetails")
    private Order order;
}