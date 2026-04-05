package ui;

import model.Movie;
import model.Order;
import model.Totals;
import service.Cart;
import service.Customer;
import service.PremiumTicket;
import service.StandardTicket;
import service.Ticket;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

public class CashierPanel extends JPanel {
    private final AppState state;
    private final DefaultListModel<String> customerModel = new DefaultListModel<>();
    private final DefaultListModel<String> orderModel = new DefaultListModel<>();
    private final List<Customer> customerView = new ArrayList<>();
    private final List<Order> orderView = new ArrayList<>();

    public CashierPanel(AppState state, Runnable onLogout) {
        this.state = state;
        setLayout(new BorderLayout());
        setBackground(UiTheme.BG);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UiTheme.ACCENT);
        javax.swing.JLabel title = new javax.swing.JLabel("Cashier Desk");
        title.setFont(UiTheme.H2);
        title.setForeground(java.awt.Color.WHITE);
        javax.swing.JLabel sub = new javax.swing.JLabel("Customers and payments");
        sub.setForeground(new java.awt.Color(220, 220, 220));
        JButton logout = UiTheme.dangerButton("Logout");
        logout.addActionListener(e -> onLogout.run());
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        left.setBackground(UiTheme.ACCENT);
        left.add(title);
        left.add(sub);
        header.add(left, BorderLayout.WEST);
        header.add(logout, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Customers", buildCustomersTab());
        tabs.add("Orders", buildOrdersTab());
        add(tabs, BorderLayout.CENTER);

        refreshCustomers();
        refreshOrders();
    }

    private JPanel buildCustomersTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(UiTheme.cardBorder());
        JList<String> list = new JList<>(customerModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refresh = new JButton("Refresh");
        JButton add = UiTheme.primaryButton("Add Customer");
        JButton deactivate = new JButton("Deactivate");
        JButton delete = UiTheme.dangerButton("Delete");
        actions.add(refresh);
        actions.add(add);
        actions.add(deactivate);
        actions.add(delete);
        panel.add(actions, BorderLayout.SOUTH);

        refresh.addActionListener(e -> refreshCustomers());
        add.addActionListener(e -> {
            Customer c = promptCustomer();
            if (c == null) return;
            state.customerDao.insert(c);
            refreshCustomers();
        });
        deactivate.addActionListener(e -> deactivateCustomer(list.getSelectedIndex()));
        delete.addActionListener(e -> deleteCustomer(list.getSelectedIndex()));
        return panel;
    }

    private JPanel buildOrdersTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(UiTheme.cardBorder());
        JList<String> list = new JList<>(orderModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refresh = new JButton("Refresh");
        JButton create = UiTheme.primaryButton("Create Order");
        JButton refund = UiTheme.dangerButton("Refund");
        JButton cancel = new JButton("Cancel (Soft)");
        JButton delete = UiTheme.dangerButton("Delete (Hard)");
        actions.add(refresh);
        actions.add(create);
        actions.add(refund);
        actions.add(cancel);
        actions.add(delete);
        panel.add(actions, BorderLayout.SOUTH);

        refresh.addActionListener(e -> refreshOrders());
        create.addActionListener(e -> createOrder());
        refund.addActionListener(e -> refundOrder(list.getSelectedIndex()));
        cancel.addActionListener(e -> cancelOrder(list.getSelectedIndex()));
        delete.addActionListener(e -> deleteOrder(list.getSelectedIndex()));
        return panel;
    }

    private void refreshCustomers() {
        state.refreshCustomers();
        customerModel.clear();
        customerView.clear();
        for (Customer c : state.customers) {
            if (!c.isActive()) continue;
            customerModel.addElement(c.getCustomerId() + " - " + c.getFullName()
                    + " | Balance: $" + c.getBalance());
            customerView.add(c);
        }
    }

    private void refreshOrders() {
        state.refreshOrders();
        orderModel.clear();
        orderView.clear();
        for (Order o : state.orders) {
            String customer = o.customer == null ? "Unknown" : o.customer.getFullName();
            orderModel.addElement(o.id + ") " + o.description + " | " + customer + " | " + o.status);
            orderView.add(o);
        }
    }

