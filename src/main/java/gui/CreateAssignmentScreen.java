package gui;

import javax.swing.*;
import java.awt.*;

public class CreateAssignmentScreen {

    private JFrame createAssignmentScreen;
    private JPanel createAssignmentScreenPanel;

    public CreateAssignmentScreen() {
        createAssignmentScreen = new JFrame("Create Assignment");
        createAssignmentScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createAssignmentScreen.setSize(1200, 800);

        setupCreateAssignmentScreenComponents();
        displayCreateAssignmentScreen();
    }

    private void setupCreateAssignmentScreenComponents() {
        createAssignmentScreenPanel = new JPanel();

        JPanel studentManifestFileSelectionPanel = new JPanel();
        studentManifestFileSelectionPanel.setMaximumSize(new Dimension(600, 100));

        JPanel feedbackTemplateFileSelectionPanel = new JPanel();
        feedbackTemplateFileSelectionPanel.setMaximumSize(new Dimension(600, 100));

        JPanel previewPanel = new JPanel();
        previewPanel.setMinimumSize(new Dimension(800, 500));
        previewPanel.setMaximumSize(new Dimension(800, 500));
        previewPanel.setBackground(Color.green);

        JPanel confirmationPanel = new JPanel();
        confirmationPanel.setMaximumSize(new Dimension(800, 100));


        createAssignmentScreenPanel.setLayout(new BoxLayout(createAssignmentScreenPanel, BoxLayout.PAGE_AXIS));
        studentManifestFileSelectionPanel.setLayout(new FlowLayout());
        feedbackTemplateFileSelectionPanel.setLayout(new FlowLayout());
        confirmationPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        studentManifestFileSelectionPanel.add(new JLabel("Student Manifest: "));
        JTextField selectedLocation = new JTextField();
        selectedLocation.setMinimumSize(new Dimension(200, 30));
        selectedLocation.setPreferredSize(new Dimension(200, 30));
        studentManifestFileSelectionPanel.add(selectedLocation);

        JButton selectStudentManifestButton = new JButton("Select Student Manifest");
        selectStudentManifestButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.showOpenDialog(selectStudentManifestButton);

            // Need to handle file selection
        });
        studentManifestFileSelectionPanel.add(selectStudentManifestButton);



        feedbackTemplateFileSelectionPanel.add(new JLabel("Feedback Template: "));
        JTextField feedbackTemplateLocation = new JTextField();
        feedbackTemplateLocation.setMinimumSize(new Dimension(200, 30));
        feedbackTemplateLocation.setPreferredSize(new Dimension(200, 30));
        feedbackTemplateFileSelectionPanel.add(feedbackTemplateLocation);

        JButton selectFeedbackTemplateButton = new JButton("Select Feedback Template");
        selectFeedbackTemplateButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.showOpenDialog(selectFeedbackTemplateButton);

            // Need to handle file selection
        });
        feedbackTemplateFileSelectionPanel.add(selectFeedbackTemplateButton);

        JButton backButton = new JButton("Back to Home");
        JButton confirmButton = new JButton("Confirm Selections");

//        backButton.setHorizontalAlignment(SwingConstants.RIGHT);
//        confirmButton.setHorizontalAlignment(SwingConstants.RIGHT);

        confirmationPanel.add(backButton);
        confirmationPanel.add(confirmButton);

        createAssignmentScreenPanel.add(studentManifestFileSelectionPanel);
        createAssignmentScreenPanel.add(feedbackTemplateFileSelectionPanel);
        createAssignmentScreenPanel.add(previewPanel);
        createAssignmentScreenPanel.add(confirmationPanel);
        createAssignmentScreen.add(createAssignmentScreenPanel);
    }

    private void displayCreateAssignmentScreen() {
        createAssignmentScreen.setVisible(true);
    }

}
