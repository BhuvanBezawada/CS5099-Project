package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PhraseBox extends JPanel {

    private String phrase;
    private JTextArea phraseTextArea;

    public PhraseBox(String phrase) {
        this.phrase = phrase;
        this.phraseTextArea = new JTextArea();
        this.setLayout(new BorderLayout());
        setupPhraseTextArea();

        this.setMaximumSize(new Dimension(300, 100));
        this.setVisible(true);
    }

    private void setupPhraseTextArea() {
        phraseTextArea.setRows(5);
        phraseTextArea.setColumns(10);
        phraseTextArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        phraseTextArea.setText(phrase);
        this.add(phraseTextArea, BorderLayout.CENTER);
        // Add some padding to the bottom on the panel and make it visible
        this.setBorder(new EmptyBorder(20, 20, 0, 20));
    }
}
