package view;

import javax.swing.*;

public class StartScreen {
    private JFrame parent;

    private JPanel startScreenJPanel;
    private JButton startNewButton;
    private JLabel titleLabel;
    private JButton loadButton;
    private JButton helpButton;
    private JTextPane welcomeText;

    public JPanel getStartScreenJPanel() {
        return startScreenJPanel;
    }

    public StartScreen(JFrame parent) {
        // Start button action listener
        startNewButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(parent, "Starting New Assignment!");

            FeedbackScreen feedbackScreen = new FeedbackScreen();
        });
    }
}
