package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FeedbackScreen extends JFrame {

    private JPanel documentsPanel;
    private JPanel editorPanel;
    private JPanel controlsPanel;
    private JButton testButton;
    private JPanel feedbackScreenPanel;

    private int xLoc = 0;
    private int yLoc = 100;


    public FeedbackScreen() {
        this.setSize(1200, 800);
        this.setContentPane(feedbackScreenPanel);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);

        testButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "New doc created!");

            JTextPane textPane = new JTextPane();
            textPane.setSize(xLoc, yLoc);
            textPane.setLocation(xLoc, yLoc);
            textPane.setText("New Doc");
            textPane.setEditable(false);
            textPane.setVisible(true);
            textPane.setBackground(Color.lightGray);
            documentsPanel.add(textPane);

            yLoc += 320;
        });
    }
}
