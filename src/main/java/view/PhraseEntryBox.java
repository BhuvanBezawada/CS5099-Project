package view;

import controller.IAppController;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;

/**
 * Phrase Entry Box Class.
 */
public class PhraseEntryBox extends JPanel {

    // Instance variables
    private final IAppController controller;
    private JTextArea textArea;
    private JButton submitButton;

    /**
     * Constructor.
     *
     * @param controller The controller.
     */
    public PhraseEntryBox(IAppController controller) {
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
        this.add(this.textArea, BorderLayout.CENTER);
    }

    /**
     * Setup the submit button.
     */
    private void setupSubmitButton() {
        this.submitButton = new JButton(new ImageIcon(new ImageIcon(this.getClass().getResource("/submit_arrow.png")).getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT)));
        this.add(this.submitButton, BorderLayout.LINE_END);

        this.submitButton.addActionListener(l -> {
            String phrase = this.textArea.getText();
            this.controller.addNewCustomPhraseFromView(phrase);
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

        // Refresh the UI
        this.repaint();
        this.revalidate();
    }

    /**
     * Enable the phrase entry box.
     */
    public void enablePhraseEntryBox() {
        this.submitButton.setEnabled(true);
        this.textArea.setEditable(true);
        this.textArea.setCaretColor(Color.BLACK);

        // Refresh the UI
        this.repaint();
        this.revalidate();
    }

}
