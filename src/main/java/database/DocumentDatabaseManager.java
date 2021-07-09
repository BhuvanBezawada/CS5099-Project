package database;

import model.Assignment;
import model.FeedbackDocument;
import org.dizitart.no2.Cursor;
import org.dizitart.no2.Document;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.NitriteCollection;

import java.util.ArrayList;
import java.util.List;

public class DocumentDatabaseManager {//implements IDocumentDatabase {

    private Nitrite databaseConnection;

    public NitriteCollection createCollection(String collectionName) {
        return databaseConnection.getCollection(collectionName);
    }

    public void saveChanges() {
        databaseConnection.commit();
    }


    public boolean documentDatabaseIsReady() {
        return !databaseConnection.isClosed();
    }

    public boolean openDocumentDatabase(String databasePath) {
        databaseConnection = Nitrite.builder()
                .compressed()
                .filePath(databasePath) // "./test.db"
                .openOrCreate("user", "password");

        return !databaseConnection.isClosed();
    }

    public boolean createDocumentDatabase(String databasePath) {
        return openDocumentDatabase(databasePath + ".db");
    }

    public boolean closeDocumentDatabaseConnection() {
        if (!databaseConnection.isClosed()) {
            databaseConnection.close();
        }
        return databaseConnection.isClosed();
    }

    public boolean createFeedbackDocuments(Assignment assignment) {

        System.out.println("In createFeedbackDocuments: " + assignment.getDatabaseFilePath() + "-feedback-docs");

        // Store the assignment's feedback documents into the database
        NitriteCollection assignmentCollection = databaseConnection.getCollection(assignment.getDatabaseFilePath() + "-feedback-docs");

        assignment.getFeedbackDocuments().forEach( feedbackDocument -> {
            Document dbDoc = Document.createDocument("feedbackFor", feedbackDocument.getStudentId());
            dbDoc.put("feedbackDocObject", feedbackDocument);

            // Setup the headings of the document
            for (String heading : assignment.getAssignmentHeadings()) {
                dbDoc.put(heading, "");
            }

            assignmentCollection.insert(dbDoc);
        });

        // Commit the created documents
        databaseConnection.commit();

        return databaseConnection.hasCollection(assignment.getDatabaseFilePath());
    }

    public List<FeedbackDocument> loadFeedbackDocumentsForAssignment(Assignment assignment) {

        if (documentDatabaseIsReady()) {
            String collectionName = assignment.getDatabaseFilePath() + "-feedback-docs";
            if (databaseConnection.hasCollection(collectionName)) {
                System.out.println("Found collection");
                NitriteCollection assignmentCollection = databaseConnection.getCollection(collectionName);
                Cursor results = assignmentCollection.find();

                System.out.println("Found " + results.size() + " results");

                List<FeedbackDocument> feedbackDocuments = new ArrayList<FeedbackDocument>();
                results.forEach(result -> {
                    FeedbackDocument feedbackDocument = (FeedbackDocument) result.get("feedbackDocObject");

                    // Assign data into feedback doc
                    assignment.getAssignmentHeadings().forEach(heading -> {
                        feedbackDocument.setDataForHeading(heading, (String) result.get(heading));
                    });

                    feedbackDocuments.add(feedbackDocument);
                });
                return feedbackDocuments;
            } else {
                System.out.println("Collection not found :(");
            }
        }
        return null;
    }

    public boolean saveFeedbackDocument(FeedbackDocument feedbackDocument) {
        return false;
    }

    public boolean updateFeedbackDocument(FeedbackDocument feedbackDocument) {
        return false;
    }
}
