package service;

public class Customer {

    private String customerId;
    private String fullName;
    private String phone;
    private String password;     
    private double balance;
    private boolean active;

    // Who created this customer (object reference)
    private istaff createdBy;

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
    public String getCustomerId() { return customerId; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public double getBalance() { return balance; }
    public boolean isActive() { return active; }
    public istaff getCreatedBy() { return createdBy; }

    public boolean checkPassword(String input) {
        return password != null && password.equals(input);
    }

    // ===== Setters with simple validation =====
    public void setCustomerId(String customerId) {
        if (isBlank(customerId)) this.customerId = "UNKNOWN";
        else this.customerId = customerId.trim();
    }

    public void setFullName(String fullName) {
        if (isBlank(fullName)) this.fullName = "No Name";
        else this.fullName = fullName.trim();
    }

    public void setPhone(String phone) {
        String p = (phone == null) ? "" : phone.trim();
        if (!isDigits(p) || p.length() < 8 || p.length() > 15) this.phone = "00000000";
        else this.phone = p;
    }

    public void setPassword(String password) {
        String pw = (password == null) ? "" : password;
        if (pw.length() < 4) this.password = "0000";
        else this.password = pw;
    }

    public void setBalance(double balance) {
        if (balance < 0) this.balance = 0;
        else this.balance = balance;
    }

    public void setActive(boolean active) { this.active = active; }

    // ===== Balance helpers =====
    public void addBalance(double amount) {
        if (amount > 0) balance += amount;
    }

    public boolean deductBalance(double amount) {
        if (amount <= 0) return false;
        if (amount > balance) return false;
        balance -= amount;
        return true;
    }

    // ===== Helpers =====
    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private boolean isDigits(String s) {
        if (isBlank(s)) return false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < '0' || c > '9') return false;
        }
        return true;
    }

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
