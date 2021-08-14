package view;

import controller.AppController;
import model.LinkedPhrases;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Phrases Panel Class.
 */
public class PhrasesPanel extends JPanel {

    // Instance variables
    private final PhraseType phraseType;
    private final List<PhraseBox> phraseBoxes;
    private final AppController controller;

    /**
     * Constructor.
     *
     * @param controller The controller.
     * @param phraseType The type of phrases to show on the panel.
     */
    public PhrasesPanel(AppController controller, PhraseType phraseType) {
        this.phraseType = phraseType;
        this.phraseBoxes = new ArrayList<PhraseBox>();
        this.controller = controller;

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setVisible(true);
    }

    /**
     * Get the phrase type of the panel.
     *
     * @return The phrase type of the panel.
     */
    public PhraseType getPhraseType() {
        return phraseType;
    }

    /**
     * Add a phrase to the panel.
     *
     * @param phrase      The phrase to add. // TODO: replace with phrase object?
     * @param phraseCount The usage count of the phrase.
     */
    public void addPhrase(String phrase, int phraseCount) {
        PhraseBox phraseBox = new PhraseBox(controller, phrase, phraseCount);
        phraseBoxes.add(phraseBox);
        this.add(phraseBox);
        updatePhrasePanel();
    }

    /**
     * Remove a phrase from the panel.
     *
     * @param phrase The phrase to remove.
     */
    public void removePhrase(String phrase) {
        // Find the index of the phrase box
        int toRemove = 0;
        for (int i = 0; i < phraseBoxes.size(); i++) {
            if (phraseBoxes.get(i).getPhrase().equals(phrase)) {
                toRemove = i;
            }
        }

        // Remove the component and then remove it from the list
        this.remove(phraseBoxes.get(toRemove));
        phraseBoxes.remove(toRemove);
        updatePhrasePanel();
    }

    /**
     * Update the phrase counter of a phrase.
     *
     * @param phrase        The phrase to update.
     * @param phraseCounter The new usage count value.
     */
    public void updatePhraseCounter(String phrase, int phraseCounter) {
        // Find the phrase to update
        for (PhraseBox phraseBox : phraseBoxes) {
            if (phraseBox.getPhrase().equals(phrase)) {
                phraseBox.setUsageCount(phraseCounter);
            }
        }

        // Sort the list
        this.removeAll();
        Collections.sort(phraseBoxes);
        phraseBoxes.forEach(this::add);
        updatePhrasePanel();
    }

    /**
     * Clear the panel.
     */
    public void clearPanel() {
        this.removeAll();
        this.phraseBoxes.clear();
        updatePhrasePanel();
    }

    /**
     * Refresh the panel.
     */
    private void updatePhrasePanel() {
        this.revalidate();
        this.repaint();
    }

    /**
     * Add an insight box to the panel (only if it is an Insight Panel)
     *
     * @param linkedPhrases The linked phrases to display in the insight box.
     */
    public void addInsightBox(LinkedPhrases linkedPhrases) {
        if (this.phraseType == PhraseType.INSIGHTS) {
            this.add(new InsightBox(controller, linkedPhrases));
            updatePhrasePanel();
        }
    }
}
