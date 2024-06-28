package com.stores.stridestar.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "VariantAttributes")
public class VariantAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "productVariant_id")
    @JsonIgnoreProperties("variantAttributes")
    private ProductVariant productVariant;

    @ManyToOne
    @JoinColumn(name = "productAttributeValue_id")
    @JsonIgnoreProperties("variantAttributes")
    private ProductAttributeValue productAttributeValue;
}