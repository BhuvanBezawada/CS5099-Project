package view;

import controller.AppController;
import model.Assignment;
import model.LinkedPhrases;
import model.Phrase;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Feedback Screen Class.
 */
public class FeedbackScreen implements PropertyChangeListener {

    // Instance variables
    private JFrame feedbackScreen;
    private JPanel feedbackScreenPanel;
    private JSplitPane previewAndEditorSplitPane;
    private JScrollPane previewPanelScrollPane;
    private PreviewPanel previewPanel;
    private JScrollPane editorPanelScrollPane;
    private EditorPanel editorPanel;
    private EditingPopupMenu editingPopupMenu;
    private JSplitPane phrasesAndPhraseEntrySplitPane;
    private PhrasesSection phrasesSection;
    private PhraseEntryBox phraseEntryBox;
    private GridBagConstraints gridBagConstraints;
    private Assignment assignment;
    private AppController controller;

    /**
     * Constructor.
     *
     * @param controller The controller.
     * @param assignment The assignment.
     */
    public FeedbackScreen(AppController controller, Assignment assignment) {
        this.controller = controller;
        this.controller.registerWithModel(this);
        this.assignment = assignment;

        // Setup components
        setupFeedbackScreen();
        setupFeedbackScreenPanel();
        setupPreviewPanel();
        setupEditorPanel();
        setupPhrasesSection();
        setupPreviewAndEditorSplitPane();
        setupPhrasesAndPhraseEntrySplitPane();
        setupMenuBar();
        positionEditorSplitPane();
        positionPhrasesSplitPane();

        // Add the main panel to the screen and set visibility
        feedbackScreen.add(feedbackScreenPanel, BorderLayout.CENTER);
        feedbackScreen.setVisible(true);
    }

    /**
     * Position the phrases split pane with the gridbag constraints.
     */
    private void positionPhrasesSplitPane() {
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        feedbackScreenPanel.add(phrasesAndPhraseEntrySplitPane, gridBagConstraints);
    }

    /**
     * Position the editor split pane with the gridbag constraints.
     */
    private void positionEditorSplitPane() {
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        feedbackScreenPanel.add(previewAndEditorSplitPane, gridBagConstraints);
    }

    /**
     * Setup the feedback screen.
     */
    private void setupFeedbackScreen() {
        feedbackScreen = new JFrame("Feedback Composition");
        feedbackScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        feedbackScreen.setSize(1200, 800);
        feedbackScreen.setLayout(new BorderLayout());

        // Centre the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - feedbackScreen.getWidth()) / 2;
        int y = (screenSize.height - feedbackScreen.getHeight()) / 2;
        feedbackScreen.setLocation(x, y);
    }

    /**
     * Setup the feedback screen panel.
     */
    private void setupFeedbackScreenPanel() {
        feedbackScreenPanel = new JPanel(new GridBagLayout());
        this.gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
    }

