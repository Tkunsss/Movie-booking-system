package ui;

import javax.swing.SwingUtilities;

public class GuiApp {
    public static void launch() {
        SwingUtilities.invokeLater(() -> {
            AppState state = new AppState();
            state.init();
            MainFrame frame = new MainFrame(state);
            frame.setVisible(true);
        });
    }
}
