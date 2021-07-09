package controller;

import database.DocumentDatabaseManager;
import model.AppModel;
import model.Assignment;
import model.FeedbackDocument;

import java.io.File;
import java.util.List;

public class AppController {

    AppModel appModel;
    DocumentDatabaseManager documentDatabase;

    public AppController(AppModel appModel) {
        this.appModel = appModel;
        this.documentDatabase = new DocumentDatabaseManager();
    }

    public Assignment createAssignment(String assignmentTitle, String headings, File studentManifestFile) {
        // Create assignment in the model
        Assignment assignment = appModel.createAssignment(assignmentTitle, headings, studentManifestFile);

        // Create the assignment database
        documentDatabase.createDocumentDatabase(assignment.getDatabaseFilePath());

        // Create the feedback files for the assignment in the database
        documentDatabase.createFeedbackDocuments(assignment);

        System.out.println("Assignment: " + assignment.getDatabaseFilePath());
        return assignment;
    }

    public Assignment loadAssignment(String assignmentFilePath) {
        Assignment assignment = appModel.loadAssignment(assignmentFilePath);
        documentDatabase.openDocumentDatabase(assignment.getDatabaseFilePath());
        return assignment;
    }

    public void saveAssignment(Assignment assignment, String fileName) {
        assignment.saveAssignmentDetails(fileName);
    }

    public void loadFeedbackDocuments(Assignment assignment) {
        List<FeedbackDocument> feedbackDocumentList = documentDatabase.loadFeedbackDocumentsForAssignment(assignment);
        System.out.println("Got back " + feedbackDocumentList.size() + " documents from db");
    }

    public void getFeedbackDocument(Assignment assignment, String studentId) {

    }

    public void saveFeedbackDocument(Assignment assignment, String studentId) {

    }

}
