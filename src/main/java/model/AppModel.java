package model;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;

public class AppModel implements IAppModel {

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

}
