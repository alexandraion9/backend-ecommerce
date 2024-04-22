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

    /*@Autowired
    private EmailService emailService;*/

    @Autowired
    ShoppingCartService shoppingCartService;

    @Transactional
    public WebOrder createOrderFromShoppingCart(String username,
                                String deliveryAddressLine1,
                                String deliveryAddressLine2,
                                String deliveryCity,
                                String deliveryCountry) {
        // Retrieve shopping cart for user
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByUsername(username);

        // Create a new WebOrder
        WebOrder order = new WebOrder();
        order.setUser(shoppingCart.getUser());
        order.setDate(new Date()); // Set the current date for the order

        // Set delivery address
        order.setDeliveryAddressLine1(deliveryAddressLine1);
        order.setDeliveryAddressLine2(deliveryAddressLine2);
        order.setDeliveryCity(deliveryCity);
        order.setDeliveryCountry(deliveryCountry);

        // Calculate total price and update inventory
        BigDecimal totalPrice = BigDecimal.ZERO;
        List<WebOrderDetails> orderItems = new ArrayList<>();
        for (Map.Entry<Book, Integer> entry : shoppingCart.getItems().entrySet()) {
            Book book = entry.getKey();
            int quantity = entry.getValue();

            // Calculate the total price for the item
            BigDecimal itemTotalPrice = book.getPrice().multiply(BigDecimal.valueOf(quantity));
            totalPrice = totalPrice.add(itemTotalPrice);

            // Check if there is sufficient inventory
            Inventory inventory = inventoryDAO.findByBook(book);
            if (inventory.getQuantity() < quantity) {
                throw new InsufficientInventoryException("Insufficient inventory for book: " + book.getTitle());
            }

            // Update the inventory quantity
            inventory.setQuantity(inventory.getQuantity() - quantity);
            inventoryDAO.save(inventory);

            // Create and add WebOrderDetails for the item
            WebOrderDetails orderDetail = new WebOrderDetails();
            orderDetail.setBook(book);
            orderDetail.setAmount(itemTotalPrice);
            orderDetail.setWebOrder(order);
            orderItems.add(orderDetail);
        }

        // Set the order's total price and items
        order.setTotalPrice(totalPrice);
        order.setOrderItems(orderItems);

        // Set initial order state to PENDING
        order.setState(OrderStatus.PENDING);

        // Calculați data estimată de livrare (10 zile lucrătoare de la data comenzii)
        Calendar estimatedDeliveryDate = Calendar.getInstance();
        estimatedDeliveryDate.setTime(new Date());
        estimatedDeliveryDate.add(Calendar.DAY_OF_MONTH, 10);

        // Setarea datei estimative de livrare în comandă
        order.setEstimatedDeliveryDate(estimatedDeliveryDate.getTime());

        // Save the order in the database
        webOrderDAO.save(order);

        // Clear shopping cart after order creation
        shoppingCart.clear();

        /*String orderDetails = "Starea comenzii: " + order.getState() + "\n"
                + "Data estimativă de livrare: " + order.getEstimatedDeliveryDate() + "\n"
                + "Plata totală: " + order.getTotalPrice() + "\n";
        emailService.sendOrderConfirmationEmail(username, orderDetails);*/

        // Return the created order
        return order;
    }

}

