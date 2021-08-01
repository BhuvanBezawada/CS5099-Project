package view;//package view;
//
//import model.Assignment;
//
//import javax.swing.*;
//import java.awt.*;
//
//public class CreateAssignmentScreen {
//
//    private JFrame createAssignmentScreen;
//    private JPanel createAssignmentScreenPanel;
//
//    private Assignment assignment;
//
//    public CreateAssignmentScreen() {
//        createAssignmentScreen = new JFrame("Create Assignment");
//        createAssignmentScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        createAssignmentScreen.setSize(1200, 800);
//
//        this.assignment = new Assignment();
//
//        setupCreateAssignmentScreenComponents();
//        displayCreateAssignmentScreen();
//    }
//
//    private void setupCreateAssignmentScreenComponents() {
//        createAssignmentScreenPanel = new JPanel();
//
//        JPanel studentManifestFileSelectionPanel = new JPanel();
//        studentManifestFileSelectionPanel.setMaximumSize(new Dimension(600, 100));
//
//        JPanel feedbackTemplateFileSelectionPanel = new JPanel();
//        feedbackTemplateFileSelectionPanel.setMaximumSize(new Dimension(600, 100));
//
//        JPanel previewPanel = new JPanel();
//        previewPanel.setMinimumSize(new Dimension(800, 500));
//        previewPanel.setMaximumSize(new Dimension(800, 500));
//        previewPanel.setBackground(Color.green);
//
//        JPanel confirmationPanel = new JPanel();
//        confirmationPanel.setMaximumSize(new Dimension(800, 100));
//
//
//        createAssignmentScreenPanel.setLayout(new BoxLayout(createAssignmentScreenPanel, BoxLayout.PAGE_AXIS));
//        studentManifestFileSelectionPanel.setLayout(new FlowLayout());
//        feedbackTemplateFileSelectionPanel.setLayout(new FlowLayout());
//        confirmationPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
//
//        studentManifestFileSelectionPanel.add(new JLabel("Student Manifest: "));
//        JTextField selectedLocation = new JTextField();
//        selectedLocation.setMinimumSize(new Dimension(200, 30));
//        selectedLocation.setPreferredSize(new Dimension(200, 30));
//        studentManifestFileSelectionPanel.add(selectedLocation);
//
//        JButton selectStudentManifestButton = new JButton("Select Student Manifest");
//        selectStudentManifestButton.addActionListener(e -> {
//            JFileChooser fileChooser = new JFileChooser();
//            fileChooser.showOpenDialog(selectStudentManifestButton);
//
//            // Need to handle file selection
//        });
//        studentManifestFileSelectionPanel.add(selectStudentManifestButton);
//
//
//
//        feedbackTemplateFileSelectionPanel.add(new JLabel("Feedback Template: "));
//        JTextField feedbackTemplateLocation = new JTextField();
//        feedbackTemplateLocation.setMinimumSize(new Dimension(200, 30));
//        feedbackTemplateLocation.setPreferredSize(new Dimension(200, 30));
//        feedbackTemplateFileSelectionPanel.add(feedbackTemplateLocation);
//
//        JButton selectFeedbackTemplateButton = new JButton("Select Feedback Template");
//        selectFeedbackTemplateButton.addActionListener(e -> {
//            JFileChooser fileChooser = new JFileChooser();
//            fileChooser.showOpenDialog(selectFeedbackTemplateButton);
//
//            // Need to handle file selection
//        });
//        feedbackTemplateFileSelectionPanel.add(selectFeedbackTemplateButton);
//
//        JButton backButton = new JButton("Back to Home");
//        JButton confirmButton = new JButton("Confirm Selections");
//
//        confirmButton.addActionListener(e -> {
//            FeedbackScreen feedbackScreen = new FeedbackScreen(new Assignment());
//            createAssignmentScreen.dispose();
//        });
//
//        backButton.addActionListener(e -> {
//            HomeScreen homeScreen = new HomeScreen();
//            createAssignmentScreen.dispose();
//        });
//
////        backButton.setHorizontalAlignment(SwingConstants.RIGHT);
////        confirmButton.setHorizontalAlignment(SwingConstants.RIGHT);
//
//        confirmationPanel.add(backButton);
//        confirmationPanel.add(confirmButton);
//
//        createAssignmentScreenPanel.add(studentManifestFileSelectionPanel);
//        createAssignmentScreenPanel.add(feedbackTemplateFileSelectionPanel);
//        createAssignmentScreenPanel.add(previewPanel);
//        createAssignmentScreenPanel.add(confirmationPanel);
//        createAssignmentScreen.add(createAssignmentScreenPanel);
//    }
//
//    private void displayCreateAssignmentScreen() {
//        createAssignmentScreen.setVisible(true);
//    }
//
//}


