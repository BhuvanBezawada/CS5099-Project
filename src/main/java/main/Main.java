package main;


import database.IDocumentDatabase;
import database.DocumentDatabaseManager;
import model.Assignment;
import model.AssignmentConfig;
import org.dizitart.no2.Document;

import java.io.File;
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
        Assignment assignment = new Assignment(); //"db-assignment", assignmentConfig);
        File studentManifestFile = new File("/Users/bhuvan/Desktop/CS5099-Project/src/main/java/test_data/student_manifest.txt");

        // Open connection to the database
        DocumentDatabaseManager documentDatabase = new DocumentDatabaseManager();
        documentDatabase.openDocumentDatabase(ASSIGNMENTS_DATABASE);

//        // Create a collection of feedback documents for the assignment
//        if (documentDatabase.documentDatabaseIsReady()){
//            documentDatabase.createFeedbackDocuments(assignment, studentManifestFile);
////            feedbackDocuments.forEach(System.out::println);
//            //System.out.println("Created " + feedbackDocuments.size() + " feedback docs for " + assignment.getAssignmentName());
//
//            System.out.println("Retrieving data: ");
//            List<Document> docs = documentDatabase.loadFeedbackDocumentsForAssignment(assignment);
//            System.out.println(docs);
//        }
    }

}
