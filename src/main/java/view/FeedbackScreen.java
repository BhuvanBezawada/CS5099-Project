package view;

import controller.AppController;
import model.Assignment;
import org.neo4j.cypher.internal.frontend.v2_3.ast.functions.Has;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.List;

public class FeedbackScreen implements PropertyChangeListener {

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
    private PhraseEntryBox phraseEntryBox;


    private Assignment assignment;
    private AppController controller;


    public FeedbackScreen(AppController controller, Assignment assignment) {
        feedbackScreen = new JFrame("Feedback Composition");
        feedbackScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        feedbackScreen.setSize(1200, 800);
        feedbackScreen.setLayout(new BorderLayout());

        this.controller = controller;
        this.controller.registerWithModel(this);
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
        this.phraseEntryBox = new PhraseEntryBox(controller);

        phrasesAndControlsSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, phrasesSection, phraseEntryBox);
        phrasesAndControlsSplitPane.setOneTouchExpandable(false);
        phrasesAndControlsSplitPane.setDividerLocation(600);
        phrasesAndControlsSplitPane.setMaximumSize(new Dimension(300, 800));
        phrasesAndControlsSplitPane.setPreferredSize(new Dimension(300, 800));
        phrasesAndControlsSplitPane.setMinimumSize(new Dimension(300, 800));
    }

    private void setupControlPanel() {

    }

    private void setupPhrasesSection() {
        phrasesSection = new PhrasesSection();

        PhrasesPanel customPhrasesPanel = new PhrasesPanel(controller, PhraseType.CUSTOM);
        PhrasesPanel frequentlyUsedPhrasesPanel = new PhrasesPanel(controller, PhraseType.FREQUENTLY_USED);
        PhrasesPanel insightsPhrasesPanel = new PhrasesPanel(controller, PhraseType.INSIGHTS);

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
        editorPanel = new EditorPanel(controller, assignment.getAssignmentTitle(), assignment.getAssignmentHeadings());
        editingPopupMenu = new EditingPopupMenu();
        editorPanel.registerPopupMenu(editingPopupMenu);

        // Set the document data if it exists
        editorPanel.setData(assignment.getFeedbackDocuments().get(0));
        System.out.println("In Feedback Screen Class: " + assignment.getFeedbackDocuments().get(0).getHeadingData("1"));

        editorPanelScrollPane.add(editorPanel);
        editorPanelScrollPane.getViewport().setView(editorPanel);
    }

    private void setupPreviewPanel() {
        previewPanelScrollPane = new JScrollPane();

        // Create preview boxes
        List<PreviewBox> previewBoxes = new ArrayList<PreviewBox>();

        assignment.getFeedbackDocuments().forEach(feedbackDocument -> {
            PreviewBox previewBox = new PreviewBox(controller, feedbackDocument.getStudentId(), -1, "<empty file>");
            previewBox.setAssignment(assignment);
            previewBoxes.add(previewBox);
        });


//        assignment.getStudentIds().forEach(studentId -> {
//            previewBoxes.add(new PreviewBox(controller, studentId, -1, "<empty file>"));
//        });

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
            String filePath = assignment.getAssignmentTitle().toLowerCase().trim().replace(" ",  "-") + ".fht";
            JOptionPane.showMessageDialog(feedbackScreen, "Saving assignment to file: " + filePath);
            controller.saveAssignment(assignment, filePath);
        });

        exportOption.addActionListener(l -> {
            controller.exportFeedbackDocuments(assignment);
            JOptionPane.showMessageDialog(feedbackScreen, "Exporting assignment...");
        });

        fileMenu.add(saveOption);
        fileMenu.add(loadOption);
        fileMenu.add(exportOption);

        menuBar.add(fileMenu);

        feedbackScreen.add(menuBar, BorderLayout.PAGE_START);
    }

    /**
     * Listen for change messages from the model and perform appropriate
     * action to the GUI to reflect the changes in the model.
     * @param event The incoming message from the model.
     */
    @Override
    public void propertyChange(PropertyChangeEvent event) {
//        // Make sure event change is coming from model
//        if (event.getSource() != model) {
//            return;
//        }

        // Perform action based on the incoming message
        switch (event.getPropertyName()) {
            case "docViewChange":
                System.out.println("Got event from model, will update editor...");
                String newDocInView = (String) event.getNewValue();
                editorPanel.setData(assignment.getFeedbackDocumentForStudent(newDocInView));
                previewPanel.unhighlightPreviewBox(controller.getLastDocInView());
                previewPanel.highlightPreviewBox(newDocInView);
                previewPanel.repaint();
                previewPanel.revalidate();
                break;
            case "saveDoc":
                String studentId = (String) event.getNewValue();
                System.out.println("Saving doc: " + studentId);
                Map<String, String> headingsAndData = editorPanel.saveDataAsMap();
                double grade = editorPanel.getGrade();
                if (grade > 0) {
                    controller.saveFeedbackDocument(assignment, studentId, headingsAndData, grade);
                }
                break;
            case "updatePhrases":
                String samplePhrase = (String) event.getNewValue();
                phrasesSection.addPhraseToPanel(samplePhrase, PhraseType.CUSTOM);
                break;
            case "insertPhrase":
                String phrase = (String) event.getNewValue();
                String heading = controller.getCurrentHeadingBeingEdited();
                editorPanel.insertPhraseIntoFeedbackBox(phrase, heading);
                break;
            case "newPhrase":
                String newPhrase = (String) event.getNewValue();
                phrasesSection.addPhraseToPanel(newPhrase, PhraseType.CUSTOM);
                break;
            case "deletePhrase":
                String phraseToDelete = (String) event.getNewValue();
                phrasesSection.removePhraseFromPanel(phraseToDelete, PhraseType.CUSTOM);
                break;
            case "resetPhrasesPanel":
                System.out.println("removing all phrases from panel");
                phrasesSection.resetPhrasesPanel(PhraseType.CUSTOM);
                break;
            case "error":
                String errorMessage = (String) event.getNewValue();
                JOptionPane.showMessageDialog(feedbackScreen, errorMessage, "Error!", JOptionPane.ERROR_MESSAGE);
                break;
            default:
                System.out.println("Received unknown message!");
                System.out.println(event.getNewValue());
                break;
        }
    }
}