import controller.AppController;
import model.Assignment;
import org.neo4j.cypher.internal.frontend.v2_3.ast.functions.Has;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

public class CreateAssignmentScreen {

    private String assignmentTitle;
    private List<String> documentHeadings;
    private File studentManifestFile;

    private JFrame createAssignmentScreen;
    private JPanel createAssignmentScreenPanel;
    private JPanel configForm;

    private Map<String, Component> editableComponents;

    private AppController controller;

    private Map<String, String> underlineStyles = Collections.unmodifiableMap(
            new LinkedHashMap<String, String>() {{
                put("No underline", "");
                put("Single underline (---)", "-");
                put("Double underline (===)", "=");
            }});

    private Map<String, String> headingStyles = Collections.unmodifiableMap(
            new LinkedHashMap<String, String>() {{
                put("No hash", "");
                put("Single hash (#)", "#");
                put("Double hash (##)", "##");
            }});

    public CreateAssignmentScreen(AppController controller) {
        this.controller = controller;

        createAssignmentScreen = new JFrame("Create Assignment");
        createAssignmentScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createAssignmentScreen.setSize(800, 600);

        this.editableComponents = new HashMap<String, Component>();

        this.createAssignmentScreenPanel = new JPanel();
        createAssignmentScreenPanel.setLayout(new BoxLayout(createAssignmentScreenPanel, BoxLayout.PAGE_AXIS));

        setupConfigForm();
        setupConfirmationPanel();

        createAssignmentScreen.add(createAssignmentScreenPanel);
        createAssignmentScreen.setVisible(true);
    }


    private void setupHeadingStylePanel() {
        JPanel headingStyleConfigPanel = new JPanel(new FlowLayout());
        JLabel headingStyleConfigLabel = new JLabel("Heading style:");

        JComboBox<String> selections = new JComboBox<String>();
        headingStyles.keySet().forEach(selections::addItem);

        editableComponents.put("headingStyle", selections);

        headingStyleConfigPanel.add(headingStyleConfigLabel);
        headingStyleConfigPanel.add(selections);
        configForm.add(headingStyleConfigPanel);
    }

    private void setupHeadingUnderlinePanel() {
        JPanel headingUnderlineConfigPanel = new JPanel(new FlowLayout());
        JLabel underlineConfigLabel = new JLabel("Heading underline style:");

        JComboBox<String> selections = new JComboBox<String>();
        underlineStyles.keySet().forEach(selections::addItem);

        editableComponents.put("headingUnderlineStyle", selections);

        headingUnderlineConfigPanel.add(underlineConfigLabel);
        headingUnderlineConfigPanel.add(selections);
        configForm.add(headingUnderlineConfigPanel);
    }

    private void setupHeadingLineSpacingPanel() {
        JPanel lineSpacingConfigPanel = new JPanel(new FlowLayout());
        JLabel lineSpacingConfigLabel = new JLabel("Line spacing after sections:");

        JComboBox<Integer> selections = new JComboBox<Integer>();
        selections.addItem(1);
        selections.addItem(2);
        selections.addItem(3);

        editableComponents.put("headingLineSpacing", selections);

        lineSpacingConfigPanel.add(lineSpacingConfigLabel);
        lineSpacingConfigPanel.add(selections);
        configForm.add(lineSpacingConfigPanel);
    }

    private void setupLineMarkerPanel() {
        JPanel lineMarkerConfigPanel = new JPanel(new FlowLayout());
        JLabel lineMarkerLabel = new JLabel("Line marker style:");

        JComboBox<String> selections = new JComboBox<String>();
        selections.addItem("-");
        selections.addItem("->");
        selections.addItem("=>");
        selections.addItem("*");
        selections.addItem("+");

        editableComponents.put("lineMarker", selections);

        lineMarkerConfigPanel.add(lineMarkerLabel);
        lineMarkerConfigPanel.add(selections);
        configForm.add(lineMarkerConfigPanel);
    }


