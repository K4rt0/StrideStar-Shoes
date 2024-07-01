package com.stores.stridestar.models;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.stores.stridestar.models.enums.OrderStatus;
import com.stores.stridestar.models.enums.Payment;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_date", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdDate;

    private String fullName;
    private String phoneNumber;
    private String address;
    private String shipping;
    private String note;

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private Payment payment;

    @Column(name = "payment_status")
    private boolean paymentStatus;

    @Column(name = "total_price")
    @Min(value = 0, message = "Giảm giá không được nhỏ hơn 0 !")
    private double totalPrice;

    @Column(name = "discount")
    @Min(value = 0, message = "Giảm giá không được nhỏ hơn 0 !")
    private double discount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order")
    @JsonIgnoreProperties("order")
    private List<OrderDetail> orderDetails;
}
