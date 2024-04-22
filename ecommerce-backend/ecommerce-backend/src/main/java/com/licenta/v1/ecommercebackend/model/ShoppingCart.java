package com.licenta.v1.ecommercebackend.model;

import jakarta.persistence.*;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name =  "shopping_cart")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "shopping_cart_items", joinColumns = @JoinColumn(name = "cart_id"))
    @MapKeyJoinColumn(name = "book_id")
    @Column(name = "quantity")
    private Map<Book, Integer> items; // Mapare între produse și cantități

    @OneToOne(orphanRemoval = true)
    @JoinTable(name = "shopping_cart_localUser",
            joinColumns = @JoinColumn(name = "shoppingCart_id"),
            inverseJoinColumns = @JoinColumn(name = "localUser_id"))
    private LocalUser user;

    public LocalUser getUser() {
        return user;
    }

    public void setUser(LocalUser user) {
        this.user = user;
    }

    public ShoppingCart() { this.items = new HashMap<>(); }

    public void addItem(Book book, int quantity) {
        if (items.containsKey(book)) {
            int currentQuantity = items.get(book);
            items.put(book, currentQuantity + quantity);
        } else {
            items.put(book, quantity);
        }
    }

    public void removeItem(Long bookId, int quantity) {
        Book bookToRemove = null;
        for (Map.Entry<Book, Integer> entry : items.entrySet()) {
            if (entry.getKey().getId().equals(bookId)) {
                bookToRemove = entry.getKey();
                break;
            }
        }
        if (bookToRemove != null) {
            int currentQuantity = items.get(bookToRemove);
            if (currentQuantity > quantity) {
                items.put(bookToRemove, currentQuantity - quantity);
            } else {
                items.remove(bookToRemove);
            }
        }
    }

    public void updateItemQuantity(Book book, int newQuantity) {
        if (items.containsKey(book)) {
            items.put(book, newQuantity);
        }
    }

    public void clear() { items.clear(); }

    public Map<Book, Integer> getItems() { return items; }

    public void setItems(Map<Book, Integer> items) {this.items = items;}

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public BigDecimal calculateTotalPrice() {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Map.Entry<Book, Integer> entry : items.entrySet()) {
            Book book = entry.getKey();
            int quantity = entry.getValue();
            BigDecimal itemPrice = book.getPrice().multiply(BigDecimal.valueOf(quantity));
            totalPrice = totalPrice.add(itemPrice);
        }
        return totalPrice;
    }
}
