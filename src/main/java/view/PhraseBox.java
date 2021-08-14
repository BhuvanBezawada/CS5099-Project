package view;

import controller.AppController;
import model.Sentiment;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Phrase Box Class.
 */
public class PhraseBox extends JPanel implements Comparable<PhraseBox> {

    // Instance variables
    private String phrase;
    private String phraseSentiment;
    private int usageCount;
    private JTextArea phraseTextArea;
    private JButton insertButton;
    private JLabel sentimentLabel;
    private JLabel usageCountLabel;
    private AppController controller;

    /**
     * Constructor.
     * @param controller The controller.
     * @param phrase The phrase to display.
     * @param usageCount The usage count of the phrase. // TODO: maybe just pass in phrase object?
     */
    public PhraseBox(AppController controller, String phrase, int usageCount) {
        this.phrase = phrase;
        this.phraseTextArea = new JTextArea();
        this.phraseSentiment = controller.getPhraseSentiment(phrase);
        this.usageCount = usageCount;

        System.out.println("Sentiment: " + phraseSentiment);

        this.controller = controller;

        // Following resize code is adapted from:
        // https://stackoverflow.com/questions/6714045/how-to-resize-jlabel-imageicon
        this.insertButton = new JButton(
                new ImageIcon(new ImageIcon(this.getClass().getResource("/green_arrow.png")).getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT)));

        this.setLayout(new BorderLayout());

        setupInsertButton();
        setupPhraseTextArea();
        setupSentimentLabel();
        setupUsageCountLabel();

        this.setMaximumSize(new Dimension(300, 100));
        this.setVisible(true);
    }

    private void setupInsertButton() {
        this.insertButton.addActionListener(l -> {
            controller.insertPhraseIntoCurrentFeedbackBox(phrase);
            controller.saveFeedbackDocument(controller.getCurrentDocumentInView());
        });

        this.add(insertButton, BorderLayout.LINE_START);
    }

    // TODO: Fix checking of sentiment
    private void setupSentimentLabel() {
        ImageIcon icon = null;
        URL emojiFilePath = null; //this.getClass().getResource("../"); // maybe replace with ? for unknown ones...
        if (this.phraseSentiment.equals(Sentiment.NEUTRAL.getSentimentAsString())) {
            emojiFilePath = this.getClass().getResource("/neutral.png");
        } else if (this.phraseSentiment.equals(Sentiment.POSITIVE.getSentimentAsString()) || this.phraseSentiment.equals(Sentiment.VERY_POSITIVE.getSentimentAsString())) {
            emojiFilePath = this.getClass().getResource("/positive.png");
        } else {
            emojiFilePath = this.getClass().getResource("/negative.png");
        }

        this.sentimentLabel = new JLabel(new ImageIcon(new ImageIcon(emojiFilePath).getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT)));
        this.add(sentimentLabel, BorderLayout.LINE_END);
    }

    private void setupUsageCountLabel() {
        this.usageCountLabel = new JLabel(String.valueOf(usageCount));
        this.add(usageCountLabel, BorderLayout.BEFORE_FIRST_LINE);
    }


    private void setupPhraseTextArea() {
        phraseTextArea.setRows(5);
        phraseTextArea.setColumns(10);
        phraseTextArea.setBorder(BorderCreator.createEmptyBorderLeavingBottom(BorderCreator.PADDING_10_PIXELS));
        phraseTextArea.setText(phrase);
        phraseTextArea.setLineWrap(true);
        phraseTextArea.setWrapStyleWord(true);

        this.add(phraseTextArea, BorderLayout.CENTER);

        //phraseTextArea.setDragEnabled(true);
        //phraseTextArea.setFocusable(true);

        // Add some padding to the bottom on the panel and make it visible
        this.setBorder(BorderCreator.createEmptyBorderLeavingBottom(BorderCreator.PADDING_20_PIXELS));
    }

    public String getPhrase() {
        return this.phrase;
    }

    public int getUsageCount() {
        return this.usageCount;
    }

    public void setUsageCount(int usageCount) {
        this.usageCount = usageCount;
        this.usageCountLabel.setText(String.valueOf(usageCount));
        repaint();
        revalidate();
    }

    @Override
    public int compareTo(PhraseBox o) {
        return o.getUsageCount() - this.getUsageCount();
    }
}
