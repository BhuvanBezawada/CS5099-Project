package main;

import database.DocumentDatabaseManager;
import model.Assignment;
import model.AssignmentConfig;
import model.FeedbackDocument;
import org.dizitart.no2.Document;
import org.dizitart.no2.NitriteCollection;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseLoader {
    private String ASSIGNMENTS_DATABASE = "./assignments.db";

    public static void main(String[] args) {
        DatabaseLoader databaseLoader = new DatabaseLoader();
        //databaseLoader.saveSampleFilesIntoDatabase();
        //databaseLoader.loadSampleFilesFromDatabase();
    }

//    private void loadSampleFilesFromDatabase() {
//        // Open connection to the database
//        DocumentDatabaseManager documentDatabaseInterface = new DocumentDatabaseManager();
//        documentDatabaseInterface.openDocumentDatabase(ASSIGNMENTS_DATABASE);
//
//        // Assignment Details
//        AssignmentConfig assignmentConfig = new AssignmentConfig("/Users/bhuvan/Desktop/CS5099-Project/src/main/java/test_data/assignment_config.txt");
//        Assignment assignment = new Assignment(); //"db-assignment", assignmentConfig);
//
//        List<Document> documents = documentDatabaseInterface.loadFeedbackDocumentsForAssignment(assignment);
//        List<String> databaseCreationSection = (ArrayList<String>) documents.get(0).get("Database creation");
//        databaseCreationSection.forEach(System.out::println);
//
//        documentDatabaseInterface.closeDocumentDatabaseConnection();
//    }

    private void saveSampleFilesIntoDatabase() {
        // Assignment and student manifest
        AssignmentConfig assignmentConfig = new AssignmentConfig("/Users/bhuvan/Desktop/CS5099-Project/src/main/java/test_data/assignment_config.txt");
        Assignment assignment = new Assignment(); //"db-assignment", assignmentConfig);
        assignment.saveAssignmentDetails(assignment.getDatabaseFilePath() + ".fht");
        File studentManifestFile = new File("/Users/bhuvan/Desktop/CS5099-Project/src/main/java/test_data/student_manifest.txt");

        // Open connection to the database
        DocumentDatabaseManager documentDatabaseInterface = new DocumentDatabaseManager();
        documentDatabaseInterface.openDocumentDatabase(ASSIGNMENTS_DATABASE);

        // Create a collection of feedback documents for the assignment
        if (documentDatabaseInterface.documentDatabaseIsReady()){
            List<String> studentIds = new ArrayList<>();

            // Get the student ids
            try (BufferedReader manifestFileReader = new BufferedReader(new FileReader(studentManifestFile))) {
                while (manifestFileReader.ready()) {
                    studentIds.add(manifestFileReader.readLine().trim());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Student Ids: " + studentIds);

            // Create a collection of the feedback documents
            NitriteCollection feedbackDocsCollections = documentDatabaseInterface.createCollection(assignment.getDatabaseFilePath());

            studentIds.forEach( studentId -> {
                try (BufferedReader studentFileReader = new BufferedReader(new FileReader("/Users/bhuvan/Desktop/CS5099-Project/src/main/java/test_data/" + studentId + ".txt"))) {
                    Document dbDoc = Document.createDocument("studentId", studentId);
                    FeedbackDocument feedbackDocument = new FeedbackDocument(assignment, studentId);
                    dbDoc.put("feedbackDocumentObject", feedbackDocument);

                    List<String> headings = feedbackDocument.getHeadings();
                    String currentHeading = "";
                    List<String> currentHeadingSection = new ArrayList<>();

                    while (studentFileReader.ready()) {
                        try {
                            String line = studentFileReader.readLine();
                            if (headings.contains(line)) {
                                System.out.println("Done: " + currentHeading);
                                System.out.println(currentHeadingSection);

                                // Save section to db
                                dbDoc.put(currentHeading, currentHeadingSection);

                                currentHeading = line;
                                currentHeadingSection = new ArrayList<>();
                            } else {
                                currentHeadingSection.add(line);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    // Save last section
                    dbDoc.put(currentHeading, currentHeadingSection);
                    feedbackDocsCollections.insert(dbDoc);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        // Commit the changes
        documentDatabaseInterface.saveChanges();
        documentDatabaseInterface.closeDocumentDatabaseConnection();
    }
}
