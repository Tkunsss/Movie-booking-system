package service;

import model.Movie;
import java.util.ArrayList;
import java.util.List;

public class Cart {

    private List<Movie> items;

    public Cart() {
        items = new ArrayList<>();
    }

    public void addItem(Movie movie) {
        items.add(movie);
        System.out.println(movie.getTitle() + " added to cart.");
    }

    public List<Movie> getItems() {
        return items;
    }

    public double calculateSubtotal() {
        double total = 0;
        for (Movie m : items) {
            total += m.getPrice();
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