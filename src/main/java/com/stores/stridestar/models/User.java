package com.stores.stridestar.models;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_date", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdDate;

    @Column(name = "username", length = 50, unique = true)
    @NotBlank(message = "Tên người dùng không được bỏ trống !")
    @Size(min = 1, max = 50, message = "Tên người dùng phải từ 1 đến 50 ký tự !")
    private String userName;

    @Column(name = "full_name", length = 50)
    @NotBlank(message = "Họ tên không được bỏ trống !")
    @Size(min = 1, max = 50, message = "Họ tên phải từ 1 đến 50 ký tự !")
    private String fullName;

    @Column(name = "phone_number", length = 10, unique = true)
    @NotBlank(message = "Số điện thoại không được bỏ trống !")
    @Pattern(regexp = "^[0-9]*$", message = "Số điện thoại phải là số !")
    private String phoneNumber;

    @Column(name = "address", length = 100)
    @NotBlank(message = "Địa chỉ không được bỏ trống !")
    @Size(min = 1, max = 100, message = "Địa chỉ phải từ 1 đến 100 ký tự !")
    private String address;

    @Column(name = "email", length = 50, unique = true)
    @NotBlank(message = "Email không được bỏ trống !")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email không hợp lệ !")
    private String email;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private boolean emailConfirmed;

    @Column(name = "password", length = 250)
    @NotBlank(message = "Mật khẩu không được bỏ trống !")
    @Size(min = 6, message = "Mật khẩu phải từ 6 ký tự trở lên !")
    private String password;

    @Column(name = "birth_date")
    @NotBlank(message = "Ngày sinh không được bỏ trống !")
    @Temporal(TemporalType.DATE)
    private Date birthDate;

    private String avatar;

    @Column(name = "lockout", columnDefinition = "boolean default false", nullable = false)
    private boolean lockout;

    @OneToMany(mappedBy = "user")
    private List<DeliveryAddress> deliveryAddresses;
}