    private Customer promptCustomer() {
        JTextField name = new JTextField();
        JTextField phone = new JTextField();
        JTextField balance = new JTextField("0");
        Object[] fields = {
                "Customer Name", name,
                "Phone", phone,
                "Balance", balance
        };
        int res = JOptionPane.showConfirmDialog(this, fields, "Create Customer",
                JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return null;
        try {
            if (name.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name is required.");
                return null;
            }
            double bal = Double.parseDouble(balance.getText().trim());
            String id = generateCustomerId();
            return new Customer(id, name.getText().trim(),
                    phone.getText().trim(), "0000", bal, null);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid balance.");
            return null;
        }
    }

    private String generateCustomerId() {
        String id;
        do {
            id = "CUST-" + System.currentTimeMillis();
        } while (state.customerDao.existsById(id));
        return id;
    }

    private void createOrder() {
        List<Customer> activeCustomers = getActiveCustomers();
        if (activeCustomers.isEmpty() || state.movies.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Need customers and movies.");
            return;
        }
        String[] customerChoices = activeCustomers.stream()
                .map(c -> c.getCustomerId() + " - " + c.getFullName())
                .toArray(String[]::new);
        String customerSel = (String) JOptionPane.showInputDialog(this,
                "Select Customer", "Create Order",
                JOptionPane.PLAIN_MESSAGE, null, customerChoices, customerChoices[0]);
        if (customerSel == null) return;
        Customer customer = activeCustomers.get(indexOf(customerChoices, customerSel));

        String[] movieChoices = state.movies.stream()
                .map(m -> m.getId() + " - " + m.getTitle())
                .toArray(String[]::new);
        String movieSel = (String) JOptionPane.showInputDialog(this,
                "Select Movie", "Create Order",
                JOptionPane.PLAIN_MESSAGE, null, movieChoices, movieChoices[0]);
        if (movieSel == null) return;
        Movie movie = state.movies.get(indexOf(movieChoices, movieSel));

        JTextField ticketsField = new JTextField("1");
        JTextField startSeatField = new JTextField("1");
        javax.swing.JComboBox<String> typeBox = new javax.swing.JComboBox<>(new String[]{"Standard", "Premium"});
        Object[] fields = {
                "Tickets", ticketsField,
                "Start Seat", startSeatField,
                "Type", typeBox
        };
        int res = JOptionPane.showConfirmDialog(this, fields, "Order Details",
                JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;
        try {
            int tickets = Integer.parseInt(ticketsField.getText().trim());
            int startSeat = Integer.parseInt(startSeatField.getText().trim());
            String type = (String) typeBox.getSelectedItem();

            if (!areSeatsAvailable(movie, startSeat, tickets)) {
                JOptionPane.showMessageDialog(this, "Some seats are already booked.");
                return;
            }
            Cart cart = new Cart();
            for (int i = 0; i < tickets; i++) {
                int seat = startSeat + i;
                Ticket t = "Premium".equals(type)
                        ? new PremiumTicket(movie, seat)
                        : new StandardTicket(movie, seat);
                cart.addItem(t);
            }
            Totals totals = TotalsUtil.calculateTotals(cart);
            String desc = "Movie: " + movie.getTitle()
                    + ", Tickets: " + tickets
                    + ", Total: $" + totals.total;
            Order order = new Order(0, desc, "PENDING_PAYMENT", customer, cart, totals);
            int id = state.orderDao.insert(order);
            order.id = id;
            state.orders.add(order);
            refreshOrders();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid numbers.");
        }
    }

    private void refundOrder(int index) {
        if (index < 0 || index >= orderView.size()) return;
        Order order = orderView.get(index);
        if ("CANCELLED".equalsIgnoreCase(order.status)) return;
        if ("PAID".equalsIgnoreCase(order.status) && order.customer != null) {
            order.customer.addBalance(order.totals.total);
            state.customerDao.updateBalance(order.customer.getCustomerId(), order.customer.getBalance());
        }
        order.status = "CANCELLED";
        state.orderDao.updateStatus(order.id, order.status);
        refreshOrders();
    }

    private void cancelOrder(int index) {
        if (index < 0 || index >= orderView.size()) return;
        Order order = orderView.get(index);
        if ("CANCELLED".equalsIgnoreCase(order.status)) return;
        int confirm = JOptionPane.showConfirmDialog(this,
                "Cancel order " + order.id + " without refund?",
                "Confirm Cancel", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        order.status = "CANCELLED";
        state.orderDao.cancelById(order.id);
        refreshOrders();
    }

    private void deleteOrder(int index) {
        if (index < 0 || index >= orderView.size()) return;
        Order order = orderView.get(index);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete order " + order.id + "? This cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        state.orderDao.deleteById(order.id);
        state.orders.remove(order);
        refreshOrders();
    }

    private void deactivateCustomer(int index) {
        if (index < 0 || index >= customerView.size()) return;
        Customer customer = customerView.get(index);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Deactivate customer " + customer.getCustomerId() + "?",
                "Confirm Deactivate", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        customer.setActive(false);
        state.customerDao.setActive(customer.getCustomerId(), false);
        refreshCustomers();
    }

    private void deleteCustomer(int index) {
        if (index < 0 || index >= customerView.size()) return;
        Customer customer = customerView.get(index);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete customer " + customer.getCustomerId() + "? This cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        state.customerDao.deleteById(customer.getCustomerId());
        state.customers.remove(customer);
        refreshCustomers();
    }

    private List<Customer> getActiveCustomers() {
        List<Customer> active = new ArrayList<>();
        for (Customer c : state.customers) {
            if (c.isActive()) active.add(c);
        }
        return active;
    }

    private boolean areSeatsAvailable(Movie movie, int startSeat, int tickets) {
        for (int i = 0; i < tickets; i++) {
            int seat = startSeat + i;
            if (!state.orderDao.isSeatAvailable(movie.getId(), seat)) return false;
        }
        return true;
    }

    private int indexOf(String[] arr, String value) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(value)) return i;
        }
        return 0;
    }
}
