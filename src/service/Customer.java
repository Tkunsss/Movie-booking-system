package service;

// OOP: Encapsulation (private fields + validation in setters).
// OOP: Methods change object state (addBalance/deductBalance).
// This class stores customer info and balance.
public class Customer {

    // Basic info.
    // Encapsulation: private fields with validation in setters.
    private String customerId;
    private String fullName;
    private String phone;
    private String password;     
    private double balance;
    private boolean active;

    // Who created this customer (staff).
    // Abstraction/Polymorphism: uses istaff interface, any staff type can create a customer.
    private istaff createdBy;

    // Build a customer with data.
    public Customer(String customerId, String fullName, String phone,
                    String password, double balance, istaff createdBy) {

        setCustomerId(customerId);
        setFullName(fullName);
        setPhone(phone);
        setPassword(password);
        setBalance(balance);

        this.createdBy = createdBy;   // can be null, CoffeeShop should validate
        this.active = true;
    }

    // ===== Getters =====
    // Get customer id.
    public String getCustomerId() { return customerId; }
    // Get customer name.
    public String getFullName() { return fullName; }
    // Get phone.
    public String getPhone() { return phone; }
    // Get balance.
    public double getBalance() { return balance; }
    // Check active flag.
    public boolean isActive() { return active; }
    // Who created this customer.
    public istaff getCreatedBy() { return createdBy; }

    // Check password.
    public boolean checkPassword(String input) {
        return password != null && password.equals(input);
    }

    // ===== Setters with simple validation =====
    // Set customer id.
    public void setCustomerId(String customerId) {
        if (isBlank(customerId)) this.customerId = "UNKNOWN";
        else this.customerId = customerId.trim();
    }

    // Set customer name.
    public void setFullName(String fullName) {
        if (isBlank(fullName)) this.fullName = "No Name";
        else this.fullName = fullName.trim();
    }

    // Set phone with simple rule.
    public void setPhone(String phone) {
        String p = (phone == null) ? "" : phone.trim();
        if (!isDigits(p) || p.length() < 8 || p.length() > 15) this.phone = "00000000";
        else this.phone = p;
    }

    // Set password (min length 4).
    public void setPassword(String password) {
        String pw = (password == null) ? "" : password;
        if (pw.length() < 4) this.password = "0000";
        else this.password = pw;
    }

    // Set balance (no negative).
    public void setBalance(double balance) {
        if (balance < 0) this.balance = 0;
        else this.balance = balance;
    }

    // Set active flag.
    public void setActive(boolean active) { this.active = active; }

    // ===== Balance helpers =====
    // Add money.
    public void addBalance(double amount) {
        if (amount > 0) balance += amount;
    }

    // Spend money if possible.
    public boolean deductBalance(double amount) {
        if (amount <= 0) return false;
        if (amount > balance) return false;
        balance -= amount;
        return true;
    }

    // ===== Helpers =====
    // Check blank string.
    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    // Check digits only.
    private boolean isDigits(String s) {
        if (isBlank(s)) return false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < '0' || c > '9') return false;
        }
        return true;
    }

    // Show customer as text.
    @Override
    public String toString() {
        String staffInfo = (createdBy == null) ? "UNKNOWN" : createdBy.getStaffId();
        return "Customer{" +
                "customerId='" + customerId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", balance=" + balance +
                ", active=" + active +
                ", createdByStaffId='" + staffInfo + '\'' +
                '}';
    }
}
