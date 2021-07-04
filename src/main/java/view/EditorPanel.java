package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EditorPanel extends JPanel {

    // Data
    private String titleText;
    private List<String> headings;

    // Components
    private JLabel titleLabel;
    private JPanel feedbackBoxesPanel;
    private List<FeedbackBox> feedbackBoxes;

    public EditorPanel(String titleText, List<String> headings) {
        // Set data variables
        this.titleText = titleText;
        this.headings = headings;

        this.titleLabel = new JLabel(this.titleText);
        this.feedbackBoxesPanel = new JPanel();
        this.feedbackBoxesPanel.setLayout(new BoxLayout(feedbackBoxesPanel, BoxLayout.PAGE_AXIS));
        this.feedbackBoxes = new ArrayList<FeedbackBox>();

        // Layout components from top to bottom
        this.setLayout(new BorderLayout());
        this.setupTitle();
        this.setupFeedbackBoxes();
        this.setVisible(true);
    }


    private void setupTitle() {
        titleLabel.setFont(new Font("Helvetica Neue", Font.PLAIN, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        titleLabel.setBorder(new EmptyBorder(20, 20,20,20));
        this.add(titleLabel, BorderLayout.PAGE_START);
    }

    private void setupFeedbackBoxes(){
        headings.forEach(heading -> {
            FeedbackBox feedbackBox = new FeedbackBox(heading);
            feedbackBoxes.add(feedbackBox);
            feedbackBoxesPanel.add(feedbackBox);
        });

        feedbackBoxesPanel.setVisible(true);
        this.add(feedbackBoxesPanel, BorderLayout.CENTER);
    }
}
