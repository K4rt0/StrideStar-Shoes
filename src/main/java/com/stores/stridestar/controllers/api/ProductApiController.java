package com.stores.stridestar.controllers.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.stores.stridestar.extensions.CommonFunction;
import com.stores.stridestar.extensions.ResourceNotFoundException;
import com.stores.stridestar.models.Product;
import com.stores.stridestar.models.ProductImage;
import com.stores.stridestar.models.ProductVariant;
import com.stores.stridestar.models.VariantAttribute;
import com.stores.stridestar.services.AmazonS3Service;
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

    @Autowired
    private AmazonS3Service amazonS3Service;

    @GetMapping("/getAll")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();

        if (products.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        
        return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) {
        Product product = productService.getProductById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product with ID does not exist."));
        if(product == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }
    
    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestPart("product") Product product,
                                                @RequestPart(value = "avatar", required = false) MultipartFile avatar,
                                                @RequestPart(value = "images", required = false) MultipartFile[] images) {
        // Save Product Avatar
        String seoUrl = "";
        if(avatar != null) {
            seoUrl = CommonFunction.SEOUrl(product.getName());
            product.setImage(amazonS3Service.uploadFile(avatar, seoUrl));
        }
        
        // Save Product Images
        if(images.length > 0) {
            product.setProductImages(new ArrayList<ProductImage>());
            ProductImage productImage;
            for (MultipartFile image : images) {
                productImage = new ProductImage();
                seoUrl = CommonFunction.SEOUrl(product.getName());
                productImage.setUrl(amazonS3Service.uploadFile(image, seoUrl));
                productImage.setProduct(product);
                product.getProductImages().add(productImage);
            }
        }
        Product productCreated = productService.addProduct(product);
        product.getProductImages().forEach(image -> {
            productImageService.addProductImage(image);
        });
        product.getProductVariants().forEach(variant -> {
            List<VariantAttribute> tmp = variant.getVariantAttributes();
            variant.setVariantAttributes(new ArrayList<>());
            variant.setProduct(productCreated);
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
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long id,
                                                @RequestPart("product") Product product,
                                                @RequestPart(value = "avatar", required = false) MultipartFile avatar,
                                                @RequestPart(value = "images", required = false) MultipartFile[] images,
                                                @RequestPart(value = "oldImages", required = false) ProductImage[] oldImage,
                                                @RequestPart(value = "variants", required = false) ProductVariant[] variants)
    {
        Product existingProduct = productService.getProductById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product with ID does not exist."));

        existingProduct.setName(product.getName());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setDisplay(product.isDisplay());
        existingProduct.setDescription(product.getDescription());
        
        // Save Product Avatar
        if(avatar != null) {
            amazonS3Service.deleteFile(amazonS3Service.getFileName(existingProduct.getImage()));

            String seoUrl = CommonFunction.SEOUrl(product.getName());
            existingProduct.setImage(amazonS3Service.uploadFile(avatar, seoUrl));
        }
        productImageService.getAllProductImages().forEach(image -> {
            if(image.getProduct().getId() == existingProduct.getId()) {
                boolean found = false;
                if(oldImage != null) {
                    for (ProductImage item : oldImage) {
                        if(image.getId() == item.getId()) {
                            found = true;
                            break; // Image found in oldImage, no need to delete, exit the loop
                        }
                    }
                }
                if (!found) {
                    // If the image is not found in oldImage, delete it
                    amazonS3Service.deleteFile(amazonS3Service.getFileName(image.getUrl()));
                    productImageService.deleteProductImageById(image.getId());
                }
            }
        });
        productVariantService.getAllProductVariants().forEach(variant -> {
            if(variant.getProduct().getId() == existingProduct.getId()) {
                boolean found = false;
                if(product.getProductVariants() != null) {
                    for (ProductVariant item : product.getProductVariants()) {
                        if (variant.getId() == item.getId()) {
                            item.setProduct(existingProduct);
                            item.setPrice(item.getPrice());
                            item.getVariantAttributes().forEach(attribute -> {
                                if (attribute.getId() == null) {
                                    VariantAttribute vAttribute = new VariantAttribute();
                                    vAttribute.setProductVariant(item);
                                    vAttribute.setProductAttributeValue(productAttributeValueService
                                        .getAttributeValueById(attribute.getProductAttributeValue().getId())
                                        .orElseThrow(() -> new IllegalStateException("Category with ID does not exist.")));
                                    VariantAttribute createdAttribute = variantAttributeService.addVariantAttribute(vAttribute);
                                    attribute.setId(createdAttribute.getId()); // Set the created ID to attribute
                                } else {
                                    attribute.setProductVariant(item);
                                    variantAttributeService.updateVariantAttribute(attribute);
                                }
                            });
                            productVariantService.updateProductVariant(item);
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    // If the variant is not found in product.getProductVariants(), delete it
                    variant.getVariantAttributes().forEach(attribute -> {
                        variantAttributeService.deleteVariantAttributeById(attribute.getId());
                    });
                    productVariantService.deleteById(variant.getId());
                }
            }
        });

        if(images != null) {
            for (MultipartFile productImage : images) {
                ProductImage image = new ProductImage();
                String seoUrl = CommonFunction.SEOUrl(product.getName());
                image.setUrl(amazonS3Service.uploadFile(productImage, seoUrl));
                image.setProduct(existingProduct);
                productImageService.addProductImage(image);
            }
        }
        if(variants != null) {
            for (ProductVariant variant : variants) {
                List<VariantAttribute> tmp = variant.getVariantAttributes();
                variant.setVariantAttributes(new ArrayList<>());
                variant.setProduct(existingProduct);
                ProductVariant variantCreated = productVariantService.addProductVariant(variant);

                tmp.forEach(item -> {
                    VariantAttribute attribute = new VariantAttribute();
                    attribute.setProductVariant(variantCreated);
                    attribute.setProductAttributeValue(productAttributeValueService
                        .getAttributeValueById(item.getProductAttributeValue().getId())
                            .orElseThrow(() -> new IllegalStateException("Category with ID does not exist.")));
                    variantAttributeService.addVariantAttribute(attribute);
                });
            }
        }
        return new ResponseEntity<Product>(existingProduct, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") Long id) {
        Product product = productService.getProductById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product with ID does not exist."));
        if(product == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        
        amazonS3Service.deleteFile(amazonS3Service.getFileName(product.getImage()));
        product.getProductImages().forEach(image -> {
            amazonS3Service.deleteFile(amazonS3Service.getFileName(image.getUrl()));
            productImageService.deleteProductImageById(image.getId());
        });
        product.getProductVariants().forEach(variant -> {
            variant.getVariantAttributes().forEach(attribute -> {
                variantAttributeService.deleteVariantAttributeById(attribute.getId());
            });
            productVariantService.deleteById(variant.getId());
        });
        productService.deleteById(id);
        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }
}