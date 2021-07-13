package view;

import controller.AppController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PhraseBox extends JPanel {

    private String phrase;
    private JTextArea phraseTextArea;
    private JButton insertButton;

    private AppController controller;

    public PhraseBox(AppController controller, String phrase) {
        this.phrase = phrase;
        this.phraseTextArea = new JTextArea();

        this.controller = controller;

        // Following resize code is adapted from:
        // https://stackoverflow.com/questions/6714045/how-to-resize-jlabel-imageicon
        this.insertButton = new JButton(
                new ImageIcon(new ImageIcon("images/green_arrow.png").getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT)));

        this.setLayout(new BorderLayout());

        setupInsertButton();
        setupPhraseTextArea();

        this.setMaximumSize(new Dimension(300, 100));
        this.setVisible(true);
    }

    private void setupInsertButton() {
        this.insertButton.addActionListener(l -> {
            controller.insertPhraseIntoCurrentFeedbackBox(phrase);
        });
    }

    private void setupPhraseTextArea() {
        phraseTextArea.setRows(5);
        phraseTextArea.setColumns(10);
        phraseTextArea.setBorder(BorderCreator.createEmptyBorder(BorderCreator.PADDING_10_PIXELS));
        phraseTextArea.setText(phrase);
        phraseTextArea.setLineWrap(true);

        this.add(insertButton, BorderLayout.LINE_START);
        this.add(phraseTextArea, BorderLayout.CENTER);

        //phraseTextArea.setDragEnabled(true);
        //phraseTextArea.setFocusable(true);

        // Add some padding to the bottom on the panel and make it visible
        this.setBorder(BorderCreator.createEmptyBorder(BorderCreator.PADDING_20_PIXELS));
    }
}
