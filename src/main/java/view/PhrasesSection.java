package view;

import controller.AppController;
import model.LinkedPhrases;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Phrases Section Class.
 */
public class PhrasesSection extends JPanel {

    // Instance variables
    private final JTabbedPane tabbedPane;
    private final Map<PhraseType, PhrasesPanel> phrasesPanelsByType;
    private final AppController controller;

    /**
     * Constructor.
     *
     * @param controller The controller.
     */
    public PhrasesSection(AppController controller) {
        this.controller = controller;
        this.tabbedPane = new JTabbedPane();
        this.phrasesPanelsByType = new HashMap<PhraseType, PhrasesPanel>();

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setupTabbedPane();
        this.setVisible(true);
    }

    /**
     * Add a phrases panel.
     *
     * @param phrasesPanel The phrases panel to add.
     */
    public void addPhrasesPanel(PhrasesPanel phrasesPanel) {
        JScrollPane phrasesPanelScrollPane = new JScrollPane(phrasesPanel);
        phrasesPanelsByType.put(phrasesPanel.getPhraseType(), phrasesPanel);
        tabbedPane.addTab(phrasesPanel.getPhraseType().getPhraseTypeAsString(), phrasesPanelScrollPane);
        updatePhrasesSection();
    }

    /**
     * Reset a phrases panel.
     *
     * @param phraseType The type of the panel to reset.
     */
    public void resetPhrasesPanel(PhraseType phraseType) {
        phrasesPanelsByType.get(phraseType).clearPanel();
        updatePhrasesSection();
    }

    /**
     * Add a phrase to a panel.
     *
     * @param phrase          The phrase to add.
     * @param phraseCount     The phrase usage count.
     * @param phrasePanelType The type of the panel to add the phrase to.
     */
    public void addPhraseToPanel(String phrase, int phraseCount, PhraseType phrasePanelType) {
        this.phrasesPanelsByType.get(phrasePanelType).addPhrase(phrase, phraseCount);
    }

    /**
     * Remove a phrase from a given panel.
     *
     * @param phrase          The phrase to remove.
     * @param phrasePanelType The type of the panel the phrase is currently shown on.
     */
    public void removePhraseFromPanel(String phrase, PhraseType phrasePanelType) {
        this.phrasesPanelsByType.get(phrasePanelType).removePhrase(phrase);
    }

    /**
     * Refresh the panels.
     */
    private void updatePhrasesSection() {
        this.revalidate();
        this.repaint();
    }

    /**
     * Update the phrase counter of a phrase.
     *
     * @param phraseType  The phrase panel the phrase is on.
     * @param phrase      The phrase to update.
     * @param phraseCount The new usage count value.
     */
    public void updatePhraseCounter(PhraseType phraseType, String phrase, int phraseCount) {
        this.phrasesPanelsByType.get(phraseType).updatePhraseCounter(phrase, phraseCount);
    }

    /**
     * Setup the tabbed pane of phrase panels.
     */
    public void setupTabbedPane() {
        tabbedPane.addChangeListener(e -> {
            // Set text colour
            if (tabbedPane.getTabCount() > 0) {
                tabbedPane.setForeground(Color.BLACK);
            }

            // Set un-highlighted colour
            for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                tabbedPane.setBackgroundAt(i, Color.LIGHT_GRAY);
            }

            // Set selected colour
            if (tabbedPane.getSelectedIndex() >= 0) {
                tabbedPane.setBackgroundAt(tabbedPane.getSelectedIndex(), Color.BLUE);

                if (tabbedPane.getSelectedIndex() == 0) {
                    controller.setCurrentPhrasePanelInView(PhraseType.CUSTOM);
                } else if (tabbedPane.getSelectedIndex() == 1) {
                    controller.setCurrentPhrasePanelInView(PhraseType.FREQUENTLY_USED);
                } else {
                    controller.setCurrentPhrasePanelInView(PhraseType.INSIGHTS);
                }

            }
        });

        this.add(tabbedPane);
    }

    /**
     * Set the highlight of a pane.
     *
     * @param index The pane to highlight.
     */
    public void setHighlightedPane(int index) {
        if (index >= 0) {
            tabbedPane.setSelectedIndex(index);
        }
    }

    /**
     * Add an insight to the insights panel.
     *
     * @param linkedPhrases The linked phrases to display as an insight.
     */
    public void addInsightToInsightPanel(LinkedPhrases linkedPhrases) {
        phrasesPanelsByType.get(PhraseType.INSIGHTS).addInsightBox(linkedPhrases);
    }
}
