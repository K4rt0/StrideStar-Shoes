package com.stores.stridestar.controllers.api;

import com.stores.stridestar.services.RevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/revenue")
public class RevenueApiController {
    @Autowired
    private RevenueService revenueService;

    @GetMapping()
    public Map<String, Object> getRevenue(@RequestParam int year, @RequestParam int month) {
        return revenueService.getRevenueData(year, month);
    }
}