    private void setupConfigForm() {
        this.configForm = new JPanel();
        configForm.setLayout(new BoxLayout(configForm, BoxLayout.PAGE_AXIS));

        JLabel assignmentTitleLabel = new JLabel("Assignment title: ");
        JLabel assignmentHeadingsLabel = new JLabel("Assignment headings: ");
        JLabel studentManifestFileLabel = new JLabel("Student manifest file: ");
        JLabel assignmentDirectoryLabel = new JLabel("Assignment directory: ");

        JTextField assignmentTitleTextField = new JTextField("e.g. CS 5000 Assignment 1");
        assignmentTitleTextField.setBorder(BorderCreator.createAllSidesEmptyBorder(10));
        assignmentTitleTextField.setColumns(30);

        JTextArea assignmentHeadingsTextArea = new JTextArea(7, 30);
        assignmentHeadingsTextArea.setBorder(BorderCreator.createAllSidesEmptyBorder(10));

        JButton studentManifestFileButton = new JButton("Select student manifest file...");

        JTextField assignmentDirectoryTextArea = new JTextField(System.getProperty("user.home") + File.separator + "Desktop" + File.separator);
        assignmentDirectoryTextArea.setBorder(BorderCreator.createAllSidesEmptyBorder(10));
        assignmentDirectoryTextArea.setColumns(30);

        JPanel assignmentTitlePanel = new JPanel(new FlowLayout());
        assignmentTitlePanel.add(assignmentTitleLabel);
        assignmentTitlePanel.add(assignmentTitleTextField);

        JPanel assignmentHeadingsPanel = new JPanel(new FlowLayout());
        assignmentHeadingsPanel.add(assignmentHeadingsLabel);
        assignmentHeadingsPanel.add(new JScrollPane(assignmentHeadingsTextArea));

        JPanel studentManifestPanel = new JPanel(new FlowLayout());
        studentManifestPanel.add(studentManifestFileLabel);
        studentManifestPanel.add(studentManifestFileButton);

        JPanel assignmentDirectoryPanel = new JPanel(new FlowLayout());
        assignmentDirectoryPanel.add(assignmentDirectoryLabel);
        assignmentDirectoryPanel.add(assignmentDirectoryTextArea);



        configForm.add(new JLabel("Assignment Configuration"));
        configForm.add(assignmentTitlePanel);
        configForm.add(assignmentDirectoryPanel);
        configForm.add(assignmentHeadingsPanel);
        setupHeadingStylePanel();
        setupHeadingUnderlinePanel();
        setupLineMarkerPanel();
        setupHeadingLineSpacingPanel();
        configForm.add(studentManifestPanel);


        studentManifestFileButton.addActionListener(l -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose a student manifest file...");

            fileChooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
            fileChooser.addChoosableFileFilter(filter);

            int returnValue = fileChooser.showDialog(studentManifestFileButton, "Select this student manifest file");
            String assignmentFilePath = null;
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                assignmentFilePath = fileChooser.getSelectedFile().getPath();
                studentManifestFile = new File(assignmentFilePath);
            }
        });

        // Keep track of editable components
        editableComponents.put("assignmentTitle", assignmentTitleTextField);
        editableComponents.put("assignmentHeadings", assignmentHeadingsTextArea);
        editableComponents.put("assignmentDirectory", assignmentDirectoryTextArea);

        createAssignmentScreenPanel.add(configForm);
    }

    private void setupConfirmationPanel() {
        JPanel confirmationPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backButton = new JButton("Back");
        JButton confirmButton = new JButton("Confirm");

        confirmationPanel.add(backButton);
        confirmationPanel.add(confirmButton);

        confirmButton.addActionListener(e -> {

            // Tell controller to create the assignment
            String assignmentTitle = ((JTextField) editableComponents.get("assignmentTitle")).getText();
            String assignmentHeadings = ((JTextArea) editableComponents.get("assignmentHeadings")).getText();
            String assignmentDirectoryPath = ((JTextField) editableComponents.get("assignmentDirectory")).getText();

            String headingStyle = (String) ((JComboBox<String>) editableComponents.get("headingStyle")).getSelectedItem();
            String headingUnderlineStyle = (String) ((JComboBox<String>) editableComponents.get("headingUnderlineStyle")).getSelectedItem();
            int lineSpacing = (Integer) ((JComboBox<Integer>) editableComponents.get("headingLineSpacing")).getSelectedItem();
            String lineMarker = (String) ((JComboBox<String>) editableComponents.get("lineMarker")).getSelectedItem();


            System.out.println("Selected heading style: " + headingStyles.get(headingStyle));
            System.out.println("Selected underline style: " + underlineStyles.get(headingUnderlineStyle));
            System.out.println("Selected line spacing: " + lineSpacing);
            System.out.println("Selected line marker: " + lineMarker);

            // Setup assignment and db for it
            Assignment assignment = controller.createAssignment(assignmentTitle, assignmentHeadings, studentManifestFile, assignmentDirectoryPath);
            controller.setAssignmentPreferences(assignment, headingStyles.get(headingStyle), underlineStyles.get(headingUnderlineStyle), lineSpacing, lineMarker);

            FeedbackScreen feedbackScreen = new FeedbackScreen(controller, assignment);
            createAssignmentScreen.dispose();
        });

        backButton.addActionListener(e -> {
            HomeScreen homeScreen = new HomeScreen(controller);
            createAssignmentScreen.dispose();
        });

        createAssignmentScreenPanel.add(confirmationPanel);
    }
}