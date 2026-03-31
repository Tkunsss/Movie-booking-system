import model.Movie;
import model.Order;
import model.Totals;
import db.Database;
import db.CustomerDao;
import db.OrderDao;
import db.StaffDao;
import db.MovieDao;
import service.Cart;
import service.CashierStaff;
import service.Customer;
import service.ManagerStaff;
import service.ReceiptGenerator;
import service.ShopSettings;
import service.StandardTicket;
import service.Ticketing_Agent;
import service.PremiumTicket;
import service.Ticket;
import service.istaff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// Main application class.
public class App {
    // Method: main - entry point, sets up initial data and role menu loop.
    public static void main(String[] args) {
        if (args.length == 0 || !"console".equalsIgnoreCase(args[0])) {
            ui.GuiApp.launch();
            return;
        }
        try (Scanner sc = new Scanner(System.in)) {
            Database.init();
            MovieDao movieDao = new MovieDao();
            StaffDao staffDao = new StaffDao();
            CustomerDao customerDao = new CustomerDao();
            OrderDao orderDao = new OrderDao();

            List<Movie> movies = movieDao.getAll();
            if (movies.isEmpty()) {
                movieDao.insert(new Movie("Avatar 2", 5.0, "2023-12-16"));
                movieDao.insert(new Movie("John Wick 4", 6.0, "2023-03-24"));
                movieDao.insert(new Movie("Spider-Man", 7.0, "2024-07-03"));
                movies = movieDao.getAll();
            }

            List<istaff> staffList = staffDao.getAll();
            if (staffList.isEmpty()) {
                ManagerStaff manager = new ManagerStaff("M001", "Admin User", "012345678", "admin", "1234", "Manager");
                Ticketing_Agent ticketingAgent = new Ticketing_Agent("S001", "Agent One", "012345679", "agent1", "1234", "Ticketing Agent");
                CashierStaff cashier = new CashierStaff("C001", "Cashier One", "012345680", "cashier1", "1234", "Cashier");
                staffDao.insert(manager, "1234", true);
                staffDao.insert(ticketingAgent, "1234", true);
                staffDao.insert(cashier, "1234", true);
                staffList = staffDao.getAll();
            }

            Map<String, istaff> staffMap = buildStaffMap(staffList);
            List<Customer> customers = customerDao.getAll(staffMap);
            Map<String, Customer> customerMap = buildCustomerMap(customers);
            Map<Integer, Movie> movieMap = buildMovieMap(movies);
            List<Order> orders = orderDao.getAll(movieMap, customerMap);
            List<String> menuItems = new ArrayList<>();

            int choice;
            do {
                printRoleMenu();
                choice = readInt(sc, "Choose role: ", 0, 4);

                switch (choice) {
                    case 1:
                        managerFlow(sc, staffList, movies, movieDao, staffDao, orderDao);
                        break;
                    case 2:
                        ticketingAgentFlow(sc, staffList, movies, menuItems, orders, orderDao);
                        break;
                    case 3:
                        customerFlow(sc, movies, orders, customers, customerDao, orderDao);
                        break;
                    case 4:
                        cashierFlow(sc, staffList, movies, customers, orders, customerDao, orderDao);
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

    // Method: managerFlow - manager login and manager menu actions.
    private static void managerFlow(Scanner sc, List<istaff> staffList, List<Movie> movies,
                                    MovieDao movieDao, StaffDao staffDao, OrderDao orderDao) {
        System.out.println("\n=== Manager Login ===");
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        istaff manager = authenticateRole(staffList, username, password, "manager");
        if (manager == null) {
            System.out.println("Login failed.");
            return;
        }

        int choice;
        do {
            System.out.println("\n=== Manager Menu ===");
            System.out.println("1) View Movies");
            System.out.println("2) Create New Movie");
            System.out.println("3) Edit Movie");
            System.out.println("4) Delete Movie");
            System.out.println("5) Create Staff");
            System.out.println("6) View Staff");
            System.out.println("7) Delete Staff");
            System.out.println("8) Sales Report");
            System.out.println("0) Logout");
            choice = readInt(sc, "Choose: ", 0, 8);

            switch (choice) {
                case 1:
                    printMovies(movies);
                    break;
                case 2:
                    if (manager.can("create_movie")) {
                        createMovie(sc, movies, movieDao);
                    } else {
                        System.out.println("Permission denied.");
                    }
                    break;
                case 3:
                    editMovie(sc, movies, movieDao);
                    break;
                case 4:
                    deleteMovie(sc, movies, movieDao, orderDao);
                    break;
                case 5:
                    createStaff(sc, staffList, staffDao);
                    break;
                case 6:
                    viewStaff(staffList);
                    break;
                case 7:
                    deleteStaff(sc, staffList, staffDao);
                    break;
                case 8:
                    printSalesReport(orderDao);
                    break;
                case 0:
                    System.out.println("Manager logged out.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

    // Method: ticketingAgentFlow - ticketing agent login and order/menu actions.
    private static void ticketingAgentFlow(Scanner sc, List<istaff> staffList, List<Movie> movies,
                                           List<String> menuItems, List<Order> orders, OrderDao orderDao) {
        System.out.println("\n=== Ticketing Agent Login ===");
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        istaff selected = authenticateRole(staffList, username, password, "ticketing");
        if (selected == null) {
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
            choice = readInt(sc, "Choose: ", 0, 4);

            switch (choice) {
                case 1:
                    printMovies(movies);
                    break;
                case 2:
                    if (selected.can("create_menu_item")) {
                        createMenuItem(sc, menuItems);
                    } else {
                        System.out.println("Permission denied.");
                    }
                    break;
                case 3:
                    if (selected.can("view_orders")) {
                        viewOrders(orders);
                    } else {
                        System.out.println("Permission denied.");
                    }
                    break;
                case 4:
                    if (selected.can("update_order_status")) {
                        updateOrderStatus(sc, orders, orderDao);
                    } else {
                        System.out.println("Permission denied.");
                    }
                    break;
                case 0:
                    System.out.println("Ticketing Agent logged out.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

    // Method: customerFlow - customer menu for viewing movies and booking tickets.
    private static void customerFlow(Scanner sc, List<Movie> movies, List<Order> orders,
                                     List<Customer> customers, CustomerDao customerDao, OrderDao orderDao) {
        int choice;
        do {
            System.out.println("\n=== Customer Menu ===");
            System.out.println("1) View Movies");
            System.out.println("2) Book Ticket");
            System.out.println("0) Back");
            choice = readInt(sc, "Choose: ", 0, 2);

            switch (choice) {
                case 1:
                    printMovies(movies);
                    break;
                case 2:
                    bookTickets(sc, movies, orders, customers, customerDao, orderDao);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

    // Method: printRoleMenu - shows the role selection menu.
    private static void printRoleMenu() {
        System.out.println("\n=== LOGIN ROLE ===");
        System.out.println("1) Manager");
        System.out.println("2) Ticketing Agent");
        System.out.println("3) Customer");
        System.out.println("4) Cashier");
        System.out.println("0) Exit");
    }

    // Method: doStaffAction - helper to show permission result.
    private static void doStaffAction(istaff staff, String action, String label) {
        if (staff.can(action)) {
            System.out.println(label + ": " + mark(true));
        } else {
            System.out.println(label + ": " + mark(false));
        }
    }

    // Method: createMovie - adds a new movie to the list.
    private static void createMovie(Scanner sc, List<Movie> movies, MovieDao movieDao) {
        System.out.print("Movie title: ");
        String title = sc.nextLine();

        double price = readDouble(sc, "Price: ", 0, Double.MAX_VALUE);

        System.out.print("Release date (YYYY-MM-DD): ");
        String releaseDate = sc.nextLine();

        Movie movie = new Movie(title, price, releaseDate);
        movies.add(movie);
        movieDao.insert(movie);
        System.out.println("Movie created successfully.");
    }

    // Method: cashierFlow - cashier login and cashier menu actions.
    private static void cashierFlow(Scanner sc, List<istaff> staffList, List<Movie> movies,
                                    List<Customer> customers, List<Order> orders,
                                    CustomerDao customerDao, OrderDao orderDao) {
        System.out.println("\n=== Cashier Login ===");
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        istaff cashier = authenticateRole(staffList, username, password, "cashier");
        if (cashier == null) {
            System.out.println("Login failed.");
            return;
        }

        int choice;
        do {
            System.out.println("\n=== Cashier Menu ===");
            System.out.println("1) Create Customer");
            System.out.println("2) Create Order");
            System.out.println("3) View Customers");
            System.out.println("4) View Orders");
            System.out.println("5) Checkout");
            System.out.println("6) Refund Order");
            System.out.println("0) Logout");
            choice = readInt(sc, "Choose: ", 0, 6);

            switch (choice) {
                case 1:
                    if (cashier.can("create_customer")) {
                        createCustomer(sc, customers, cashier, customerDao);
                    } else {
                        System.out.println("Permission denied.");
                    }
                    break;
                case 2:
                    if (cashier.can("create_order")) {
                        createCashierOrder(sc, movies, customers, orders, orderDao);
                    } else {
                        System.out.println("Permission denied.");
                    }
                    break;
                case 3:
                    if (cashier.can("view_customers")) {
                        viewCustomers(customers);
                    } else {
                        System.out.println("Permission denied.");
                    }
                    break;
                case 4:
                    if (cashier.can("view_orders")) {
                        viewOrders(orders);
                    } else {
                        System.out.println("Permission denied.");
                    }
                    break;
                case 5:
                    checkoutOrder(sc, orders, customerDao, orderDao);
                    break;
                case 6:
                    refundOrder(sc, orders, customerDao, orderDao);
                    break;
                case 0:
                    System.out.println("Cashier logged out.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

    // Method: createStaff - creates staff accounts (manager/agent/cashier).
    private static void createStaff(Scanner sc, List<istaff> staffList, StaffDao staffDao) {
        System.out.println("\n=== Create Staff ===");
        System.out.println("1) Manager");
        System.out.println("2) Ticketing Agent");
        System.out.println("3) Cashier");
        int role = readInt(sc, "Choose role: ", 1, 3);

        System.out.print("Staff ID: ");
        String staffId = sc.nextLine().trim();
        System.out.print("Full name: ");
        String fullName = sc.nextLine().trim();
        System.out.print("Phone: ");
        String phone = sc.nextLine().trim();
        System.out.print("Username: ");
        String username = sc.nextLine().trim();
        System.out.print("Password: ");
        String password = sc.nextLine().trim();

        istaff newStaff;
        if (role == 1) {
            newStaff = new ManagerStaff(staffId, fullName, phone, username, password, "Manager");
        } else if (role == 2) {
            newStaff = new Ticketing_Agent(staffId, fullName, phone, username, password, "Ticketing Agent");
        } else {
            newStaff = new CashierStaff(staffId, fullName, phone, username, password, "Cashier");
        }

        staffList.add(newStaff);
        staffDao.insert(newStaff, password, true);
        System.out.println("Staff created: " + newStaff.getStaffId() + " (" + newStaff.getPosition() + ")");
        System.out.println("Total staff in system: " + staffList.size());
    }

    // Method: bookTickets - customer booking flow with checkout.
    private static void bookTickets(Scanner sc, List<Movie> movies, List<Order> orders,
                                    List<Customer> customers, CustomerDao customerDao, OrderDao orderDao) {
        if (movies.isEmpty()) {
            System.out.println("No movies available.");
            return;
        }

        printMovies(movies);
        int movieChoice = readInt(sc, "Select a movie number: ", 1, movies.size());

        int tickets = readInt(sc, "Enter number of tickets: ", 1, Integer.MAX_VALUE);

        System.out.println("Select ticket type:");
        System.out.println("1) Standard (normal seat)");
        System.out.println("2) Premium (VIP seat)");
        int ticketType = readInt(sc, "Choose: ", 1, 2);
        Movie selectedMovie = movies.get(movieChoice - 1);
        showBookedSeats(orderDao, selectedMovie);
        int startSeat = readInt(sc, "Starting seat number: ", 1, Integer.MAX_VALUE);
        while (!areSeatsAvailable(orderDao, selectedMovie, startSeat, tickets)) {
            System.out.println("Some seats are already booked. Please choose another starting seat.");
            startSeat = readInt(sc, "Starting seat number: ", 1, Integer.MAX_VALUE);
        }

        Cart cart = new Cart();
        for (int i = 0; i < tickets; i++) {
            int seatNumber = startSeat + i;
            Ticket ticket = (ticketType == 1)
                    ? new StandardTicket(selectedMovie, seatNumber)
                    : new PremiumTicket(selectedMovie, seatNumber);
            cart.addItem(ticket);
        }

        Totals totals = calculateTotals(cart);
        printCheckout(cart, totals);
        Customer customer = getOrCreateCustomer(sc, customers, customerDao, null);
        Order order = addOrder(orders, selectedMovie, tickets, totals, customer, cart, orderDao);
        boolean paid = processPayment(customer, cart, totals, order, customerDao, orderDao);
        if (paid) {
            System.out.println("Order payment successful. Order ID: " + order.id);
        } else {
            System.out.println("Order created but not paid. Order ID: " + order.id);
        }
    }

    // Method: printMovies - lists all available movies.
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

    // Method: printCheckout - shows ticket list and totals.
    private static void printCheckout(Cart cart, Totals totals) {
        System.out.println("======================== Movie Ticket Booking =======================");
        System.out.println("Tickets:");
        for (Ticket ticket : cart.getItems()) {
            System.out.println("- " + ticket);
        }
        System.out.println("Subtotal: $" + totals.subtotal);
        System.out.println("Discount: -$" + totals.discount);
        System.out.println("Tax: $" + totals.tax);
        System.out.println("Grand Total: $" + totals.total);
        System.out.println("=====================================================================");
        System.out.println("                        | Total price: $" + totals.total + " |");
        System.out.println("                        =======================");
    }

    // Method: authenticate - checks username/password and active status.
    private static boolean authenticate(istaff staff, String username, String password) {
        return staff != null
                && staff.getUsername().equals(username)
                && staff.checkPassword(password)
                && staff.isActive();
    }

    // Method: mark - returns a check/cross icon for permissions.
    private static String mark(boolean allowed) {
        return allowed ? "\u2705" : "\u274C";
    }

    // Method: readInt - safe integer input with range validation.
    private static int readInt(Scanner sc, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value < min || value > max) {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }

    // Method: readDouble - safe decimal input with range validation.
    private static double readDouble(Scanner sc, String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                double value = Double.parseDouble(line);
                if (value < min || value > max) {
                    System.out.println("Please enter a value between " + min + " and " + max + ".");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }

    private static Map<String, istaff> buildStaffMap(List<istaff> staffList) {
        Map<String, istaff> map = new HashMap<>();
        for (istaff s : staffList) {
            map.put(s.getStaffId(), s);
        }
        return map;
    }

    private static Map<String, Customer> buildCustomerMap(List<Customer> customers) {
        Map<String, Customer> map = new HashMap<>();
        for (Customer c : customers) {
            map.put(c.getCustomerId(), c);
        }
        return map;
    }

    private static Map<Integer, Movie> buildMovieMap(List<Movie> movies) {
        Map<Integer, Movie> map = new HashMap<>();
        for (Movie m : movies) {
            map.put(m.getId(), m);
        }
        return map;
    }

    // Method: calculateTotals - computes subtotal, discount, tax, and total.
    private static Totals calculateTotals(Cart cart) {
        double subtotal = cart.calculateSubtotal();
        double discount = subtotal * ShopSettings.DISCOUNT_RATE;
        double taxable = subtotal - discount;
        double tax = taxable * ShopSettings.TAX_RATE;
        double total = taxable + tax;
        return new Totals(subtotal, discount, tax, total);
    }

    // Method: collectCustomerInfo - gathers customer details and returns a Customer.
    private static Customer collectCustomerInfo(Scanner sc, istaff createdBy) {
        System.out.println("\n=== Customer Details ===");
        System.out.print("Customer name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) {
            name = "Walk-in Customer";
        }

        System.out.print("Customer ID: ");
        String customerId = sc.nextLine().trim();
        if (customerId.isEmpty()) {
            customerId = "WALKIN-" + System.currentTimeMillis();
        }

        System.out.print("Phone number: ");
        String phone = sc.nextLine().trim();
        if (phone.isEmpty()) {
            phone = "00000000";
        }

        double balance = readDouble(sc, "Current balance: ", 0, Double.MAX_VALUE);
        return new Customer(customerId, name, phone, "0000", balance, createdBy);
    }

    // Method: processPayment - deducts balance, writes receipt, updates order status.
    private static boolean processPayment(Customer customer, Cart cart, Totals totals, Order order,
                                          CustomerDao customerDao, OrderDao orderDao) {
        if (!customer.deductBalance(totals.total)) {
            System.out.println("Insufficient balance. Checkout cancelled.");
            order.status = "PAYMENT_FAILED";
            orderDao.updateStatus(order.id, order.status);
            return false;
        }

        customerDao.updateBalance(customer.getCustomerId(), customer.getBalance());
        ReceiptGenerator.generateReceipt(customer, cart, totals.subtotal, totals.discount, totals.tax, totals.total);
        System.out.println("Receipt saved.");
        order.status = "PAID";
        orderDao.updateStatus(order.id, order.status);
        return true;
    }

    // Method: createMenuItem - adds a new item to the menu list.
    private static void createMenuItem(Scanner sc, List<String> menuItems) {
        System.out.print("Menu item name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Menu item name cannot be empty.");
            return;
        }
        menuItems.add(name);
        System.out.println("Menu item created: " + name);
        System.out.println("Total menu items: " + menuItems.size());
    }

    // Method: viewOrders - lists all orders with customer and status.
    private static void viewOrders(List<Order> orders) {
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }
        System.out.println("Orders:");
        for (Order order : orders) {
            String customerName = (order.customer == null) ? "Unknown" : order.customer.getFullName();
            System.out.println(order.id + ") " + order.description
                    + " | Customer: " + customerName
                    + " | Total: $" + order.totals.total
                    + " | Status: " + order.status);
        }
    }

    // Method: updateOrderStatus - updates a selected order's status.
    private static void updateOrderStatus(Scanner sc, List<Order> orders, OrderDao orderDao) {
        if (orders.isEmpty()) {
            System.out.println("No orders to update.");
            return;
        }
        viewOrders(orders);
        int id = readInt(sc, "Enter order ID to update: ", 1, Integer.MAX_VALUE);
        Order target = null;
        for (Order order : orders) {
            if (order.id == id) {
                target = order;
                break;
            }
        }
        if (target == null) {
            System.out.println("Order not found.");
            return;
        }
        System.out.print("New status: ");
        String status = sc.nextLine().trim();
        if (status.isEmpty()) {
            System.out.println("Status cannot be empty.");
            return;
        }
        target.status = status;
        orderDao.updateStatus(target.id, status);
        System.out.println("Order updated.");
    }

    // Method: createCustomer - creates and stores a customer.
    private static void createCustomer(Scanner sc, List<Customer> customers, istaff createdBy,
                                       CustomerDao customerDao) {
        Customer customer = collectCustomerInfo(sc, createdBy);
        addCustomerIfNew(customers, customer, customerDao);
        System.out.println("Customer created: " + customer.getCustomerId() + " (" + customer.getFullName() + ")");
        System.out.println("Total customers: " + customers.size());
    }

    private static Customer getOrCreateCustomer(Scanner sc, List<Customer> customers,
                                                CustomerDao customerDao, istaff createdBy) {
        System.out.print("Customer ID (leave blank for new): ");
        String id = sc.nextLine().trim();
        if (!id.isEmpty()) {
            Customer existing = findCustomerById(customers, id);
            if (existing != null) return existing;
            Customer created = collectCustomerInfoWithId(sc, id, createdBy);
            addCustomerIfNew(customers, created, customerDao);
            return created;
        }
        Customer created = collectCustomerInfo(sc, createdBy);
        addCustomerIfNew(customers, created, customerDao);
        return created;
    }

    private static Customer collectCustomerInfoWithId(Scanner sc, String customerId, istaff createdBy) {
        System.out.println("\n=== Customer Details ===");
        System.out.print("Customer name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) name = "Walk-in Customer";

        System.out.print("Phone number: ");
        String phone = sc.nextLine().trim();
        if (phone.isEmpty()) phone = "00000000";

        double balance = readDouble(sc, "Current balance: ", 0, Double.MAX_VALUE);
        return new Customer(customerId, name, phone, "0000", balance, createdBy);
    }

    private static Customer findCustomerById(List<Customer> customers, String id) {
        for (Customer c : customers) {
            if (c.getCustomerId().equalsIgnoreCase(id)) return c;
        }
        return null;
    }

    // Method: addCustomerIfNew - prevents duplicate customer IDs.
    private static void addCustomerIfNew(List<Customer> customers, Customer customer, CustomerDao customerDao) {
        for (Customer existing : customers) {
            if (existing.getCustomerId().equalsIgnoreCase(customer.getCustomerId())) {
                return;
            }
        }
        customers.add(customer);
        customerDao.insert(customer);
    }

    // Method: viewCustomers - lists all customers with balances.
    private static void viewCustomers(List<Customer> customers) {
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
            return;
        }
        System.out.println("Customers:");
        for (int i = 0; i < customers.size(); i++) {
            Customer c = customers.get(i);
            System.out.println((i + 1) + ") " + c.getCustomerId() + " - " + c.getFullName()
                    + " | Balance: $" + c.getBalance());
        }
    }

    private static void viewStaff(List<istaff> staffList) {
        if (staffList.isEmpty()) {
            System.out.println("No staff found.");
            return;
        }
        System.out.println("Staff:");
        for (int i = 0; i < staffList.size(); i++) {
            istaff s = staffList.get(i);
            System.out.println((i + 1) + ") " + s.getStaffId()
                    + " - " + s.getFullName()
                    + " | Username: " + s.getUsername()
                    + " | Position: " + s.getPosition()
                    + " | Active: " + s.isActive());
        }
    }

    private static void deleteStaff(Scanner sc, List<istaff> staffList, StaffDao staffDao) {
        if (staffList.isEmpty()) {
            System.out.println("No staff found.");
            return;
        }
        viewStaff(staffList);
        int idx = readInt(sc, "Select staff number to delete: ", 1, staffList.size());
        istaff staff = staffList.get(idx - 1);
        staffList.remove(idx - 1);
        staffDao.deleteById(staff.getStaffId());
        System.out.println("Staff deleted: " + staff.getStaffId());
    }

    private static void setStaffActive(istaff staff, boolean active) {
        if (staff instanceof ManagerStaff) {
            ((ManagerStaff) staff).setActive(active);
        } else if (staff instanceof Ticketing_Agent) {
            ((Ticketing_Agent) staff).setActive(active);
        } else if (staff instanceof CashierStaff) {
            ((CashierStaff) staff).setActive(active);
        }
    }

    private static istaff authenticateRole(List<istaff> staffList, String username,
                                           String password, String roleKeyword) {
        String role = roleKeyword == null ? "" : roleKeyword.toLowerCase();
        for (istaff staff : staffList) {
            if (!staff.isActive()) continue;
            if (!staff.getUsername().equals(username)) continue;
            if (!staff.checkPassword(password)) continue;
            String pos = staff.getPosition() == null ? "" : staff.getPosition().toLowerCase();
            if (role.isEmpty()) return staff;
            if (pos.contains(role)) return staff;
            if (role.equals("manager") && pos.contains("admin")) return staff;
        }
        return null;
    }

    private static void editMovie(Scanner sc, List<Movie> movies, MovieDao movieDao) {
        if (movies.isEmpty()) {
            System.out.println("No movies available.");
            return;
        }
        printMovies(movies);
        int idx = readInt(sc, "Select movie number to edit: ", 1, movies.size());
        Movie movie = movies.get(idx - 1);

        System.out.print("New title (leave blank to keep): ");
        String title = sc.nextLine().trim();
        if (!title.isEmpty()) movie.setTitle(title);

        String priceInput;
        System.out.print("New price (leave blank to keep): ");
        priceInput = sc.nextLine().trim();
        if (!priceInput.isEmpty()) {
            try {
                movie.setPrice(Double.parseDouble(priceInput));
            } catch (NumberFormatException e) {
                System.out.println("Invalid price. Keeping old price.");
            }
        }

        System.out.print("New release date (leave blank to keep): ");
        String date = sc.nextLine().trim();
        if (!date.isEmpty()) movie.setReleaseDate(date);

        movieDao.update(movie);
        System.out.println("Movie updated.");
    }

    private static void deleteMovie(Scanner sc, List<Movie> movies, MovieDao movieDao, OrderDao orderDao) {
        if (movies.isEmpty()) {
            System.out.println("No movies available.");
            return;
        }
        printMovies(movies);
        int idx = readInt(sc, "Select movie number to delete: ", 1, movies.size());
        Movie movie = movies.get(idx - 1);
        if (movieDao == null) return;
        if (movie.getId() > 0 && orderDao.hasOrdersForMovie(movie.getId())) {
            System.out.println("Cannot delete. Movie already has orders.");
            return;
        }
        movies.remove(idx - 1);
        movieDao.deleteById(movie.getId());
        System.out.println("Movie deleted.");
    }

    private static void printSalesReport(OrderDao orderDao) {
        double totalSales = orderDao.getTotalSales();
        int totalOrders = orderDao.getTotalOrders();
        String topMovie = orderDao.getTopMovieTitle();
        System.out.println("\n=== Sales Report ===");
        System.out.println("Total orders: " + totalOrders);
        System.out.println("Total sales: $" + totalSales);
        System.out.println("Top movie: " + topMovie);
    }

    private static void refundOrder(Scanner sc, List<Order> orders,
                                    CustomerDao customerDao, OrderDao orderDao) {
        if (orders.isEmpty()) {
            System.out.println("No orders to refund.");
            return;
        }
        viewOrders(orders);
        int id = readInt(sc, "Enter order ID to refund: ", 1, Integer.MAX_VALUE);
        Order target = null;
        for (Order order : orders) {
            if (order.id == id) {
                target = order;
                break;
            }
        }
        if (target == null) {
            System.out.println("Order not found.");
            return;
        }
        if ("CANCELLED".equalsIgnoreCase(target.status)) {
            System.out.println("Order already cancelled.");
            return;
        }
        if ("PAID".equalsIgnoreCase(target.status) && target.customer != null) {
            target.customer.addBalance(target.totals.total);
            customerDao.updateBalance(target.customer.getCustomerId(), target.customer.getBalance());
        }
        target.status = "CANCELLED";
        orderDao.updateStatus(target.id, target.status);
        System.out.println("Order refunded and cancelled.");
    }

    private static void showBookedSeats(OrderDao orderDao, Movie movie) {
        List<Integer> booked = orderDao.getBookedSeats(movie.getId());
        if (booked.isEmpty()) {
            System.out.println("Booked seats: none");
            return;
        }
        System.out.print("Booked seats: ");
        for (int i = 0; i < booked.size(); i++) {
            System.out.print(booked.get(i));
            if (i < booked.size() - 1) System.out.print(", ");
        }
        System.out.println();
    }

    private static boolean areSeatsAvailable(OrderDao orderDao, Movie movie, int startSeat, int tickets) {
        for (int i = 0; i < tickets; i++) {
            int seat = startSeat + i;
            if (!orderDao.isSeatAvailable(movie.getId(), seat)) return false;
        }
        return true;
    }

    // Method: createCashierOrder - cashier creates an order for a customer.
    private static void createCashierOrder(Scanner sc, List<Movie> movies, List<Customer> customers,
                                           List<Order> orders, OrderDao orderDao) {
        if (movies.isEmpty()) {
            System.out.println("No movies available.");
            return;
        }
        if (customers.isEmpty()) {
            System.out.println("No customers. Create a customer first.");
            return;
        }

        viewCustomers(customers);
        int customerIndex = readInt(sc, "Select customer number: ", 1, customers.size());
        Customer customer = customers.get(customerIndex - 1);

        printMovies(movies);
        int movieChoice = readInt(sc, "Select a movie number: ", 1, movies.size());
        Movie selectedMovie = movies.get(movieChoice - 1);

        int tickets = readInt(sc, "Enter number of tickets: ", 1, Integer.MAX_VALUE);
        System.out.println("Select ticket type:");
        System.out.println("1) Standard (normal seat)");
        System.out.println("2) Premium (VIP seat)");
        int ticketType = readInt(sc, "Choose: ", 1, 2);
        showBookedSeats(orderDao, selectedMovie);
        int startSeat = readInt(sc, "Starting seat number: ", 1, Integer.MAX_VALUE);
        while (!areSeatsAvailable(orderDao, selectedMovie, startSeat, tickets)) {
            System.out.println("Some seats are already booked. Please choose another starting seat.");
            startSeat = readInt(sc, "Starting seat number: ", 1, Integer.MAX_VALUE);
        }

        Cart cart = new Cart();
        for (int i = 0; i < tickets; i++) {
            int seatNumber = startSeat + i;
            Ticket ticket = (ticketType == 1)
                    ? new StandardTicket(selectedMovie, seatNumber)
                    : new PremiumTicket(selectedMovie, seatNumber);
            cart.addItem(ticket);
        }

        Totals totals = calculateTotals(cart);
        printCheckout(cart, totals);
        addOrder(orders, selectedMovie, tickets, totals, customer, cart, orderDao);
    }

    // Method: checkoutOrder - cashier completes payment for an existing order.
    private static void checkoutOrder(Scanner sc, List<Order> orders, CustomerDao customerDao, OrderDao orderDao) {
        if (orders.isEmpty()) {
            System.out.println("No orders to checkout.");
            return;
        }
        viewOrders(orders);
        int id = readInt(sc, "Enter order ID to checkout: ", 1, Integer.MAX_VALUE);
        Order target = null;
        for (Order order : orders) {
            if (order.id == id) {
                target = order;
                break;
            }
        }
        if (target == null) {
            System.out.println("Order not found.");
            return;
        }
        if (target.customer == null || target.totals == null || target.cart == null) {
            System.out.println("Order is missing details. Cannot checkout.");
            return;
        }
        if ("PAID".equalsIgnoreCase(target.status)) {
            System.out.println("Order already paid.");
            return;
        }
        processPayment(target.customer, target.cart, target.totals, target, customerDao, orderDao);
    }

    // Method: addOrder - creates and stores an order object.
    private static Order addOrder(List<Order> orders, Movie movie, int tickets,
                                  Totals totals, Customer customer, Cart cart, OrderDao orderDao) {
        String desc = "Movie: " + movie.getTitle()
                + ", Tickets: " + tickets
                + ", Total: $" + totals.total;
        Order order = new Order(0, desc, "PENDING_PAYMENT", customer, cart, totals);
        int id = orderDao.insert(order);
        order.id = id;
        orders.add(order);
        System.out.println("Order created with ID: " + id);
        return order;
    }
}
