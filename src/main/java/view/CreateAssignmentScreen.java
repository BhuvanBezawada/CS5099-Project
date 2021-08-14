package view;

import controller.AppController;
import model.Assignment;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Create Assignment Screen Class.
 */
public class CreateAssignmentScreen {

    // Underline styles map
    private static final Map<String, String> UNDERLINE_STYLES = Collections.unmodifiableMap(
            new LinkedHashMap<String, String>() {{
                put("No underline", "");
                put("Single underline (---)", "-");
                put("Double underline (===)", "=");
            }});

    // Heading styles map
    private static final Map<String, String> HEADING_STYLES = Collections.unmodifiableMap(
            new LinkedHashMap<String, String>() {{
                put("No hash", "");
                put("Single hash (#)", "#");
                put("Double hash (##)", "##");
            }});

    // Instance variables
    private final AppController controller;
    private JFrame createAssignmentScreen;
    private JPanel createAssignmentScreenPanel;
    private JPanel configFormPanel;
    private Map<String, Component> editableComponents;
    private File studentManifestFile;

    /**
     * Constructor.
     *
     * @param controller The controller.
     */
    public CreateAssignmentScreen(AppController controller) {
        this.controller = controller;
        this.editableComponents = new HashMap<String, Component>();

        // Setup components
        setupAssignmentScreen();
        setupAssignmentScreenPanel();
        setupConfigForm();
        setupConfirmationPanel();

        // Add the main panel and set visibility
        createAssignmentScreen.add(createAssignmentScreenPanel);
        createAssignmentScreen.setVisible(true);
    }

    /**
     * Setup the assignment screen panel.
     */
    private void setupAssignmentScreenPanel() {
        this.createAssignmentScreenPanel = new JPanel();
        this.createAssignmentScreenPanel.setLayout(new BoxLayout(createAssignmentScreenPanel, BoxLayout.PAGE_AXIS));
        this.createAssignmentScreenPanel.setBorder(BorderFactory.createEmptyBorder(20, 200, 20, 200));
    }

    /**
     * Setup the assignment screen.
     */
    private void setupAssignmentScreen() {
        this.createAssignmentScreen = new JFrame("Create Assignment");
        this.createAssignmentScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.createAssignmentScreen.setSize(1000, 800);

        // Centre the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - createAssignmentScreen.getWidth()) / 2;
        int y = (screenSize.height - createAssignmentScreen.getHeight()) / 2;
        createAssignmentScreen.setLocation(x, y);
    }

    /**
     * Setup the heading style panel.
     */
    private void setupHeadingStylePanel() {
        // Create main panel
        JPanel headingStylePanel = new JPanel();
        headingStylePanel.setLayout(new BoxLayout(headingStylePanel, BoxLayout.LINE_AXIS));

        // Set label
        JPanel headingStyleLabelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel headingStyleLabel = new JLabel("Heading style: ");
        headingStyleLabelPanel.add(headingStyleLabel);

        // Set size
        headingStyleLabelPanel.setMaximumSize(new Dimension(200, 20));
        headingStyleLabelPanel.setPreferredSize(new Dimension(200, 20));
        headingStyleLabelPanel.setMinimumSize(new Dimension(200, 20));

        // Set selectable values
        JComboBox<String> selections = new JComboBox<String>();
        HEADING_STYLES.keySet().forEach(selections::addItem);

        // Store in components map
        editableComponents.put("headingStyle", selections);

        // Package up main panel
        headingStylePanel.add(headingStyleLabelPanel);
        headingStylePanel.add(selections);

        // Add main panel to config form
        configFormPanel.add(headingStylePanel);
    }

    /**
     * Setup the heading underline panel.
     */
    private void setupHeadingUnderlinePanel() {
        // Create main panel
        JPanel headingUnderlinePanel = new JPanel();
        headingUnderlinePanel.setLayout(new BoxLayout(headingUnderlinePanel, BoxLayout.LINE_AXIS));

        // Set label
        JPanel headingUnderlineLabelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel headingUnderlineLabel = new JLabel("Heading underline style:");
        headingUnderlineLabelPanel.add(headingUnderlineLabel);

        // Set size
        headingUnderlineLabelPanel.setMaximumSize(new Dimension(200, 20));
        headingUnderlineLabelPanel.setPreferredSize(new Dimension(200, 20));
        headingUnderlineLabelPanel.setMinimumSize(new Dimension(200, 20));

        // Set selectable values
        JComboBox<String> selections = new JComboBox<String>();
        UNDERLINE_STYLES.keySet().forEach(selections::addItem);

        // Store in components map
        editableComponents.put("headingUnderlineStyle", selections);

        // Package up main panel
        headingUnderlinePanel.add(headingUnderlineLabelPanel);
        headingUnderlinePanel.add(selections);

        // Add main panel to config form
        configFormPanel.add(headingUnderlinePanel);
    }

