package com.stores.stridestar.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "deliveryAddresses")
public class DeliveryAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", length = 50)
    @NotBlank(message = "Tên không được bỏ trống !")
    @Size(min = 1, max = 50, message = "Tên phải từ 1 đến 50 ký tự !")
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Tên không hợp lệ !")
    private String fullName;

    @Column(name = "address", length = 255)
    @NotBlank(message = "Địa chỉ không được bỏ trống !")
    @Size(min = 1, max = 255, message = "Địa chỉ phải từ 1 đến 255 ký tự !")
    private String address;

    @Column(name = "phone_number", length = 15)
    @NotBlank(message = "Số điện thoại không được bỏ trống !")
    @Size(min = 1, max = 10, message = "Số điện thoại phải từ 1 đến 10 ký tự !")
    @Pattern(regexp = "^[0-9]*$", message = "Số điện thoại không hợp lệ !")
    private String phoneNumber;

    @Column(name = "is_default")
    private boolean isDefault;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
