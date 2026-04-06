package service;

// Abstraction/Polymorphism: implements istaff interface.
// This class is for cashier staff.
public class CashierStaff implements istaff{
   
    // ====== Fields (Encapsulation) ======
    // Basic info.
    private String staffId;
    private String fullName;
    private String phone;
    private String username;
    private String password;   
    private String position;  
    private boolean active;

    @Override
    // Check if cashier can do an action.
    public boolean can(String action) {
        if (!active || isBlank(action)) return false;

        String permission = action.trim().toLowerCase();
        return permission.equals("create_customer")
                || permission.equals("create_order")
                || permission.equals("view_customers")
                || permission.equals("view_orders");
    }

    // ====== Constructor ======
    // Make a cashier with data.
    public CashierStaff(String staffId, String fullName, String phone,
                 String username, String password, String position) {

        setStaffId(staffId);
        setFullName(fullName);
        setPhone(phone);
        setUsername(username);
        setPassword(password);
        setPosition(position);

        this.active = true;
    }

    // ====== Getters ======
    // Simple getters for fields.
    public String getStaffId() { return staffId; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getUsername() { return username; }
    public String getPosition() { return position; }
    public boolean isActive() { return active; }

    // For login check (simple for lesson)
    // Check password string.
    public boolean checkPassword(String input) {
        return password != null && password.equals(input);
    }

    // ====== Setters (with simple validation) ======
    // Set fields with small checks.
    public void setStaffId(String staffId) {
        if (isBlank(staffId)) this.staffId = "UNKNOWN";
        else this.staffId = staffId.trim();
    }

    public void setFullName(String fullName) {
        if (isBlank(fullName)) this.fullName = "No Name";
        else this.fullName = fullName.trim();
    }

    public void setPhone(String phone) {
        String p = (phone == null) ? "" : phone.trim();
        // simple validation: only digits, length 8–15
        if (!isDigits(p) || p.length() < 8 || p.length() > 15) this.phone = "00000000";
        else this.phone = p;
    }

    public void setUsername(String username) {
        if (isBlank(username)) this.username = "staff_" + this.staffId;
        else this.username = username.trim();
    }

    public void setPassword(String password) {
        String pw = (password == null) ? "" : password;
        // simple rule for teaching: >= 4 chars
        if (pw.length() < 4) this.password = "0000";
        else this.password = pw;
    }

    public void setPosition(String position) {
        if (isBlank(position)) this.position = "Staff";
        else this.position = position.trim();
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // ====== Helpers ======
    // Small helper checks.
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

    // Process order by quantity string.
    public void processOrder(String qtyInput){
    int qty;
    try {
        qty = Integer.parseInt(qtyInput);
        System.out.println("Order accepted for" + qty + " tickets.");
        
    } catch (NumberFormatException e) {
        System.out.println("Invalid quantity input. Please enter a valid integer.");
        return;
    }
}

    // Checkout and generate receipt.
    public void checkout (Customer customer, double totalAmount){
        if(customer.deductBalance(totalAmount)){
            System.out.println("Checkout successful. Remaining balance: $" + customer.getBalance());
            ReceiptGenerator.generateReceipt(customer, totalAmount);
        } else {
            System.out.println("Checkout failed. Insufficient balance.");
        }
}

    // ====== toString ======
    // Show staff as text.
    @Override
    public String toString() {
        return "Staff{" +
                "staffId='" + staffId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", username='" + username + '\'' +
                ", position='" + position + '\'' +
                ", active=" + active +
                '}';
    }
}
