package controller;

import database.DocumentDatabaseManager;
import database.GraphDatabaseManager;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import model.*;
import nlp.NLPPipline;
import view.PhraseType;
import visualisation.Visualisations;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * App Controller Class.
 */
public class AppController {

    // Instance variables
    private final AppModel appModel;
    private final DocumentDatabaseManager documentDatabase;
    private final GraphDatabaseManager graphDatabase;
    private final StanfordCoreNLP nlp;

    /**
     * Constructor.
     *
     * @param appModel - The model to interact with.
     */
    public AppController(AppModel appModel) {
        this.appModel = appModel;
        this.documentDatabase = new DocumentDatabaseManager();
        this.graphDatabase = new GraphDatabaseManager();
        this.nlp = NLPPipline.getPipeline();
    }

    /**
     * Create an assignment in the model.
     *
     * @param assignmentTitle         The title of the assignment.
     * @param headings                The headings of the feedback document.
     * @param studentManifestFile     The student list file.
     * @param assignmentDirectoryPath The directory location to save assignment related documents.
     * @return - The Assignment object that was created.
     */
    public Assignment createAssignment(String assignmentTitle, String headings, File studentManifestFile, String assignmentDirectoryPath) {
        // Create assignment in the model
        Assignment assignment = appModel.createAssignment(assignmentTitle, headings, studentManifestFile, assignmentDirectoryPath);
        assignment.saveAssignmentDetails(assignmentTitle
                .toLowerCase()
                .replace(" ", "-")
                .replace(".db", ""));

        // Create the assignment database
        documentDatabase.createDocumentDatabase(assignment.getAssignmentDirectoryPath() + File.separator + assignment.getDatabaseName());

        // Create the feedback files for the assignment in the document database
        documentDatabase.createFeedbackDocuments(assignment);

        // Create the graph database
        graphDatabase.createGraphDatabase(assignment.getAssignmentDirectoryPath() + File.separator + "graphDB" + File.separator + assignment.getDatabaseName());
        graphDatabase.setUpGraphDbForAssignment(assignment.getAssignmentHeadings());

        return assignment;
    }

    /**
     * Load an assignment from an FHT file.
     *
     * @param assignmentFilePath The location of the assignment FHT file.
     * @return The Assignment object for the assignment.
     */
    public Assignment loadAssignment(String assignmentFilePath) {
        // Load the assignment
        Assignment assignment = appModel.loadAssignment(assignmentFilePath);

        // Load the feedback documents into the assignment
        loadFeedbackDocuments(assignment);
        return assignment;
    }

    /**
     * Save an assignment.
     *
     * @param assignment The assignment to save.
     * @param fileName   The file name to save the assignment FHT as.
     */
    public void saveAssignment(Assignment assignment, String fileName) {
        assignment.saveAssignmentDetails(fileName);
    }

    /**
     * Load the feedback documents for an assignment.
     *
     * @param assignment The assignment to load the feedback documents for.
     */
    private void loadFeedbackDocuments(Assignment assignment) {
        // Open the databases
        documentDatabase.openDocumentDatabase(assignment.getAssignmentDirectoryPath() + File.separator + assignment.getDatabaseName());
        graphDatabase.openGraphDatabase(assignment.getAssignmentDirectoryPath() + File.separator + "graphDB" + File.separator + assignment.getDatabaseName());

        // Get the feedback documents from the document database
        List<FeedbackDocument> feedbackDocuments = documentDatabase.loadFeedbackDocumentsForAssignment(assignment);
        assignment.setFeedbackDocuments(feedbackDocuments);

        // Get the phrases data for each heading from the graph database
        assignment.getAssignmentHeadings().forEach(heading -> {
            List<Phrase> phrasesForHeading = graphDatabase.getPhrasesForHeading(heading);
            appModel.setPreviousHeadingPhraseSet(heading, phrasesForHeading);
            appModel.setCurrentHeadingPhraseSet(heading, phrasesForHeading);
        });
    }

    /**
     * Ask the model to save a feedback document.
     *
     * @param studentId The ID of the feedback document to save.
     */
    public void saveFeedbackDocument(String studentId) {
        appModel.notifySubscribers("saveDoc", studentId);
    }

