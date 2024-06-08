package com.licenta.v1.ecommercebackend.service;

import com.licenta.v1.ecommercebackend.model.LocalUser;
import com.licenta.v1.ecommercebackend.model.OrderStatus;
import com.licenta.v1.ecommercebackend.model.WebOrder;
import com.licenta.v1.ecommercebackend.model.dao.LocalUserDAO;
import com.licenta.v1.ecommercebackend.model.dao.WebOrderDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class WebOrderService {
    @Autowired
    private WebOrderDetailsService webOrderDetailsService;

    @Autowired
    private WebOrderDAO webOrderDAO;

    @Autowired
    private LocalUserDAO localUserDAO;

    public List<WebOrder> getAllOrders() {
        return webOrderDAO.findAll();
    }

    public List<WebOrder> getUserOrders(String username) {
        LocalUser user = localUserDAO.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        return webOrderDAO.findByUser(user);
    }

    public void changeOrderStatus(Long id, OrderStatus newStatus) {
        WebOrder order = webOrderDAO.findById(id).orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));

        List<OrderStatus> possibleStatuses = getPossibleOrderStatuses(order.getState());
        if (!possibleStatuses.contains(newStatus)) {
            throw new IllegalArgumentException("Invalid status transition: " + order.getState() + " to " + newStatus);
        }

        order.setState(newStatus);
        webOrderDAO.save(order);
        ResponseEntity.ok(order);
    }

    public List<OrderStatus> getPossibleOrderStatuses(OrderStatus currentStatus) {
        return switch (currentStatus) {
            case PENDING -> Arrays.asList(OrderStatus.PROCESSING, OrderStatus.CANCELED);
            case PROCESSING -> Arrays.asList(OrderStatus.SHIPPED, OrderStatus.CANCELED);
            case SHIPPED -> List.of(OrderStatus.DELIVERED);
            default -> Collections.emptyList();
        };
    }

    public Optional<WebOrder> getUserOrderById(Long orderId, String username) {
        return webOrderDAO.findByIdAndUserUsername(orderId, username);
    }

    public void cancelOrder(Long id, String username) {
        Optional<LocalUser> userOptional = localUserDAO.findByUsernameIgnoreCase(username);
        LocalUser user = userOptional.orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        WebOrder order = webOrderDAO.findById(id).orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));

        if (!order.getState().equals(OrderStatus.PENDING) && !order.getState().equals(OrderStatus.PROCESSING)) {
            throw new IllegalArgumentException("Order cannot be canceled in current state: " + order.getState());
        }

        if (!order.getUser().equals(user)) {
            throw new AccessDeniedException("You cannot cancel an order that doesn't belong to you.");
        }

        order.setState(OrderStatus.CANCELED);

        webOrderDAO.save(order);
    }


}

