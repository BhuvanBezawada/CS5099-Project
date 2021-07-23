package view;

import controller.AppController;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class PhrasesPanel extends JPanel {

    public PhraseType getPhraseType() {
        return phraseType;
    }

    private PhraseType phraseType;
    private List<PhraseBox> phraseBoxes;

    private AppController controller;

    public PhrasesPanel(AppController controller, PhraseType phraseType) {
        this.phraseType = phraseType;
        this.phraseBoxes = new ArrayList<PhraseBox>();

        this.controller = controller;

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setVisible(true);
    }

    public void addPhrase(String phrase) {
        PhraseBox phraseBox = new PhraseBox(controller, phrase);
        phraseBoxes.add(phraseBox);
        this.add(phraseBox);
        updatePhrasePanel();
    }

    public void removePhrase(String phrase) {
        int toRemove = 0;
        for (int i = 0; i < phraseBoxes.size(); i++) {
            if (phraseBoxes.get(i).getPhrase().equals(phrase)) {
                toRemove = i;
            }
        }

        this.remove(phraseBoxes.get(toRemove)); // Remove component
        phraseBoxes.remove(toRemove); // Remove from list
        updatePhrasePanel();
    }

    private void updatePhrasePanel() {
        this.revalidate();
        this.repaint();
    }
}
