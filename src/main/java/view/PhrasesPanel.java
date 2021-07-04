package view;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class PhrasesPanel extends JPanel {

    private String panelName;
    private List<PhraseBox> phraseBoxes;

    public PhrasesPanel(String panelName) {
        this.panelName = panelName;
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
