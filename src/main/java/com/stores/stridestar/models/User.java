package com.stores.stridestar.models;

import java.time.LocalDateTime;
import java.util.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "username", length = 50, unique = true)
    @NotBlank(message = "Tên người dùng không được bỏ trống !")
    @Size(min = 1, max = 50, message = "Tên người dùng phải từ 1 đến 50 ký tự !")
    private String username;

    @Column(name = "full_name", length = 50)
    @NotBlank(message = "Họ tên không được bỏ trống !")
    @Size(min = 1, max = 50, message = "Họ tên phải từ 1 đến 50 ký tự !")
    private String fullName;

    @Column(name = "phone_number", length = 10, unique = true)
    @Pattern(regexp = "^[0-9]*$", message = "Số điện thoại phải là số !")
    private String phoneNumber;

    @Column(name = "address", length = 100)
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

    @Transient
    private String newPassword;

    @Transient
    private String confirmPassword;

    @Column(name = "birth_date")
    @Temporal(TemporalType.DATE)
    private Date birthDate;

    private String avatar;

    @Column(name = "lockout", columnDefinition = "boolean default false", nullable = false)
    private boolean lockout;

    @OneToMany(mappedBy = "user")
    private List<DeliveryAddress> deliveryAddresses;

    @Column(name = "provider", length = 50)
    private String provider;
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> userRoles = this.getRoles();
        return userRoles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return username;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return
                false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}