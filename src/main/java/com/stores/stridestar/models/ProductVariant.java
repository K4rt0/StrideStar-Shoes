package com.stores.stridestar.models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "productVariants")
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price")
    @NotBlank(message = "Giá sản phẩm không được bỏ trống !")
    private double price;

    @Column(name = "quantity")
    @NotBlank(message = "Số lượng sản phẩm không được bỏ trống !")
    @Size(min = 0, message = "Số lượng sản phẩm phải từ 0 !")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

    @ManyToMany(mappedBy = "variants")
    private Set<ProductAttributeValue> attributeValues = new HashSet<>();

}
