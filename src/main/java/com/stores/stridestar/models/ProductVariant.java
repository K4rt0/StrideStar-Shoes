package com.stores.stridestar.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
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
    @Min(value = 0, message = "Giá của sản phẩm phải từ 0đ !")
    private double price;

    @Column(name = "quantity")
    @Min(value = 0, message = "Số lượng sản phẩm phải từ 0 !")
    private int quantity;

    @ManyToOne
    @JsonBackReference("product-variant")
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "productVariant")
    @JsonManagedReference("variant-attribute-variant")
    private List<VariantAttribute> variantAttributes = new ArrayList<>();

}
