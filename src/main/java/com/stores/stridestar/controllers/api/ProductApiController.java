package com.stores.stridestar.controllers.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.stores.stridestar.extensions.CommonFunction;
import com.stores.stridestar.models.Product;
import com.stores.stridestar.models.ProductImage;
import com.stores.stridestar.models.ProductVariant;
import com.stores.stridestar.models.VariantAttribute;
import com.stores.stridestar.services.ProductAttributeValueService;
import com.stores.stridestar.services.ProductImageService;
import com.stores.stridestar.services.ProductService;
import com.stores.stridestar.services.ProductVariantService;
import com.stores.stridestar.services.VariantAttributeService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/products")
public class ProductApiController {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductImageService productImageService;
    
    @Autowired
    private ProductVariantService productVariantService;

    @Autowired
    private VariantAttributeService variantAttributeService;

    @Autowired
    private ProductAttributeValueService productAttributeValueService;

    @GetMapping("/getAll")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();

        if (products.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        
        return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
    }
    
    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestPart("product") Product product,
                                                @RequestPart(value = "avatar", required = false) MultipartFile avatar,
                                                @RequestPart(value = "images", required = false) MultipartFile[] images) {
        // Save Product Avatar
        try {
            String seoUrl = CommonFunction.SEOUrl(product.getName());
            product.setImage(CommonFunction.saveFile(seoUrl, "/products", avatar));
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        // Save Product Images
        if(images.length > 0) {
            product.setProductImages(new ArrayList<ProductImage>());
            ProductImage productImage;
            int i = 1;
            for (MultipartFile image : images) {
                try {
                    productImage = new ProductImage();
                    String seoUrl = CommonFunction.SEOUrl(product.getName()) + "-" + i++;
                    productImage.setUrl(CommonFunction.saveFile(seoUrl, "/product-details", image));
                    productImage.setProduct(product);
                    product.getProductImages().add(productImage);
                } catch (IOException e) {
                    e.printStackTrace();
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }
        product = productService.addProduct(product);
        product.getProductImages().forEach(image -> {
            productImageService.addProductImage(image);
        });
        product.getProductVariants().forEach(variant -> {
            /* variant.getVariantAttributes().forEach(attribute -> {
                attribute.setProductVariant(variant);
            }); */
            List<VariantAttribute> tmp = variant.getVariantAttributes();
            variant.setVariantAttributes(new ArrayList<>());
            ProductVariant variantCreated = productVariantService.addProductVariant(variant);

            tmp.forEach(item -> {
                VariantAttribute attribute = new VariantAttribute();
                attribute.setProductVariant(variantCreated);
                attribute.setProductAttributeValue(productAttributeValueService
                    .getAttributeValueById(item.getProductAttributeValue().getId())
                        .orElseThrow(() -> new IllegalStateException("Category with ID does not exist.")));
                variantAttributeService.addVariantAttribute(attribute);
            });
        });
        // productVariantService.addAllProductVariant(product.getProductVariants());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
