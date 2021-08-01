package view;

import controller.AppController;
import model.Phrase;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
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

    public void addPhrase(String phrase, int phraseCount) {
        System.out.println("[GUI DEBUG] adding new phrase box for p: " + phrase);
        PhraseBox phraseBox = new PhraseBox(controller, phrase, phraseCount);
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

    public void updatePhraseCounter(String phrase, int phraseCounter) {
        System.out.println("[DEBUG] updating the phrase counter: p: " + phrase + " c: " + phraseCounter);
        for (int i = 0; i < phraseBoxes.size(); i++) {
            if (phraseBoxes.get(i).getPhrase().equals(phrase)) {
                phraseBoxes.get(i).setUsageCount(phraseCounter);
            }
        }

        // Sort the list
        this.removeAll();
        Collections.sort(phraseBoxes);
        phraseBoxes.forEach(this::add);

        updatePhrasePanel();
    }

    public void clearPanel() {
        this.removeAll();
        this.phraseBoxes.clear();
        updatePhrasePanel();
    }

    private void updatePhrasePanel() {
        this.revalidate();
        this.repaint();
    }
}
