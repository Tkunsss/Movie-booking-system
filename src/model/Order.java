package model;

import service.Cart;
import service.Customer;

// OOP: Class modeling order state; object = one booking/order.

// OOP: Composition (has-a): Order has Customer, Cart, Totals.

// Stores one order.
public class Order {
    // Composition: Order has Customer, Cart, and Totals objects.
    public int id;
    public String description;
    public String status;
    public Customer customer;
    public Cart cart;
    public Totals totals;

    public Order(int id, String description, String status,
                 Customer customer, Cart cart, Totals totals) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.customer = customer;
        this.cart = cart;
        this.totals = totals;
    }
}
