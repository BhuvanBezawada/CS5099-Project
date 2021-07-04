package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import java.awt.*;

public class FeedbackBox extends JPanel {

    private String heading;
    private JLabel headingLabel;
    private JTextArea textPane;
    private EditingPopupMenu editingPopupMenu;

    public FeedbackBox(String heading) {
        // Store heading
        this.heading = heading;

        // Setup components
        setupLabel();
        setupTextArea();

        // Layout components from top to bottom on this panel
        this.setLayout(new BorderLayout());

        // Add components to the panel
        this.add(headingLabel, BorderLayout.PAGE_START);
        this.add(textPane, BorderLayout.CENTER);

        // Add some padding to the bottom on the panel and make it visible
        this.setBorder(new EmptyBorder(0, 20, 20, 20));
        this.setVisible(true);
    }

    private void setupLabel() {
        this.headingLabel = new JLabel(heading, SwingConstants.LEFT);
        this.headingLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
    }

    private void setupTextArea() {
        this.textPane = new JTextArea();
        this.textPane.setRows(10);
        this.textPane.setBackground(Color.WHITE);
        this.textPane.setEditable(true);
        this.textPane.setLineWrap(true);
    }

    public JTextArea getTextPane() {
        return this.textPane;
    }

    public void registerPopupMenu(EditingPopupMenu editingPopupMenu) {
        editingPopupMenu.registerFeedbackBox(this);
    }
}
