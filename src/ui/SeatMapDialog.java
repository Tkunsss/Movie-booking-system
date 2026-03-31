package ui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SeatMapDialog extends JDialog {
    private final List<Integer> selected = new ArrayList<>();
    private boolean confirmed = false;

    public SeatMapDialog(java.awt.Frame owner, int requiredCount, Set<Integer> booked) {
        super(owner, "Select Seats", true);
        setSize(520, 520);
        setLocationRelativeTo(owner);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UiTheme.BG);
        JLabel info = new JLabel("Select " + requiredCount + " seats");
        info.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        root.add(info, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(8, 10, 6, 6));
        grid.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        for (int seat = 1; seat <= 80; seat++) {
            JButton b = new JButton(String.valueOf(seat));
            b.setFocusPainted(false);
            if (booked.contains(seat)) {
                b.setEnabled(false);
                b.setBackground(new Color(200, 200, 200));
            } else {
                b.setBackground(Color.WHITE);
                int s = seat;
                b.addActionListener(e -> toggleSeat(b, s, requiredCount));
            }
            grid.add(b);
        }
        root.add(grid, BorderLayout.CENTER);

        JPanel actions = new JPanel();
        JButton ok = UiTheme.primaryButton("Confirm");
        JButton cancel = new JButton("Cancel");
        ok.addActionListener(e -> {
            if (selected.size() != requiredCount) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Please select exactly " + requiredCount + " seats.");
                return;
            }
            confirmed = true;
            setVisible(false);
        });
        cancel.addActionListener(e -> setVisible(false));
        actions.add(ok);
        actions.add(cancel);
        root.add(actions, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private void toggleSeat(JButton b, int seat, int required) {
        if (selected.contains(seat)) {
            selected.remove((Integer) seat);
            b.setBackground(Color.WHITE);
            return;
        }
        if (selected.size() >= required) return;
        selected.add(seat);
        b.setBackground(new Color(255, 230, 230));
    }

    public boolean isConfirmed() { return confirmed; }

    public List<Integer> getSelectedSeats() { return selected; }
}
