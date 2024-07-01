package com.stores.stridestar.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {
    @GetMapping
    public String index() {
        return "admin/orders/order-list";
    }

    @GetMapping("/{id}")
    public String detail() {
        return "admin/orders/order-detail";
    }
}
