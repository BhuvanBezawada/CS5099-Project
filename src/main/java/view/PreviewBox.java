package view;

import controller.AppController;
import model.Assignment;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Preview Box Class.
 */
public class PreviewBox extends JPanel implements Comparable<PreviewBox> {

    // Instance variables
    private String heading;
    private String firstLine;
    private double grade;
    private JTextArea textPane;
    private Border unselectedBorder;
    private Border selectedBorder;
    private AppController controller;
    private Assignment assignment; // TODO is assignment needed here?

    /**
     * Constructor.
     *
     * @param controller The controller.
     * @param heading    The heading of the preview box (usually student ID)
     * @param grade      The grade of the student.
     * @param firstLine A unique line from the student's feedback document.
     */
    public PreviewBox(AppController controller, String heading, double grade, String firstLine) {
        // Store variables
        this.controller = controller;
        this.heading = heading;
        this.firstLine = firstLine;
        this.grade = grade;

        // Layout components from top to bottom on this panel
        this.setLayout(new BorderLayout());

        // Setup components
        setupBorders();
        setupTextArea();

        // Add some padding to the bottom on the panel and make it visible
        this.setBorder(BorderCreator.createEmptyBorderLeavingBottom(BorderCreator.PADDING_20_PIXELS));
        this.setVisible(true);
    }

    /**
     * Setup the borders for when a box is selected and unselected.
     */
    private void setupBorders() {
        this.unselectedBorder = BorderCreator.unselectedBorder();
        this.selectedBorder = BorderCreator.selectedBorder();
    }

    /**
     * Set the assignment.
     *
     * @param assignment The assignment. // TODO is this really needed here?
     */
    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    /**
     * Setup the text area.
     */
    private void setupTextArea() {
        // Set properties
        this.textPane = new JTextArea();
        this.textPane.setRows(5);
        this.textPane.setBackground(Color.WHITE);
        this.textPane.setBorder(unselectedBorder);
        this.textPane.setEditable(false);
        this.textPane.setLineWrap(false);

        // Listen for clicks on the text area
        this.textPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.displayNewDocument(assignment, heading);
            }
        });

        // Set the contents of the preview box
        this.textPane.setText(heading + "\n\n" + firstLine + "\n\n" + "Grade: " + grade);
        this.add(textPane, BorderLayout.CENTER);
    }

    /**
     * Get the heading of the preview box.
     *
     * @return The heading of the preview box.
     */
    public String getHeading() {
        return this.heading;
    }

    /**
     * Highlight the preview box.
     */
    public void highlight() {
        textPane.setBorder(selectedBorder);
        textPane.repaint();
        textPane.revalidate();
    }

    /**
     * Unhighlight the preview box.
     */
    public void unhighlight() {
        textPane.setBorder(unselectedBorder);
        textPane.repaint();
        textPane.revalidate();
    }

    /**
     * Set the grade in the preview box.
     *
     * @param grade The grade to display.
     */
    public void setGrade(double grade) {
        this.grade = grade;
        updatePreviewBox();
    }

    /**
     * Set the unique line in the preview box.
     *
     * @param line The unique line to display.
     */
    public void setFirstLine(String line) {
        this.firstLine = line;
        updatePreviewBox();
    }

    /**
     * Update the preview box.
     */
    private void updatePreviewBox() {
        System.out.println("IN UPDATE PREVIEW BOX for: " + heading + " line " + firstLine);
        this.textPane.setText("");
        this.textPane.setText(heading + "\n\n" + firstLine + "\n\n" + "Grade: " + grade);
        textPane.repaint();
        textPane.revalidate();
    }

    /**
     * Compare the preview box to another preview box. Used to order the boxes.
     *
     * @param o The preview box to compare with.
     * @return An integer >0, <0 or equal to 0.
     */
    @Override
    public int compareTo(PreviewBox o) {
        try {
            int thisNum = Integer.parseInt(heading);
            int otherNum = Integer.parseInt(o.getHeading());
            return thisNum - otherNum;
        } catch (NumberFormatException | ClassCastException e) {
            return 0;
        }
    }
}
