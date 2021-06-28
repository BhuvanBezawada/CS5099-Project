package main;


import database.DocumentDatabaseInterface;
import database.DocumentDatabaseManager;
import model.Assignment;
import model.AssignmentConfig;
import model.FeedbackDocument;
import org.dizitart.no2.Document;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private String ASSIGNMENTS_DATABASE = "./assignments.db";

    public static void main(String[] args) {
        System.out.println("CS 5099 Project - Feedback Helper Tool");
        Main mainClass = new Main();
        mainClass.testDocumentDatabase();
    }

    public void testDocumentDatabase() {
        // Assignment and student manifest
        AssignmentConfig assignmentConfig = new AssignmentConfig("/Users/bhuvan/Desktop/CS5099-Project/src/main/java/test_data/assignment_config.txt");
        Assignment assignment = new Assignment("db-assignment", assignmentConfig);
        File studentManifestFile = new File("/Users/bhuvan/Desktop/CS5099-Project/src/main/java/test_data/student_manifest.txt");

        // Open connection to the database
        DocumentDatabaseInterface documentDatabaseInterface = new DocumentDatabaseManager();
        documentDatabaseInterface.openDocumentDatabaseConnection(ASSIGNMENTS_DATABASE);

        // Create a collection of feedback documents for the assignment
        if (documentDatabaseInterface.documentDatabaseIsReady()){
            documentDatabaseInterface.createFeedbackDocuments(assignment, studentManifestFile);
//            feedbackDocuments.forEach(System.out::println);
            //System.out.println("Created " + feedbackDocuments.size() + " feedback docs for " + assignment.getAssignmentName());

            System.out.println("Retrieving data: ");
            List<Document> docs = documentDatabaseInterface.loadFeedbackDocumentsForAssignment(assignment);
            System.out.println(docs);
        }
    }

}
