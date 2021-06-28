package model;

import java.io.Serializable;
import java.util.List;

public class FeedbackDocument implements Serializable {

    private Assignment assignment;
    private String studentId;

    public FeedbackDocument(Assignment assignment, String studentId) {
        this.assignment = assignment;
        this.studentId = studentId;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public String getStudentId() {
        return studentId;
    }

    public List<String> getHeadings() {
        return assignment.getAssignmentConfig().getAssignmentHeadings();
    }

    @Override
    public String toString() {
        return "FeedbackDocument{" +
                "assignment=" + assignment.getAssignmentName() +
                ", studentId='" + studentId + '\'' +
                '}';
    }
}
