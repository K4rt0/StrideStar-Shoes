package com.stores.stridestar.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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

    @Column(name = "name", length = 50)
    @NotBlank(message = "Giá trị thuộc tính không được bỏ trống !")
    @Size(min = 1, max = 50, message = "Giá trị thuộc tính phải từ 1 đến 50 ký tự !")
    private String value;

    @Column(name = "price")
    @NotBlank(message = "Giá sản phẩm không được bỏ trống !")
    private double price;

    @Column(name = "quantity")
    @NotBlank(message = "Số lượng sản phẩm không được bỏ trống !")
    @Size(min = 0, message = "Số lượng sản phẩm phải từ 0 !")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "productVariant")
    private List<VariantAttribute> variantAttributes;
}
