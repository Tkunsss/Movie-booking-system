package service;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private List<Ticket> items;

    public Cart() {
        items = new ArrayList<>();
    }

    public void addItem(Ticket ticket) {
        items.add(ticket);
        System.out.println(ticket.toString());
    }

    public List<Ticket> getItems() {
        return items;
    }

    public double calculateSubtotal() {
        double total = 0;
        for (Ticket t : items) {
            total += t.calculatePrice();
        }
        return total;
    }

    public double calculateTax() {
        return calculateSubtotal() * ShopSettings.TAX_RATE;
    }

    public double calculateGrandTotal() {
        return calculateSubtotal() + calculateTax();
    }
}
