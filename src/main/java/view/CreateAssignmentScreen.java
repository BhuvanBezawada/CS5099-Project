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


import model.Assignment;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateAssignmentScreen {

    private String assignmentTitle;
    private List<String> documentHeadings;
    private File studentManifestFile;

    private JFrame createAssignmentScreen;
    private JPanel createAssignmentScreenPanel;
    private JPanel configForm;

    private Map<String, Component> editableComponents;

    private Assignment assignment;

    public CreateAssignmentScreen() {
        createAssignmentScreen = new JFrame("Create Assignment");
        createAssignmentScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createAssignmentScreen.setSize(1200, 800);

        this.editableComponents = new HashMap<String, Component>();

        this.createAssignmentScreenPanel = new JPanel();
        createAssignmentScreenPanel.setLayout(new BoxLayout(createAssignmentScreenPanel, BoxLayout.PAGE_AXIS));

        setupConfigForm();
        setupConfirmationPanel();

        createAssignmentScreen.add(createAssignmentScreenPanel);
        createAssignmentScreen.setVisible(true);
    }

    private void setupConfigForm() {
        this.configForm = new JPanel();
        configForm.setLayout(new BoxLayout(configForm, BoxLayout.PAGE_AXIS));

        JLabel assignmentTitleLabel = new JLabel("Assignment title: ");
        JLabel assignmentHeadingsLabel = new JLabel("Assignment headings: ");
        JLabel studentManifestFileLabel = new JLabel("Student manifest file: ");

        JTextField assignmentTitleTextField = new JTextField("e.g. CS 5000 Assignment 1");
        JTextArea assignmentHeadingsTextArea = new JTextArea(5, 20);
        JButton studentManifestFileButton = new JButton("Select student manifest file...");

        JPanel assignmentTitlePanel = new JPanel(new FlowLayout());
        assignmentTitlePanel.add(assignmentTitleLabel);
        assignmentTitlePanel.add(assignmentTitleTextField);

        JPanel assignmentHeadingsPanel = new JPanel(new FlowLayout());
        assignmentHeadingsPanel.add(assignmentHeadingsLabel);
        assignmentHeadingsPanel.add(new JScrollPane(assignmentHeadingsTextArea));

        JPanel studentManifestPanel = new JPanel(new FlowLayout());
        studentManifestPanel.add(studentManifestFileLabel);
        studentManifestPanel.add(studentManifestFileButton);

        configForm.add(assignmentTitlePanel);
        configForm.add(assignmentHeadingsPanel);
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

        createAssignmentScreenPanel.add(configForm);
    }

    private void setupConfirmationPanel() {
        JPanel confirmationPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backButton = new JButton("Back");
        JButton confirmButton = new JButton("Confirm");

        confirmationPanel.add(backButton);
        confirmationPanel.add(confirmButton);

        confirmButton.addActionListener(e -> {
            Assignment assignment = new Assignment();
            assignment.setAssignmentTitle(((JTextField) editableComponents.get("assignmentTitle")).getText());
            assignment.setAssignmentHeadings(((JTextArea) editableComponents.get("assignmentHeadings")).getText());
            assignment.setStudentIds(studentManifestFile);

            FeedbackScreen feedbackScreen = new FeedbackScreen(assignment);
            createAssignmentScreen.dispose();
        });

        backButton.addActionListener(e -> {
            HomeScreen homeScreen = new HomeScreen();
            createAssignmentScreen.dispose();
        });

        createAssignmentScreenPanel.add(confirmationPanel);
    }
}