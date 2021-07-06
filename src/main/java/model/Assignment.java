package model;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Assignment implements Serializable {

    private String assignmentName;
    private AssignmentConfig assignmentConfig;

    private List<String> studentIds;
    private String assignmentTitle;
    private List<String> assignmentHeadings;

    public Assignment() {
        this.studentIds = new ArrayList<String>();
        this.assignmentHeadings = new ArrayList<String>();
    }

    public void setStudentIds(File studentManifestFile) {
        System.out.println("Loading file: " + studentManifestFile);
        this.studentIds = new ArrayList<String>();

        try (BufferedReader reader = new BufferedReader(new FileReader(studentManifestFile))) {
            while (reader.ready()) {
                this.studentIds.add(reader.readLine().trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Student ids: " + studentIds);
    }

    public String getAssignmentTitle() {
        return assignmentTitle;
    }

    public void setAssignmentTitle(String assignmentTitle) {
        this.assignmentTitle = assignmentTitle;
    }

    public List<String> getAssignmentHeadings() {
        return assignmentHeadings;
    }

    public void setAssignmentHeadings(String assignmentHeadings) {
        this.assignmentHeadings = new ArrayList<String>(Arrays.asList(assignmentHeadings.split("\n")));
        System.out.println("Converting to list: " + this.assignmentHeadings);
    }

    private static final long serialVersionUID = 1200109309800080026L;

    public Assignment(String assignmentName, AssignmentConfig assignmentConfig) {
        this.assignmentName = assignmentName;
        this.assignmentConfig = assignmentConfig;
        this.studentIds = new ArrayList<String>();
        this.studentIds = new ArrayList<String>();
    }

    public List<String> getStudentIds() {
        return studentIds;
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
