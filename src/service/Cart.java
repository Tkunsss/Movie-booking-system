package service;

import model.Movie;
import java.util.ArrayList;

public class Cart {
    private ArrayList<Movie> items; // Unlimited items

    public Cart() {
        items = new ArrayList<>();
    }

    public void addItem(Movie movie) {
        items.add(movie);
        System.out.println(movie.getTitle() + " added to cart.");
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
    public static void main(String[] args) {
        Cart cart = new Cart();
        Movie movie1 = new Movie("Inception", 10.0);
        Movie movie2 = new Movie("The Matrix", 12.0);

        cart.addItem(movie1);
        cart.addItem(movie2);
        
        System.out.println("Subtotal: $" + cart.calculateSubtotal());
        System.out.println("Tax: $" + cart.calculateTax());
        System.out.println("Grand Total: $" + cart.calculateGrandTotal());
    }
}
