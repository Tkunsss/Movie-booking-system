package service;

import java.util.ArrayList;
import java.util.List;

// OOP: Composition (Cart has a list of Ticket objects).
// This class holds tickets before checkout.
public class Cart {

    // List of tickets in cart.
    // Composition: Cart has a list of Ticket objects.
    private List<Ticket> items;

    // Start with empty cart.
    public Cart() {
        items = new ArrayList<>();
    }

    // Add one ticket to cart.
    public void addItem(Ticket ticket) {
        items.add(ticket);
    }

    // Get all tickets.
    public List<Ticket> getItems() {
        return items;
    }

    // Sum ticket prices.
    public double calculateSubtotal() {
        double total = 0;
        for (Ticket t : items) {
            // Polymorphism: calculatePrice() uses the actual Ticket subclass.
            total += t.calculatePrice();
        }
        return total;
    }

    // Compute tax amount.
    public double calculateTax() {
        return calculateSubtotal() * ShopSettings.TAX_RATE;
    }

    // Subtotal + tax.
    public double calculateGrandTotal() {
        return calculateSubtotal() + calculateTax();
    }
}
