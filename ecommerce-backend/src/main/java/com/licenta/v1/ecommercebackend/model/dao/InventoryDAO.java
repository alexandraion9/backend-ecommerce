package com.licenta.v1.ecommercebackend.model.dao;

import com.licenta.v1.ecommercebackend.model.Book;
import com.licenta.v1.ecommercebackend.model.Inventory;
import org.springframework.data.repository.ListCrudRepository;

public interface InventoryDAO extends ListCrudRepository<Inventory,Long> {
    Inventory findByBook(Book book);
}
