package view;

import controller.IAppController;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Grade Box Class.
 */
public class GradeBox extends JPanel {

    // Class variable
    final static private String GRADE = "Grade: ";

    // Instance variables
    private final IAppController controller;
    private JLabel gradeLabel;
    private JTextField textField;
    private JButton confirmButton;
    private String studentId;

    /**
     * Constructor.
     *
     * @param controller The controller.
     */
    public GradeBox(IAppController controller) {
        this.controller = controller;

        // Setup components
        this.setupLabel();
        this.setupTextField();
        this.setupConfirmButton();

        // Add some padding to the bottom on the panel and make it visible
        this.setBorder(BorderCreator.createEmptyBorderLeavingTop(BorderCreator.PADDING_20_PIXELS));

        // Set visibility
        this.setVisible(true);
    }

    /**
     * Setup the label.
     */
    private void setupLabel() {
        this.gradeLabel = new JLabel(GRADE);
        this.add(this.gradeLabel);
    }

    /**
     * Setup the text field.
     */
    private void setupTextField() {
        this.textField = new JTextField(10);
        this.textField.setEditable(true);
        this.add(this.textField);
    }

    /**
     * Setup the confirm button.
     */
    private void setupConfirmButton() {
        this.confirmButton = new JButton("Confirm");
        this.confirmButton.addActionListener(l -> {
            if (getGrade() > 0) {
                JOptionPane.showMessageDialog(this.confirmButton, "Saving grade of " + getGrade() + " for student " + this.studentId);
                this.controller.saveFeedbackDocument(this.studentId);
            }
        });
        this.add(this.confirmButton);
    }

    /**
     * Get the grade value.
     *
     * @return The grade value.
     */
    public double getGrade() {
        // Get the string version of the grade
        String gradeAsString = this.textField.getText();
        double grade = 0.0;

        // Return 0.0 if it is empty
        if (gradeAsString.isEmpty()) {
            return grade;
        }

        // Parse string into a valid double
        try {
            grade = Double.parseDouble(gradeAsString);
            if (grade < 0.0 || grade > 20.0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            this.controller.error("Grade must be a real number from 0.0 to 20.0!");
            setGrade(0.0);
            return -1.0;
        }

        return grade;
    }

    /**
     * Set the grade on the UI.
     *
     * @param grade The grade value to set.
     */
    public void setGrade(double grade) {
        this.textField.setText(String.valueOf(grade));
    }

    /**
     * Set the student ID that the grade is for.
     *
     * @param studentId The student ID that the grade is for.
     */
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

}
