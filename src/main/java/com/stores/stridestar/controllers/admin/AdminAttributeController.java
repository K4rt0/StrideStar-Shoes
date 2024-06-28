package com.stores.stridestar.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/admin/attributes")
public class AdminAttributeController {
    @GetMapping
    public String attributeList(Model model) {
        return "admin/attributes/attribute-list";
    }

    @GetMapping("/create")
    public String attributeCreateForm() {
        return "admin/attributes/attribute-create";
    }

    @GetMapping("/edit/{id}")
    public String editAttribute(@PathVariable("id") Long id) {
        return "admin/attributes/attribute-edit";
    }
}