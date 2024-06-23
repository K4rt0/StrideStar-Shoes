package com.stores.stridestar.controllers.admin;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.stores.stridestar.models.ProductAttribute;
import com.stores.stridestar.services.ProductAttributeService;

@Controller
@RequestMapping("/admin/attributes")
public class AdminAttributeController {
    @Autowired
    private ProductAttributeService attributeService;

    @GetMapping
    public String attributeList(Model model) {
        return "admin/attributes/attribute-list";
    }

    @GetMapping("/create")
    public String attributeCreateForm() {
        return "admin/attributes/attribute-create";
    }

    @GetMapping("/edit/{id}")
    public String editAttribute(@PathVariable("id") Long id, Model model) {
        Optional<ProductAttribute> productAttribute = attributeService.getAttributeById(id);

        if(productAttribute == null)
            return "redirect:/admin/attributes";

        return "admin/attributes/attribute-edit";
    }
}