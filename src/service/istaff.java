package service;

// Abstraction: interface defines a contract for staff behavior.
// Polymorphism: different staff classes implement this interface.
public interface istaff {

    // Staff id.
    String getStaffId();
    // Login username.
    String getUsername();
    // Job position.
    String getPosition();
    // Is active?
    boolean isActive();
    // Check password.
    boolean checkPassword(String input);
    // Full name.
    String getFullName();

    // Permission check.
    boolean can(String action);
}
