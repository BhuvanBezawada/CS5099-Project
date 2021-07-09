package view;

import controller.AppController;
import model.Assignment;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class FeedbackScreen {

    private JFrame feedbackScreen;
    private JPanel feedbackScreenPanel;

    private JSplitPane previewAndEditorSplitPane;

    private JScrollPane previewPanelScrollPane;
    private PreviewPanel previewPanel;

    private JScrollPane editorPanelScrollPane;
    private EditorPanel editorPanel;
    private EditingPopupMenu editingPopupMenu;

    private JSplitPane phrasesAndControlsSplitPane;
    private PhrasesSection phrasesSection;
    private JPanel controlPanel;


    private Assignment assignment;
    private AppController controller;


    public FeedbackScreen(AppController controller, Assignment assignment) {
        feedbackScreen = new JFrame("Feedback Composition");
        feedbackScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        feedbackScreen.setSize(1200, 800);
        feedbackScreen.setLayout(new BorderLayout());

        this.controller = controller;
        this.assignment = assignment;

        feedbackScreenPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;


        setupPreviewPanel();
        setupEditorPanel();
        setupPhrasesSection();
        setupControlPanel();

        setupPreviewAndEditorSplitPane();
        setupPhrasesAndControlsSplitPane();

        setupMenuBar();

        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        feedbackScreenPanel.add(previewAndEditorSplitPane, gridBagConstraints);

        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        feedbackScreenPanel.add(phrasesAndControlsSplitPane, gridBagConstraints);

        feedbackScreen.add(feedbackScreenPanel, BorderLayout.CENTER);
        feedbackScreen.setVisible(true);


    }

    private void setupPhrasesAndControlsSplitPane() {
        phrasesAndControlsSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, controlPanel, phrasesSection);
        phrasesAndControlsSplitPane.setOneTouchExpandable(true);
        phrasesAndControlsSplitPane.setDividerLocation(0.5);
        phrasesAndControlsSplitPane.setMaximumSize(new Dimension(300, 800));
        phrasesAndControlsSplitPane.setPreferredSize(new Dimension(300, 800));
        phrasesAndControlsSplitPane.setMinimumSize(new Dimension(300, 800));
    }

    private void setupControlPanel() {

    }

    private void setupPhrasesSection() {
        phrasesSection = new PhrasesSection();

        PhrasesPanel customPhrasesPanel = new PhrasesPanel(PhraseType.CUSTOM);
        PhrasesPanel frequentlyUsedPhrasesPanel = new PhrasesPanel(PhraseType.FREQUENTLY_USED);
        PhrasesPanel insightsPhrasesPanel = new PhrasesPanel(PhraseType.INSIGHTS);

        phrasesSection.addPhrasesPanel(customPhrasesPanel);
        phrasesSection.addPhrasesPanel(frequentlyUsedPhrasesPanel);
        phrasesSection.addPhrasesPanel(insightsPhrasesPanel);

        editingPopupMenu.registerPhrasesPanel(customPhrasesPanel);
        editingPopupMenu.registerPhrasesPanel(frequentlyUsedPhrasesPanel);
        editingPopupMenu.registerPhrasesPanel(insightsPhrasesPanel);
    }

    private void setupPreviewAndEditorSplitPane() {
        previewAndEditorSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, previewPanelScrollPane, editorPanelScrollPane);
        previewAndEditorSplitPane.setMaximumSize(new Dimension(900, 800));
        previewAndEditorSplitPane.setPreferredSize(new Dimension(900, 800));
        previewAndEditorSplitPane.setMinimumSize(new Dimension(900, 800));
        previewAndEditorSplitPane.setOneTouchExpandable(true);
        previewAndEditorSplitPane.setDividerLocation(300);
    }

    private void setupEditorPanel() {
        editorPanelScrollPane = new JScrollPane();

        // Create editor panel with popup menu
        editorPanel = new EditorPanel(assignment.getAssignmentTitle(), assignment.getAssignmentHeadings());
        editingPopupMenu = new EditingPopupMenu();
        editorPanel.registerPopupMenu(editingPopupMenu);

        editorPanelScrollPane.add(editorPanel);
        editorPanelScrollPane.getViewport().setView(editorPanel);
    }

    private void setupPreviewPanel() {
        previewPanelScrollPane = new JScrollPane();

        // Create preview boxes
        List<PreviewBox> previewBoxes = new ArrayList<PreviewBox>();
        assignment.getStudentIds().forEach(studentId -> {
            previewBoxes.add(new PreviewBox(studentId, -1, "<empty file>"));
        });

        previewPanel = new PreviewPanel(previewBoxes);
        previewPanelScrollPane.add(previewPanel);
        previewPanelScrollPane.getViewport().setView(previewPanel);

        // The following line is adapted from: https://stackoverflow.com/questions/1166072/setting-scroll-bar-on-a-jscrollpane
        SwingUtilities.invokeLater(() -> previewPanelScrollPane.getVerticalScrollBar().setValue(0));
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveOption = new JMenuItem("Save");
        JMenuItem loadOption = new JMenuItem("Load");
        JMenuItem exportOption = new JMenuItem("Export");

        saveOption.addActionListener(l -> {
            String filePath = assignment.getAssignmentTitle().toLowerCase().trim() + ".fht";
            JOptionPane.showMessageDialog(feedbackScreen, "Saving assignment to file: " + filePath);
            controller.saveAssignment(assignment, filePath);
        });

        fileMenu.add(saveOption);
        fileMenu.add(loadOption);
        fileMenu.add(exportOption);

        menuBar.add(fileMenu);

        feedbackScreen.add(menuBar, BorderLayout.PAGE_START);
    }
}
