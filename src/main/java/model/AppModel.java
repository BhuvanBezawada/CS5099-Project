package model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.neo4j.cypher.internal.frontend.v3_4.phases.Do;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class AppModel implements IAppModel {

    private String currentStudentId;
    private String lastStudentId;

    private String currentHeadingBeingEdited;
    private String previousHeadingBeingEdited;

    private PropertyChangeSupport subscribers;

    private Map<String, List<Phrase>> currentHeadingAndUsedPhrases;
    private Map<String, List<Phrase>> previousHeadingAndUsedPhrases;

    private Assignment assignment;
    private boolean assignmentResumed;

    public AppModel() {
        this.subscribers = new PropertyChangeSupport(this);

        this.currentHeadingAndUsedPhrases = new HashMap<String, List<Phrase>>();
        this.previousHeadingAndUsedPhrases = new HashMap<String, List<Phrase>>();
    }


    /*
     * Methods for observers of the model.
     */

    public void subscribe(PropertyChangeListener listener) {
        subscribers.addPropertyChangeListener(listener);
        System.out.println("Got a new subscriber!");
    }

    public void unsubscribe(PropertyChangeListener listener) {
        subscribers.removePropertyChangeListener(listener);
    }

    public void notifySubscribers(String property, String notification) {
        subscribers.firePropertyChange(property, "oldValue", notification);
    }



    /* Create model elements from the controller, requested by GUI */

    public Assignment createAssignment(String assignmentTitle, String assignmentHeadings, File studentManifestFile, String assignmentDirectoryPath) {
        // Create assignment object
        Assignment assignment = new Assignment();
        assignment.setAssignmentTitle(assignmentTitle);
        assignment.setAssignmentHeadings(assignmentHeadings);
        assignment.setStudentIds(studentManifestFile);
        assignment.setAssignmentDirectoryPath(assignmentDirectoryPath);

        // Create the assignment directory if it does not exist
        File outputDirectory = new File(assignmentDirectoryPath);
        if (!outputDirectory.exists()) {
            outputDirectory.mkdir();
        }

        // Once an assignment is created, notify the observers
        notifySubscribers("assignment", "created");
        this.assignment = assignment;

        return assignment;
    }

    public Assignment loadAssignment(String assignmentFilePath) {
        this.assignment = Assignment.loadAssignment(assignmentFilePath);
        return this.assignment;
    }

    public void setCurrentScreenView(String studentId) {
        this.lastStudentId = this.currentStudentId;
        this.currentStudentId = studentId;
        notifySubscribers("docViewChange", studentId);
    }

    public String getLastScreenView() {
        return lastStudentId;
    }

    public String getCurrentScreenView() {
        return currentStudentId;
    }

    public void exportGrades(Assignment assignment) {
        File outputDirectory = new File(assignment.getAssignmentDirectoryPath() + File.separator + assignment.getAssignmentTitle().trim().replace(" ", "-"));
        if (!outputDirectory.exists()) {
            outputDirectory.mkdir();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputDirectory + File.separator + "grades.txt"))) {
            assignment.getFeedbackDocuments().forEach(feedbackDocument -> {
                try {
                    writer.write(feedbackDocument.getStudentId() + "," + feedbackDocument.getGrade());
                    writer.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<Integer> getGrades(Assignment assignment) {

        Map<Double, Integer> gradeAndNumber = new LinkedHashMap<Double, Integer>();
        for (double i = 0.0; i <= 20.0; i+= 0.5) {
            gradeAndNumber.put(i, 0);
        }

        assignment.getFeedbackDocuments().forEach(feedbackDocument -> {
            double grade = feedbackDocument.getGrade();
            int currentCount = gradeAndNumber.get(grade);
            gradeAndNumber.put(grade, currentCount+1);
        });

        System.out.println(gradeAndNumber);

        return new ArrayList<>(gradeAndNumber.values());
    }

    public void exportFeedbackDocuments(Assignment assignment) {
        File outputDirectory = new File(assignment.getAssignmentDirectoryPath() + File.separator + assignment.getAssignmentTitle().trim().replace(" ", "-"));
        if (!outputDirectory.exists()) {
            outputDirectory.mkdir();
        }

        assignment.getFeedbackDocuments().forEach(feedbackDocument -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputDirectory + File.separator + feedbackDocument.getStudentId() + ".txt"))) {
                feedbackDocument.getHeadings().forEach(heading -> {
                    try {
                        writer.write(heading);
                        writer.newLine();
                        writer.write(feedbackDocument.getHeadingData(heading));
                        writer.newLine();
                        writer.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    public void setCurrentHeadingBeingEdited(String currentHeadingBeingEdited) {
        this.previousHeadingBeingEdited = this.currentHeadingBeingEdited;
        this.currentHeadingBeingEdited = currentHeadingBeingEdited;
    }

    public void updatePhrasesForCurrentHeading() {
        // Get phrases for the current heading
        // Send those phrases to the view
        notifySubscribers("updatePhrases", "sample phrase for " + currentHeadingBeingEdited + " section");
    }

    public void insertPhraseIntoCurrentFeedbackBox(String phrase) {
        notifySubscribers("insertPhrase", phrase);
    }

    public String getCurrentHeadingBeingEdited() {
        return this.currentHeadingBeingEdited;
    }

    public String getPreviousHeadingBeingEdited(){
        return this.previousHeadingBeingEdited;
    }

    public void addNewPhrase(String phrase) {
        notifySubscribers("newPhrase", phrase);
    }

    public void removePhrase(String phrase) {
        notifySubscribers("deletePhrase", phrase);
    }

    public void resetPhrasesPanel() {
        notifySubscribers("resetPhrasesPanel", "");
    }

    public List<Phrase> getCurrentPhraseSet(String heading) {
        return this.currentHeadingAndUsedPhrases.get(heading);
    }

    public void setCurrentPhraseSet(String heading, List<Phrase> phrases) {
        this.currentHeadingAndUsedPhrases.put(heading, phrases);
    }

    public List<Phrase> getPreviousPhraseSet(String heading) {
        return this.previousHeadingAndUsedPhrases.get(heading);
    }

    public void setPreviousPhraseSet(String heading, List<Phrase> phrases) {
        this.previousHeadingAndUsedPhrases.put(heading, phrases);
    }

    public Assignment createAssignmentFromConfig(String configFilePath) {
        try {
            JSONObject configDoc = (JSONObject) new JSONParser().parse(new FileReader(configFilePath));

            String title = (String) configDoc.get("title");
            String assignmentLocation = (String) configDoc.get("assignment_location");
            String lineMarker = (String) configDoc.get("line_marker");
            JSONArray headings = (JSONArray) configDoc.get("headings");

            StringBuilder headingsString = new StringBuilder();
            headings.forEach(heading -> {
                headingsString.append(heading);
                headingsString.append("\n");
            });

            Map headingStyle = ((Map) configDoc.get("heading_style"));
            String headingMarker = (String) headingStyle.get("heading_marker");
            String headingUnderlined = (String) headingStyle.get("heading_underlined");
            long numLinesAfterSectionEnds = (long) headingStyle.get("num_lines_after_section_ends");

            String studentManifestFileLocation = (String) configDoc.get("student_manifest_location");
            File studentManifestFile = new File(studentManifestFileLocation);

            System.out.println("Title: " + title);
            System.out.println("Loc: " + assignmentLocation);
            System.out.println("LineMarker: " + lineMarker);
            System.out.println("Headings: " + headingsString.toString());
            System.out.println("HeadingStyle: ");
            System.out.println("HeadingMarker: " + headingMarker);
            System.out.println("HeadingUnderlined: " + headingUnderlined);
            System.out.println("HeadingLineSpace: " + numLinesAfterSectionEnds);

            Assignment assignment = createAssignment(title, headingsString.toString(), studentManifestFile, assignmentLocation);
            setAssignmentPreferences(assignment, headingMarker, headingUnderlined, (int) numLinesAfterSectionEnds, lineMarker);

            return assignment;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setAssignmentPreferences(Assignment assignment, String headingStyle, String underlineStyle, int lineSpacing, String lineMarker) {
        assignment.setHeadingStyle(headingStyle);
        assignment.setUnderlineStyle(underlineStyle);
        assignment.setLineSpacing(lineSpacing);
        assignment.setLineMarker(lineMarker);
    }
}
















