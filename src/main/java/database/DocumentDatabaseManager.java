package database;

import model.Assignment;
import model.FeedbackDocument;
import org.dizitart.no2.Cursor;
import org.dizitart.no2.Document;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.NitriteCollection;
import org.dizitart.no2.filters.Filters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        return openDocumentDatabase(databasePath);
    }

    public boolean closeDocumentDatabaseConnection() {
        if (!databaseConnection.isClosed()) {
            databaseConnection.close();
        }
        return databaseConnection.isClosed();
    }

    public boolean createFeedbackDocuments(Assignment assignment) {

        System.out.println("In createFeedbackDocuments: " + assignment.getDatabaseName() + "-feedback-docs");

        // Store the assignment's feedback documents into the database
        NitriteCollection assignmentCollection = databaseConnection.getCollection(assignment.getDatabaseCollectionName());

        assignment.getFeedbackDocuments().forEach( feedbackDocument -> {
            Document dbDoc = Document.createDocument("feedbackFor", feedbackDocument.getStudentId());
            dbDoc.put("feedbackDocObject", feedbackDocument);

            // Setup the headings of the document
            for (String heading : assignment.getAssignmentHeadings()) {
                dbDoc.put(heading, "");
            }

            dbDoc.put("grade", feedbackDocument.getGrade());

            assignmentCollection.insert(dbDoc);
        });

        // Commit the created documents
        databaseConnection.commit();

        return databaseConnection.hasCollection(assignment.getDatabaseName());
    }

    public List<FeedbackDocument> loadFeedbackDocumentsForAssignment(Assignment assignment) {

        if (documentDatabaseIsReady()) {
            String collectionName = assignment.getDatabaseCollectionName();
            System.out.println(databaseConnection.listCollectionNames());

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

                    feedbackDocument.setGrade((double) result.get("grade"));
                    feedbackDocuments.add(feedbackDocument);
                });
                return feedbackDocuments;
            } else {
                System.out.println("Collection not found :(");
            }
        }
        return null;
    }

    public boolean saveFeedbackDocument(Assignment assignment, String studentId, Map<String, String> headingsAndData, double grade) {
        if (databaseConnection.hasCollection(assignment.getDatabaseCollectionName())) {

            NitriteCollection collection = databaseConnection.getCollection(assignment.getDatabaseCollectionName());
            Cursor documents = collection.find(Filters.eq("feedbackFor", studentId));
            Document document = documents.toList().get(0);

            System.out.println("Doc found is: " + document);

            System.out.println("Going to save data: " + headingsAndData.keySet());
            System.out.println("Going to save data: " + headingsAndData.values());
            headingsAndData.keySet().forEach(heading -> {
                document.put(heading, headingsAndData.get(heading));
            });

            document.put("grade", grade);

            collection.update(document);
            databaseConnection.commit();
            return true;
        }
        return false;
    }

    public void updateAndStoreFeedbackDocument(Assignment assignment, String studentId) {
        if (databaseConnection.hasCollection(assignment.getDatabaseCollectionName())) {

            NitriteCollection collection = databaseConnection.getCollection(assignment.getDatabaseCollectionName());
            Cursor documents = collection.find(Filters.eq("feedbackFor", studentId));
            Document document = documents.toList().get(0);

            System.out.println("Doc found is: " + document);
            FeedbackDocument feedbackDocument = (FeedbackDocument) document.get("feedbackDocObject");

            feedbackDocument.getHeadings().forEach(heading -> {
                feedbackDocument.setDataForHeading(heading, (String) document.get(heading));
            });

            feedbackDocument.setGrade((double) document.get("grade"));

            assignment.setFeedbackDocument(studentId, feedbackDocument);
        }
    }

    public boolean updateFeedbackDocument(FeedbackDocument feedbackDocument) {
        return false;
    }
}
