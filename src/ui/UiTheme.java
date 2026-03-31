package ui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Font;

public class UiTheme {
    public static final Color BG = new Color(245, 245, 248);
    public static final Color CARD = Color.WHITE;
    public static final Color ACCENT = new Color(18, 18, 22);
    public static final Color ACCENT_2 = new Color(229, 9, 20);
    public static final Color TEXT = new Color(26, 26, 26);
    public static final Color MUTED = new Color(120, 120, 120);

    public static final Font TITLE = new Font("Segoe UI Semibold", Font.BOLD, 26);
    public static final Font H2 = new Font("Segoe UI Semibold", Font.BOLD, 18);
    public static final Font BODY = new Font("Segoe UI", Font.PLAIN, 13);

    public static void apply() {
        UIManager.put("Label.font", BODY);
        UIManager.put("Button.font", BODY);
        UIManager.put("TextField.font", BODY);
        UIManager.put("PasswordField.font", BODY);
        UIManager.put("ComboBox.font", BODY);
        UIManager.put("List.font", BODY);
        UIManager.put("TabbedPane.font", BODY);
    }

    public static Border cardBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        );
    }

    public static JButton primaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(ACCENT_2);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        return btn;
    }

    public static JButton dangerButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(180, 20, 30));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        return btn;
    }

    public static JLabel title(String text) {
        JLabel label = new JLabel(text);
        label.setFont(TITLE);
        label.setForeground(TEXT);
        return label;
    }

    public static JLabel subtitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(BODY);
        label.setForeground(MUTED);
        return label;
    }

    public static JPanel card(JComponent content) {
        JPanel panel = new JPanel();
        panel.setBackground(CARD);
        panel.setBorder(cardBorder());
        panel.setLayout(new java.awt.BorderLayout());
        panel.add(content, java.awt.BorderLayout.CENTER);
        return panel;
    }
}
