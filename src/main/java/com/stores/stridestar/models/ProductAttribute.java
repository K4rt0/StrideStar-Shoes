package com.stores.stridestar.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "productAttributes")
public class ProductAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 50)
    @NotBlank(message = "Tên thuộc tính không được bỏ trống !")
    @Size(min = 1, max = 50, message = "Tên thuộc tính phải từ 1 đến 50 ký tự !")
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "productAttribute")
    @JsonManagedReference
    private List<ProductAttributeValue> productAttributeValues;
}
