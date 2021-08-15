package view;

import controller.IAppController;
import model.LinkedPhrases;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Phrases Section Class.
 */
public class PhrasesSection extends JPanel {

    // Instance variables
    private final IAppController controller;
    private JTabbedPane tabbedPane;
    private Map<PhraseType, PhrasesPanel> phrasesPanelsByType;

    /**
     * Constructor.
     *
     * @param controller The controller.
     */
    public PhrasesSection(IAppController controller) {
        this.controller = controller;
        this.tabbedPane = new JTabbedPane();
        this.phrasesPanelsByType = new HashMap<PhraseType, PhrasesPanel>();

        // Set layout, pane and visibility
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
        this.phrasesPanelsByType.put(phrasesPanel.getPhraseType(), phrasesPanel);
        this.tabbedPane.addTab(phrasesPanel.getPhraseType().getPhraseTypeAsString(), phrasesPanelScrollPane);
        this.updatePhrasesSection();
    }

    /**
     * Reset a phrases panel.
     *
     * @param phraseType The type of the panel to reset.
     */
    public void resetPhrasesPanel(PhraseType phraseType) {
        this.phrasesPanelsByType.get(phraseType).clearPanel();
        this.updatePhrasesSection();
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
        this.tabbedPane.addChangeListener(l -> {
            // Set text colour
            if (this.tabbedPane.getTabCount() > 0) {
                this.tabbedPane.setForeground(Color.BLACK);
            }

            // Set un-highlighted colour
            for (int i = 0; i < this.tabbedPane.getTabCount(); i++) {
                this.tabbedPane.setBackgroundAt(i, Color.LIGHT_GRAY);
            }

            // Set selected colour
            if (this.tabbedPane.getSelectedIndex() >= 0) {
                this.tabbedPane.setBackgroundAt(this.tabbedPane.getSelectedIndex(), Color.BLUE);

                if (this.tabbedPane.getSelectedIndex() == 0) {
                    this.controller.setCurrentPhrasePanelInView(PhraseType.CUSTOM);
                } else if (tabbedPane.getSelectedIndex() == 1) {
                    this.controller.setCurrentPhrasePanelInView(PhraseType.FREQUENTLY_USED);
                } else {
                    this.controller.setCurrentPhrasePanelInView(PhraseType.INSIGHTS);
                }
            }
        });

        this.add(this.tabbedPane);
    }

    /**
     * Set the highlight of a pane.
     *
     * @param index The pane to highlight.
     */
    public void setHighlightedPane(int index) {
        if (index >= 0) {
            this.tabbedPane.setSelectedIndex(index);
        }
    }

    /**
     * Add an insight to the insights panel.
     *
     * @param linkedPhrases The linked phrases to display as an insight.
     */
    public void addInsightToInsightPanel(LinkedPhrases linkedPhrases) {
        this.phrasesPanelsByType.get(PhraseType.INSIGHTS).addInsightBox(linkedPhrases);
    }

}
