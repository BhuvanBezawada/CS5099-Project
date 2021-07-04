package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PhrasesSection extends JPanel {

    private JTabbedPane tabbedPane;
    private List<JScrollPane> phrasesPanelsScrollPanes;

    public PhrasesSection() {
        this.tabbedPane = new JTabbedPane();
        this.phrasesPanelsScrollPanes = new ArrayList<JScrollPane>();

        setupTabbedPane();

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.add(tabbedPane);
        this.setVisible(true);
    }

    public void addPhrasesPanel(PhrasesPanel phrasesPanel) {
        JScrollPane phrasesPanelScrollPane = new JScrollPane(phrasesPanel);
        phrasesPanelsScrollPanes.add(phrasesPanelScrollPane);
        tabbedPane.addTab(phrasesPanel.getPhraseType().getPhraseTypeAsString(), phrasesPanelScrollPane);
        updatePhrasesSection();
    }

    private void updatePhrasesSection() {
        this.revalidate();
        this.repaint();
    }

    public void setupTabbedPane() {
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getTabCount() > 0) {
                tabbedPane.setForeground(Color.BLACK);
            }

            for (int i = 0; i< tabbedPane.getTabCount(); i++) {
                tabbedPane.setBackgroundAt(i, Color.LIGHT_GRAY);
            }

            if (tabbedPane.getSelectedIndex() >= 0) {
                tabbedPane.setBackgroundAt(tabbedPane.getSelectedIndex(), Color.BLUE);
            }
        });
    }

}
