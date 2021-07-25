package view;

import controller.AppController;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class PhraseBox extends JPanel {

    private String phrase;
    private String phraseSentiment;

    private JTextArea phraseTextArea;
    private JButton insertButton;

    private JLabel sentimentLabel;

    private AppController controller;

    public PhraseBox(AppController controller, String phrase) {
        this.phrase = phrase;
        this.phraseTextArea = new JTextArea();
        this.phraseSentiment = controller.getPhraseSentiment(phrase);
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

        this.setMaximumSize(new Dimension(300, 100));
        this.setVisible(true);
    }

    private void setupInsertButton() {
        this.insertButton.addActionListener(l -> {
            controller.insertPhraseIntoCurrentFeedbackBox(phrase);
            controller.saveFeedbackDocument(controller.getCurrentDocInView());
        });

        this.add(insertButton, BorderLayout.LINE_START);
    }

    private void setupSentimentLabel() {
        ImageIcon icon = null;
        URL emojiFilePath = null; //this.getClass().getResource("../"); // maybe replace with ? for unknown ones...
        if (this.phraseSentiment.equals("Neutral")) {
            emojiFilePath = this.getClass().getResource("/neutral.png");
        } else if (this.phraseSentiment.equals("Positive") || this.phraseSentiment.equals("Very positive")) {
            emojiFilePath = this.getClass().getResource("/positive.png");
        } else {
            emojiFilePath = this.getClass().getResource("/negative.png");
        }

        this.sentimentLabel = new JLabel(new ImageIcon(new ImageIcon(emojiFilePath).getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT)));
        this.add(sentimentLabel, BorderLayout.LINE_END);
    }
    private void setupPhraseTextArea() {
        phraseTextArea.setRows(5);
        phraseTextArea.setColumns(10);
        phraseTextArea.setBorder(BorderCreator.createEmptyBorder(BorderCreator.PADDING_10_PIXELS));
        phraseTextArea.setText(phrase);
        phraseTextArea.setLineWrap(true);
        phraseTextArea.setWrapStyleWord(true);

        this.add(phraseTextArea, BorderLayout.CENTER);

        //phraseTextArea.setDragEnabled(true);
        //phraseTextArea.setFocusable(true);

        // Add some padding to the bottom on the panel and make it visible
        this.setBorder(BorderCreator.createEmptyBorder(BorderCreator.PADDING_20_PIXELS));
    }

    public String getPhrase() {
        return this.phrase;
    }
}