    /**
     * Save the feedback document.
     *
     * @param assignment      The assignment the feedback document belows to
     * @param studentId       The ID of the feedback document to save.
     * @param headingsAndData The feedback data to save.
     * @param grade           The grade to save.
     */
    public void saveFeedbackDocument(Assignment assignment, String studentId, Map<String, String> headingsAndData, double grade) {
        documentDatabase.saveFeedbackDocument(assignment, studentId, headingsAndData, grade);
    }

    /**
     * Get the last document ID that was edited.
     *
     * @return The last document's ID.
     */
    public String getLastDocumentInView() {
        return appModel.getLastDocumentInView();
    }

    /**
     * Get the document ID of the current document being edited.
     *
     * @return The current document's ID.
     */
    public String getCurrentDocumentInView() {
        return appModel.getCurrentDocumentInView();
    }

    /**
     * Update the model with the current ID of the document that is being edited.
     *
     * @param studentId The current document's ID.
     */
    public void setCurrentDocumentInView(String studentId) {
        appModel.setCurrentDocumentInView(studentId, false);
    }

    /**
     * Process a request to display a new document to edit.
     *
     * @param assignment The assignment the document belongs to.
     * @param studentId  The ID of the document to display.
     */
    public void displayNewDocument(Assignment assignment, String studentId) {
        // Get the latest data for the requested document
        documentDatabase.updateFeedbackDocument(assignment, studentId);
        appModel.setCurrentDocumentInView(studentId, true);
    }

    /**
     * Register as a subscriber of the model.
     *
     * @param propertyChangeListener The listener object to register with the model.
     */
    public void registerWithModel(PropertyChangeListener propertyChangeListener) {
        appModel.subscribe(propertyChangeListener);
    }


    /**
     * Export the feedback documents.
     *
     * @param assignment The assignment the feedback documents belong to.
     */
    public void exportFeedbackDocuments(Assignment assignment) {
        // Make sure feedback documents are updated
        documentDatabase.loadFeedbackDocumentsForAssignment(assignment);
        // Export them
        appModel.exportFeedbackDocuments(assignment);
    }

    /**
     * Export the grade document.
     *
     * @param assignment The assignment the grade document belongs to.
     */
    public void exportGrades(Assignment assignment) {
        // Make sure feedback documents are updated
        documentDatabase.loadFeedbackDocumentsForAssignment(assignment);
        // Export them
        appModel.exportGrades(assignment);
    }


    /**
     * Create a bar chart visualisation of the grades.
     *
     * @param assignment The assignment grades to visualise.
     */
    public void visualiseGrades(Assignment assignment) {
        // Make sure feedback documents are updated
        documentDatabase.loadFeedbackDocumentsForAssignment(assignment);

        // Extract the grades and visualise them
        List<Integer> grades = appModel.getGrades(assignment);
        Visualisations.createBarChart(grades);
    }


    /**
     * Notify the model of the current feedback box heading being edited.
     *
     * @param currentHeadingBeingEdited The current heading being edited.
     */
    public void updateCurrentHeadingBeingEdited(String currentHeadingBeingEdited) {
        appModel.setCurrentHeadingBeingEdited(currentHeadingBeingEdited);
    }

    /**
     * Insert a phrase into the current feedback box being edited.
     *
     * @param phrase The string representation of the phrase to be inserted.
     */
    public void insertPhraseIntoCurrentFeedbackBox(String phrase) {
        appModel.insertPhraseIntoCurrentFeedbackBox(phrase);
    }

    /**
     * Get the current feedback box heading being edited.
     *
     * @return The current heading being edited.
     */
    public String getCurrentHeadingBeingEdited() {
        return appModel.getCurrentHeadingBeingEdited();
    }

