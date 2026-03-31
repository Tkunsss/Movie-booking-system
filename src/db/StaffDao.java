package db;

import service.CashierStaff;
import service.ManagerStaff;
import service.Ticketing_Agent;
import service.istaff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// OOP: DAO pattern (separates DB access from UI/logic).
public class StaffDao {

    public List<istaff> getAll() {
        List<istaff> staffList = new ArrayList<>();
        String sql = "SELECT staff_id, full_name, phone, username, password, position, active FROM staff";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String position = rs.getString("position");
                istaff staff = buildStaff(
                        rs.getString("staff_id"),
                        rs.getString("full_name"),
                        rs.getString("phone"),
                        rs.getString("username"),
                        rs.getString("password"),
                        position
                );
                if (staff != null) {
                    boolean active = rs.getInt("active") == 1;
                    setActive(staff, active);
                    staffList.add(staff);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Load staff failed", e);
        }
        return staffList;
    }

    public void insert(istaff staff, String password, boolean active) {
        String sql = "INSERT INTO staff(staff_id, full_name, phone, username, password, position, active) " +
                "VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, staff.getStaffId());
            ps.setString(2, staff.getFullName());
            ps.setString(3, getPhone(staff));
            ps.setString(4, staff.getUsername());
            ps.setString(5, password);
            ps.setString(6, staff.getPosition());
            ps.setInt(7, active ? 1 : 0);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Insert staff failed", e);
        }
    }

    public void deactivate(String staffId) {
        String sql = "UPDATE staff SET active=0 WHERE staff_id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, staffId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Deactivate staff failed", e);
        }
    }

    public void deleteById(String staffId) {
        String sql = "DELETE FROM staff WHERE staff_id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, staffId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Delete staff failed", e);
        }
    }

    private istaff buildStaff(String staffId, String fullName, String phone,
                              String username, String password, String position) {
        String p = position == null ? "" : position.toLowerCase();
        if (p.contains("manager") || p.contains("admin")) {
            return new ManagerStaff(staffId, fullName, phone, username, password, position);
        }
        if (p.contains("ticketing")) {
            return new Ticketing_Agent(staffId, fullName, phone, username, password, position);
        }
        if (p.contains("cashier")) {
            return new CashierStaff(staffId, fullName, phone, username, password, position);
        }
        return null;
    }

    private void setActive(istaff staff, boolean active) {
        if (staff instanceof ManagerStaff) {
            ((ManagerStaff) staff).setActive(active);
        } else if (staff instanceof Ticketing_Agent) {
            ((Ticketing_Agent) staff).setActive(active);
        } else if (staff instanceof CashierStaff) {
            ((CashierStaff) staff).setActive(active);
        }
    }

    private String getPhone(istaff staff) {
        if (staff instanceof ManagerStaff) return ((ManagerStaff) staff).getPhone();
        if (staff instanceof Ticketing_Agent) return ((Ticketing_Agent) staff).getPhone();
        if (staff instanceof CashierStaff) return ((CashierStaff) staff).getPhone();
        return "00000000";
    }
}
