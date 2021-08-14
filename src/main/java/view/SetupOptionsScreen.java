package view;

import controller.AppController;
import model.Assignment;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

/**
 * Setup Options Screen Class.
 */
public class SetupOptionsScreen {

    // Instance variables
    private JFrame setupOptionsScreen;
    private JPanel setupOptionsScreenPanel;
    private JButton useConfigFileButton;
    private JButton useManualSetupButton;
    private JButton backButton;
    private JLabel titleLabel;
    private JTextPane instructions;
    private AppController controller;


    /**
     * Constructor.
     *
     * @param controller The controller.
     */
    public SetupOptionsScreen(AppController controller) {
        this.controller = controller;

        // Setup components
        setupSetupOptionsScreen();
        setupTitleLabel();
        setupConfigFileButton();
        setupManualButton();
        setupBackButton();
        setupInstructions();
        setupSetupOptionScreenPanel();

        // Add the options screen panel to the main screen and set visibility
        setupOptionsScreen.add(setupOptionsScreenPanel);
        setupOptionsScreen.setVisible(true);
    }

    /**
     * Setup the back button.
     */
    private void setupBackButton() {
        backButton = new JButton("Back");
        backButton.setMinimumSize(new Dimension(200, 50));
        backButton.setPreferredSize(new Dimension(200, 50));
        backButton.setMaximumSize(new Dimension(200, 50));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(l -> {
            setupOptionsScreen.dispose();
            HomeScreen homeScreen = new HomeScreen(controller);
        });
    }

    /**
     * Setup the manual setup button.
     */
    private void setupManualButton() {
        useManualSetupButton = new JButton("Use manual setup");
        useManualSetupButton.setMinimumSize(new Dimension(200, 50));
        useManualSetupButton.setPreferredSize(new Dimension(200, 50));
        useManualSetupButton.setMaximumSize(new Dimension(200, 50));
        useManualSetupButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        useManualSetupButton.addActionListener(l -> {
            setupOptionsScreen.dispose();
            CreateAssignmentScreen createAssignmentScreen = new CreateAssignmentScreen(controller);
        });
    }

    /**
     * Setup config file button.
     */
    private void setupConfigFileButton() {
        useConfigFileButton = new JButton("Use config file...");
        useConfigFileButton.setMinimumSize(new Dimension(200, 50));
        useConfigFileButton.setPreferredSize(new Dimension(200, 50));
        useConfigFileButton.setMaximumSize(new Dimension(200, 50));
        useConfigFileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        useConfigFileButton.addActionListener(l -> {
            // Show file chooser
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose a config file...");

            // Only accept JSON files
            fileChooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON files", "json");
            fileChooser.addChoosableFileFilter(filter);

            // Get the selected file and continue if it is valid
            int returnValue = fileChooser.showDialog(useConfigFileButton, "Select this config file");
            String configFilePath = null;
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                configFilePath = fileChooser.getSelectedFile().getPath();
                File configFile = new File(configFilePath);

                // Check file exists
                if (configFile.exists()) {
                    setupOptionsScreen.dispose();
                    new Thread(LoadingScreens::showLoadingScreen).start();

                    // Create the assignment using the config file
                    Assignment assignment = controller.createAssignmentFromConfig(configFilePath);
                    new FeedbackScreen(controller, assignment);
                } else {
                    JOptionPane.showMessageDialog(setupOptionsScreen, "Please select a JSON config file!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * Setup the setup options screen panel.
     */
    private void setupSetupOptionScreenPanel() {
        // Create the panel
        setupOptionsScreenPanel = new JPanel();
        setupOptionsScreenPanel.setLayout(new BoxLayout(setupOptionsScreenPanel, BoxLayout.PAGE_AXIS));
        setupOptionsScreenPanel.setBorder(BorderCreator.createEmptyBorderLeavingBottom(50));

        // Add all the components
        setupOptionsScreenPanel.add(titleLabel);
        setupOptionsScreenPanel.add(Box.createRigidArea(new Dimension(100, 20)));
        setupOptionsScreenPanel.add(instructions);
        setupOptionsScreenPanel.add(Box.createRigidArea(new Dimension(100, 20)));
        setupOptionsScreenPanel.add(useConfigFileButton);
        setupOptionsScreenPanel.add(Box.createRigidArea(new Dimension(100, 20)));
        setupOptionsScreenPanel.add(useManualSetupButton);
        setupOptionsScreenPanel.add(Box.createRigidArea(new Dimension(100, 20)));
        setupOptionsScreenPanel.add(backButton);
    }

    /**
     * Setup the title label
     */
    private void setupTitleLabel() {
        titleLabel = new JLabel("Setup Options");
        titleLabel.setFont(new Font("Helvetica Neue", Font.PLAIN, 28));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));//top,left,bottom,right
    }

    /**
     * Setup the setup options screen.
     */
    private void setupSetupOptionsScreen() {
        setupOptionsScreen = new JFrame("Please select a setup option");
        setupOptionsScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setupOptionsScreen.setSize(800, 600);

        // Centre the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - setupOptionsScreen.getWidth()) / 2;
        int y = (screenSize.height - setupOptionsScreen.getHeight()) / 2;
        setupOptionsScreen.setLocation(x, y);
    }

    /**
     * Setup the instructions text area.
     */
    public void setupInstructions() {
        this.instructions = new JTextPane();

        instructions.setText(
                "Please select 'Use config file...' if you have a JSON config file ready. " +
                        "Otherwise, please select 'Use manual setup' where you will be guided through a configuration setup."
        );

        instructions.setMaximumSize(new Dimension(400, 130));
        instructions.setPreferredSize(new Dimension(400, 130));
        instructions.setMinimumSize(new Dimension(400, 130));
        instructions.setBorder(BorderCreator.createEmptyBorderLeavingBottom(20));
        instructions.setFont(new Font("Helvetica Neue", Font.PLAIN, 18));
        instructions.setEditable(false);
        instructions.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}
