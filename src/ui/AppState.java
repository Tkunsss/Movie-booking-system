package ui;

import db.CustomerDao;
import db.Database;
import db.MovieDao;
import db.OrderDao;
import db.StaffDao;
import model.Movie;
import model.Order;
import service.CashierStaff;
import service.Customer;
import service.ManagerStaff;
import service.Ticketing_Agent;
import service.istaff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// OOP: Central state holder (composition of lists + DAOs).
public class AppState {
    public final MovieDao movieDao = new MovieDao();
    public final StaffDao staffDao = new StaffDao();
    public final CustomerDao customerDao = new CustomerDao();
    public final OrderDao orderDao = new OrderDao();

    public List<Movie> movies = new ArrayList<>();
    public List<istaff> staffList = new ArrayList<>();
    public List<Customer> customers = new ArrayList<>();
    public List<Order> orders = new ArrayList<>();

    public void init() {
        Database.init();
        movies = movieDao.getAll();
        if (movies.isEmpty()) {
            movieDao.insert(new Movie("Avatar 2", 5.0, "2023-12-16"));
            movieDao.insert(new Movie("John Wick 4", 6.0, "2023-03-24"));
            movieDao.insert(new Movie("Spider-Man", 7.0, "2024-07-03"));
            movies = movieDao.getAll();
        }

        staffList = staffDao.getAll();
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
        customers = customerDao.getAll(staffMap);
        Map<String, Customer> customerMap = buildCustomerMap(customers);
        Map<Integer, Movie> movieMap = buildMovieMap(movies);
        orders = orderDao.getAll(movieMap, customerMap);
    }

    public void refreshMovies() {
        movies = movieDao.getAll();
    }

    public void refreshStaff() {
        staffList = staffDao.getAll();
    }

    public void refreshCustomers() {
        Map<String, istaff> staffMap = buildStaffMap(staffList);
        customers = customerDao.getAll(staffMap);
    }

    public void refreshOrders() {
        Map<String, Customer> customerMap = buildCustomerMap(customers);
        Map<Integer, Movie> movieMap = buildMovieMap(movies);
        orders = orderDao.getAll(movieMap, customerMap);
    }

    public istaff authenticateRole(String username, String password, String roleKeyword) {
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

    private Map<String, istaff> buildStaffMap(List<istaff> list) {
        Map<String, istaff> map = new HashMap<>();
        for (istaff s : list) map.put(s.getStaffId(), s);
        return map;
    }

    private Map<String, Customer> buildCustomerMap(List<Customer> list) {
        Map<String, Customer> map = new HashMap<>();
        for (Customer c : list) map.put(c.getCustomerId(), c);
        return map;
    }

    private Map<Integer, Movie> buildMovieMap(List<Movie> list) {
        Map<Integer, Movie> map = new HashMap<>();
        for (Movie m : list) map.put(m.getId(), m);
        return map;
    }
}
