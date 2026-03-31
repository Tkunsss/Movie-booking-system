package ui;

import service.istaff;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;

public class MainFrame extends JFrame {
    private final AppState state;
    private final CardLayout cards = new CardLayout();
    private final JPanel root = new JPanel(cards);

    public MainFrame(AppState state) {
        super("Movie Booking System");
        this.state = state;
        UiTheme.apply();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 680);
        setLocationRelativeTo(null);

        LoginPanel loginPanel = new LoginPanel(state, this::onLogin);
        root.add(loginPanel, "login");

        root.add(new ManagerPanel(state, this::showLogin), "manager");
        root.add(new CashierPanel(state, this::showLogin), "cashier");
        root.add(new CustomerPanel(state, this::showLogin), "customer");
        root.add(new AgentPanel(state, this::showLogin), "ticketing");

        setContentPane(root);
        cards.show(root, "login");
    }

    private void onLogin(String roleKey, istaff staff) {
        if ("manager".equals(roleKey)) {
            cards.show(root, "manager");
        } else if ("cashier".equals(roleKey)) {
            cards.show(root, "cashier");
        } else if ("ticketing".equals(roleKey)) {
            cards.show(root, "ticketing");
        } else {
            cards.show(root, "customer");
        }
    }

    private void showLogin() {
        cards.show(root, "login");
    }
}
