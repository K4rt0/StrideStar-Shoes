package com.stores.stridestar.controllers.api;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stores.stridestar.extensions.ResourceNotFoundException;
import com.stores.stridestar.models.ProductAttribute;
import com.stores.stridestar.models.ProductAttributeValue;
import com.stores.stridestar.services.ProductAttributeService;
import com.stores.stridestar.services.ProductAttributeValueService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/attributes")
public class AttributeApiController {
    @Autowired
    private ProductAttributeService attributeService;

    @Autowired
    private ProductAttributeValueService attributeValueService;

    @GetMapping("/getAll")
    public ResponseEntity<List<ProductAttribute>> getAll() {
        List<ProductAttribute> attributes = attributeService.getAllAttributes();

		if(attributes.isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);

		return new ResponseEntity<List<ProductAttribute>>(attributes, HttpStatus.OK);
    }
    @PostMapping("/create")
    public ResponseEntity<ProductAttribute> createAttribute(@RequestBody ProductAttribute attribute) {
        ProductAttribute createdAttribute = attributeService.addAttribute(attribute);
        if (createdAttribute.getProductAttributeValues() != null) {
            for (ProductAttributeValue item : createdAttribute.getProductAttributeValues()) {
                ProductAttributeValue attributeValue = new ProductAttributeValue();
                attributeValue.setValue(item.getValue());
                attributeValue.setProductAttribute(createdAttribute);
                attributeValueService.addAttributeValue(attributeValue);
            }
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    @GetMapping("/getById/{id}")
    public ResponseEntity<ProductAttribute> getAttributeById(@PathVariable("id") Long id) {
        ProductAttribute attribute = attributeService.getAttributeById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Invalid Attribute Id:" + id));

		if(attribute == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		return new ResponseEntity<ProductAttribute>(attribute, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProductAttribute> updateAttribute(@PathVariable("id") Long id, @RequestBody ProductAttribute attribute) {
        Optional<ProductAttribute> existingAttributeOpt = attributeService.getAttributeById(id);

        if (!existingAttributeOpt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ProductAttribute existingAttribute = existingAttributeOpt.get();
        existingAttribute.setName(attribute.getName());

        // Lấy danh sách các giá trị hiện có từ database
        List<ProductAttributeValue> existingAttributeValues = existingAttribute.getProductAttributeValues();

        // Tạo danh sách các id của giá trị thuộc tính hiện tại từ request
        List<Long> newAttributeValueIds = attribute.getProductAttributeValues()
                                                .stream()
                                                .map(ProductAttributeValue::getId)
                                                .collect(Collectors.toList());

        // Tạo danh sách các giá trị cần xóa
        List<ProductAttributeValue> valuesToDelete = existingAttributeValues.stream()
            .filter(attributeValue -> !newAttributeValueIds.contains(attributeValue.getId()))
            .collect(Collectors.toList());

        // Xóa các giá trị thuộc tính không có trong danh sách mới
        valuesToDelete.forEach(attributeValue -> {
            existingAttribute.getProductAttributeValues().remove(attributeValue);
            attributeValueService.deleteAttributeValueById(attributeValue.getId());
        });

        // Thêm mới hoặc cập nhật các giá trị thuộc tính
        for (ProductAttributeValue newValue : attribute.getProductAttributeValues()) {
            if (newValue.getId() == 0) {
                // Tạo mới giá trị thuộc tính
                newValue.setProductAttribute(existingAttribute);
                attributeValueService.addAttributeValue(newValue);
            } else {
                // Cập nhật giá trị thuộc tính hiện có
                Optional<ProductAttributeValue> existingValueOpt = attributeValueService.getAttributeValueById(newValue.getId());
                if (existingValueOpt.isPresent()) {
                    ProductAttributeValue existingValue = existingValueOpt.get();
                    existingValue.setValue(newValue.getValue());
                    attributeValueService.updateAttributeValue(existingValue);
                }
            }
        }

        attributeService.updateAttribute(existingAttribute);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAttribute(@PathVariable("id") Long id) {
        Optional<ProductAttribute> existingAttributeOpt = attributeService.getAttributeById(id);

        if (!existingAttributeOpt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Xóa tất cả các giá trị thuộc tính trước khi xóa thuộc tính
        List<ProductAttributeValue> attributeValues = attributeValueService.getAllByProductAttributeId(id);
        attributeValues.forEach(attributeValue -> {
            attributeValueService.deleteAttributeValueById(attributeValue.getId());
        });

        attributeService.deleteAttributeById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
