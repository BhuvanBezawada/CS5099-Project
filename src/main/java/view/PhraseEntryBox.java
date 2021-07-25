package view;

import controller.AppController;

import javax.swing.*;
import java.awt.*;

public class PhraseEntryBox extends JPanel {

    private JTextArea textArea;
    private JButton submitButton;
    private AppController controller;

    public PhraseEntryBox(AppController controller) {
        this.controller = controller;
        this.setLayout(new BorderLayout());
        this.textArea = new JTextArea();
        this.textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.textArea.setLineWrap(true);
        this.textArea.setWrapStyleWord(true);
        this.submitButton = new JButton(new ImageIcon(new ImageIcon(this.getClass().getResource("/submit_arrow.png")).getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT)));
        this.add(textArea, BorderLayout.CENTER);
        this.add(submitButton, BorderLayout.LINE_END);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10,10));

        setupSubmitButton();
        this.setVisible(true);
    }

    private void setupSubmitButton() {
        this.submitButton.addActionListener(l -> {
            String phrase = this.textArea.getText();
            controller.addNewPhrase(phrase);
            this.textArea.setText("");
        });
    }
}
