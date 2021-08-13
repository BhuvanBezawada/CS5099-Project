package view;

import controller.AppController;
import model.LinkedPhrases;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Insight Box Class.
 */
public class InsightBox extends JPanel {

    // Instance variables
    private LinkedPhrases linkedPhrases;
    private PhraseBox phraseBox1;
    private PhraseBox phraseBox2;
    private AppController controller;

    /**
     * Constructor.
     * @param controller The controller.
     * @param linkedPhrases The pair of linked phrases.
     */
    public InsightBox(AppController controller, LinkedPhrases linkedPhrases) {
        this.controller = controller;
        this.linkedPhrases = linkedPhrases;

        // Set layout and border
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setBorder(new CompoundBorder(
                new EmptyBorder(20, 10, 0, 10),
                new LineBorder(Color.black, 1)
        ));

        // Setup components
        setupInfoText();
        setupPhraseBoxes();

        // Set size and visibility
        this.setMaximumSize(new Dimension(260, 300));
        this.setVisible(true);
    }

    /**
     * Setup the phrase boxes.
     */
    private void setupPhraseBoxes() {
        this.phraseBox1 = new PhraseBox(controller, linkedPhrases.getFirst().getPhraseAsString(), linkedPhrases.getFirst().getUsageCount());
        this.phraseBox2 = new PhraseBox(controller, linkedPhrases.getSecond().getPhraseAsString(), linkedPhrases.getSecond().getUsageCount());
        this.add(phraseBox1);
        this.add(phraseBox2);
    }

    /**
     * Setup the info text.
     */
    private void setupInfoText() {
        JTextArea infoText = new JTextArea("The following phrases have been paired together " + linkedPhrases.getCount() + " times.");
        infoText.setBackground(Color.WHITE);
        infoText.setBorder(BorderCreator.createAllSidesEmptyBorder(BorderCreator.PADDING_10_PIXELS));
        infoText.setWrapStyleWord(true);
        infoText.setLineWrap(true);
        this.add(infoText);
    }
}
