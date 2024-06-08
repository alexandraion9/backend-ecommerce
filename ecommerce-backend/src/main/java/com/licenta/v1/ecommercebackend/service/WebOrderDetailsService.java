package com.licenta.v1.ecommercebackend.service;

import com.licenta.v1.ecommercebackend.api.exception.InsufficientInventoryException;
import com.licenta.v1.ecommercebackend.model.*;
import com.licenta.v1.ecommercebackend.model.dao.BookDAO;
import com.licenta.v1.ecommercebackend.model.dao.InventoryDAO;
import com.licenta.v1.ecommercebackend.model.dao.LocalUserDAO;
import com.licenta.v1.ecommercebackend.model.dao.WebOrderDAO;
import com.licenta.v1.ecommercebackend.model.dto.CreateOrderDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class WebOrderDetailsService {

    @Autowired
    private WebOrderDAO webOrderDAO;


    @Autowired
    private InventoryDAO inventoryDAO;

    @Autowired
    ShoppingCartService shoppingCartService;

    @Transactional
    public WebOrder createOrderFromShoppingCart(String username,
                                String deliveryAddressLine1,
                                String deliveryAddressLine2,
                                String deliveryCity,
                                String deliveryCountry) {
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByUsername(username);

        WebOrder order = new WebOrder();
        order.setUser(shoppingCart.getUser());
        order.setDate(new Date()); // Set the current date for the order

        order.setDeliveryAddressLine1(deliveryAddressLine1);
        order.setDeliveryAddressLine2(deliveryAddressLine2);
        order.setDeliveryCity(deliveryCity);
        order.setDeliveryCountry(deliveryCountry);

        BigDecimal totalPrice = BigDecimal.ZERO;
        List<WebOrderDetails> orderItems = new ArrayList<>();
        for (Map.Entry<Book, Integer> entry : shoppingCart.getItems().entrySet()) {
            Book book = entry.getKey();
            int quantity = entry.getValue();

            BigDecimal itemTotalPrice = book.getPrice().multiply(BigDecimal.valueOf(quantity));
            totalPrice = totalPrice.add(itemTotalPrice);

            Inventory inventory = inventoryDAO.findByBook(book);
            if (inventory.getQuantity() < quantity) {
                throw new InsufficientInventoryException("Insufficient inventory for book: " + book.getTitle());
            }

            inventory.setQuantity(inventory.getQuantity() - quantity);
            inventoryDAO.save(inventory);

            WebOrderDetails orderDetail = new WebOrderDetails();
            orderDetail.setBook(book);
            orderDetail.setAmount(itemTotalPrice);
            orderDetail.setWebOrder(order);
            orderItems.add(orderDetail);
        }

        order.setTotalPrice(totalPrice);
        order.setOrderItems(orderItems);

        order.setState(OrderStatus.PENDING);

        Calendar estimatedDeliveryDate = Calendar.getInstance();
        estimatedDeliveryDate.setTime(new Date());
        estimatedDeliveryDate.add(Calendar.DAY_OF_MONTH, 10);

        order.setEstimatedDeliveryDate(estimatedDeliveryDate.getTime());

        webOrderDAO.save(order);

        shoppingCart.clear();

        return order;
    }

}

