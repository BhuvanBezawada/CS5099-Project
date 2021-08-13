package view;

import controller.AppController;
import model.FeedbackDocument;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Editor Panel Class.
 */
public class EditorPanel extends JPanel {

    // Instance variables
    private String titleText;
    private List<String> headings;
    private JLabel titleLabel;
    private JPanel feedbackBoxesPanel;
    private List<FeedbackBox> feedbackBoxes;
    private GradeBox gradeBox;
    private Map<String, FeedbackBox> headingAndFeedbackBox;
    private AppController controller;

    /**
     * Constructor.
     *
     * @param controller The controller.
     * @param titleText  The title text for the editor panel.
     * @param headings   The headings of the feedback boxes to create.
     */
    public EditorPanel(AppController controller, String titleText, List<String> headings) {
        // Set data variables
        this.titleText = titleText;
        this.headings = headings;
        this.controller = controller;
        this.feedbackBoxes = new ArrayList<FeedbackBox>();
        this.headingAndFeedbackBox = new HashMap<String, FeedbackBox>();

        // Layout components from top to bottom
        this.setLayout(new BorderLayout());
        this.setupTitle();
        this.setupFeedbackBoxesPanel();
        this.setupFeedbackBoxes();
        this.setupGradeBox();

        // Set visibility
        this.setVisible(true);
    }

    /**
     * Setup the feedback boxes panel.
     */
    private void setupFeedbackBoxesPanel() {
        this.feedbackBoxesPanel = new JPanel();
        this.feedbackBoxesPanel.setLayout(new BoxLayout(feedbackBoxesPanel, BoxLayout.PAGE_AXIS));
    }

    /**
     * Setup the grade box.
     */
    private void setupGradeBox() {
        this.gradeBox = new GradeBox(controller);
        this.add(gradeBox, BorderLayout.PAGE_END);
    }


    /**
     * Set the title label.
     *
     * @param titleText The title label text.
     */
    public void setTitleLabel(String titleText) {
        this.titleLabel.setText(titleText);
    }


    /**
     * Setup the title.
     */
    private void setupTitle() {
        this.titleLabel = new JLabel(this.titleText);
        this.titleLabel.setFont(new Font("Helvetica Neue", Font.PLAIN, 20));
        this.titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        this.titleLabel.setBorder(new EmptyBorder(20, 20, 20, 20));
        this.add(titleLabel, BorderLayout.PAGE_START);
    }

    /**
     * Setup the feedback boxes.
     */
    private void setupFeedbackBoxes() {
        headings.forEach(heading -> {
            FeedbackBox feedbackBox = new FeedbackBox(controller, heading);
            feedbackBoxes.add(feedbackBox);
            headingAndFeedbackBox.put(heading, feedbackBox);
            feedbackBoxesPanel.add(feedbackBox);
        });

        feedbackBoxesPanel.setVisible(true);
        this.add(feedbackBoxesPanel, BorderLayout.CENTER);
    }

    /**
     * Register the feedback boxes with the popup editing menu.
     *
     * @param editingPopupMenu The editing menu to register with.
     */
    public void registerPopupMenu(EditingPopupMenu editingPopupMenu) {
        feedbackBoxes.forEach(feedbackBox -> feedbackBox.registerPopupMenu(editingPopupMenu));
    }

    /**
     * Set the data from a feedback document.
     *
     * @param feedbackDocument The feedback document to set the data from.
     */
    public void setData(FeedbackDocument feedbackDocument) {
        // Fill up the feedback boxes with the data
        setTitleLabel("Document for: " + feedbackDocument.getStudentId());

        // Set the data for each feedback box
        feedbackBoxes.forEach(feedbackBox -> {
            feedbackBox.setTextAreaText("");
            feedbackBox.setTextAreaText(feedbackDocument.getHeadingData(feedbackBox.getHeading()));
        });

        // Set the grade box
        gradeBox.setStudentId(feedbackDocument.getStudentId());
        gradeBox.setGrade(feedbackDocument.getGrade());

        // Refresh the UI
        revalidate();
        repaint();
    }

    /**
     * Insert a phrase into the feedback box.
     *
     * @param phrase             The phrase to insert.
     * @param feedbackBoxHeading The feedback box to insert the phrase into.
     */
    public void insertPhraseIntoFeedbackBox(String phrase, String feedbackBoxHeading) {
        this.headingAndFeedbackBox.get(feedbackBoxHeading).insertPhrase(phrase);
    }

    /**
     * Save the data from each feedback box and return it as a map.
     *
     * @return A map of the headings and their data.
     */
    public Map<String, String> saveDataAsMap() {
        Map<String, String> headingsAndData = new HashMap<String, String>();
        feedbackBoxes.forEach(feedbackBox -> {
            headingsAndData.put(feedbackBox.getHeading(), feedbackBox.getTextArea().getText());
        });

        return headingsAndData;
    }

    /**
     * Get the grade.
     *
     * @return The grade value.
     */
    public double getGrade() {
        return this.gradeBox.getGrade();
    }
}
