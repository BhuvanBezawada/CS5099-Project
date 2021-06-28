package model;

import java.io.Serializable;

public class Assignment implements Serializable {

    private String assignmentName;
    private AssignmentConfig assignmentConfig;

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
}
