package db;

import service.Customer;
import service.istaff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// OOP: DAO pattern (separates DB access from UI/logic).
public class CustomerDao {

    public List<Customer> getAll(Map<String, istaff> staffMap) {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT customer_id, full_name, phone, password, balance, active, created_by_staff_id FROM customers";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String staffId = rs.getString("created_by_staff_id");
                istaff createdBy = staffId == null ? null : staffMap.get(staffId);
                Customer c = new Customer(
                        rs.getString("customer_id"),
                        rs.getString("full_name"),
                        rs.getString("phone"),
                        rs.getString("password"),
                        rs.getDouble("balance"),
                        createdBy
                );
                c.setActive(rs.getInt("active") == 1);
                customers.add(c);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Load customers failed", e);
        }
        return customers;
    }

    public void insert(Customer customer) {
        String sql = "INSERT INTO customers(customer_id, full_name, phone, password, balance, active, created_by_staff_id) " +
                "VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customer.getCustomerId());
            ps.setString(2, customer.getFullName());
            ps.setString(3, customer.getPhone());
            ps.setString(4, "0000");
            ps.setDouble(5, customer.getBalance());
            ps.setInt(6, customer.isActive() ? 1 : 0);
            ps.setString(7, customer.getCreatedBy() == null ? null : customer.getCreatedBy().getStaffId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Insert customer failed", e);
        }
    }

    public void updateBalance(String customerId, double balance) {
        String sql = "UPDATE customers SET balance=? WHERE customer_id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, balance);
            ps.setString(2, customerId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Update customer balance failed", e);
        }
    }

    public void setActive(String customerId, boolean active) {
        String sql = "UPDATE customers SET active=? WHERE customer_id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, active ? 1 : 0);
            ps.setString(2, customerId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Update customer active failed", e);
        }
    }

    public void deleteById(String customerId) {
        String sql = "DELETE FROM customers WHERE customer_id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customerId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Delete customer failed", e);
        }
    }

    public boolean existsById(String customerId) {
        String sql = "SELECT 1 FROM customers WHERE customer_id=? LIMIT 1";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Check customer exists failed", e);
        }
    }
}
