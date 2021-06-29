package model;

import java.io.*;

public class Assignment implements Serializable {

    private String assignmentName;
    private AssignmentConfig assignmentConfig;
    private static final long serialVersionUID = 1200109309800080026L;

    public Assignment(String assignmentName, AssignmentConfig assignmentConfig) {
        this.assignmentName = assignmentName;
        this.assignmentConfig = assignmentConfig;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public AssignmentConfig getAssignmentConfig() {
        return assignmentConfig;
    }

    public void saveAssignmentDetails(String fileName) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Assignment loadAssignment(String filePath) {
        Assignment loadedAssignment = null;
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
             loadedAssignment = (Assignment) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return loadedAssignment;
    }
}
