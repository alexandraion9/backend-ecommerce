package com.licenta.v1.ecommercebackend.api.controller;

import com.licenta.v1.ecommercebackend.model.OrderStatus;
import com.licenta.v1.ecommercebackend.model.WebOrder;
import com.licenta.v1.ecommercebackend.service.WebOrderDetailsService;
import com.licenta.v1.ecommercebackend.service.WebOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class WebOrderController {
    @Autowired
    private WebOrderService webOrderService;

    @Autowired
    private WebOrderDetailsService webOrderDetailsService;

    // Admin attributes
    @GetMapping
    public ResponseEntity<List<WebOrder>> getAllOrders() {
        List<WebOrder> orders = webOrderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> changeOrderStatus(@PathVariable Long id, @RequestParam("status") OrderStatus newStatus) {
        webOrderService.changeOrderStatus(id, newStatus);
        return ResponseEntity.ok().build();
    }

    // User attributes
    @PostMapping
    public ResponseEntity<WebOrder> createOrder(@RequestParam("username") String username,
                                                @RequestParam("deliveryAddressLine1") String deliveryAddressLine1,
                                                @RequestParam("deliveryAddressLine2") String deliveryAddressLine2,
                                                @RequestParam("deliveryCity") String deliveryCity,
                                                @RequestParam("deliveryCountry") String deliveryCountry) {
        WebOrder order = webOrderDetailsService.createOrderFromShoppingCart(username,deliveryAddressLine1, deliveryAddressLine2, deliveryCity, deliveryCountry);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id, @RequestParam("username") String username) {
        webOrderService.cancelOrder(id, username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WebOrder> getOrderById(@PathVariable Long id, @RequestParam("username") String username) {
        Optional<WebOrder> orderOptional = webOrderService.getUserOrderById(id, username);
        return orderOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user")
    public ResponseEntity<List<WebOrder>> getUserOrders(@RequestParam("username") String username) {
        List<WebOrder> userOrders = webOrderService.getUserOrders(username);
        return ResponseEntity.ok(userOrders);
    }

}