    /**
     * Setup the phrases section and phrase entry box.
     */
    private void setupPhrasesAndPhraseEntrySplitPane() {
        this.phraseEntryBox = new PhraseEntryBox(controller);
        this.phraseEntryBox.disablePhraseEntryBox();
        phrasesAndPhraseEntrySplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, phrasesSection, phraseEntryBox);
        phrasesAndPhraseEntrySplitPane.setOneTouchExpandable(false);
        phrasesAndPhraseEntrySplitPane.setDividerLocation(600);
        phrasesAndPhraseEntrySplitPane.setMaximumSize(new Dimension(300, 800));
        phrasesAndPhraseEntrySplitPane.setPreferredSize(new Dimension(300, 800));
        phrasesAndPhraseEntrySplitPane.setMinimumSize(new Dimension(300, 800));
    }

    /**
     * Setup the phrase panels and the phrases section.
     */
    private void setupPhrasesSection() {
        phrasesSection = new PhrasesSection(controller);

        // Create panels
        PhrasesPanel customPhrasesPanel = new PhrasesPanel(controller, PhraseType.CUSTOM);
        PhrasesPanel frequentlyUsedPhrasesPanel = new PhrasesPanel(controller, PhraseType.FREQUENTLY_USED);
        PhrasesPanel insightsPhrasesPanel = new PhrasesPanel(controller, PhraseType.INSIGHTS);

        // Add panels
        phrasesSection.addPhrasesPanel(customPhrasesPanel);
        phrasesSection.addPhrasesPanel(frequentlyUsedPhrasesPanel);
        phrasesSection.addPhrasesPanel(insightsPhrasesPanel);

        // Start on frequently used pane
        phrasesSection.setHighlightedPane(1);
        controller.setCurrentPhrasePanelInView(PhraseType.FREQUENTLY_USED);
    }

    /**
     * Setup the preview and editor split pane.
     */
    private void setupPreviewAndEditorSplitPane() {
        previewAndEditorSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, previewPanelScrollPane, editorPanelScrollPane);
        previewAndEditorSplitPane.setMaximumSize(new Dimension(900, 800));
        previewAndEditorSplitPane.setPreferredSize(new Dimension(900, 800));
        previewAndEditorSplitPane.setMinimumSize(new Dimension(900, 800));
        previewAndEditorSplitPane.setOneTouchExpandable(true);
        previewAndEditorSplitPane.setDividerLocation(300);
    }

    /**
     * Setup the editor panel.
     */
    private void setupEditorPanel() {
        editorPanelScrollPane = new JScrollPane();

        // Create editor panel with popup menu
        editorPanel = new EditorPanel(controller, assignment.getAssignmentTitle(), assignment.getAssignmentHeadings());
        editingPopupMenu = new EditingPopupMenu();
        editorPanel.registerPopupMenu(editingPopupMenu);

        // Set the document data if it exists
        editorPanel.setData(assignment.getFeedbackDocumentForStudent(controller.getCurrentDocumentInView()));

        // Make the panel scrollable
        editorPanelScrollPane.add(editorPanel);
        editorPanelScrollPane.getViewport().setView(editorPanel);
    }

    /**
     * Setup the preview panel.
     */
    private void setupPreviewPanel() {
        previewPanelScrollPane = new JScrollPane();

        // Create preview boxes
        List<PreviewBox> previewBoxes = new ArrayList<PreviewBox>();
        assignment.getFeedbackDocuments().forEach(feedbackDocument -> {
            PreviewBox previewBox = new PreviewBox(controller, feedbackDocument.getStudentId(), feedbackDocument.getGrade(), controller.getFirstLineFromDocument(assignment, feedbackDocument.getStudentId()));
            previewBox.setAssignment(assignment);
            previewBoxes.add(previewBox);
        });

        // Order the preview boxes by the id if possible
        Collections.sort(previewBoxes);
        controller.setCurrentDocumentInView(previewBoxes.get(0).getHeading());

        // Make the preview panel scrollable
        previewPanel = new PreviewPanel(previewBoxes);
        previewPanelScrollPane.add(previewPanel);
        previewPanelScrollPane.getViewport().setView(previewPanel);

        // Set scroll position to top
        // The following line is adapted from: https://stackoverflow.com/questions/1166072/setting-scroll-bar-on-a-jscrollpane
        SwingUtilities.invokeLater(() -> previewPanelScrollPane.getVerticalScrollBar().setValue(0));
    }

    /**
     * Setup the menubar.
     */
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveOption = new JMenuItem("Save current document");
        JMenuItem sentimentOption = new JMenuItem("Show overall sentiment of current document");
        JMenuItem exportDocsOption = new JMenuItem("Export feedback documents");
        JMenuItem exportGradesOption = new JMenuItem("Export grades");
        JMenuItem visGradesOption = new JMenuItem("Visualise grades");

        // Save operation
        saveOption.addActionListener(l -> {
            JOptionPane.showMessageDialog(feedbackScreen, "Saving document for student: " + controller.getCurrentDocumentInView());
            controller.saveFeedbackDocument(controller.getCurrentDocumentInView());
        });

        // View sentiment option
        sentimentOption.addActionListener(l -> {
            new SentimentViewer(controller, assignment.getFeedbackDocumentForStudent(controller.getCurrentDocumentInView()));
        });

        // Export documents option
        exportDocsOption.addActionListener(l -> {
            controller.exportFeedbackDocuments(assignment);
            JOptionPane.showMessageDialog(feedbackScreen, "Exporting assignment...");
        });

        // Export grades option
        exportGradesOption.addActionListener(l -> {
            controller.exportGrades(assignment);
            JOptionPane.showMessageDialog(feedbackScreen, "Exporting assignment grades...");
        });

        // Visualise grades option
        visGradesOption.addActionListener(l -> {
            controller.visualiseGrades(assignment);
            JOptionPane.showMessageDialog(feedbackScreen, "Generating visualisation of assignment grades...");
        });

        // Add all options to menu
        fileMenu.add(saveOption);
        fileMenu.add(sentimentOption);
        fileMenu.add(exportDocsOption);
        fileMenu.add(exportGradesOption);
        fileMenu.add(visGradesOption);

        // Add the menu bar to the screen
        menuBar.add(fileMenu);
        feedbackScreen.add(menuBar, BorderLayout.PAGE_START);
    }

    /**
     * Listen for change messages from the model and perform appropriate
     * action to the GUI to reflect the changes in the model.
     *
     * @param event The incoming message from the model.
     */
    @Override
    public void propertyChange(PropertyChangeEvent event) {
        // Perform action based on the incoming message
        switch (event.getPropertyName()) {
            case "docViewChange":
                performDocumentViewChange(event);
                break;
            case "saveDoc":
                performDocumentSave(event);
                break;
            case "updatePhrases":
                Phrase samplePhrase = (Phrase) event.getNewValue();
                phrasesSection.addPhraseToPanel(samplePhrase.getPhraseAsString(), samplePhrase.getUsageCount(), PhraseType.FREQUENTLY_USED);
                break;
            case "insertPhrase":
                performInsertPhrase(event);
                break;
            case "newPhrase":
                performAddNewPhrase(event, PhraseType.FREQUENTLY_USED);
                break;
            case "deletePhrase":
                performDeletePhrase(event);
                break;
            case "updatePhraseCounter":
                performUpdatePhrase(event);
                break;
            case "resetPhrasesPanel":
                performResetPanel();
                break;
            case "newCustomPhrase":
                performAddNewPhrase(event, PhraseType.CUSTOM);
                break;
            case "phrasePanelChange":
                performPhrasePanelChange(event);
                break;
            case "error":
                displayError(event);
                break;
            default:
                System.out.println("Received unknown message!");
                System.out.println(event.getNewValue());
                break;
        }
    }

    /**
     * Display an error message.
     *
     * @param event The event notification from the model.
     */
    private void displayError(PropertyChangeEvent event) {
        String errorMessage = (String) event.getNewValue();
        JOptionPane.showMessageDialog(feedbackScreen, errorMessage, "Error!", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Perform a phrase panel change.
     *
     * @param event The event notification from the model.
     */
    private void performPhrasePanelChange(PropertyChangeEvent event) {
        if (phraseEntryBox != null) {
            PhraseType panelInView = (PhraseType) event.getNewValue();
            if (panelInView == PhraseType.CUSTOM) {
                phrasesSection.resetPhrasesPanel(PhraseType.CUSTOM);
                controller.showCustomPhrases();
                phraseEntryBox.enablePhraseEntryBox();
            } else {
                phraseEntryBox.disablePhraseEntryBox();
            }

            if (panelInView == PhraseType.INSIGHTS) {
                List<LinkedPhrases> linkedPhrasesForHeading = controller.getLinkedPhrasesForHeading(controller.getCurrentHeadingBeingEdited());
                linkedPhrasesForHeading.forEach(linkedPhrases -> {
                    phrasesSection.addInsightToInsightPanel(linkedPhrases);
                });
            }
        }
    }

    /**
     * Reset the panels. // TODO need to know which panel to reset.
     */
    private void performResetPanel() {
        System.out.println("removing all phrases from panel");
        phrasesSection.resetPhrasesPanel(PhraseType.FREQUENTLY_USED);
    }

    /**
     * Perform an update to an existing phrase.
     *
     * @param event The event notification from the model.
     */
    private void performUpdatePhrase(PropertyChangeEvent event) {
        Phrase phraseToUpdate = (Phrase) event.getNewValue();
        phrasesSection.updatePhraseCounter(PhraseType.FREQUENTLY_USED, phraseToUpdate.getPhraseAsString(), phraseToUpdate.getUsageCount());
    }

    /**
     * Delete a phrase from the frequently used panel.
     *
     * @param event The event notification from the model.
     */
    private void performDeletePhrase(PropertyChangeEvent event) {
        Phrase phraseToDelete = (Phrase) event.getNewValue();
        phrasesSection.removePhraseFromPanel(phraseToDelete.getPhraseAsString(), PhraseType.FREQUENTLY_USED);
    }

    /**
     * Add a phrase to the given panel.
     *
     * @param event      The event notification from the model.
     * @param phraseType The panel to add the phrase to.
     */
    private void performAddNewPhrase(PropertyChangeEvent event, PhraseType phraseType) {
        Phrase newPhrase = (Phrase) event.getNewValue();
        phrasesSection.addPhraseToPanel(newPhrase.getPhraseAsString(), newPhrase.getUsageCount(), phraseType);
    }

    /**
     * Insert a phrase into the feedback box being currently edited.
     *
     * @param event The event notification from the model.
     */
    private void performInsertPhrase(PropertyChangeEvent event) {
        String phrase = (String) event.getNewValue();
        String heading = controller.getCurrentHeadingBeingEdited();
        editorPanel.insertPhraseIntoFeedbackBox(phrase, heading);
    }

    /**
     * Save a document.
     *
     * @param event The event notification from the model.
     */
    private void performDocumentSave(PropertyChangeEvent event) {
        String studentId = (String) event.getNewValue();
        System.out.println("Saving doc: " + studentId);
        Map<String, String> headingsAndData = editorPanel.saveDataAsMap();
        double grade = editorPanel.getGrade();
        if (grade >= 0) {
            controller.saveFeedbackDocument(assignment, studentId, headingsAndData, grade);
            previewPanel.updatePreviewBox(studentId, controller.getFirstLineFromDocument(assignment, studentId), grade);
        }
    }

    /**
     * Change the document in the current view.
     *
     * @param event The event notification from the model.
     */
    private void performDocumentViewChange(PropertyChangeEvent event) {
        System.out.println("Got event from model, will update editor...");
        String newDocInView = (String) event.getNewValue();
        editorPanel.setData(assignment.getFeedbackDocumentForStudent(newDocInView));

        if (controller.getLastDocumentInView() != null) {
            previewPanel.updatePreviewBoxLine(
                    controller.getLastDocumentInView(),
                    controller.getFirstLineFromDocument(assignment, controller.getLastDocumentInView())
            );
            previewPanel.unhighlightPreviewBox(controller.getLastDocumentInView());
        }

        previewPanel.highlightPreviewBox(newDocInView);

        previewPanel.repaint();
        previewPanel.revalidate();
    }
}
