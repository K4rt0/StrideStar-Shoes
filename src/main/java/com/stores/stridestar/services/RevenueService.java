package com.stores.stridestar.services;

import com.stores.stridestar.models.Order;
import com.stores.stridestar.models.enums.Payment;
import com.stores.stridestar.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RevenueService {
    @Autowired
    private OrderRepository orderRepository;

    public Map<String, Object> getRevenueData(int year, int month) {
        LocalDate currentDate = LocalDate.now();
        year = (year == 0) ? currentDate.getYear() : year;
        month = (month == 0) ? currentDate.getMonthValue() : month;
        // Calculate start and end date of the given month
        LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDate = startDate.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59);

        // Calculate start and end date of the previous month
        LocalDateTime prevStartDate = startDate.minusMonths(1);
        LocalDateTime prevEndDate = prevStartDate.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59);

        // Fetch orders for the current and previous month
        List<Order> currentMonthOrders = orderRepository.findAll().stream()
                .filter(order -> order.getCreatedDate().isAfter(startDate) && order.getCreatedDate().isBefore(endDate))
                .collect(Collectors.toList());

        List<Order> previousMonthOrders = orderRepository.findAll().stream()
                .filter(order -> order.getCreatedDate().isAfter(prevStartDate) && order.getCreatedDate().isBefore(prevEndDate))
                .collect(Collectors.toList());

        // Calculate revenue and discount for current and previous month
        double monthRevenue = currentMonthOrders.stream().mapToDouble(Order::getTotalPrice).sum();
        double monthPrevRevenue = previousMonthOrders.stream().mapToDouble(Order::getTotalPrice).sum();

        double monthRevenueCOD = currentMonthOrders.stream()
                .filter(order -> order.getPayment() == Payment.COD)
                .mapToDouble(Order::getTotalPrice)
                .sum();
        double monthPrevRevenueCOD = previousMonthOrders.stream()
                .filter(order -> order.getPayment() == Payment.COD)
                .mapToDouble(Order::getTotalPrice)
                .sum();

        double monthRevenueVnPay = currentMonthOrders.stream()
                .filter(order -> order.getPayment() == Payment.VNPAY)
                .mapToDouble(Order::getTotalPrice)
                .sum();
        double monthPrevRevenueVnPay = previousMonthOrders.stream()
                .filter(order -> order.getPayment() == Payment.VNPAY)
                .mapToDouble(Order::getTotalPrice)
                .sum();


        double monthDiscount = currentMonthOrders.stream().mapToDouble(Order::getDiscount).sum();
        double monthPrevDiscount = previousMonthOrders.stream().mapToDouble(Order::getDiscount).sum();

        // Calculate percentage changes

        double monthRevenuePercentage = (monthPrevRevenue != 0) ?
                Math.round(((monthRevenue - monthPrevRevenue) / monthPrevRevenue) * 100.0 * 100.0) / 100.0 : 0;
        double monthDiscountPercentage = (monthPrevDiscount != 0) ?
                Math.round(((monthDiscount - monthPrevDiscount) / monthPrevDiscount) * 100.0 * 100.0) / 100.0 : 0;
        double monthRevenueCODPercentage = (monthPrevRevenueCOD != 0) ?
                Math.round(((monthRevenueCOD - monthPrevRevenueCOD) / monthPrevRevenueCOD) * 100.0 * 100.0) / 100.0 : 0;
        double monthRevenueVnPayPercentage = (monthPrevRevenueVnPay != 0) ?
                Math.round(((monthRevenueVnPay - monthPrevRevenueVnPay) / monthPrevRevenueVnPay) * 100.0 * 100.0) / 100.0 : 0;

        // Prepare response data
        Map<String, Object> revenueData = new HashMap<>();
        revenueData.put("monthRevenue", monthRevenue);
        revenueData.put("monthPrevRevenue", monthPrevRevenue);
        revenueData.put("monthRevenuePercentage", monthRevenuePercentage);
        revenueData.put("monthDiscount", monthDiscount);
        revenueData.put("monthPrevDiscount", monthPrevDiscount);
        revenueData.put("monthDiscountPercentage", monthDiscountPercentage);
        revenueData.put("monthRevenueVnPay", monthRevenueVnPay);
        revenueData.put("monthRevenueCOD", monthRevenueCOD);
        revenueData.put("monthRevenueCODPercentage", monthRevenueCODPercentage);
        revenueData.put("monthRevenueVnPayPercentage", monthRevenueVnPayPercentage);
        return revenueData;
    }
}
