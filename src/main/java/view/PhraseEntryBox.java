package view;

import controller.AppController;

import javax.swing.*;
import java.awt.*;

/**
 * Phrase Entry Box Class.
 */
public class PhraseEntryBox extends JPanel {

    // Instance variables
    private JTextArea textArea;
    private JButton submitButton;
    private final AppController controller;

    /**
     * Constructor.
     * @param controller The controller.
     */
    public PhraseEntryBox(AppController controller) {
        this.controller = controller;

        // Setup components
        this.setLayout(new BorderLayout());
        this.setupTextArea();
        this.setupSubmitButton();

        // Set border and visibility
        this.setBorder(BorderCreator.createAllSidesEmptyBorder(BorderCreator.PADDING_10_PIXELS));
        this.setVisible(true);
    }

    /**
     * Setup the text area.
     */
    private void setupTextArea() {
        this.textArea = new JTextArea();
        this.textArea.setBorder(BorderCreator.createAllSidesEmptyBorder(BorderCreator.PADDING_10_PIXELS));
        this.textArea.setLineWrap(true);
        this.textArea.setWrapStyleWord(true);
        this.add(textArea, BorderLayout.CENTER);
    }

    /**
     * Setup the submit button.
     */
    private void setupSubmitButton() {
        this.submitButton = new JButton(new ImageIcon(new ImageIcon(this.getClass().getResource("/submit_arrow.png")).getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT)));
        this.add(submitButton, BorderLayout.LINE_END);

        this.submitButton.addActionListener(l -> {
            String phrase = this.textArea.getText();
            controller.addNewCustomPhraseFromView(phrase);
            this.textArea.setText("");
        });
    }

    /**
     * Disable the phrase entry box.
     */
    public void disablePhraseEntryBox() {
        this.submitButton.setEnabled(false);
        this.textArea.setEditable(false);
        this.textArea.setCaretColor(Color.WHITE);
        repaint();
        revalidate();
    }

    /**
     * Enable the phrase entry box.
     */
    public void enablePhraseEntryBox() {
        this.submitButton.setEnabled(true);
        this.textArea.setEditable(true);
        this.textArea.setCaretColor(Color.BLACK);
        repaint();
        revalidate();
    }
}
