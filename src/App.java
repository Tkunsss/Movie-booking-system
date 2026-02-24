import model.Movie;
import service.Cart;
import service.ManagerStaff;
import service.ShopSettings;
import service.Ticketing_Agent;
import service.istaff;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            List<Movie> movies = new ArrayList<>();
            movies.add(new Movie("Avatar 2", 5.0, "2023-12-16"));
            movies.add(new Movie("John Wick 4", 6.0, "2023-03-24"));
            movies.add(new Movie("Spider-Man", 7.0, "2024-07-03"));

            ManagerStaff manager = new ManagerStaff("M001", "Admin User", "012345678", "admin", "1234", "Admin");
            Ticketing_Agent ticketingAgent = new Ticketing_Agent("S001", "Agent One", "012345679", "agent1", "1234", "Ticketing Agent");

            int choice;
            do {
                printRoleMenu();
                System.out.print("Choose role: ");
                choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        managerFlow(sc, manager, movies);
                        break;
                    case 2:
                        ticketingAgentFlow(sc, ticketingAgent, movies);
                        break;
                    case 3:
                        customerFlow(sc, movies);
                        break;
                    case 0:
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } while (choice != 0);
        }
    }

    private static void managerFlow(Scanner sc, ManagerStaff manager, List<Movie> movies) {
        System.out.println("\n=== Manager Login ===");
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        if (!authenticate(manager, username, password)) {
            System.out.println("Login failed.");
            return;
        }

        int choice;
        do {
            System.out.println("\n=== Manager Menu ===");
            System.out.println("1) View Movies");
            System.out.println("2) Create New Movie");
            System.out.println("3) Staff Action Check");
            System.out.println("0) Logout");
            System.out.print("Choose: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    printMovies(movies);
                    break;
                case 2:
                    if (manager.can("create_movie")) {
                        createMovie(sc, movies);
                    } else {
                        System.out.println("Permission denied.");
                    }
                    break;
                case 3:
                    System.out.println("Create customer: " + mark(manager.can("create_customer")));
                    System.out.println("Create order: " + mark(manager.can("create_order")));
                    System.out.println("Create menu item: " + mark(manager.can("create_menu_item")));
                    System.out.println("Update order status: " + mark(manager.can("update_order_status")));
                    System.out.println("Customer action (book ticket): " + mark(manager.can("book_ticket")) + " (manager should not use customer flow)");
                    break;
                case 0:
                    System.out.println("Manager logged out.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

    private static void ticketingAgentFlow(Scanner sc, Ticketing_Agent selected, List<Movie> movies) {
        System.out.println("\n=== Ticketing Agent Login ===");
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        if (!authenticate(selected, username, password)) {
            System.out.println("Login failed.");
            return;
        }

        int choice;
        do {
            System.out.println("\n=== Ticketing Agent Menu ===");
            System.out.println("1) View Movies");
            System.out.println("2) Create Menu Item");
            System.out.println("3) View Orders");
            System.out.println("4) Update Order Status");
            System.out.println("0) Logout");
            System.out.print("Choose: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    printMovies(movies);
                    break;
                case 2:
                    doStaffAction(selected, "create_menu_item", "Create menu item");
                    break;
                case 3:
                    doStaffAction(selected, "view_orders", "View orders");
                    break;
                case 4:
                    doStaffAction(selected, "update_order_status", "Update order status");
                    break;
                case 0:
                    System.out.println("Ticketing Agent logged out.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

    private static void customerFlow(Scanner sc, List<Movie> movies) {
        int choice;
        do {
            System.out.println("\n=== Customer Menu ===");
            System.out.println("1) View Movies");
            System.out.println("2) Book Ticket");
            System.out.println("0) Back");
            System.out.print("Choose: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    printMovies(movies);
                    break;
                case 2:
                    bookTickets(sc, movies);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

    private static void printRoleMenu() {
        System.out.println("\n=== LOGIN ROLE ===");
        System.out.println("1) Manager");
        System.out.println("2) Ticketing Agent");
        System.out.println("3) Customer");
        System.out.println("0) Exit");
    }

    private static void doStaffAction(istaff staff, String action, String label) {
        if (staff.can(action)) {
            System.out.println(label + ": " + mark(true));
        } else {
            System.out.println(label + ": " + mark(false));
        }
    }

    private static void createMovie(Scanner sc, List<Movie> movies) {
        System.out.print("Movie title: ");
        String title = sc.nextLine();

        System.out.print("Price: ");
        double price = sc.nextDouble();
        sc.nextLine();

        System.out.print("Release date (YYYY-MM-DD): ");
        String releaseDate = sc.nextLine();

        movies.add(new Movie(title, price, releaseDate));
        System.out.println("Movie created successfully.");
    }

    private static void bookTickets(Scanner sc, List<Movie> movies) {
        if (movies.isEmpty()) {
            System.out.println("No movies available.");
            return;
        }

        printMovies(movies);
        System.out.print("Select a movie number: ");
        int movieChoice = sc.nextInt();
        sc.nextLine();

        if (movieChoice < 1 || movieChoice > movies.size()) {
            System.out.println("Invalid movie choice.");
            return;
        }

        System.out.print("Enter number of tickets: ");
        int tickets = sc.nextInt();
        sc.nextLine();

        if (tickets <= 0) {
            System.out.println("Ticket count must be greater than 0.");
            return;
        }

        Cart cart = new Cart();
        Movie selectedMovie = movies.get(movieChoice - 1);
        for (int i = 0; i < tickets; i++) {
            cart.addItem(selectedMovie);
        }

        printCheckout(cart);
    }

    private static void printMovies(List<Movie> movies) {
        if (movies.isEmpty()) {
            System.out.println("No movies available.");
            return;
        }

        System.out.println("Available movies:");
        for (int i = 0; i < movies.size(); i++) {
            System.out.println((i + 1) + ". " + movies.get(i));
        }
    }

    private static void printCheckout(Cart cart) {
        System.out.println("======================== Movie Ticket Booking =======================");
        System.out.println("Subtotal: $" + cart.calculateSubtotal());
        System.out.println("Tax: $" + cart.calculateTax());
        System.out.println("Grand Total: $" + cart.calculateGrandTotal());
        System.out.println("=====================================================================");
        System.out.println("                        | Total price: $" + cart.calculateGrandTotal() * (1 - ShopSettings.DISCOUNT_RATE) + " |");
        System.out.println("                        =======================");
    }

    private static boolean authenticate(istaff staff, String username, String password) {
        return staff != null
                && staff.getUsername().equals(username)
                && staff.checkPassword(password)
                && staff.isActive();
    }

    private static String mark(boolean allowed) {
        return allowed ? "\u2705" : "\u274C";
    }
}
