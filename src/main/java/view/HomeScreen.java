package view;

import controller.AppController;
import model.Assignment;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

/**
 * Home Screen Class.
 */
public class HomeScreen {

    // Instance variables
    private final JFrame homeScreen;
    private final AppController controller;
    private JPanel homeScreenPanel;
    private JButton startNewButton;
    private JLabel titleLabel;
    private JTextPane descriptionLabel;
    private JButton loadButton;
    private JButton helpButton;

    /**
     * Constructor.
     *
     * @param controller The controller.
     */
    public HomeScreen(AppController controller) {
        this.controller = controller;

        // Create the home screen jFrame
        homeScreen = new JFrame("Feedback Helper Tool");
        homeScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        homeScreen.setSize(800, 600);

        // Setup the components and display the screen
        createHomeScreenComponents();
        displayHomeScreen();
    }

    /**
     * Create the home screen components.
     */
    public void createHomeScreenComponents() {
        createHomeScreenPanel();
        createTitleLabel();
        createDescriptionArea();
        createButtons();
    }

    /**
     * Display the home screen.
     */
    public void displayHomeScreen() {
        // Add components
        homeScreenPanel.add(titleLabel);
        homeScreenPanel.add(Box.createRigidArea(new Dimension(100, 20)));
        homeScreenPanel.add(descriptionLabel);
        homeScreenPanel.add(Box.createRigidArea(new Dimension(100, 20)));
        homeScreenPanel.add(startNewButton);
        homeScreenPanel.add(Box.createRigidArea(new Dimension(100, 20)));
        homeScreenPanel.add(loadButton);
        homeScreenPanel.add(Box.createRigidArea(new Dimension(100, 20)));

        // Centre the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - homeScreen.getWidth()) / 2;
        int y = (screenSize.height - homeScreen.getHeight()) / 2;
        homeScreen.setLocation(x, y);

        // Add home screen panel to home screen frame and set visibility
        homeScreen.add(homeScreenPanel);
        homeScreen.setVisible(true);
    }

    /**
     * Create the home screen panel.
     */
    public void createHomeScreenPanel() {
        homeScreenPanel = new JPanel();

        // Layout from top to bottom
        homeScreenPanel.setLayout(new BoxLayout(homeScreenPanel, BoxLayout.PAGE_AXIS));
        homeScreenPanel.setBorder(BorderCreator.createEmptyBorderLeavingBottom(BorderCreator.PADDING_50_PIXELS));
    }

    /**
     * Create the title label.
     */
    public void createTitleLabel() {
        titleLabel = new JLabel();
        titleLabel.setText("Feedback Helper Tool");
        titleLabel.setFont(new Font("Helvetica Neue", Font.PLAIN, 28));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderCreator.createEmptyBorderBottomOnly(BorderCreator.PADDING_20_PIXELS));
    }

    /**
     * Create the description area.
     */
    public void createDescriptionArea() {
        descriptionLabel = new JTextPane();
        descriptionLabel.setText(
                "Welcome to the Feedback Helper Tool! " +
                        "To get started with creating feedback documents click the 'Start New Assignment' button. " +
                        "You will then be prompted to setup your assignment via a JSON configuration file or through a manual guided setup. " +
                        "To resume creating feedback documents click the 'Load Assignment' button and select your '.fht' file."
        );
        descriptionLabel.setBorder(BorderCreator.createAllSidesEmptyBorder(20));
        descriptionLabel.setMaximumSize(new Dimension(500, 210));
        descriptionLabel.setPreferredSize(new Dimension(500, 210));
        descriptionLabel.setMinimumSize(new Dimension(500, 210));
        descriptionLabel.setFont(new Font("Helvetica Neue", Font.PLAIN, 18));
        descriptionLabel.setEditable(false);
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    /**
     * Create the buttons.
     */
    public void createButtons() {
        createStartButton();
        createLoadButton();
    }

    /**
     * Create the load button.
     */
    private void createLoadButton() {
        loadButton = new JButton("Resume Assignment");
        loadButton.setMaximumSize(new Dimension(300, 50));
        loadButton.setPreferredSize(new Dimension(300, 50));
        loadButton.setMinimumSize(new Dimension(300, 50));
        loadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadButton.addActionListener(e -> {
            // Show a file chooser
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose an assignment to resume");

            // Only allow FHT files to be selected
            fileChooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Assignment Files", "fht");
            fileChooser.addChoosableFileFilter(filter);

            // Get the selected file
            int returnValue = fileChooser.showDialog(this.homeScreen, "Resume this assignment");
            String assignmentFilePath = null;
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                assignmentFilePath = fileChooser.getSelectedFile().getPath();
                homeScreen.dispose();
            }

            // Ensure selected file is valid and show feedback screen
            if (assignmentFilePath != null) {
                new Thread(SetupOptionsScreen::showLoadingScreen).start(); // TODO extraact loading screens into class
                Assignment assignment = controller.loadAssignment(assignmentFilePath);
                new FeedbackScreen(controller, assignment);
            }
        });
    }

    /**
     * Create the start button.
     */
    private void createStartButton() {
        startNewButton = new JButton("Start New Assignment");
        startNewButton.setMaximumSize(new Dimension(300, 50));
        startNewButton.setPreferredSize(new Dimension(300, 50));
        startNewButton.setMinimumSize(new Dimension(300, 50));
        startNewButton.addActionListener(e -> {
            homeScreen.dispose();
            new SetupOptionsScreen(controller);
        });
        startNewButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}
