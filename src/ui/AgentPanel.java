package ui;

import model.Order;

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

public class AgentPanel extends JPanel {
    private final AppState state;
    private final DefaultListModel<String> orderModel = new DefaultListModel<>();

    public AgentPanel(AppState state, Runnable onLogout) {
        this.state = state;
        setLayout(new BorderLayout());
        setBackground(UiTheme.BG);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UiTheme.ACCENT);
        javax.swing.JLabel title = new javax.swing.JLabel("Ticketing Agent");
        title.setFont(UiTheme.H2);
        title.setForeground(java.awt.Color.WHITE);
        javax.swing.JLabel sub = new javax.swing.JLabel("Orders");
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
        tabs.add("Orders", buildOrdersTab());
        add(tabs, BorderLayout.CENTER);

        refreshOrders();
    }

    private JPanel buildOrdersTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(UiTheme.cardBorder());
        JList<String> list = new JList<>(orderModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refresh = new JButton("Refresh");
        JButton update = UiTheme.primaryButton("Update Status");
        actions.add(refresh);
        actions.add(update);
        panel.add(actions, BorderLayout.SOUTH);

        refresh.addActionListener(e -> refreshOrders());
        update.addActionListener(e -> updateStatus(list.getSelectedIndex()));
        return panel;
    }

    private void refreshOrders() {
        state.refreshOrders();
        orderModel.clear();
        for (Order o : state.orders) {
            String customer = o.customer == null ? "Unknown" : o.customer.getFullName();
            orderModel.addElement(o.id + ") " + o.description + " | " + customer + " | " + o.status);
        }
    }

    private void updateStatus(int index) {
        if (index < 0 || index >= state.orders.size()) return;
        Order order = state.orders.get(index);
        JTextField status = new JTextField(order.status);
        int res = JOptionPane.showConfirmDialog(this, status, "Update Status",
                JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;
        String newStatus = status.getText().trim();
        if (newStatus.isEmpty()) return;
        order.status = newStatus;
        state.orderDao.updateStatus(order.id, newStatus);
        refreshOrders();
    }
}