    /**
     * Get the sentiment of a phrase.
     *
     * @param phrase The string representation of the phrase to be evaluated.
     * @return The sentiment of the phrase.
     */
    public String getPhraseSentiment(String phrase) {
        CoreDocument coreDocument = new CoreDocument(phrase);
        nlp.annotate(coreDocument);

        List<CoreSentence> sentenceList = coreDocument.sentences();
        sentenceList.forEach(e -> System.out.println(e + " - " + e.sentiment()));

        // TODO: Change the algorithm.

        return sentenceList.get(0).sentiment();
    }

    /**
     * Get the sentiment annotations for a body of text.
     *
     * @param text The text body to be evaluated.
     * @return A CoreDocument object containing the sentiment annotations.
     */
    public CoreDocument getSentimentForText(String text) {
        CoreDocument coreDocument = new CoreDocument(text);
        nlp.annotate(coreDocument);
        return coreDocument;
    }

    /**
     * Add a custom phrase from the GUI.
     *
     * @param phrase The string representation of the phrase to be added.
     */
    public void addNewCustomPhraseFromView(String phrase) {
        // Filter out empty lines
        if (!phrase.trim().isEmpty() && !phrase.trim().equals(getLineMarker())) {
            Phrase phrase1 = new Phrase(phrase);
            graphDatabase.addPhraseToCustomNode(phrase1);
            appModel.addNewCustomPhraseToView(phrase1);
        } else {
            System.out.println("[DEBUG]: empty string detected, not creating a phrase");
        }
    }

    /**
     * Manage the links between phrases.
     *
     * @param heading             The current feedback box heading being edited.
     * @param previousBoxContents The last list of phrases for the feedback box.
     * @param currentBoxContents  The current list of phrases for the feedback box.
     */
    public void managePhraseLinks(String heading, List<String> previousBoxContents, List<String> currentBoxContents) {
        graphDatabase.managePhraseLinks(heading, previousBoxContents, currentBoxContents);
    }

    /**
     * Get a list of linked phrases for a given heading.
     *
     * @param heading The heading the linked phrases are for.
     * @return A list of linked phrases for the given heading.
     */
    public List<LinkedPhrases> getLinkedPhrasesForHeading(String heading) {
        return graphDatabase.getLinkedPhrases(heading);
    }

    /**
     * Update the phrases stored in the graph database.
     *
     * @param heading             The current feedback box heading being edited.
     * @param previousBoxContents The last list of phrases for the feedback box.
     * @param currentBoxContents  The current list of phrases for the feedback box.
     */
    public void updatePhrases(String heading, List<String> previousBoxContents, List<String> currentBoxContents) {
        System.out.println("[DEBUG] in controller updatePhrases method");
        // Store previous phrase set
        List<Phrase> previousPhrasesForHeading = graphDatabase.getPhrasesForHeading(heading);
        appModel.setPreviousHeadingPhraseSet(heading, previousPhrasesForHeading);

        // Update the database with the new phrases
        graphDatabase.updatePhrasesForHeading(heading, previousBoxContents, currentBoxContents);
        List<Phrase> currentPhrasesForHeading = graphDatabase.getPhrasesForHeading(heading);
        appModel.setCurrentHeadingPhraseSet(heading, currentPhrasesForHeading);

        // Find what's changed and send those changes to GUI
        List<Phrase> removalsFromList = Utilities.getRemovalsFromList(previousPhrasesForHeading, currentPhrasesForHeading);
        List<Phrase> additionsToList = Utilities.getAdditionsToList(previousPhrasesForHeading, currentPhrasesForHeading);

        // Find what's stayed same and update the usage counts
        List<Phrase> stayedSameList = Utilities.getIntersection(previousPhrasesForHeading, currentPhrasesForHeading);

        System.out.println("DEBUG: removals -> " + removalsFromList);
        System.out.println("DEBUG: additions -> " + additionsToList);
        System.out.println("DEBUG: stayed same -> " + stayedSameList);

        removalsFromList.forEach(phraseToRemove -> {
            appModel.removePhraseFromView(phraseToRemove);
        });

        additionsToList.forEach(phraseToAdd -> {
            appModel.addNewPhraseToView(phraseToAdd);
        });

        stayedSameList.forEach(phrase -> {
            appModel.updatePhraseCounterInView(phrase);
        });
    }