    /**
     * Setup heading line spacing panel.
     */
    private void setupHeadingLineSpacingPanel() {
        // Create main panel
        JPanel headingLineSpacingPanel = new JPanel();
        headingLineSpacingPanel.setLayout(new BoxLayout(headingLineSpacingPanel, BoxLayout.LINE_AXIS));

        // Set label
        JPanel headingLineSpacingLabelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel headingLineSpacingLabel = new JLabel("Line spacing after sections:");
        headingLineSpacingLabelPanel.add(headingLineSpacingLabel);

        // Set size
        headingLineSpacingLabelPanel.setMaximumSize(new Dimension(200, 20));
        headingLineSpacingLabelPanel.setPreferredSize(new Dimension(200, 20));
        headingLineSpacingLabelPanel.setMinimumSize(new Dimension(200, 20));

        // Set selectable values
        JComboBox<Integer> selections = new JComboBox<Integer>();
        selections.addItem(1);
        selections.addItem(2);
        selections.addItem(3);

        // Store in components map
        editableComponents.put("headingLineSpacing", selections);

        // Package up main panel
        headingLineSpacingPanel.add(headingLineSpacingLabelPanel);
        headingLineSpacingPanel.add(selections);

        // Add main panel to config form
        configFormPanel.add(headingLineSpacingPanel);
    }

    /**
     * Setup the line marker panel.
     */
    private void setupLineMarkerPanel() {
        // Create main panel
        JPanel lineMarkerPanel = new JPanel();
        lineMarkerPanel.setLayout(new BoxLayout(lineMarkerPanel, BoxLayout.LINE_AXIS));

        // Set label
        JPanel lineMarkerLabelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel lineMarkerLabel = new JLabel("Line marker style:");
        lineMarkerLabelPanel.add(lineMarkerLabel);

        // Set size
        lineMarkerLabelPanel.setMaximumSize(new Dimension(200, 20));
        lineMarkerLabelPanel.setPreferredSize(new Dimension(200, 20));
        lineMarkerLabelPanel.setMinimumSize(new Dimension(200, 20));

        // Set selectable values
        JComboBox<String> selections = new JComboBox<String>();
        selections.addItem("-");
        selections.addItem("->");
        selections.addItem("=>");
        selections.addItem("*");
        selections.addItem("+");

        // Store in components map
        editableComponents.put("lineMarker", selections);

        // Package up main panel
        lineMarkerPanel.add(lineMarkerLabelPanel);
        lineMarkerPanel.add(selections);

        // Add main panel to config form
        configFormPanel.add(lineMarkerPanel);
    }

    /**
     * Setup the config form.
     */
    private void setupConfigForm() {
        // Create the config panel
        this.configFormPanel = new JPanel();
        configFormPanel.setLayout(new BoxLayout(configFormPanel, BoxLayout.PAGE_AXIS));

        // Setup config components
        setupTitleLabel();
        setupAssignmentTitlePanel();
        setupAssignmentDirectoryPanel();
        setupAssignmentHeadingsPanel();
        setupHeadingStylePanel();
        setupHeadingUnderlinePanel();
        setupLineMarkerPanel();
        setupHeadingLineSpacingPanel();
        setupStudentManifestPanel();

        // Add config panel to the screen panel
        configFormPanel.add(Box.createRigidArea(new Dimension(100, 50)));
        createAssignmentScreenPanel.add(configFormPanel);
    }

