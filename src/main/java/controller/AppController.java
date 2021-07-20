package controller;

import database.DocumentDatabaseManager;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import model.AppModel;
import model.Assignment;
import model.FeedbackDocument;
import nlp.BasicPipelineExample;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;
import java.util.Map;

public class AppController {

    AppModel appModel;
    DocumentDatabaseManager documentDatabase;


    StanfordCoreNLP nlp;

    public AppController(AppModel appModel) {
        this.appModel = appModel;
        this.documentDatabase = new DocumentDatabaseManager();
        this.nlp = BasicPipelineExample.getPipeline();
    }

    public Assignment createAssignment(String assignmentTitle, String headings, File studentManifestFile) {
        // Create assignment in the model
        Assignment assignment = appModel.createAssignment(assignmentTitle, headings, studentManifestFile);

        // Create the assignment database
        documentDatabase.createDocumentDatabase(assignment.getDatabaseName());

        // Create the feedback files for the assignment in the database
        documentDatabase.createFeedbackDocuments(assignment);

        System.out.println("Assignment: " + assignment.getDatabaseName());
        return assignment;
    }

    public Assignment loadAssignment(String assignmentFilePath) {
        Assignment assignment = appModel.loadAssignment(assignmentFilePath);
        loadFeedbackDocuments(assignment);
        return assignment;
    }

    public void saveAssignment(Assignment assignment, String fileName) {
        assignment.saveAssignmentDetails(fileName);
    }

    private void loadFeedbackDocuments(Assignment assignment) {
        documentDatabase.openDocumentDatabase(assignment.getDatabaseName());
        List<FeedbackDocument> feedbackDocuments = documentDatabase.loadFeedbackDocumentsForAssignment(assignment);
        assignment.setFeedbackDocuments(feedbackDocuments);

        System.out.println("1st doc is: " + assignment.getFeedbackDocuments().get(0).getHeadingData("1"));
        System.out.println("Got back " + assignment.getFeedbackDocuments().size() + " documents from db");
    }

    public void getFeedbackDocument(Assignment assignment, String studentId) {

    }

    public void saveFeedbackDocument(String studentId) {
        appModel.notifySubscribers("saveDoc", studentId);
    }

    public void saveFeedbackDocument(Assignment assignment, String studentId, Map<String, String> headingsAndData) {
        documentDatabase.saveFeedbackDocument(assignment, studentId, headingsAndData);
    }

    public void setCurrentDocInView(String studentId) {
        appModel.setCurrentScreenView(studentId);
    }

    public String getLastDocInView() {
        return appModel.getLastScreenView();
    }

    public String getCurrentDocInView() {
        return appModel.getCurrentScreenView();
    }

    public void displayNewDocument(Assignment assignment, String studentId) {
        System.out.println("New doc clicked: " + studentId);
        documentDatabase.updateAndStoreFeedbackDocument(assignment, studentId);
        appModel.setCurrentScreenView(studentId);
    }

    public void registerWithModel(PropertyChangeListener propertyChangeListener){
        appModel.subscribe(propertyChangeListener);
    }


    public void exportFeedbackDocuments(Assignment assignment) {
        // Make sure feedback documents are updated
        documentDatabase.loadFeedbackDocumentsForAssignment(assignment);
        // Export them
        appModel.exportFeedbackDocuments(assignment);
    }


    public void updatePhrasePanel(String currentHeadingBeingEdited) {
        appModel.setCurrentHeadingBeingEdited(currentHeadingBeingEdited);
        appModel.updatePhrasesForCurrentHeading();
    }

    public void updateCurrentHeadingBeingEdited(String currentHeadingBeingEdited) {
        appModel.setCurrentHeadingBeingEdited(currentHeadingBeingEdited);
    }

    public void insertPhraseIntoCurrentFeedbackBox(String phrase) {
        appModel.insertPhraseIntoCurrentFeedbackBox(phrase);
    }

    public String getCurrentHeadingBeingEdited() {
        return appModel.getCurrentHeadingBeingEdited();
    }

    public String getPhraseSentiment(String phrase) {
        CoreDocument coreDocument = new CoreDocument(phrase);
        nlp.annotate(coreDocument);

        List<CoreSentence> sentenceList = coreDocument.sentences();
        sentenceList.forEach(e -> System.out.println( e + " - " + e.sentiment()));

        return sentenceList.get(0).sentiment();
    }

    public void addNewPhrase(String phrase) {
        appModel.addNewPhrase(phrase);
    }

    public void updatePhrases(String heading, List<String> previousBoxContents, List<String> newBoxContents) {

    }
}
