package view;

import controller.AppController;
import model.LinkedPhrases;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class InsightBox extends JPanel {

    private PhraseBox phraseBox1;
    private PhraseBox phraseBox2;

    public InsightBox(AppController controller, LinkedPhrases linkedPhrases) {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setBorder(new CompoundBorder(
                new EmptyBorder(20, 10, 0, 10),
                new LineBorder(Color.black, 1)
        ));

        this.phraseBox1 = new PhraseBox(controller, linkedPhrases.getFirst().getPhraseAsString(), linkedPhrases.getFirst().getUsageCount());
        this.phraseBox2 = new PhraseBox(controller, linkedPhrases.getSecond().getPhraseAsString(), linkedPhrases.getSecond().getUsageCount());

        JTextArea infoText = new JTextArea("The following phrases have been paired together " + linkedPhrases.getCount() + " times.");
        infoText.setBackground(Color.WHITE);
        infoText.setBorder(BorderCreator.createAllSidesEmptyBorder(10));
        infoText.setWrapStyleWord(true);
        infoText.setLineWrap(true);

        this.add(infoText);
        this.add(phraseBox1);
        this.add(phraseBox2);

        this.setMaximumSize(new Dimension(260, 300));
        this.setVisible(true);
    }
}
