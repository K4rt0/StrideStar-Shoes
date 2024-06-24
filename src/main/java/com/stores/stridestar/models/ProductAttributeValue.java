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
@Table(name = "productAttributeValues")
public class ProductAttributeValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 50)
    @NotBlank(message = "Giá trị thuộc tính không được bỏ trống !")
    @Size(min = 1, max = 50, message = "Giá trị thuộc tính phải từ 1 đến 50 ký tự !")
    private String value;

    @ManyToOne
    @JoinColumn(name = "productAttribute_id")
    @JsonBackReference
    private ProductAttribute productAttribute;

    @ManyToMany
    @JoinTable(
            name = "product_attribute_value_variant",
            joinColumns = @JoinColumn(name = "product_attribute_value_id"),
            inverseJoinColumns = @JoinColumn(name = "product_variant_id")
    )
    private Set<ProductVariant> variants = new HashSet<>();
}