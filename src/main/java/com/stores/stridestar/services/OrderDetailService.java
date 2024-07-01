package com.stores.stridestar.services;

import com.stores.stridestar.models.OrderDetail;
import com.stores.stridestar.repositories.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public List<OrderDetail> getOrderDetails() {
        return orderDetailRepository.findAll();
    }

    public void addOrderDetail(OrderDetail orderDetail) {
        orderDetailRepository.save(orderDetail);
    }

    public OrderDetail saveOrderDetail(OrderDetail orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }

    public OrderDetail getOrderDetailById(Long id) {
        return orderDetailRepository.findById(id).orElse(null);
    }

    public void deleteOrderDetail(Long id) {
        orderDetailRepository.deleteById(id);
    }
}