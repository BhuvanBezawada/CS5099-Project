package view;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class PhrasesPanel extends JPanel {

    public PhraseType getPhraseType() {
        return phraseType;
    }

    private PhraseType phraseType;
    private List<PhraseBox> phraseBoxes;

    public PhrasesPanel(PhraseType phraseType) {
        this.phraseType = phraseType;
        this.phraseBoxes = new ArrayList<PhraseBox>();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setVisible(true);
    }

    public void addPhrase(String phrase) {
        PhraseBox phraseBox = new PhraseBox(phrase);
        phraseBoxes.add(phraseBox);
        this.add(phraseBox);
        updatePhrasePanel();
    }

    private void updatePhrasePanel() {
        this.revalidate();
        this.repaint();
    }
}
