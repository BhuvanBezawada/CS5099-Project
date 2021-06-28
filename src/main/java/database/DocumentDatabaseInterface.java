package database;

import model.Assignment;
import model.FeedbackDocument;
import org.dizitart.no2.Document;

import java.io.File;
import java.util.List;

/**
 * Document Database Interface.
 * <p>
 * Allows application to open/close the embedded database and perform CRUD operations.
 */
public interface DocumentDatabaseInterface {

    boolean documentDatabaseIsReady();

    /**
     * Open a connection to the embedded document database.
     *
     * @param databasePath The path of the required database file.
     * @return True if connection was successful, False otherwise.
     */
    boolean openDocumentDatabaseConnection(String databasePath);

    /**
     * Close a connection to the embedded document database.
     *
     * @return True if connection was closed successfully, False otherwise.
     */
    boolean closeDocumentDatabaseConnection();

    /**
     * Create feedback documents for a given assignment.
     *
     * @param assignment      - The assignment the feedback documents are for.
     * @param studentManifest - A file containing the matriculation numbers of students to be marked.
     * @return True if documents were created successfully, False otherwise.
     */
    boolean createFeedbackDocuments(Assignment assignment, File studentManifest);

    /**
     * Load all the feedback documents for a given assignment.
     *
     * @param assignment - The assignment the feedback document belongs to.
     * @return A list of the feedback documents belonging to the assignment.
     */
    List<Document> loadFeedbackDocumentsForAssignment(Assignment assignment);

    /**
     * Save a feedback document to the database.
     *
     * @param feedbackDocument - The feedback document to be saved.
     * @return True if the save was successful, false otherwise.
     */
    boolean saveFeedbackDocument(FeedbackDocument feedbackDocument);

    /**
     * Update a feedback document on the database.
     *
     * @param feedbackDocument - The feedback document to be updated.
     * @return True if the update was successful, false otherwise.
     */
    boolean updateFeedbackDocument(FeedbackDocument feedbackDocument);
}
