package db;

import model.Movie;
import model.Order;
import model.Totals;
import service.Cart;
import service.Customer;
import service.PremiumTicket;
import service.StandardTicket;
import service.Ticket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// OOP: DAO pattern (separates DB access from UI/logic).
public class OrderDao {

    public List<Order> getAll(Map<Integer, Movie> movieMap, Map<String, Customer> customerMap) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT id, customer_id, description, status, subtotal, discount, tax, total FROM orders";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String customerId = rs.getString("customer_id");
                Customer customer = customerId == null ? null : customerMap.get(customerId);
                Totals totals = new Totals(
                        rs.getDouble("subtotal"),
                        rs.getDouble("discount"),
                        rs.getDouble("tax"),
                        rs.getDouble("total")
                );
                Cart cart = loadCart(conn, id, movieMap);
                Order order = new Order(
                        id,
                        rs.getString("description"),
                        rs.getString("status"),
                        customer,
                        cart,
                        totals
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Load orders failed", e);
        }
        return orders;
    }

    public int insert(Order order) {
        String sql = "INSERT INTO orders(customer_id, description, status, subtotal, discount, tax, total) " +
                "VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, order.customer == null ? null : order.customer.getCustomerId());
            ps.setString(2, order.description);
            ps.setString(3, order.status);
            ps.setDouble(4, order.totals.subtotal);
            ps.setDouble(5, order.totals.discount);
            ps.setDouble(6, order.totals.tax);
            ps.setDouble(7, order.totals.total);
            ps.executeUpdate();
            int orderId = 0;
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) orderId = keys.getInt(1);
            }
            if (orderId > 0) {
                insertItems(conn, orderId, order.cart);
            }
            return orderId;
        } catch (SQLException e) {
            throw new RuntimeException("Insert order failed", e);
        }
    }

    public void updateStatus(int orderId, String status) {
        String sql = "UPDATE orders SET status=? WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Update order status failed", e);
        }
    }

    public void deleteById(int orderId) {
        try (Connection conn = Database.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM order_items WHERE order_id=?")) {
                ps.setInt(1, orderId);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM orders WHERE id=?")) {
                ps.setInt(1, orderId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Delete order failed", e);
        }
    }

    public boolean isSeatAvailable(int movieId, int seatNumber) {
        String sql = """
            SELECT COUNT(*) AS cnt
            FROM order_items oi
            JOIN orders o ON oi.order_id = o.id
            WHERE oi.movie_id = ? AND oi.seat_number = ? AND o.status <> 'CANCELLED'
            """;
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, movieId);
            ps.setInt(2, seatNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("cnt") == 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Check seat availability failed", e);
        }
        return true;
    }

    public List<Integer> getBookedSeats(int movieId) {
        List<Integer> seats = new ArrayList<>();
        String sql = """
            SELECT oi.seat_number
            FROM order_items oi
            JOIN orders o ON oi.order_id = o.id
            WHERE oi.movie_id = ? AND o.status <> 'CANCELLED'
            ORDER BY oi.seat_number
            """;
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, movieId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) seats.add(rs.getInt("seat_number"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Load booked seats failed", e);
        }
        return seats;
    }

    public boolean hasOrdersForMovie(int movieId) {
        String sql = "SELECT COUNT(*) AS cnt FROM order_items WHERE movie_id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, movieId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("cnt") > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Check movie orders failed", e);
        }
        return false;
    }

    public double getTotalSales() {
        String sql = "SELECT IFNULL(SUM(total),0) AS total_sales FROM orders WHERE status='PAID'";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble("total_sales");
        } catch (SQLException e) {
            throw new RuntimeException("Load total sales failed", e);
        }
        return 0;
    }

    public int getTotalOrders() {
        String sql = "SELECT COUNT(*) AS cnt FROM orders";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt("cnt");
        } catch (SQLException e) {
            throw new RuntimeException("Load order count failed", e);
        }
        return 0;
    }

    public String getTopMovieTitle() {
        String sql = """
            SELECT m.title, COUNT(*) AS cnt
            FROM order_items oi
            JOIN orders o ON oi.order_id = o.id
            JOIN movies m ON oi.movie_id = m.id
            WHERE o.status='PAID'
            GROUP BY m.title
            ORDER BY cnt DESC
            LIMIT 1
            """;
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getString("title");
        } catch (SQLException e) {
            throw new RuntimeException("Load top movie failed", e);
        }
        return "N/A";
    }

    private void insertItems(Connection conn, int orderId, Cart cart) throws SQLException {
        if (cart == null || cart.getItems().isEmpty()) return;
        String sql = "INSERT INTO order_items(order_id, movie_id, seat_number, ticket_type, price) VALUES(?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Ticket ticket : cart.getItems()) {
                int movieId = ticket.getMovie().getId();
                ps.setInt(1, orderId);
                ps.setInt(2, movieId);
                ps.setInt(3, ticket.getSeatNumber());
                ps.setString(4, ticket.getType());
                ps.setDouble(5, ticket.calculatePrice());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private Cart loadCart(Connection conn, int orderId, Map<Integer, Movie> movieMap) throws SQLException {
        Cart cart = new Cart();
        String sql = "SELECT movie_id, seat_number, ticket_type FROM order_items WHERE order_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Movie movie = movieMap.get(rs.getInt("movie_id"));
                    if (movie == null) continue;
                    int seat = rs.getInt("seat_number");
                    String type = rs.getString("ticket_type");
                    Ticket ticket = (type != null && type.toLowerCase().contains("premium"))
                            ? new PremiumTicket(movie, seat)
                            : new StandardTicket(movie, seat);
                    cart.addItem(ticket);
                }
            }
        }
        return cart;
    }
}
