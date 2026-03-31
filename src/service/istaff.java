package service;

// OOP: Interface (contract) for polymorphism across staff types.
// Interface for all staff types.
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
