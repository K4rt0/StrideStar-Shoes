package com.stores.stridestar.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 50)
    @NotBlank(message = "Tên sản phẩm không được bỏ trống !")
    @Size(min = 1, max = 50, message = "Tên sản phẩm phải từ 1 đến 50 ký tự !")
    private String name;
    
    @Column(name = "display", columnDefinition = "boolean default false")
    private boolean display;
    
    private String description;
    private String image;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
