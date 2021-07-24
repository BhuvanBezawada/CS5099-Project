package controller;

import database.DocumentDatabaseManager;
import database.GraphDatabaseManager;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import main.Vis;
import model.AppModel;
import model.Assignment;
import model.FeedbackDocument;
import model.Phrase;
import nlp.BasicPipelineExample;

import javax.rmi.CORBA.Util;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;
import java.util.Map;

public class AppController {

    AppModel appModel;
    DocumentDatabaseManager documentDatabase;
    GraphDatabaseManager graphDatabase;


    StanfordCoreNLP nlp;

    public AppController(AppModel appModel) {
        this.appModel = appModel;
        this.documentDatabase = new DocumentDatabaseManager();
        this.graphDatabase = new GraphDatabaseManager();
        this.nlp = BasicPipelineExample.getPipeline();
    }

    public Assignment createAssignment(String assignmentTitle, String headings, File studentManifestFile) {
        // Create assignment in the model
        Assignment assignment = appModel.createAssignment(assignmentTitle, headings, studentManifestFile);
        assignment.saveAssignmentDetails(assignmentTitle
                .toLowerCase()
                .replace(" ", "-")
                .replace(".db", ""));

        // Create the assignment database
        documentDatabase.createDocumentDatabase(assignment.getDatabaseName());

        // Create the feedback files for the assignment in the database
        documentDatabase.createFeedbackDocuments(assignment);

        graphDatabase.openOrCreateGraphDatabase("data/" + assignment.getDatabaseName());
        graphDatabase.setUpGraphDbForAssignment(assignment.getAssignmentHeadings());

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
        graphDatabase.openOrCreateGraphDatabase("data/" + assignment.getDatabaseName());
        List<FeedbackDocument> feedbackDocuments = documentDatabase.loadFeedbackDocumentsForAssignment(assignment);
        assignment.setFeedbackDocuments(feedbackDocuments);

        System.out.println("1st doc is: " + assignment.getFeedbackDocuments().get(0).getHeadingData("1"));
        System.out.println("Got back " + assignment.getFeedbackDocuments().size() + " documents from db");

        assignment.getAssignmentHeadings().forEach(heading -> {
            List<Phrase> phrasesForHeading = graphDatabase.getPhrasesForHeading(heading);
            appModel.setPreviousPhraseSet(heading, phrasesForHeading);
            appModel.setCurrentPhraseSet(heading, phrasesForHeading);
        });
    }

    public void getFeedbackDocument(Assignment assignment, String studentId) {

    }

    public void saveFeedbackDocument(String studentId) {
        appModel.notifySubscribers("saveDoc", studentId);
    }

    public void saveFeedbackDocument(Assignment assignment, String studentId, Map<String, String> headingsAndData, double grade) {
        documentDatabase.saveFeedbackDocument(assignment, studentId, headingsAndData, grade);
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

    public void exportGrades(Assignment assignment) {
        // Make sure feedback documents are updated
        documentDatabase.loadFeedbackDocumentsForAssignment(assignment);
        // Export them
        appModel.exportGrades(assignment);
    }

    public void visualiseGrades(Assignment assignment) {
        documentDatabase.loadFeedbackDocumentsForAssignment(assignment);
        List<Integer> grades = appModel.getGrades(assignment);
        System.out.println(grades);
        Vis.createBarChart(grades);
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
        // Filter out empty lines
        if (!phrase.trim().isEmpty() && !phrase.trim().equals("-")) {
            appModel.addNewPhrase(phrase);
        } else {
            System.out.println("[DEBUG]: empty string detected, not creating a phrase");
        }
    }

    public void updatePhrases(String heading, List<String> previousBoxContents, List<String> newBoxContents) {
        // Store previous phrase set
        List<Phrase> previousPhrasesForHeading = graphDatabase.getPhrasesForHeading(heading);
        appModel.setPreviousPhraseSet(heading, previousPhrasesForHeading);

        // Update the database with the new phrases
        graphDatabase.updatePhrasesForHeading(heading, previousBoxContents, newBoxContents);
        List<Phrase> currentPhrasesForHeading = graphDatabase.getPhrasesForHeading(heading);
        appModel.setCurrentPhraseSet(heading, currentPhrasesForHeading);

        // Find what's changed and send those changes to GUI
        List<Phrase> removalsFromList = Utilities.getRemovalsFromList(previousPhrasesForHeading, currentPhrasesForHeading);
        List<Phrase> additionsToList = Utilities.getAdditionsToList(previousPhrasesForHeading, currentPhrasesForHeading);

        System.out.println("DEBUG: removals -> " + removalsFromList);
        System.out.println("DEBUG: additions -> " + additionsToList);

        removalsFromList.forEach(phraseToRemove -> {
            appModel.removePhrase(phraseToRemove.getPhraseAsString());
        });

        additionsToList.forEach(phraseToAdd -> {
            appModel.addNewPhrase(phraseToAdd.getPhraseAsString());
        });
    }

    public void resetPhrasesPanel() {
        appModel.resetPhrasesPanel();
    }

    public boolean headingChanged() {
        return !appModel.getCurrentHeadingBeingEdited().equals(appModel.getPreviousHeadingBeingEdited());
    }

    public void showPhrasesForHeading(String heading) {
        List<Phrase> currentPhraseSet = appModel.getCurrentPhraseSet(heading);
        currentPhraseSet.forEach(phrase -> {
            addNewPhrase(phrase.getPhraseAsString());
        });
    }

    public void error(String errorMessage) {
        appModel.notifySubscribers("error", errorMessage);
    }
}
