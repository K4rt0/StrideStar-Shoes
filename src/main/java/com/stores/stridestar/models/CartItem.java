package com.stores.stridestar.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "quantity")
    private int quantity;
    
    @Column(name = "user_id")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "productVariant_id", nullable = false)
    @JsonIgnoreProperties("cartItems")
    private ProductVariant productVariant;
}
