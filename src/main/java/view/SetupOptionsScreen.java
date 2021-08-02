package view;

import controller.AppController;
import model.Assignment;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

public class SetupOptionsScreen {

    private JFrame setupOptionsScreen;
    private JPanel setupOptionsScreenPanel;
    private JButton useConfigFileButton;
    private JButton useManualSetupButton;
    private JButton backButton;
    private JLabel setupOptionScreenTitleLabel;

    private JTextPane instructions;

    private AppController controller;


    public SetupOptionsScreen(AppController controller) {
        this.controller = controller;

        setupOptionsScreen = new JFrame("Please select a setup option");
        setupOptionsScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setupOptionsScreen.setSize(800, 600);

        setupOptionScreenTitleLabel = new JLabel("Setup Options");
        setupOptionScreenTitleLabel.setFont(new Font("Helvetica Neue", Font.PLAIN, 28));
        setupOptionScreenTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        setupOptionScreenTitleLabel.setBorder(new EmptyBorder(0,0,20,0));//top,left,bottom,right

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

                setupOptionsScreen.dispose();
                new Thread(SetupOptionsScreen::showLoadingScreen).start();

                Assignment assignment = controller.createAssignmentFromConfig(configFilePath);
                FeedbackScreen feedbackScreen = new FeedbackScreen(controller, assignment);
            }
        });


        useManualSetupButton = new JButton("Use manual setup");
        useManualSetupButton.setMinimumSize(new Dimension(200, 50));
        useManualSetupButton.setPreferredSize(new Dimension(200, 50));
        useManualSetupButton.setMaximumSize(new Dimension(200, 50));
        useManualSetupButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        useManualSetupButton.addActionListener(l -> {
            setupOptionsScreen.dispose();
            CreateAssignmentScreen createAssignmentScreen = new CreateAssignmentScreen(controller);
        });

        backButton = new JButton("Back");
        backButton.setMinimumSize(new Dimension(200, 50));
        backButton.setPreferredSize(new Dimension(200, 50));
        backButton.setMaximumSize(new Dimension(200, 50));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(l -> {
            setupOptionsScreen.dispose();
            HomeScreen homeScreen = new HomeScreen(controller);
        });

        setupInstructions();

        setupOptionsScreenPanel.add(setupOptionScreenTitleLabel);
        setupOptionsScreenPanel.add(Box.createRigidArea(new Dimension(100, 20)));
        setupOptionsScreenPanel.add(instructions);
        setupOptionsScreenPanel.add(Box.createRigidArea(new Dimension(100, 20)));
        setupOptionsScreenPanel.add(useConfigFileButton);
        setupOptionsScreenPanel.add(Box.createRigidArea(new Dimension(100, 20)));
        setupOptionsScreenPanel.add(useManualSetupButton);
        setupOptionsScreenPanel.add(Box.createRigidArea(new Dimension(100, 20)));
        setupOptionsScreenPanel.add(backButton);


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - setupOptionsScreen.getWidth())/2;
        int y = (screenSize.height - setupOptionsScreen.getHeight())/2;
        setupOptionsScreen.setLocation(x, y);

        setupOptionsScreen.add(setupOptionsScreenPanel);
        setupOptionsScreen.setVisible(true);
    }

    public void setupInstructions() {
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


    public static void showLoadingScreen() {
        // Splash screen adapted from:
        // https://www.tutorialspoint.com/how-can-we-implement-a-splash-screen-using-jwindow-in-java

        JWindow splash = new JWindow();
        ImageIcon imageIcon = new ImageIcon(SetupOptionsScreen.class.getResource("/loadingscreen.png"));

        splash.setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - imageIcon.getIconWidth())/2;
        int y = (screenSize.height - imageIcon.getIconHeight())/2;
        splash.setLocation(x, y);
        splash.setVisible(true);

        Graphics graphics = splash.getGraphics();
        splash.paint(graphics);
        graphics.drawImage(imageIcon.getImage(), 0, 0, splash);

        try {
            Thread.sleep(3000);
            splash.dispose();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
