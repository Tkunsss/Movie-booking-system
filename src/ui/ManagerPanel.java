package ui;

import model.Movie;
import service.CashierStaff;
import service.ManagerStaff;
import service.Ticketing_Agent;
import service.istaff;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.DefaultListModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

public class ManagerPanel extends JPanel {
    private final AppState state;
    private final DefaultListModel<Movie> movieModel = new DefaultListModel<>();
    private final DefaultListModel<String> staffModel = new DefaultListModel<>();
    private final JLabel reportLabel = new JLabel(" ");

    public ManagerPanel(AppState state, Runnable onLogout) {
        this.state = state;
        setLayout(new BorderLayout());
        setBackground(UiTheme.BG);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UiTheme.ACCENT);
        JLabel title = new JLabel("Manager Dashboard");
        title.setFont(UiTheme.H2);
        title.setForeground(java.awt.Color.WHITE);
        JLabel sub = new JLabel("Movies, staff and sales overview");
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
        tabs.add("Movies", buildMoviesTab());
        tabs.add("Staff", buildStaffTab());
        tabs.add("Report", buildReportTab());
        add(tabs, BorderLayout.CENTER);

        refreshAll();
    }

    private JPanel buildMoviesTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(UiTheme.cardBorder());
        JList<Movie> list = new JList<>(movieModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refresh = new JButton("Refresh");
        JButton add = UiTheme.primaryButton("Add");
        JButton edit = new JButton("Edit");
        JButton delete = UiTheme.dangerButton("Delete");
        actions.add(refresh);
        actions.add(add);
        actions.add(edit);
        actions.add(delete);
        panel.add(actions, BorderLayout.SOUTH);

        refresh.addActionListener(e -> refreshMovies());
        add.addActionListener(e -> {
            Movie m = promptMovie(null);
            if (m == null) return;
            state.movieDao.insert(m);
            refreshMovies();
        });
        edit.addActionListener(e -> {
            Movie selected = list.getSelectedValue();
            if (selected == null) return;
            Movie updated = promptMovie(selected);
            if (updated == null) return;
            state.movieDao.update(updated);
            refreshMovies();
        });
        delete.addActionListener(e -> {
            Movie selected = list.getSelectedValue();
            if (selected == null) return;
            if (selected.getId() > 0 && state.orderDao.hasOrdersForMovie(selected.getId())) {
                JOptionPane.showMessageDialog(this, "Cannot delete: movie has orders.");
                return;
            }
            state.movieDao.deleteById(selected.getId());
            refreshMovies();
        });
        return panel;
    }

    private JPanel buildStaffTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(UiTheme.cardBorder());
        JList<String> list = new JList<>(staffModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refresh = new JButton("Refresh");
        JButton add = UiTheme.primaryButton("Add");
        JButton delete = UiTheme.dangerButton("Delete");
        actions.add(refresh);
        actions.add(add);
        actions.add(delete);
        panel.add(actions, BorderLayout.SOUTH);

        refresh.addActionListener(e -> refreshStaff());
        add.addActionListener(e -> {
            istaff staff = promptStaff();
            if (staff == null) return;
            state.staffDao.insert(staff, "1234", true);
            refreshStaff();
        });
        delete.addActionListener(e -> {
            int idx = list.getSelectedIndex();
            if (idx < 0) return;
            istaff staff = state.staffList.get(idx);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Delete staff " + staff.getStaffId() + "?", "Confirm",
                    JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;
            state.staffDao.deleteById(staff.getStaffId());
            refreshStaff();
        });
        return panel;
    }

    private JPanel buildReportTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(UiTheme.cardBorder());
        reportLabel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        panel.add(reportLabel, BorderLayout.NORTH);
        JButton refresh = new JButton("Refresh Report");
        refresh.addActionListener(e -> refreshReport());
        panel.add(refresh, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshAll() {
        refreshMovies();
        refreshStaff();
        refreshReport();
    }

    private void refreshMovies() {
        state.refreshMovies();
        movieModel.clear();
        for (Movie m : state.movies) movieModel.addElement(m);
    }

    private void refreshStaff() {
        state.refreshStaff();
        staffModel.clear();
        for (istaff s : state.staffList) {
            staffModel.addElement(s.getStaffId() + " - " + s.getFullName()
                    + " | " + s.getUsername()
                    + " | " + s.getPosition()
                    + " | Active: " + s.isActive());
        }
    }

    private void refreshReport() {
        double totalSales = state.orderDao.getTotalSales();
        int totalOrders = state.orderDao.getTotalOrders();
        String topMovie = state.orderDao.getTopMovieTitle();
        reportLabel.setText("<html>Total orders: " + totalOrders
                + "<br>Total sales: $" + totalSales
                + "<br>Top movie: " + topMovie + "</html>");
    }

    private Movie promptMovie(Movie existing) {
        JTextField title = new JTextField(existing == null ? "" : existing.getTitle());
        JTextField price = new JTextField(existing == null ? "" : String.valueOf(existing.getPrice()));
        JTextField date = new JTextField(existing == null ? "" : existing.getReleaseDate());
        Object[] fields = {
                "Title", title,
                "Price", price,
                "Release Date (YYYY-MM-DD)", date
        };
        int res = JOptionPane.showConfirmDialog(this, fields,
                existing == null ? "Add Movie" : "Edit Movie",
                JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return null;
        try {
            double p = Double.parseDouble(price.getText().trim());
            if (existing == null) {
                return new Movie(title.getText().trim(), p, date.getText().trim());
            }
            existing.setTitle(title.getText().trim());
            existing.setPrice(p);
            existing.setReleaseDate(date.getText().trim());
            return existing;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid price.");
            return null;
        }
    }

    private istaff promptStaff() {
        String[] roles = {"Manager", "Ticketing Agent", "Cashier"};
        String role = (String) JOptionPane.showInputDialog(this,
                "Role:", "Create Staff",
                JOptionPane.PLAIN_MESSAGE, null, roles, roles[0]);
        if (role == null) return null;

        JTextField staffId = new JTextField();
        JTextField fullName = new JTextField();
        JTextField phone = new JTextField();
        JTextField username = new JTextField();
        JTextField password = new JTextField("1234");
        Object[] fields = {
                "Staff ID", staffId,
                "Full Name", fullName,
                "Phone", phone,
                "Username", username,
                "Password", password
        };
        int res = JOptionPane.showConfirmDialog(this, fields, "Create Staff",
                JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return null;

        if ("Manager".equals(role)) {
            return new ManagerStaff(staffId.getText().trim(), fullName.getText().trim(),
                    phone.getText().trim(), username.getText().trim(), password.getText().trim(), role);
        }
        if ("Ticketing Agent".equals(role)) {
            return new Ticketing_Agent(staffId.getText().trim(), fullName.getText().trim(),
                    phone.getText().trim(), username.getText().trim(), password.getText().trim(), role);
        }
        return new CashierStaff(staffId.getText().trim(), fullName.getText().trim(),
                phone.getText().trim(), username.getText().trim(), password.getText().trim(), role);
    }
}
