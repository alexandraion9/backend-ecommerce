package com.licenta.v1.ecommercebackend.api.controller;

import com.licenta.v1.ecommercebackend.model.LocalUser;
import com.licenta.v1.ecommercebackend.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public ResponseEntity<?> addItemToCart(@RequestParam Long bookId, @RequestParam int quantity, Authentication authentication) {
        String username = authentication.getName(); // Obține numele de utilizator din obiectul Authentication
        shoppingCartService.addProductToCart(username, bookId, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/update/{bookId}")
    public ResponseEntity<?> updateItemQuantity(@PathVariable Long bookId, @RequestParam int newQuantity, Authentication authentication) {
        String username = authentication.getName();
        shoppingCartService.updateProductQuantity(username,bookId, newQuantity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove/{bookId}")
    public ResponseEntity<?> removeItemFromCart(@PathVariable Long bookId, @RequestParam int quantity, Authentication authentication) {
        String username = authentication.getName();
        shoppingCartService.removeProductFromCart(username,bookId,quantity);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
