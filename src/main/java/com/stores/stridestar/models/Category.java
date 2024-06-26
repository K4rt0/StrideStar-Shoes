package com.stores.stridestar.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên danh mục không được bỏ trống !")
    @Size(min = 1, max = 50, message = "Tên danh mục phải từ 1 đến 50 ký tự !")
    private String name;

    private String avatar;

    @Column(name = "display", columnDefinition = "boolean default false")
    private boolean display;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "category")
    @JsonManagedReference("category-product")
    @JsonIgnore
    private List<Product> products;
}
