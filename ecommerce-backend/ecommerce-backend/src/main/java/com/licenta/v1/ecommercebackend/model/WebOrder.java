package com.licenta.v1.ecommercebackend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "web_order")
public class WebOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "state", nullable = false, length = 100)
    private OrderStatus state;

    @Temporal(TemporalType.DATE)
    @Column(name = "estimated_delivery_date")
    private Date estimatedDeliveryDate;

    @JsonIgnoreProperties("webOrders")
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private LocalUser user;

    @JsonManagedReference
    @OneToMany(mappedBy = "webOrder", cascade = CascadeType.ALL) // Assuming a bidirectional relationship with WebOrderDetails
    private List<WebOrderDetails> orderItems;

    @Column(name = "delivery_address_line1")
    private String deliveryAddressLine1;

    @Column(name = "delivery_address_line2")
    private String deliveryAddressLine2;

    @Column(name = "delivery_city")
    private String deliveryCity;

    @Column(name = "delivery_country")
    private String deliveryCountry;


    private BigDecimal totalPrice;

    public List<WebOrderDetails> getOrderItems() {return orderItems;}

    public void setOrderItems(List<WebOrderDetails> orderItems) {this.orderItems = orderItems;}

    public BigDecimal getTotalPrice() {return totalPrice;}

    public void setTotalPrice(BigDecimal totalPrice) {this.totalPrice = totalPrice;}

    public LocalUser getUser() {return user;}

    public void setUser(LocalUser user) {this.user = user;}

    public OrderStatus getState() {return state;}

    public void setState(OrderStatus state) {this.state = state;}

    public Date getDate() {return date;}

    public void setDate(Date date) {this.date = date;}

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getDeliveryAddressLine1() {return deliveryAddressLine1;}

    public void setDeliveryAddressLine1(String deliveryAddressLine1) {this.deliveryAddressLine1 = deliveryAddressLine1;}

    public String getDeliveryAddressLine2() {return deliveryAddressLine2;}

    public void setDeliveryAddressLine2(String deliveryAddressLine2) {this.deliveryAddressLine2 = deliveryAddressLine2;}

    public String getDeliveryCity() {return deliveryCity;}

    public void setDeliveryCity(String deliveryCity) {this.deliveryCity = deliveryCity;}

    public String getDeliveryCountry() {return deliveryCountry;}

    public void setDeliveryCountry(String deliveryCountry) {this.deliveryCountry = deliveryCountry;}

    public Date getEstimatedDeliveryDate() {return estimatedDeliveryDate;}

    public void setEstimatedDeliveryDate(Date estimatedDeliveryDate) {this.estimatedDeliveryDate = estimatedDeliveryDate;}
}