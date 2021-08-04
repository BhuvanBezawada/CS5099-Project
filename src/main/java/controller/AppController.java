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
import org.checkerframework.checker.units.qual.C;
import view.PhraseType;

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

    public Assignment createAssignment(String assignmentTitle, String headings, File studentManifestFile, String assignmentDirectoryPath) {
        // Create assignment in the model
        Assignment assignment = appModel.createAssignment(assignmentTitle, headings, studentManifestFile, assignmentDirectoryPath);
        assignment.saveAssignmentDetails(assignmentTitle
                .toLowerCase()
                .replace(" ", "-")
                .replace(".db", ""));

        // Create the assignment database
        documentDatabase.createDocumentDatabase(assignment.getAssignmentDirectoryPath() + File.separator + assignment.getDatabaseName());

        // Create the feedback files for the assignment in the database
        documentDatabase.createFeedbackDocuments(assignment);

        graphDatabase.openOrCreateGraphDatabase(assignment.getAssignmentDirectoryPath() + File.separator + "graphDB" + File.separator + assignment.getDatabaseName());
        graphDatabase.setUpGraphDbForAssignment(assignment.getAssignmentHeadings());

        System.out.println("Assignment: " + assignment.getDatabaseName());

        return assignment;
    }

    public Assignment loadAssignment(String assignmentFilePath) {
        Assignment assignment = appModel.loadAssignment(assignmentFilePath);
        System.out.println("ASSIGNMENT LOADING DETAILS: " + assignment);
        System.out.println("ASSIGNMENT LOADING DETAILS: " + assignment.getLineMarker());

        loadFeedbackDocuments(assignment);
//        assignment.getAssignmentHeadings().forEach(heading -> {
//            List<Phrase> phrasesForHeading = graphDatabase.getPhrasesForHeading(heading);
//            System.out.println("Got phrases for heading from graph db: " + heading + " : " + phrasesForHeading);
//            appModel.setPreviousPhraseSet(heading, phrasesForHeading);
//            appModel.setCurrentPhraseSet(heading, phrasesForHeading);
//
//            List<Phrase> customPhrases = graphDatabase.getCustomPhrases();
//            System.out.println("[DEBUG] got custom phrases: " + customPhrases);
//            customPhrases.forEach(phrase -> appModel.addNewCustomPhrase(phrase));
//        });
        return assignment;
    }

    public void saveAssignment(Assignment assignment, String fileName) {
        assignment.saveAssignmentDetails(fileName);
    }

    private void loadFeedbackDocuments(Assignment assignment) {
        documentDatabase.openDocumentDatabase(assignment.getAssignmentDirectoryPath() + File.separator + assignment.getDatabaseName());
        graphDatabase.openOrCreateGraphDatabase(assignment.getAssignmentDirectoryPath() + File.separator + "graphDB" + File.separator + assignment.getDatabaseName());
        List<FeedbackDocument> feedbackDocuments = documentDatabase.loadFeedbackDocumentsForAssignment(assignment);
        assignment.setFeedbackDocuments(feedbackDocuments);

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

//    public void setCurrentDocInView(String studentId) {
//        appModel.setCurrentScreenView(studentId);
//    }

    public String getLastDocInView() {
        return appModel.getLastScreenView();
    }

    public String getCurrentDocInView() {
        return appModel.getCurrentScreenView();
    }

    public void displayNewDocument(Assignment assignment, String studentId) {
        System.out.println("New doc clicked: " + studentId);
        documentDatabase.updateAndStoreFeedbackDocument(assignment, studentId);
        appModel.setCurrentScreenView(studentId, true);
    }


    public void setCurrentDocInView(String studentId) {
        appModel.setCurrentScreenView(studentId, false);
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

    public CoreDocument getSentimentForText(String text) {
        CoreDocument coreDocument = new CoreDocument(text);
        nlp.annotate(coreDocument);
        return coreDocument;
    }

    public void addNewCustomPhraseFromView(String phrase) {
        // Filter out empty lines
        if (!phrase.trim().isEmpty() && !phrase.trim().equals(getLineMarker())) {
            Phrase phrase1 = new Phrase(phrase);
            graphDatabase.addPhraseToCustomNode(phrase1);
            appModel.addNewCustomPhrase(phrase1);
        } else {
            System.out.println("[DEBUG]: empty string detected, not creating a phrase");
        }
    }

    public void updatePhrases(String heading, List<String> previousBoxContents, List<String> newBoxContents) {
        System.out.println("[DEBUG] in controller updatePhrases method");
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
        
        // Find what's stayed same and update the usage counts
        List<Phrase> stayedSameList = Utilities.getIntersection(previousPhrasesForHeading, currentPhrasesForHeading);

        System.out.println("DEBUG: removals -> " + removalsFromList);
        System.out.println("DEBUG: additions -> " + additionsToList);
        System.out.println("DEBUG: stayed same -> " + stayedSameList);

        removalsFromList.forEach(phraseToRemove -> {
            appModel.removePhrase(phraseToRemove.getPhraseAsString());
        });

        additionsToList.forEach(phraseToAdd -> {
            appModel.addNewPhrase(phraseToAdd);
        });

        stayedSameList.forEach(phrase -> {
            appModel.updatePhraseCounter(phrase);
        });
    }

    public void resetPhrasesPanel() {
        appModel.resetPhrasesPanel();
    }

    public boolean headingChanged() {
        return !appModel.getCurrentHeadingBeingEdited().equals(appModel.getPreviousHeadingBeingEdited());
    }

    public void showPhrasesForHeading(String heading) {
        System.out.println("IN SHOW PHRASES FOR HEADING");
        List<Phrase> currentPhraseSet = appModel.getCurrentPhraseSet(heading);
        System.out.println("CURRENT PHRASE SET: " + heading + " : " + currentPhraseSet);
        if (currentPhraseSet != null) {
            currentPhraseSet.forEach(phrase -> {
                appModel.addNewPhrase(phrase);
            });
        }
    }

    public void error(String errorMessage) {
        appModel.notifySubscribers("error", errorMessage);
    }

    public String getRandomLineFromDoc(Assignment assignment, String studentId) {
//        documentDatabase.loadFeedbackDocumentsForAssignment(assignment);
//        FeedbackDocument feedbackDocumentForStudent = assignment.getFeedbackDocumentForStudent(studentId);
//        String headingData = feedbackDocumentForStudent.getHeadingData(getCurrentHeadingBeingEdited());
//        String[] split = headingData.split("\n");
//        return split[new Random().nextInt(split.length)];
        return "Random line";
    }

    public Assignment createAssignmentFromConfig(String configFilePath) {
        Assignment assignment = appModel.createAssignmentFromConfig(configFilePath);

        assignment.saveAssignmentDetails(assignment.getAssignmentTitle()
                .toLowerCase()
                .replace(" ", "-")
                .replace(".db", ""));

        // Create the assignment database
        documentDatabase.createDocumentDatabase(assignment.getAssignmentDirectoryPath() + File.separator + assignment.getDatabaseName());

        // Create the feedback files for the assignment in the database
        documentDatabase.createFeedbackDocuments(assignment);

        graphDatabase.openOrCreateGraphDatabase(assignment.getAssignmentDirectoryPath() + File.separator + "graphDB" + File.separator + assignment.getDatabaseName());
        graphDatabase.setUpGraphDbForAssignment(assignment.getAssignmentHeadings());

        System.out.println("Assignment: " + assignment.getDatabaseName());
        return assignment;
    }

    public void setAssignmentPreferences(Assignment assignment, String headingStyle, String underlineStyle, int lineSpacing, String lineMarker) {
        appModel.setAssignmentPreferences(assignment, headingStyle, underlineStyle, lineSpacing, lineMarker);
    }

    public String getLineMarker() {
        return appModel.getLineMarker();
    }

    public PhraseType getCurrentPhrasePanelInView() {
        return appModel.getCurrentPhrasePanelInView();
    }

    public void setCurrentPhrasePanelInView(PhraseType phraseType) {
        appModel.setCurrentPhrasePanelInView(phraseType);
    }

    public void showCustomPhrases() {
        List<Phrase> customPhrases = graphDatabase.getCustomPhrases();
        customPhrases.forEach(customPhrase -> appModel.addNewCustomPhrase(customPhrase));
    }

}
