package com.stores.stridestar.controllers.admin;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.stores.stridestar.models.ProductAttribute;
import com.stores.stridestar.models.ProductAttributeValue;
import com.stores.stridestar.services.ProductAttributeService;
import com.stores.stridestar.services.ProductAttributeValueService;



@Controller
@RequestMapping("/admin/attributes")
public class AdminAttributeController {
    @Autowired
    private ProductAttributeService attributeService;

    @Autowired
    private ProductAttributeValueService attributeValueService;
    
    @GetMapping
    public String attributeList(Model model) {
        model.addAttribute("attributes", attributeService.getAllAttributes());
        return "admin/attributes/attribute-list";
    }

    @GetMapping("/create")
    public String attributeCreateForm() {
        return "admin/attributes/attribute-create";
    }
    
    @PostMapping("/create")
    @ResponseBody
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
    
        return ResponseEntity.ok(createdAttribute);
    }

    @GetMapping("/edit/{id}")
    public String editAttribute(@PathVariable("id") Long id, Model model) {
        Optional<ProductAttribute> productAttribute = attributeService.getAttributeById(id);
        model.addAttribute("productAttribute", productAttribute);
        model.addAttribute("attributeValues", productAttribute.get().getProductAttributeValues());
        return "admin/attributes/attribute-edit";
    }

    // Controller method to handle saving the edited attribute
    @PostMapping("/edit")
    public String saveEditedAttribute(@RequestBody ProductAttribute productAttribute) {
        attributeService.updateAttribute(productAttribute);
        return "redirect:/admin/attributes";
    }
}