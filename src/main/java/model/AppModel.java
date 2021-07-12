package model;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AppModel implements IAppModel {

    private String currentStudentId;
    private String lastStudentId;

    private PropertyChangeSupport subscribers;

    private Assignment assignment;

    public AppModel() {
        this.subscribers = new PropertyChangeSupport(this);
    }

    /*
     * Methods for observers of the model.
     */

    public void subscribe(PropertyChangeListener listener) {
        subscribers.addPropertyChangeListener(listener);
        System.out.println("Got a new subscriber!");
    }

    public void unsubscribe(PropertyChangeListener listener) {
        subscribers.removePropertyChangeListener(listener);
    }

    public void notifySubscribers(String property, String notification) {
        subscribers.firePropertyChange(property, "oldValue", notification);
    }



    /* Create model elements from the controller, requested by GUI */

    public Assignment createAssignment(String assignmentTitle, String assignmentHeadings, File studentManifestFile) {
        // Create assignment object
        Assignment assignment = new Assignment();
        assignment.setAssignmentTitle(assignmentTitle);
        assignment.setAssignmentHeadings(assignmentHeadings);
        assignment.setStudentIds(studentManifestFile);

        // Once an assignment is created, notify the observers
        notifySubscribers("assignment", "created");

        return assignment;
    }

    public Assignment loadAssignment(String assignmentFilePath) {
        return Assignment.loadAssignment(assignmentFilePath);
    }

    public String getAssignmentDatabaseName(String assignmentTitle) {
        return Assignment.getAssignmentDatabaseName(assignmentTitle);
    }

    public void setCurrentScreenView(String studentId) {
        this.lastStudentId = this.currentStudentId;
        this.currentStudentId = studentId;
        notifySubscribers("docViewChange", studentId);
    }

    public String getLastScreenView() {
        return lastStudentId;
    }

    public String getCurrentScreenView() {
        return currentStudentId;
    }

    public void exportFeedbackDocuments(Assignment assignment) {
        File outputDirectory = new File(assignment.getAssignmentTitle().trim().replace(" ", "-"));
        if (!outputDirectory.exists()) {
            outputDirectory.mkdir();
        }

        assignment.getFeedbackDocuments().forEach(feedbackDocument -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputDirectory + "/" + feedbackDocument.getStudentId() + ".txt"))) {
                feedbackDocument.getHeadings().forEach(heading -> {
                    try {
                        writer.write(heading);
                        writer.newLine();
                        writer.write(feedbackDocument.getHeadingData(heading));
                        writer.newLine();
                        writer.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
