package database;

import model.Assignment;
import model.FeedbackDocument;
import org.dizitart.no2.Cursor;
import org.dizitart.no2.Document;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.NitriteCollection;
import org.dizitart.no2.objects.ObjectRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentDatabaseManager implements DocumentDatabaseInterface {

    private Nitrite databaseConnection;

    public NitriteCollection createCollection(String collectionName) {
        return databaseConnection.getCollection(collectionName);
    }

    public void saveChanges() {
        databaseConnection.commit();
    }

    @Override
    public boolean documentDatabaseIsReady() {
        return !databaseConnection.isClosed();
    }

    @Override
    public boolean openDocumentDatabaseConnection(String databasePath) {
        databaseConnection = Nitrite.builder()
                .compressed()
                .filePath(databasePath) // "./test.db"
                .openOrCreate("user", "password");

        return !databaseConnection.isClosed();
    }

    @Override
    public boolean closeDocumentDatabaseConnection() {
        if (!databaseConnection.isClosed()) {
            databaseConnection.close();
        }
        return databaseConnection.isClosed();
    }

    @Override
    public boolean createFeedbackDocuments(Assignment assignment, File studentManifest) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(studentManifest))){
            // Read student data
            List<String> studentIds = new ArrayList<String>();
            while (fileReader.ready()) {
                studentIds.add(fileReader.readLine().replace(".txt", "").trim());
            }

            // Create feedback documents
            List<FeedbackDocument> feedbackDocuments = new ArrayList<FeedbackDocument>();
            studentIds.forEach( studentId -> feedbackDocuments.add(new FeedbackDocument(assignment, studentId)));

            // Store them into the database
            NitriteCollection assignmentCollection = databaseConnection.getCollection(assignment.getAssignmentName());
            feedbackDocuments.forEach( feedbackDocument -> {
                Document dbDoc = Document.createDocument("feedbackFor", feedbackDocument.getStudentId());
                dbDoc.put("feedbackDocObject", feedbackDocument);

                for (String heading : assignment.getAssignmentConfig().getAssignmentHeadings()) {
                    dbDoc.put(heading, "");
                }

                assignmentCollection.insert(dbDoc);
            });

            // Commit the created documents
            databaseConnection.commit();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return databaseConnection.hasCollection(assignment.getAssignmentName());
    }

    @Override
    public List<Document> loadFeedbackDocumentsForAssignment(Assignment assignment) {
        if (documentDatabaseIsReady()) {
            NitriteCollection assignmentCollection = databaseConnection.getCollection(assignment.getAssignmentName());
            Cursor results = assignmentCollection.find();
            return results.toList();
        }
        return null;
    }

    @Override
    public boolean saveFeedbackDocument(FeedbackDocument feedbackDocument) {
        return false;
    }

    @Override
    public boolean updateFeedbackDocument(FeedbackDocument feedbackDocument) {
        return false;
    }
}