    /**
     * Ask the model to reset the phrases panel.
     */
    public void resetPhrasesPanel() {
        appModel.resetPhrasesPanel();
    }

    /**
     * Determine whether the user has navigated to a new feedback box.
     *
     * @return True if a new heading is being edited, false otherwise.
     */
    public boolean headingChanged() {
        return !appModel.getCurrentHeadingBeingEdited().equals(appModel.getPreviousHeadingBeingEdited());
    }

    /**
     * Show all the frequently used phrases for a given heading.
     *
     * @param heading The heading the phrases are for.
     */
    public void showPhrasesForHeading(String heading) {
        System.out.println("IN SHOW PHRASES FOR HEADING");
        List<Phrase> currentPhraseSet = appModel.getCurrentPhraseSet(heading);
        System.out.println("CURRENT PHRASE SET: " + heading + " : " + currentPhraseSet);
        if (currentPhraseSet != null) {
            currentPhraseSet.forEach(phrase -> {
                appModel.addNewPhraseToView(phrase);
            });
        }
    }

    /**
     * Show the user an error message.
     *
     * @param errorMessage The error message to show.
     */
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


    /**
     * Create an assignment from a configuration file.
     *
     * @param configFilePath The location of the configuration file.
     * @return The newly created Assignment object.
     */
    public Assignment createAssignmentFromConfig(String configFilePath) {
        // Create the assignment and save it to an FHT file
        Assignment assignment = appModel.createAssignmentFromConfig(configFilePath);
        assignment.saveAssignmentDetails(assignment.getAssignmentTitle()
                .toLowerCase()
                .replace(" ", "-")
                .replace(".db", ""));

        // Create the assignment database
        documentDatabase.createDocumentDatabase(assignment.getAssignmentDirectoryPath() + File.separator + assignment.getDatabaseName());

        // Create the feedback files for the assignment in the database
        documentDatabase.createFeedbackDocuments(assignment);

        // Setup the graph database
        graphDatabase.createGraphDatabase(assignment.getAssignmentDirectoryPath() + File.separator + "graphDB" + File.separator + assignment.getDatabaseName());
        graphDatabase.setUpGraphDbForAssignment(assignment.getAssignmentHeadings());

        return assignment;
    }

    /**
     * Set the style preferences for an assignment's exports.
     *
     * @param assignment     The assignment the preferences are for.
     * @param headingStyle   The heading style.
     * @param underlineStyle The heading underline style.
     * @param lineSpacing    The line spacing after each section.
     * @param lineMarker     The line marker for each new line.
     */
    public void setAssignmentPreferences(Assignment assignment, String headingStyle, String underlineStyle, int lineSpacing, String lineMarker) {
        appModel.setAssignmentPreferences(assignment, headingStyle, underlineStyle, lineSpacing, lineMarker);
    }

    /**
     * Get the line marker to use for denoting new lines.
     *
     * @return The line marker.
     */
    public String getLineMarker() {
        return appModel.getLineMarker();
    }

    /**
     * Set the phrase panel the user is currently viewing.
     *
     * @param currentPhrasePanelInView The phrase panel type.
     */
    public void setCurrentPhrasePanelInView(PhraseType currentPhrasePanelInView) {
        appModel.setCurrentPhrasePanelInView(currentPhrasePanelInView);
    }

    /**
     * Get the custom phrases and display them.
     */
    public void showCustomPhrases() {
        List<Phrase> customPhrases = graphDatabase.getCustomPhrases();
        customPhrases.forEach(appModel::addNewCustomPhraseToView);
    }

    /**
     * Get the heading style to use for headings when files are exported.
     *
     * @return The heading style.
     */
    public String getHeadingStyle() {
        return appModel.getHeadingStyle();
    }

    /**
     * Get the heading underline style to use for headings when files are exported.
     *
     * @return The heading underline style.
     */
    public String getUnderlineStyle() {
        return appModel.getUnderlineStyle();
    }

    /**
     * Get the number of line spaces to use between sections when files are exported.
     *
     * @return The number of line spaces.
     */
    public int getLineSpacing() {
        return appModel.getLineSpacing();
    }

}
