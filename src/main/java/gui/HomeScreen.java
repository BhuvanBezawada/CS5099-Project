package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class HomeScreen {

    private final JFrame homeScreen;
    private JPanel homeScreenPanel;
    private JButton startNewButton;
    private JLabel titleLabel;
    private JTextPane descriptionLabel;
    private JButton loadButton;
    private JButton helpButton;
    private JTextPane welcomeText;

    public static void main(String[] args) {
        HomeScreen mainHomeScreen = new HomeScreen();
    }

    public HomeScreen() {
        homeScreen = new JFrame("Feedback Helper Tool");
        homeScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        homeScreen.setSize(800, 600);
        createHomeScreenComponents();
        displayHomeScreen();
    }

    public void createHomeScreenComponents() {
        createHomeScreenPanel();
        createTitle();
        createDescription();
        createButtons();
    }

    public void displayHomeScreen() {
        homeScreenPanel.add(titleLabel);
        homeScreenPanel.add(descriptionLabel);
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
        titleLabel.setFont(new Font("Helvetica Neue", Font.PLAIN, 28));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(20,0,20,0));//top,left,bottom,right
    }

    public void createDescription() {
        descriptionLabel = new JTextPane();
        descriptionLabel.setText(
                "Welcome to the Feedback Helper Tool! " +
                "To learn how to use this tool, please click the 'Help' button at the bottom of the screen. " +
                "To get started with creating feedback documents click the 'Start New Assignment' button. " +
                "To resume creating Feedback documents click the 'Load Assignment' button."
        );

        descriptionLabel.setMaximumSize(new Dimension(500, 150));
        descriptionLabel.setPreferredSize(new Dimension(500, 150));
        descriptionLabel.setMinimumSize(new Dimension(500, 150));
        descriptionLabel.setFont(new Font("Helvetica Neue", Font.PLAIN, 18));
        descriptionLabel.setEditable(false);
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public void createButtons() {
        startNewButton = new JButton("Start New Assignment");
        startNewButton.setMaximumSize(new Dimension(300, 50));
        startNewButton.setPreferredSize(new Dimension(300, 50));
        startNewButton.setMinimumSize(new Dimension(300, 50));
        startNewButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(homeScreen, "Starting new assignment!");
            homeScreen.dispose();

//            FeedbackScreen feedbackScreen = new FeedbackScreen();
            CreateAssignmentScreen createAssignmentScreen = new CreateAssignmentScreen();
        });
        startNewButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        loadButton = new JButton("Resume Assignment");
        loadButton.setMaximumSize(new Dimension(300, 50));
        loadButton.setPreferredSize(new Dimension(300, 50));
        loadButton.setMinimumSize(new Dimension(300, 50));
        loadButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        helpButton = new JButton("Help");
        helpButton.setMaximumSize(new Dimension(300, 50));
        helpButton.setPreferredSize(new Dimension(300, 50));
        helpButton.setMinimumSize(new Dimension(300, 50));
        helpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    }



}
