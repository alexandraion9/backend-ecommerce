package com.licenta.v1.ecommercebackend.service;

import com.licenta.v1.ecommercebackend.model.OrderStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class OrderStatusService {
    public List<OrderStatus> getPossibleOrderStatuses(OrderStatus currentStatus) {
        return switch (currentStatus) {
            case PENDING -> Arrays.asList(OrderStatus.PROCESSING, OrderStatus.CANCELED);
            case PROCESSING -> Arrays.asList(OrderStatus.SHIPPED, OrderStatus.CANCELED);
            case SHIPPED -> List.of(OrderStatus.DELIVERED);
            default -> Collections.emptyList();
        };
    }
}
