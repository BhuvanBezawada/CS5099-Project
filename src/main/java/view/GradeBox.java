package view;

import controller.AppController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;

public class GradeBox extends JPanel {

    final static private String GRADE = "Grade: ";

    private JLabel gradeLabel;
    private JTextField textField;
    private JButton confirmButton;
    private AppController controller;
    private String studentId;

    public GradeBox(AppController controller) {
        this.controller = controller;
        this.gradeLabel = new JLabel(GRADE);
        this.textField = new JTextField(10);
        this.textField.setEditable(true);
        this.confirmButton = new JButton("Confirm");
        this.confirmButton.addActionListener(l -> {
            if (getGrade() > 0) {
                JOptionPane.showMessageDialog(confirmButton, "Saving grade of " + getGrade() + " for student " + studentId);
                controller.saveFeedbackDocument(studentId);
            }
        });

        this.add(gradeLabel);
        this.add(textField);
        this.add(confirmButton);

        // Add some padding to the bottom on the panel and make it visible
        this.setBorder(new EmptyBorder(0, 20, 20, 20));

        this.setVisible(true);
    }

    public double getGrade() {
        String gradeAsString = this.textField.getText();
        double grade = 0.0;

        if (gradeAsString.isEmpty()) {
            return grade;
        }

        try {
            grade = Double.parseDouble(this.textField.getText());
            if (grade < 0.0 || grade > 20.0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            controller.error("Grade must be a real number from 0.0 to 20.0!");
            setGrade(0.0);
            return -1.0;
        }

        return grade;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setGrade(double grade) {
        this.textField.setText(String.valueOf(grade));
    }
}
