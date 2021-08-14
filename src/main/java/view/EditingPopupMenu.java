package view;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Editing Popup Menu Class.
 */
public class EditingPopupMenu {

    // Instance variables
    private JPopupMenu popupMenu;
    private String selectedText;
    private JMenuItem copy = new JMenuItem("Copy");
    private JMenuItem paste = new JMenuItem("Paste");

    /**
     * Constructor.
     */
    public EditingPopupMenu() {
        setupPopupMenu();
    }

    /**
     * Setup the popup menu.
     */
    private void setupPopupMenu() {
        this.popupMenu = new JPopupMenu();

        popupMenu.add(copy);
        setupCopyOperation();

        popupMenu.add(paste);
        setupPasteOperation();
    }

    /**
     * Register the popup menu with a given feedback box.
     * @param feedbackBox The feedback box to register.
     */
    public void registerFeedbackBox(FeedbackBox feedbackBox) {
        feedbackBox.getTextArea().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    popupMenu.setInvoker(feedbackBox.getTextArea());
                    popupMenu.show(feedbackBox.getTextArea(), e.getX(), e.getY());
                }
            }
        });
    }

    /**
     * Setup the copy operation.
     */
    private void setupCopyOperation() {
        copy.addActionListener(e -> {
            selectedText = ((JTextArea) popupMenu.getInvoker()).getSelectedText();
        });
    }

    /**
     * Setup the paste operation.
     */
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
}
