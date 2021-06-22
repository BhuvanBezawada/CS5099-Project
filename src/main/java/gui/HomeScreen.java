package gui;

import javax.swing.*;
import java.awt.*;

public class HomeScreen {

    private final JFrame homeScreen;
    private JPanel homeScreenPanel;
    private JButton startNewButton;
    private JLabel titleLabel;
    private JButton loadButton;
    private JButton helpButton;
    private JTextPane welcomeText;

    public static void main(String[] args) {
        HomeScreen homeScreen = new HomeScreen();
        homeScreen.createHomeScreenComponents();
        homeScreen.displayHomeScreen();
    }

    public HomeScreen() {
        homeScreen = new JFrame("Feedback Helper Tool");
        homeScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        homeScreen.setSize(800, 600);
    }

    public void createHomeScreenComponents() {
        createHomeScreenPanel();
        createTitle();
        createButtons();
    }

    public void displayHomeScreen() {
        homeScreenPanel.add(titleLabel);
        homeScreenPanel.add(startNewButton);
        homeScreenPanel.add(loadButton);
        homeScreenPanel.add(helpButton);

        // Add home screen panel to home screen frame
        homeScreen.add(homeScreenPanel);
        homeScreen.setVisible(true);
    }


    public void createHomeScreenPanel() {
        homeScreenPanel = new JPanel();

        // Layout from top to bottom
        homeScreenPanel.setLayout(new BoxLayout(homeScreenPanel, BoxLayout.PAGE_AXIS));
    }

    public void createTitle() {
        titleLabel = new JLabel();
        titleLabel.setText("Feedback Helper Tool");
        titleLabel.setFont(new Font("Helvetica", Font.PLAIN, 20));
    }

    public void createButtons() {
        startNewButton = new JButton("Start New Assignment");
        startNewButton.setSize(300, 100);
        startNewButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(homeScreen, "Starting new assignment!");
            homeScreen.dispose();

//            FeedbackScreen feedbackScreen = new FeedbackScreen();
            CreateAssignmentScreen createAssignmentScreen = new CreateAssignmentScreen();
        });

        loadButton = new JButton("Resume Assignment");
        loadButton.setSize(300, 100);

        helpButton = new JButton("Help");
        helpButton.setSize(300, 100);
    }



}