    /**
     * Setup the confirmation panel.
     */
    private void setupConfirmationPanel() {
        // Create panel and buttons
        JPanel confirmationPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backButton = new JButton("Back");
        JButton confirmButton = new JButton("Confirm");
        confirmationPanel.add(backButton);
        confirmationPanel.add(confirmButton);

        // On confirm, save user preferences
        confirmButton.addActionListener(l -> {

            // Load all the user preferences
            String assignmentTitle = ((JTextField) editableComponents.get("assignmentTitle")).getText();
            String assignmentHeadings = ((JTextArea) editableComponents.get("assignmentHeadings")).getText();
            String assignmentDirectoryPath = ((JTextField) editableComponents.get("assignmentDirectory")).getText();
            String headingStyle = (String) ((JComboBox<String>) editableComponents.get("headingStyle")).getSelectedItem();
            String headingUnderlineStyle = (String) ((JComboBox<String>) editableComponents.get("headingUnderlineStyle")).getSelectedItem();
            int lineSpacing = (Integer) ((JComboBox<Integer>) editableComponents.get("headingLineSpacing")).getSelectedItem();
            String lineMarker = (String) ((JComboBox<String>) editableComponents.get("lineMarker")).getSelectedItem();

            // Ensure student list exists before creating the feedback screen
            if (studentManifestFile != null && studentManifestFile.exists()) {
                // Setup assignment and db for it
                new Thread(SetupOptionsScreen::showLoadingScreen).start();
                createAssignmentScreen.dispose();

                // Create the assignment
                Assignment assignment = controller.createAssignment(assignmentTitle, assignmentHeadings, studentManifestFile, assignmentDirectoryPath);
                controller.setAssignmentPreferences(assignment, HEADING_STYLES.get(headingStyle), UNDERLINE_STYLES.get(headingUnderlineStyle), lineSpacing, lineMarker);
                controller.saveAssignment(assignment, assignment.getAssignmentTitle()
                        .toLowerCase()
                        .replace(" ", "-"));

                // Create the feedback screen
                new FeedbackScreen(controller, assignment);
            } else {
                JOptionPane.showMessageDialog(createAssignmentScreen, "Please select a student manifest form!", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        });

        // On back button press go back to the setup options screen
        backButton.addActionListener(e -> {
            new SetupOptionsScreen(controller);
            createAssignmentScreen.dispose();
        });

        // Add the confirmation panel to the main screen panel
        createAssignmentScreenPanel.add(confirmationPanel);
    }

    /**
     * Setup the assignment title panel.
     */
    private void setupAssignmentTitlePanel() {
        // Create main panel
        JPanel assignmentTitlePanel = new JPanel();

        // Set label
        JPanel assignmentTitleLabelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel assignmentTitleLabel = new JLabel("Assignment title: ");
        assignmentTitleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        assignmentTitleLabel.setVerticalAlignment(SwingConstants.CENTER);

        // Set label panel size
        assignmentTitleLabelPanel.add(assignmentTitleLabel);
        assignmentTitleLabelPanel.setMaximumSize(new Dimension(200, 20));
        assignmentTitleLabelPanel.setPreferredSize(new Dimension(200, 20));
        assignmentTitleLabelPanel.setMinimumSize(new Dimension(200, 20));

        // Set text field
        JTextField assignmentTitleTextField = new JTextField("CS 5000 Assignment 1");
        assignmentTitleTextField.setBorder(BorderCreator.createAllSidesEmptyBorder(10));
        assignmentTitleTextField.setColumns(30);

        // Store in components map
        editableComponents.put("assignmentTitle", assignmentTitleTextField);

        // Package up main panel
        assignmentTitlePanel.add(assignmentTitleLabelPanel);
        assignmentTitlePanel.add(assignmentTitleTextField);

        // Add main panel to config form
        configFormPanel.add(assignmentTitlePanel);
    }

    /**
     * Setup the assignment directory panel.
     */
    private void setupAssignmentDirectoryPanel() {
        // Create main panel
        JPanel assignmentDirectoryPanel = new JPanel();

        // Set label
        JPanel assignmentDirectoryLabelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel assignmentDirectoryLabel = new JLabel("Assignment directory: ");
        assignmentDirectoryLabelPanel.add(assignmentDirectoryLabel);

        // Set label panel size
        assignmentDirectoryLabelPanel.setMaximumSize(new Dimension(200, 20));
        assignmentDirectoryLabelPanel.setPreferredSize(new Dimension(200, 20));
        assignmentDirectoryLabelPanel.setMinimumSize(new Dimension(200, 20));

        // Set text field
        JTextField assignmentDirectoryTextField = new JTextField(System.getProperty("user.home") + File.separator + "Desktop" + File.separator);
        assignmentDirectoryTextField.setBorder(BorderCreator.createAllSidesEmptyBorder(10));
        assignmentDirectoryTextField.setColumns(30);

        // Store in components map
        editableComponents.put("assignmentDirectory", assignmentDirectoryTextField);

        // Package up main panel
        assignmentDirectoryPanel.add(assignmentDirectoryLabelPanel);
        assignmentDirectoryPanel.add(assignmentDirectoryTextField);

        // Add main panel to config form
        configFormPanel.add(assignmentDirectoryPanel);
    }

    /**
     * Setup the title label.
     */
    public void setupTitleLabel() {
        JLabel titleLabel = new JLabel();
        titleLabel.setText("Assignment Configuration");
        titleLabel.setFont(new Font("Helvetica Neue", Font.PLAIN, 28));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderCreator.createEmptyBorderBottomOnly(BorderCreator.PADDING_20_PIXELS));
        configFormPanel.add(titleLabel);
    }

    /**
     * Setup the assignment headings panel.
     */
    private void setupAssignmentHeadingsPanel() {
        // Create main panel
        JPanel assignmentHeadingsPanel = new JPanel();

        // Set label
        JPanel assignmentHeadingsLabelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel assignmentHeadingsLabel = new JLabel("Assignment headings: ");
        assignmentHeadingsLabelPanel.add(assignmentHeadingsLabel);

        // Set label panel size
        assignmentHeadingsLabelPanel.setMaximumSize(new Dimension(200, 20));
        assignmentHeadingsLabelPanel.setPreferredSize(new Dimension(200, 20));
        assignmentHeadingsLabelPanel.setMinimumSize(new Dimension(200, 20));

        // Set text area
        JTextArea assignmentHeadingsTextArea = new JTextArea(7, 30);
        assignmentHeadingsTextArea.setBorder(BorderCreator.createAllSidesEmptyBorder(BorderCreator.PADDING_10_PIXELS));

        // Store in components map
        editableComponents.put("assignmentHeadings", assignmentHeadingsTextArea);

        // Package up main panel
        assignmentHeadingsPanel.add(assignmentHeadingsLabelPanel);
        assignmentHeadingsPanel.add(new JScrollPane(assignmentHeadingsTextArea));

        // Add main panel to config form
        configFormPanel.add(assignmentHeadingsPanel);
    }

    /**
     * Setup the student list panel.
     */
    private void setupStudentManifestPanel() {
        // Create main panel
        JPanel studentManifestPanel = new JPanel();
        studentManifestPanel.setLayout(new BoxLayout(studentManifestPanel, BoxLayout.LINE_AXIS));

        // Set label
        JPanel studentManifestLabelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel assignmentHeadingsLabel = new JLabel("Student manifest file: ");
        studentManifestLabelPanel.add(assignmentHeadingsLabel);

        // Set label panel size
        studentManifestLabelPanel.setMaximumSize(new Dimension(200, 20));
        studentManifestLabelPanel.setPreferredSize(new Dimension(200, 20));
        studentManifestLabelPanel.setMinimumSize(new Dimension(200, 20));

        // Set button and size
        JButton studentManifestFileButton = new JButton("Select student manifest file...");
        studentManifestFileButton.setMaximumSize(new Dimension(400, 25));
        studentManifestFileButton.setPreferredSize(new Dimension(400, 25));
        studentManifestFileButton.setMinimumSize(new Dimension(400, 25));

        // Setup button action
        studentManifestFileButton.addActionListener(l -> {
            // Open file chooser
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose a student manifest file...");

            // Only allow text files to be chosen
            fileChooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
            fileChooser.addChoosableFileFilter(filter);

            // Store the chosen file path
            int returnValue = fileChooser.showDialog(studentManifestFileButton, "Select this student manifest file");
            String assignmentFilePath = null;
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                assignmentFilePath = fileChooser.getSelectedFile().getPath();
                studentManifestFile = new File(assignmentFilePath);
            }
        });

        // Package up main panel
        studentManifestPanel.add(studentManifestLabelPanel);
        studentManifestPanel.add(studentManifestFileButton);

        // Add main panel to config form
        configFormPanel.add(studentManifestPanel);
    }
}