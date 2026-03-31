package ui;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.List;

public class TicketConfirmationDialog extends JDialog {
    public TicketConfirmationDialog(java.awt.Frame owner, String title, String customer,
                                    String seats, double total, int orderId) {
        super(owner, "Booking Confirmed", true);
        setSize(520, 360);
        setLocationRelativeTo(owner);

        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        root.setBackground(UiTheme.CARD);

        JLabel heading = UiTheme.title("Booking Confirmed");
        root.add(heading, BorderLayout.NORTH);

        JLabel details = new JLabel("<html><b>Movie:</b> " + title +
                "<br><b>Customer:</b> " + customer +
                "<br><b>Seats:</b> " + seats +
                "<br><b>Total:</b> $" + total +
                "<br><b>Order ID:</b> " + orderId + "</html>");
        details.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        root.add(details, BorderLayout.CENTER);

        JPanel qr = new JPanel();
        qr.setBackground(new Color(20, 20, 20));
        qr.setPreferredSize(new java.awt.Dimension(140, 140));
        JLabel qrLabel = new JLabel("QR");
        qrLabel.setForeground(Color.WHITE);
        qrLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        qr.add(qrLabel);
        root.add(qr, BorderLayout.EAST);

        setContentPane(root);
    }

    public static void show(java.awt.Frame owner, String title, String customer,
                            List<Integer> seats, double total, int orderId) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < seats.size(); i++) {
            sb.append(seats.get(i));
            if (i < seats.size() - 1) sb.append(", ");
        }
        TicketConfirmationDialog dialog = new TicketConfirmationDialog(
                owner, title, customer, sb.toString(), total, orderId);
        dialog.setVisible(true);
    }
}
