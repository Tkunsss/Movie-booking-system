package service;

import model.Movie;
import java.util.ArrayList;

public class Cart {
    public ArrayList<Movie> items; // Unlimited items

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
        Movie movie1 = new Movie("Avatar 2 ", 5.0, "2023-12-16 ");
        Movie movie2 = new Movie("John Wick 4   ", 6.0, "2023-03-24");

        cart.addItem(movie1);
        cart.addItem(movie2);
        
        System.out.println("======================== Movie Ticket Booking =======================");
        System.out.println("Items in Cart:");
        for (Movie m : cart.items) {
            System.out.println("- " + m.getTitle() + " - $" + m.getPrice());
        }
        System.out.println("Subtotal: $" + cart.calculateSubtotal());
        System.out.println("Tax: $" + cart.calculateTax());
        System.out.println("Grand Total: $" + cart.calculateGrandTotal());
        System.out.println("=====================================================================");
        System.out.println("                        | Total price: $" + cart.calculateGrandTotal() * (1 - ShopSettings.DISCOUNT_RATE)+ " |");
        System.out.println("                        =======================");
    }

}
