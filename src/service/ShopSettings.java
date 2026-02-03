package service;

import java.util.ArrayList;
import model.Movie;

public class ShopSettings {

    public static double TAX_RATE = 0.10;       // 10% tax
    public static double DISCOUNT_RATE = 0.05;  // 5% discount

    private ArrayList<Movie> cartItems;

    public ShopSettings() {
        cartItems = new ArrayList<>();
    }

    public void addToCart(Movie movie) {
        cartItems.add(movie);
    }

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

    public double getTotalPrice() {
        double total = 0;

        for (Movie movie : cartItems) {
            total += movie.getPrice();
        }

        return total;
    }
}
