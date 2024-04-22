package com.licenta.v1.ecommercebackend.model.dao;

import com.licenta.v1.ecommercebackend.model.LocalUser;
import com.licenta.v1.ecommercebackend.model.OrderStatus;
import com.licenta.v1.ecommercebackend.model.WebOrder;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface WebOrderDAO extends ListCrudRepository<WebOrder,Long> {

    List<WebOrder> findByState(OrderStatus orderStatus);
    List<WebOrder> findByStateAndUser(OrderStatus state, LocalUser user);
    Optional<WebOrder> findByIdAndUserUsername(Long id, String username);
}
