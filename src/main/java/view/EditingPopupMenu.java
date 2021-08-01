package view;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class EditingPopupMenu {

    private JPopupMenu popupMenu;
    private String selectedText;
    private Map<PhraseType, PhrasesPanel> phrasesPanelsMap;

    private JMenuItem cut = new JMenuItem("Cut");
    private JMenuItem copy = new JMenuItem("Copy");
    private JMenuItem paste = new JMenuItem("Paste");
    private JMenuItem addPhrase = new JMenuItem("Add phrase to bank");
    private JMenuItem excludePhrase = new JMenuItem("Exclude phrase from bank");

    public EditingPopupMenu() {
        this.popupMenu = new JPopupMenu();
        this.phrasesPanelsMap = new HashMap<PhraseType, PhrasesPanel>();
        setupPopupMenu();
    }

    private void setupPopupMenu() {
        popupMenu.add(cut);

        popupMenu.add(copy);
        setupCopyOperation();

        popupMenu.add(paste);
        setupPasteOperation();

        popupMenu.addSeparator();

        popupMenu.add(addPhrase);
        setupAddPhraseOperation();

        popupMenu.add(excludePhrase);
    }

    public void registerFeedbackBox(FeedbackBox feedbackBox) {
        feedbackBox.getTextPane().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    popupMenu.setInvoker(feedbackBox.getTextPane());
                    popupMenu.show(feedbackBox.getTextPane(), e.getX(), e.getY());
                }
            }
        });

        System.out.println("added listener");
    }

    public void registerPhrasesPanel(PhrasesPanel phrasesPanel) {
        phrasesPanelsMap.put(phrasesPanel.getPhraseType(), phrasesPanel);
    }

    private void setupCopyOperation() {
        copy.addActionListener(e -> {
            selectedText = ((JTextArea) popupMenu.getInvoker()).getSelectedText();
            System.out.println("Copying: " + selectedText);
        });
    }

    private void setupPasteOperation() {
        paste.addActionListener(e -> {
            try {
                JTextArea invoker = (JTextArea) popupMenu.getInvoker();
                invoker.getDocument().insertString(invoker.getCaretPosition(), selectedText, null);
            } catch (BadLocationException badLocationException) {
                badLocationException.printStackTrace();
            }
        });
    }

    private void setupAddPhraseOperation() {
        addPhrase.addActionListener(e -> {
            JTextArea invoker = (JTextArea) popupMenu.getInvoker();
            selectedText = invoker.getSelectedText();
            phrasesPanelsMap.get(PhraseType.CUSTOM).addPhrase(selectedText, 1);
        });
    }
}
