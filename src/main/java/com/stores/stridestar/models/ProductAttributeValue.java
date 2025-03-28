package com.stores.stridestar.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
    @JsonIgnoreProperties("productAttributeValues")
    private ProductAttribute productAttribute;

    @OneToMany(mappedBy = "productAttributeValue")
    @JsonIgnoreProperties("productAttributeValue")
    private List<VariantAttribute> variantAttributes = new ArrayList<>();
}