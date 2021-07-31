package view;

import controller.AppController;
import model.Assignment;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

public class SetupOptionsScreen {

    private JFrame setupOptionsScreen;
    private JPanel setupOptionsScreenPanel;
    private JButton useConfigFileButton;
    private JButton useManualSetupButton;

    private JTextPane instructions;

    private AppController controller;


    public SetupOptionsScreen(AppController controller) {
        this.controller = controller;

        setupOptionsScreen = new JFrame("Please select a setup option");
        setupOptionsScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setupOptionsScreen.setSize(600, 400);

        setupOptionsScreenPanel = new JPanel();
        setupOptionsScreenPanel.setLayout(new BoxLayout(setupOptionsScreenPanel, BoxLayout.PAGE_AXIS));
        setupOptionsScreenPanel.setBorder(BorderCreator.createEmptyBorder(50));

        useConfigFileButton = new JButton("Use config file...");
        useConfigFileButton.setMinimumSize(new Dimension(200, 50));
        useConfigFileButton.setPreferredSize(new Dimension(200, 50));
        useConfigFileButton.setMaximumSize(new Dimension(200, 50));
        useConfigFileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        useConfigFileButton.addActionListener(l -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose a config file...");

            fileChooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON files", "json");
            fileChooser.addChoosableFileFilter(filter);

            int returnValue = fileChooser.showDialog(useConfigFileButton, "Select this config file");
            String configFilePath = null;
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                configFilePath = fileChooser.getSelectedFile().getPath();
                System.out.println("Config file path: " + configFilePath);
                Assignment assignment = controller.createAssignmentFromConfig(configFilePath);
                FeedbackScreen feedbackScreen = new FeedbackScreen(controller, assignment);
                setupOptionsScreen.dispose();
            }
        });


        useManualSetupButton = new JButton("Use manual setup");
        useManualSetupButton.setMinimumSize(new Dimension(200, 50));
        useManualSetupButton.setPreferredSize(new Dimension(200, 50));
        useManualSetupButton.setMaximumSize(new Dimension(200, 50));
        useManualSetupButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        useManualSetupButton.addActionListener(l -> {
            JOptionPane.showMessageDialog(setupOptionsScreen, "Starting new assignment!");
            setupOptionsScreen.dispose();
            CreateAssignmentScreen createAssignmentScreen = new CreateAssignmentScreen(controller);
        });

        setupInstructions();

        setupOptionsScreenPanel.add(instructions);
        setupOptionsScreenPanel.add(Box.createRigidArea(new Dimension(100, 20)));
        setupOptionsScreenPanel.add(useConfigFileButton);
        setupOptionsScreenPanel.add(Box.createRigidArea(new Dimension(100, 20)));
        setupOptionsScreenPanel.add(useManualSetupButton);

        setupOptionsScreen.add(setupOptionsScreenPanel);
        setupOptionsScreen.setVisible(true);
    }

    private void setupInstructions() {
        this.instructions = new JTextPane();

        instructions.setText(
                "Please select 'Use config file...' if you have a JSON config file ready. " +
                "Otherwise, please select 'Use manual setup' where you will be guided through a configuration setup."
        );

        instructions.setMaximumSize(new Dimension(400, 130));
        instructions.setPreferredSize(new Dimension(400, 130));
        instructions.setMinimumSize(new Dimension(400, 130));
        instructions.setBorder(BorderCreator.createEmptyBorder(20));
        instructions.setFont(new Font("Helvetica Neue", Font.PLAIN, 18));
        instructions.setEditable(false);
        instructions.setAlignmentX(Component.CENTER_ALIGNMENT);
    }


}
