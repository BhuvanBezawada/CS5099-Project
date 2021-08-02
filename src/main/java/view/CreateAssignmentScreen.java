package view;//package view;
import controller.AppController;
import model.Assignment;
import org.apache.xalan.xsltc.DOM;
import org.neo4j.cypher.internal.frontend.v2_3.ast.functions.Has;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
        createAssignmentScreen.setSize(1000, 800);

        this.editableComponents = new HashMap<String, Component>();

        this.createAssignmentScreenPanel = new JPanel();
        createAssignmentScreenPanel.setLayout(new BoxLayout(createAssignmentScreenPanel, BoxLayout.PAGE_AXIS));
        createAssignmentScreenPanel.setBorder(BorderFactory.createEmptyBorder(20, 200, 20, 200));
        setupConfigForm();
        setupConfirmationPanel();


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - createAssignmentScreen.getWidth())/2;
        int y = (screenSize.height - createAssignmentScreen.getHeight())/2;
        createAssignmentScreen.setLocation(x, y);

        createAssignmentScreen.add(createAssignmentScreenPanel);
        createAssignmentScreen.setVisible(true);
    }


    private void setupHeadingStylePanel() {
        JPanel headingStylePanel = new JPanel();
        headingStylePanel.setLayout(new BoxLayout(headingStylePanel, BoxLayout.LINE_AXIS));

        JPanel headingStyleLabelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel headingStyleLabel = new JLabel("Heading style: ");
        headingStyleLabelPanel.add(headingStyleLabel);

        headingStyleLabelPanel.setMaximumSize(new Dimension(200, 20));
        headingStyleLabelPanel.setPreferredSize(new Dimension(200, 20));
        headingStyleLabelPanel.setMinimumSize(new Dimension(200, 20));

        JComboBox<String> selections = new JComboBox<String>();
        headingStyles.keySet().forEach(selections::addItem);

        editableComponents.put("headingStyle", selections);

        headingStylePanel.add(headingStyleLabelPanel);
        headingStylePanel.add(selections);

        configForm.add(headingStylePanel);
    }

    private void setupHeadingUnderlinePanel() {
        JPanel headingUnderlinePanel = new JPanel();
        headingUnderlinePanel.setLayout(new BoxLayout(headingUnderlinePanel, BoxLayout.LINE_AXIS));

        JPanel headingUnderlineLabelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel headingUnderlineLabel = new JLabel("Heading underline style:");
        headingUnderlineLabelPanel.add(headingUnderlineLabel);

        headingUnderlineLabelPanel.setMaximumSize(new Dimension(200, 20));
        headingUnderlineLabelPanel.setPreferredSize(new Dimension(200, 20));
        headingUnderlineLabelPanel.setMinimumSize(new Dimension(200, 20));

        JComboBox<String> selections = new JComboBox<String>();
        underlineStyles.keySet().forEach(selections::addItem);

        editableComponents.put("headingUnderlineStyle", selections);

        headingUnderlinePanel.add(headingUnderlineLabelPanel);
        headingUnderlinePanel.add(selections);

        configForm.add(headingUnderlinePanel);
    }

    private void setupHeadingLineSpacingPanel() {
        JPanel headingLineSpacingPanel = new JPanel();
        headingLineSpacingPanel.setLayout(new BoxLayout(headingLineSpacingPanel, BoxLayout.LINE_AXIS));

        JPanel headingLineSpacingLabelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel headingLineSpacingLabel = new JLabel("Line spacing after sections:");
        headingLineSpacingLabelPanel.add(headingLineSpacingLabel);
        headingLineSpacingLabelPanel.setMaximumSize(new Dimension(200, 20));
        headingLineSpacingLabelPanel.setPreferredSize(new Dimension(200, 20));
        headingLineSpacingLabelPanel.setMinimumSize(new Dimension(200, 20));

        JComboBox<Integer> selections = new JComboBox<Integer>();
        selections.addItem(1);
        selections.addItem(2);
        selections.addItem(3);

        editableComponents.put("headingLineSpacing", selections);

        headingLineSpacingPanel.add(headingLineSpacingLabelPanel);
        headingLineSpacingPanel.add(selections);

        configForm.add(headingLineSpacingPanel);
    }

    private void setupLineMarkerPanel() {
        JPanel lineMarkerPanel = new JPanel();
        lineMarkerPanel.setLayout(new BoxLayout(lineMarkerPanel, BoxLayout.LINE_AXIS));

        JPanel lineMarkerLabelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel lineMarkerLabel = new JLabel("Line marker style:");
        lineMarkerLabelPanel.add(lineMarkerLabel);

        lineMarkerLabelPanel.setMaximumSize(new Dimension(200, 20));
        lineMarkerLabelPanel.setPreferredSize(new Dimension(200, 20));
        lineMarkerLabelPanel.setMinimumSize(new Dimension(200, 20));

        JComboBox<String> selections = new JComboBox<String>();
        selections.addItem("-");
        selections.addItem("->");
        selections.addItem("=>");
        selections.addItem("*");
        selections.addItem("+");

        editableComponents.put("lineMarker", selections);

        lineMarkerPanel.add(lineMarkerLabelPanel);
        lineMarkerPanel.add(selections);

        configForm.add(lineMarkerPanel);
    }


    private void setupConfigForm() {
        this.configForm = new JPanel();
        configForm.setLayout(new BoxLayout(configForm, BoxLayout.PAGE_AXIS));

        createTitle();

        setupAssignmentTitlePanel();
        setupAssignmentDirectoryPanel();
        setupAssignmentHeadingsPanel();
        setupHeadingStylePanel();
        setupHeadingUnderlinePanel();
        setupLineMarkerPanel();
        setupHeadingLineSpacingPanel();
        setupStudentManifestPanel();
        configForm.add(Box.createRigidArea(new Dimension(100, 50)));

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

            if (studentManifestFile != null && studentManifestFile.exists()) {
                // Setup assignment and db for it
                new Thread(SetupOptionsScreen::showLoadingScreen).start();
                createAssignmentScreen.dispose();

                Assignment assignment = controller.createAssignment(assignmentTitle, assignmentHeadings, studentManifestFile, assignmentDirectoryPath);
                controller.setAssignmentPreferences(assignment, headingStyles.get(headingStyle), underlineStyles.get(headingUnderlineStyle), lineSpacing, lineMarker);
                controller.saveAssignment(assignment, assignment.getAssignmentTitle()
                        .toLowerCase()
                        .replace(" ", "-")
                        .replace(".db", ""));

                FeedbackScreen feedbackScreen = new FeedbackScreen(controller, assignment);
            } else {
                JOptionPane.showMessageDialog(createAssignmentScreen, "Please select a student manifest form!", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> {
            SetupOptionsScreen setupOptionsScreen = new SetupOptionsScreen(controller);
            createAssignmentScreen.dispose();
        });

        createAssignmentScreenPanel.add(confirmationPanel);
    }


    private void setupAssignmentTitlePanel() {
        JPanel assignmentTitlePanel = new JPanel();
        //assignmentTitlePanel.setLayout(new BoxLayout(assignmentTitlePanel, BoxLayout.LINE_AXIS));

        JPanel assignmentTitleLabelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel assignmentTitleLabel = new JLabel("Assignment title: ");
        assignmentTitleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        assignmentTitleLabel.setVerticalAlignment(SwingConstants.CENTER);

        assignmentTitleLabelPanel.add(assignmentTitleLabel);
        assignmentTitleLabelPanel.setMaximumSize(new Dimension(200, 20));
        assignmentTitleLabelPanel.setPreferredSize(new Dimension(200, 20));
        assignmentTitleLabelPanel.setMinimumSize(new Dimension(200, 20));

        JTextField assignmentTitleTextField = new JTextField("CS 5000 Assignment 1");
        assignmentTitleTextField.setBorder(BorderCreator.createAllSidesEmptyBorder(10));
        assignmentTitleTextField.setColumns(30);
        editableComponents.put("assignmentTitle", assignmentTitleTextField);

        assignmentTitlePanel.add(assignmentTitleLabelPanel);
        assignmentTitlePanel.add(assignmentTitleTextField);

        configForm.add(assignmentTitlePanel);
    }

    private void setupAssignmentDirectoryPanel() {
        JPanel assignmentDirectoryPanel = new JPanel();
        //assignmentDirectoryPanel.setLayout(new BoxLayout(assignmentDirectoryPanel, BoxLayout.LINE_AXIS));

        JPanel assignmentDirectoryLabelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel assignmentDirectoryLabel = new JLabel("Assignment directory: ");
        assignmentDirectoryLabelPanel.add(assignmentDirectoryLabel);
        assignmentDirectoryLabelPanel.setMaximumSize(new Dimension(200, 20));
        assignmentDirectoryLabelPanel.setPreferredSize(new Dimension(200, 20));
        assignmentDirectoryLabelPanel.setMinimumSize(new Dimension(200, 20));

        JTextField assignmentDirectoryTextField = new JTextField(System.getProperty("user.home") + File.separator + "Desktop" + File.separator);
        assignmentDirectoryTextField.setBorder(BorderCreator.createAllSidesEmptyBorder(10));
        assignmentDirectoryTextField.setColumns(30);
        editableComponents.put("assignmentDirectory", assignmentDirectoryTextField);

        assignmentDirectoryPanel.add(assignmentDirectoryLabelPanel);
        assignmentDirectoryPanel.add(assignmentDirectoryTextField);
//        assignmentDirectoryPanel.setAlignmentX(SwingConstants.LEFT);

        configForm.add(assignmentDirectoryPanel);
    }

    public void createTitle() {
        JLabel titleLabel = new JLabel();
        titleLabel.setText("Assignment Configuration");
        titleLabel.setFont(new Font("Helvetica Neue", Font.PLAIN, 28));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(0,0,20,0));//top,left,bottom,right
        configForm.add(titleLabel);
    }

    private void setupAssignmentHeadingsPanel() {
        JPanel assignmentHeadingsPanel = new JPanel();
        //assignmentHeadingsPanel.setLayout(new BoxLayout(assignmentHeadingsPanel, BoxLayout.LINE_AXIS));

        JPanel assignmentHeadingsLabelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel assignmentHeadingsLabel = new JLabel("Assignment headings: ");
        assignmentHeadingsLabelPanel.add(assignmentHeadingsLabel);
        assignmentHeadingsLabelPanel.setMaximumSize(new Dimension(200, 20));
        assignmentHeadingsLabelPanel.setPreferredSize(new Dimension(200, 20));
        assignmentHeadingsLabelPanel.setMinimumSize(new Dimension(200, 20));

        JTextArea assignmentHeadingsTextArea = new JTextArea(7, 30);
        assignmentHeadingsTextArea.setBorder(BorderCreator.createAllSidesEmptyBorder(10));
        editableComponents.put("assignmentHeadings", assignmentHeadingsTextArea);

        assignmentHeadingsPanel.add(assignmentHeadingsLabelPanel);
        assignmentHeadingsPanel.add(new JScrollPane(assignmentHeadingsTextArea));

        configForm.add(assignmentHeadingsPanel);
    }

    private void setupStudentManifestPanel() {
        JPanel studentManifestPanel = new JPanel();
        studentManifestPanel.setLayout(new BoxLayout(studentManifestPanel, BoxLayout.LINE_AXIS));

        JPanel studentManifestLabelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel assignmentHeadingsLabel = new JLabel("Student manifest file: ");
        studentManifestLabelPanel.add(assignmentHeadingsLabel);
        studentManifestLabelPanel.setMaximumSize(new Dimension(200, 20));
        studentManifestLabelPanel.setPreferredSize(new Dimension(200, 20));
        studentManifestLabelPanel.setMinimumSize(new Dimension(200, 20));

        JButton studentManifestFileButton = new JButton("Select student manifest file...");
        studentManifestFileButton.setMaximumSize(new Dimension(400, 25));
        studentManifestFileButton.setPreferredSize(new Dimension(400, 25));
        studentManifestFileButton.setMinimumSize(new Dimension(400, 25));

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

        studentManifestPanel.add(studentManifestLabelPanel);
        studentManifestPanel.add(studentManifestFileButton);

        configForm.add(studentManifestPanel);
    }


}