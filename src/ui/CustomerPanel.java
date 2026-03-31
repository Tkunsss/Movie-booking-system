package ui;

import model.Movie;
import model.Order;
import model.Totals;
import service.Cart;
import service.Customer;
import service.PremiumTicket;
import service.ReceiptGenerator;
import service.StandardTicket;
import service.Ticket;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomerPanel extends JPanel {
    private final AppState state;

    public CustomerPanel(AppState state, Runnable onLogout) {
        this.state = state;
        setLayout(new BorderLayout());
        setBackground(UiTheme.BG);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UiTheme.ACCENT);
        JLabel title = new JLabel("Customer Booking");
        title.setFont(UiTheme.H2);
        title.setForeground(java.awt.Color.WHITE);
        JLabel sub = new JLabel("Choose movie, seats and pay instantly");
        sub.setForeground(new java.awt.Color(220, 220, 220));
        JButton back = UiTheme.dangerButton("Back to Login");
        back.addActionListener(e -> onLogout.run());
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        left.setBackground(UiTheme.ACCENT);
        left.add(title);
        left.add(sub);
        header.add(left, BorderLayout.WEST);
        header.add(back, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(UiTheme.BG);
        JPanel form = buildForm();
        JPanel card = UiTheme.card(form);
        JPanel formWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        formWrap.setBackground(UiTheme.BG);
        formWrap.add(card);
        center.add(formWrap, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
    }

    private JPanel buildForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UiTheme.CARD);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;

        JComboBox<String> movieBox = new JComboBox<>(state.movies.stream()
                .map(m -> m.getId() + " - " + m.getTitle())
                .toArray(String[]::new));
        JTextField ticketsField = new JTextField("1", 10);
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Standard", "Premium"});
        JLabel seatsSelected = new JLabel("Seats: none");
        JButton pickSeats = UiTheme.primaryButton("Pick Seats");
        final List<Integer>[] chosenSeats = new List[]{List.of()};

        JRadioButton newCustomer = new JRadioButton("New Customer", true);
        JRadioButton existingCustomer = new JRadioButton("Existing Customer");
        ButtonGroup group = new ButtonGroup();
        group.add(newCustomer);
        group.add(existingCustomer);

        JComboBox<String> existingBox = new JComboBox<>(state.customers.stream()
                .map(cu -> cu.getCustomerId() + " - " + cu.getFullName())
                .toArray(String[]::new));
        JButton refreshCustomers = new JButton("Refresh");

        JTextField nameField = new JTextField(16);
        JTextField idField = new JTextField(16);
        JTextField phoneField = new JTextField(16);
        JTextField balanceField = new JTextField("0", 10);

        int row = 0;
        c.gridx = 0; c.gridy = row; panel.add(new JLabel("Movie"), c);
        c.gridx = 1; c.gridy = row++; panel.add(movieBox, c);
        c.gridx = 0; c.gridy = row; panel.add(new JLabel("Tickets"), c);
        c.gridx = 1; c.gridy = row++; panel.add(ticketsField, c);
        c.gridx = 0; c.gridy = row; panel.add(new JLabel("Type"), c);
        c.gridx = 1; c.gridy = row++; panel.add(typeBox, c);
        c.gridx = 0; c.gridy = row; panel.add(new JLabel("Seats"), c);
        JPanel seatRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        seatRow.setBackground(UiTheme.CARD);
        seatRow.add(pickSeats);
        seatRow.add(seatsSelected);
        c.gridx = 1; c.gridy = row++; panel.add(seatRow, c);

        c.gridx = 0; c.gridy = row; panel.add(new JLabel("Customer Type"), c);
        JPanel radios = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        radios.setBackground(UiTheme.CARD);
        radios.add(newCustomer);
        radios.add(existingCustomer);
        c.gridx = 1; c.gridy = row++; panel.add(radios, c);

        c.gridx = 0; c.gridy = row; panel.add(new JLabel("Existing Customer"), c);
        JPanel existingRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        existingRow.setBackground(UiTheme.CARD);
        existingRow.add(existingBox);
        existingRow.add(refreshCustomers);
        c.gridx = 1; c.gridy = row++; panel.add(existingRow, c);
        c.gridx = 0; c.gridy = row; panel.add(new JLabel("Customer Name"), c);
        c.gridx = 1; c.gridy = row++; panel.add(nameField, c);
        c.gridx = 0; c.gridy = row; panel.add(new JLabel("Customer ID"), c);
        c.gridx = 1; c.gridy = row++; panel.add(idField, c);
        c.gridx = 0; c.gridy = row; panel.add(new JLabel("Phone"), c);
        c.gridx = 1; c.gridy = row++; panel.add(phoneField, c);
        c.gridx = 0; c.gridy = row; panel.add(new JLabel("Balance"), c);
        c.gridx = 1; c.gridy = row++; panel.add(balanceField, c);

        JButton book = UiTheme.primaryButton("Book & Pay");
        c.gridx = 1; c.gridy = row; panel.add(book, c);

        Runnable syncMode = () -> {
            boolean isNew = newCustomer.isSelected();
            existingBox.setEnabled(!isNew);
            nameField.setEnabled(isNew);
            idField.setEnabled(isNew);
            phoneField.setEnabled(isNew);
            balanceField.setEnabled(isNew);
            if (!isNew && state.customers.size() > 0) {
                Customer cst = state.customers.get(existingBox.getSelectedIndex());
                balanceField.setText(String.valueOf(cst.getBalance()));
            }
        };
        newCustomer.addActionListener(e -> syncMode.run());
        existingCustomer.addActionListener(e -> syncMode.run());
        existingBox.addActionListener(e -> syncMode.run());
        refreshCustomers.addActionListener(e -> {
            state.refreshCustomers();
            existingBox.removeAllItems();
            for (Customer cu : state.customers) {
                existingBox.addItem(cu.getCustomerId() + " - " + cu.getFullName());
            }
            syncMode.run();
        });
        syncMode.run();

        pickSeats.addActionListener(e -> {
            try {
                int tickets = Integer.parseInt(ticketsField.getText().trim());
                Movie movie = state.movies.get(movieBox.getSelectedIndex());
                if (movie == null) return;
                Set<Integer> booked = new HashSet<>(state.orderDao.getBookedSeats(movie.getId()));
                SeatMapDialog dialog = new SeatMapDialog(
                        (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this),
                        tickets, booked);
                dialog.setVisible(true);
                if (!dialog.isConfirmed()) return;
                chosenSeats[0] = dialog.getSelectedSeats();
                seatsSelected.setText("Seats: " + chosenSeats[0]);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid ticket count.");
            }
        });

        book.addActionListener(e -> {
            try {
                if (state.movies.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No movies available.");
                    return;
                }
                Movie movie = state.movies.get(movieBox.getSelectedIndex());
                if (movie == null) return;
                int tickets = Integer.parseInt(ticketsField.getText().trim());
                String type = (String) typeBox.getSelectedItem();

                if (chosenSeats[0].size() != tickets) {
                    JOptionPane.showMessageDialog(this, "Please pick " + tickets + " seats.");
                    return;
                }

                Customer customer;
                if (existingCustomer.isSelected() && state.customers.size() > 0) {
                    customer = state.customers.get(existingBox.getSelectedIndex());
                } else {
                    String customerId = idField.getText().trim();
                    if (customerId.isEmpty()) {
                        customerId = "WALKIN-" + System.currentTimeMillis();
                    }
                    double balance = Double.parseDouble(balanceField.getText().trim());
                    customer = new Customer(customerId,
                            nameField.getText().trim(),
                            phoneField.getText().trim(),
                            "0000",
                            balance,
                            null);
                    state.customerDao.insert(customer);
                    state.customers.add(customer);
                }

                Cart cart = new Cart();
                for (int seat : chosenSeats[0]) {
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

                if (!customer.deductBalance(totals.total)) {
                    order.status = "PAYMENT_FAILED";
                    state.orderDao.updateStatus(order.id, order.status);
                    JOptionPane.showMessageDialog(this, "Insufficient balance.");
                    return;
                }
                state.customerDao.updateBalance(customer.getCustomerId(), customer.getBalance());
                ReceiptGenerator.generateReceipt(customer, cart,
                        totals.subtotal, totals.discount, totals.tax, totals.total);
                order.status = "PAID";
                state.orderDao.updateStatus(order.id, order.status);
                TicketConfirmationDialog.show(
                        (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this),
                        movie.getTitle(),
                        customer.getFullName(),
                        chosenSeats[0],
                        totals.total,
                        order.id
                );
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid numbers.");
            }
        });
        return panel;
    }
}
