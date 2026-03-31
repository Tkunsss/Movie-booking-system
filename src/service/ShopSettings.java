package service;

import java.util.ArrayList;
import model.Movie;

// OOP: Static settings shared across objects.
// Settings and a simple cart demo (not used in main flow).
public class ShopSettings {

    // Tax and discount settings.
    public static double TAX_RATE = 0.10;       // 10% tax
    public static double DISCOUNT_RATE = 0.05;  // 5% discount

    // Simple list of movies as cart items.
    private ArrayList<Movie> cartItems;

    // Start with empty list.
    public ShopSettings() {
        cartItems = new ArrayList<>();
    }

    // Add movie to cart.
    public void addToCart(Movie movie) {
        cartItems.add(movie);
    }

    // Print cart items.
    public void printInfo() {

        if (cartItems.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        for (int i = 0; i < cartItems.size(); i++) {
            Movie movie = cartItems.get(i);
            System.out.println((i + 1) + ". " + movie.getTitle() + " - $" + movie.getPrice());
        }
    }

    // Sum movie prices.
    public double getTotalPrice() {
        double total = 0;

        for (Movie movie : cartItems) {
            total += movie.getPrice();
        }

        return total;
    }
}
