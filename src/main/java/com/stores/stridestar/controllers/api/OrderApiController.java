package com.stores.stridestar.controllers.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stores.stridestar.models.Order;
import com.stores.stridestar.models.enums.OrderStatus;
import com.stores.stridestar.services.OrderService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/orders")
public class OrderApiController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/getAll")
    public ResponseEntity<List<Order>> getAll() {
        List<Order> orders = orderService.getOrders();

        if(orders.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<List<Order>>(orders, HttpStatus.OK);
    }
    @GetMapping("/getById/{id}")
    public ResponseEntity<Order> getById(@PathVariable("id") Long id) {
        Order order = orderService.getOrderById(id);
        return new ResponseEntity<Order>(order, HttpStatus.OK);
    }
    @GetMapping("/countOrderByUser/{id}")
    public ResponseEntity<Integer> countOrderByUser(@PathVariable("id") Long id) {
        int count = 0;

        for (Order order : orderService.getOrders()) {
            if (order.getUser().getId().equals(id)) {
                count++;
            }
        }
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @PutMapping("/accept/{id}")
    public ResponseEntity<Order> acceptOrder(@PathVariable("id") Long id) {
        Order order = orderService.getOrderById(id);
        order.setStatus(OrderStatus.CONFIRMED);
        orderService.saveOrder(order);
        return new ResponseEntity<Order>(order, HttpStatus.OK);
    }

    @PostMapping("/paymentChange/{id}")
    public ResponseEntity<Order> paymentChange(@PathVariable("id") Long id, @RequestParam("payment") boolean payment) {
        Order order = orderService.getOrderById(id);
        order.setPaymentStatus(payment);
        orderService.saveOrder(order);
        return new ResponseEntity<Order>(order, HttpStatus.OK);
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<Order> cancelOrder(@PathVariable("id") Long id) {
        Order order = orderService.getOrderById(id);
        order.setStatus(OrderStatus.PENDING);
        orderService.saveOrder(order);
        return new ResponseEntity<Order>(order, HttpStatus.OK);
    }
}
