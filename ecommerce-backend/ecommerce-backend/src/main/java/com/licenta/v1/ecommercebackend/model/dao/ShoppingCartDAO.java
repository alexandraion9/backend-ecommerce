package com.licenta.v1.ecommercebackend.model.dao;

import com.licenta.v1.ecommercebackend.model.ShoppingCart;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface ShoppingCartDAO extends ListCrudRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findByUserId(Long userId);
}
