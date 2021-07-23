package view;

import controller.AppController;
import model.FeedbackDocument;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditorPanel extends JPanel {

    // Data
    private String titleText;
    private List<String> headings;

    // Components
    private JLabel titleLabel;
    private JPanel feedbackBoxesPanel;
    private List<FeedbackBox> feedbackBoxes;

    private Map<String, FeedbackBox> headingAndFeedbackBox;

    private AppController controller;

    public EditorPanel(AppController controller, String titleText, List<String> headings) {
        // Set data variables
        this.titleText = titleText;
        this.headings = headings;
        this.controller = controller;

        this.titleLabel = new JLabel(this.titleText);
        this.feedbackBoxesPanel = new JPanel();
        this.feedbackBoxesPanel.setLayout(new BoxLayout(feedbackBoxesPanel, BoxLayout.PAGE_AXIS));
        this.feedbackBoxes = new ArrayList<FeedbackBox>();

        this.headingAndFeedbackBox = new HashMap<String, FeedbackBox>();

        // Layout components from top to bottom
        this.setLayout(new BorderLayout());
        this.setupTitle();
        this.setupFeedbackBoxes();
        this.setVisible(true);
    }


    public void setTitleLabel(String titleText) {
//        this.titleLabel = new JLabel(titleText);
        this.titleLabel.setText(titleText);
    }

    public void setHeadings(List<String> headings) {
        this.headings = headings;
    }

    private void setupTitle() {
        titleLabel.setFont(new Font("Helvetica Neue", Font.PLAIN, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        titleLabel.setBorder(new EmptyBorder(20, 20,20,20));
        this.add(titleLabel, BorderLayout.PAGE_START);
    }

    private void setupFeedbackBoxes(){
        headings.forEach(heading -> {
            FeedbackBox feedbackBox = new FeedbackBox(controller, heading);
            feedbackBoxes.add(feedbackBox);
            headingAndFeedbackBox.put(heading, feedbackBox);
            feedbackBoxesPanel.add(feedbackBox);
        });

        feedbackBoxesPanel.setVisible(true);
        this.add(feedbackBoxesPanel, BorderLayout.CENTER);
    }

    public void registerPopupMenu(EditingPopupMenu editingPopupMenu) {
        feedbackBoxes.forEach(feedbackBox -> feedbackBox.registerPopupMenu(editingPopupMenu));
    }

    public void setData(FeedbackDocument feedbackDocument) {
        // Fill up the feedback boxes with the data
        setTitleLabel("Document for: " + feedbackDocument.getStudentId());

        feedbackBoxes.forEach(feedbackBox -> {
            feedbackBox.setTextPaneText("");
            System.out.println("[DEBUG] feedback box: " + feedbackBox.getHeading());
            feedbackBox.setTextPaneText(feedbackDocument.getHeadingData(feedbackBox.getHeading()));
        });

        revalidate();
        repaint();
    }

    public void insertPhraseIntoFeedbackBox(String phrase, String feedbackBoxHeading) {
        this.headingAndFeedbackBox.get(feedbackBoxHeading).insertPhrase(phrase);
    }


    public Map<String, String> saveDataAsMap() {
        Map<String, String> headingsAndData = new HashMap<String, String>();
        feedbackBoxes.forEach(feedbackBox -> {
            headingsAndData.put(feedbackBox.getHeading(), feedbackBox.getTextPane().getText());
        });

        return headingsAndData;
    }
}
