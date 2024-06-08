package com.licenta.v1.ecommercebackend.service;

import com.licenta.v1.ecommercebackend.api.exception.InsufficientInventoryException;
import com.licenta.v1.ecommercebackend.model.Book;
import com.licenta.v1.ecommercebackend.model.LocalUser;
import com.licenta.v1.ecommercebackend.model.ShoppingCart;
import com.licenta.v1.ecommercebackend.model.dao.BookDAO;
import com.licenta.v1.ecommercebackend.model.dao.LocalUserDAO;
import com.licenta.v1.ecommercebackend.model.dao.ShoppingCartDAO;

import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShoppingCartService {

    private final ShoppingCartDAO shoppingCartDAO;
    private final BookDAO bookDAO;
    private final LocalUserDAO localUserDAO;

    public ShoppingCartService(ShoppingCartDAO shoppingCartDAO, LocalUserDAO localUserDAO, BookDAO bookDAO, LocalUserDAO localUserDAO1) {
        this.shoppingCartDAO = shoppingCartDAO;
        this.bookDAO = bookDAO;
        this.localUserDAO = localUserDAO1;
    }

    public Book getBookById(Long bookId) {
        Optional<Book> optionalBook = bookDAO.findById(bookId);
        return optionalBook.orElse(null);
    }

    @Transactional
    public void addProductToCart(String username, Long bookId, int quantity) {
        LocalUser user = getUser(username);
        ShoppingCart shoppingCart = getOrCreateShoppingCart(user);

        Optional<Book> optionalBook = bookDAO.findById(bookId);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            if (book.getInventory().getQuantity() >= quantity) {
                shoppingCart.addItem(book, quantity);
                shoppingCartDAO.save(shoppingCart);
            } else {
                throw new InsufficientInventoryException("Insufficient inventory for book with ID: " + bookId);
            }
        } else {
            throw new IllegalArgumentException("Book not found: " + bookId);
        }
    }

    @Transactional
    public void removeProductFromCart(String username, Long bookId, int quantity) {
        LocalUser user = getUser(username);
        ShoppingCart shoppingCart = getOrCreateShoppingCart(user);

        shoppingCart.removeItem(bookId, quantity);
        shoppingCartDAO.save(shoppingCart);
    }

    @Transactional
    public void updateProductQuantity(String username, Long bookId, int newQuantity) {
        LocalUser user = getUser(username);
        ShoppingCart shoppingCart = getOrCreateShoppingCart(user);

        Optional<Book> optionalBook = bookDAO.findById(bookId);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            shoppingCart.updateItemQuantity(book, newQuantity);
            shoppingCartDAO.save(shoppingCart);
        } else {
            throw new IllegalArgumentException("Book not found: " + bookId);
        }
    }

    private LocalUser getUser(String username) {
        Optional<LocalUser> userOptional = localUserDAO.findByUsernameIgnoreCase(username);
        return userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    private ShoppingCart getOrCreateShoppingCart(LocalUser user) {
        if (user.getShoppingCart() == null) {
            Optional<ShoppingCart> existingCartOptional = shoppingCartDAO.findByUserId(user.getId());

            if (existingCartOptional.isPresent()) {
                return existingCartOptional.get();
            } else {
                ShoppingCart shoppingCart = new ShoppingCart();
                shoppingCart.setUser(user);
                return shoppingCart;
            }
        } else {
            return user.getShoppingCart();
        }
    }

    public ShoppingCart getShoppingCartByUsername(String username) {
        LocalUser user = getUser(username);
        return getOrCreateShoppingCart(user);
    }

}