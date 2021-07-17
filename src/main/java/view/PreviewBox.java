package view;

import controller.AppController;
import model.Assignment;
import model.FeedbackDocument;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class PreviewBox extends JPanel {

    private String heading;
    private String uniqueLine;
    private int grade;
    private JTextArea textPane;

    private Border unselectedBorder;
    private Border selectedBorder;

    private AppController controller;
    private Assignment assignment;

    public PreviewBox(AppController controller, String heading, int grade, String uniqueLine) {
        this.controller = controller;

        // Store variables
        this.heading = heading;
        this.uniqueLine = uniqueLine;
        this.grade = grade;

        // Setup components
        setupBorders();
        setupTextArea();

        // Layout components from top to bottom on this panel
        this.setLayout(new BorderLayout());

        // Add components to the panel
        this.add(textPane, BorderLayout.CENTER);

        // Add some padding to the bottom on the panel and make it visible
        this.setBorder(new EmptyBorder(20, 20, 0, 20));
        this.setVisible(true);
    }

    private void setupBorders() {
        this.unselectedBorder = new CompoundBorder(
                new MatteBorder(1, 5, 1, 1, Color.LIGHT_GRAY),
                new EmptyBorder(10, 10, 10, 10)
        );

        this.selectedBorder = new CompoundBorder(
                new MatteBorder(1, 5, 1, 1, Color.GREEN),
                new EmptyBorder(10, 10, 10, 10)
        );
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    private void setupTextArea() {
        this.textPane = new JTextArea();
        this.textPane.setRows(5);
        this.textPane.setBackground(Color.WHITE);
        this.textPane.setBorder(unselectedBorder);
        this.textPane.setEditable(false);
        this.textPane.setLineWrap(false);

        // Listen for clicks on the text area
        this.textPane.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                //highlight();
                controller.displayNewDocument(assignment, heading);
            }

            @Override
            public void focusLost(FocusEvent e) {
                //controller.saveFeedbackDocument(heading);
                //unhighlight();
            }
        });

        this.textPane.setText(heading + "\n\n" + uniqueLine + "\n" + "Grade: " + grade);
    }

    public String getHeading() {
        return this.heading;
    }

    public void highlight() {
        textPane.setBorder(selectedBorder);
        textPane.repaint();
        textPane.revalidate();
    }

    public void unhighlight() {
        textPane.setBorder(unselectedBorder);
        textPane.repaint();
        textPane.revalidate();
    }
}
