package ui;

import service.istaff;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.function.BiConsumer;

public class LoginPanel extends JPanel {
    public LoginPanel(AppState state, BiConsumer<String, istaff> onLogin) {
        setLayout(new BorderLayout());
        setBackground(UiTheme.BG);

        JPanel brand = new JPanel(new BorderLayout());
        brand.setBackground(UiTheme.ACCENT);
        JLabel brandTitle = new JLabel("MOVIE BOOKING SYSTEM");
        brandTitle.setForeground(Color.WHITE);
        brandTitle.setFont(UiTheme.TITLE);
        JLabel brandSub = new JLabel("Movies  Events  Seats");
        brandSub.setForeground(new Color(220, 220, 220));
        brandSub.setFont(UiTheme.BODY);
        JPanel brandWrap = new JPanel(new GridBagLayout());
        brandWrap.setBackground(UiTheme.ACCENT);
        GridBagConstraints bc = new GridBagConstraints();
        bc.insets = new Insets(6, 6, 6, 6);
        bc.gridx = 0; bc.gridy = 0; brandWrap.add(brandTitle, bc);
        bc.gridy = 1; brandWrap.add(brandSub, bc);
        brand.add(brandWrap, BorderLayout.CENTER);
        brand.setPreferredSize(new java.awt.Dimension(320, 0));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UiTheme.CARD);
        form.setBorder(UiTheme.cardBorder());

        JLabel title = UiTheme.title("Welcome Back");
        JLabel subtitle = UiTheme.subtitle("Login to manage movies and bookings");

        JLabel roleLabel = new JLabel("Role");
        JComboBox<String> roleBox = new JComboBox<>(new String[]{
                "Manager", "Ticketing Agent", "Cashier", "Customer"
        });
        JLabel userLabel = new JLabel("Username");
        JTextField userField = new JTextField(16);
        JLabel passLabel = new JLabel("Password");
        JPasswordField passField = new JPasswordField(16);
        JLabel status = new JLabel(" ");
        JButton loginBtn = UiTheme.primaryButton("Login");

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0; c.gridy = 0; c.gridwidth = 2; form.add(title, c);
        c.gridx = 0; c.gridy = 1; c.gridwidth = 2; form.add(subtitle, c);
        c.gridwidth = 1;
        c.gridx = 0; c.gridy = 2; form.add(roleLabel, c);
        c.gridx = 1; c.gridy = 2; form.add(roleBox, c);
        c.gridx = 0; c.gridy = 3; form.add(userLabel, c);
        c.gridx = 1; c.gridy = 3; form.add(userField, c);
        c.gridx = 0; c.gridy = 4; form.add(passLabel, c);
        c.gridx = 1; c.gridy = 4; form.add(passField, c);
        c.gridx = 1; c.gridy = 5; form.add(loginBtn, c);
        c.gridx = 0; c.gridy = 6; c.gridwidth = 2; form.add(status, c);

        Runnable syncRole = () -> {
            String role = (String) roleBox.getSelectedItem();
            boolean isCustomer = "Customer".equals(role);
            userLabel.setVisible(!isCustomer);
            userField.setVisible(!isCustomer);
            passLabel.setVisible(!isCustomer);
            passField.setVisible(!isCustomer);
            status.setText(isCustomer ? "Customer booking does not require login." : " ");
            form.revalidate();
            form.repaint();
        };
        roleBox.addActionListener(e -> syncRole.run());
        syncRole.run();

        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 40));
        center.setBackground(UiTheme.BG);
        center.add(form);

        add(brand, BorderLayout.WEST);
        add(center, BorderLayout.CENTER);

        loginBtn.addActionListener(e -> {
            String role = (String) roleBox.getSelectedItem();
            if ("Customer".equals(role)) {
                onLogin.accept("customer", null);
                return;
            }
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());
            if (username.isEmpty() || password.isEmpty()) {
                status.setText("Please enter username and password.");
                return;
            }
            String roleKey = role.toLowerCase().contains("ticketing") ? "ticketing"
                    : role.toLowerCase().contains("cashier") ? "cashier"
                    : "manager";
            istaff staff = state.authenticateRole(username, password, roleKey);
            if (staff == null) {
                status.setText("Login failed.");
                return;
            }
            status.setText(" ");
            onLogin.accept(roleKey, staff);
        });
    }
}
