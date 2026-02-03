import model.Movie;
import service.Cart;
import service.ShopSettings;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            // Sample movies
            Movie m1 = new Movie("Avatar 2", 5.0);
            Movie m2 = new Movie("John Wick 4", 6.0);
            Movie m3 = new Movie("Spider-Man", 7.0);

            Cart cart = new Cart();

            // Ask user which movies to add
            System.out.println("Available movies:");
            System.out.println("1. " + m1);
            System.out.println("2. " + m2);
            System.out.println("3. " + m3);

            System.out.print("Select a movie (1-3): ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            Movie selectedMovie;
            if (choice == 1) selectedMovie = m1;
            else if (choice == 2) selectedMovie = m2;
            else selectedMovie = m3;

            System.out.print("Enter number of tickets: ");
            int tickets = sc.nextInt();
            sc.nextLine(); // consume newline

            // Add the selected movie to cart for each ticket
            for (int i = 0; i < tickets; i++) {
                cart.addItem(selectedMovie);
            }

            // Calculate totals
            double subtotal = cart.calculateSubtotal();
            double tax = subtotal * ShopSettings.TAX_RATE;
            double grandTotal = subtotal + tax;

            System.out.println("\nSubtotal: $" + subtotal);
            System.out.println("Tax (10%): $" + tax);
            System.out.println("Grand Total: $" + grandTotal);
        }
    }
}
