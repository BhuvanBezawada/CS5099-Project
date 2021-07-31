package model;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Assignment implements Serializable {

    private static final long serialVersionUID = 1200109309800080026L;

    private String assignmentTitle;
    private List<String> assignmentHeadings;
    private List<String> studentIds;
    private List<FeedbackDocument> feedbackDocuments;
    private Map<String, FeedbackDocument> studentIdAndFeedbackDocumentMap;

    private static final List<String> headingStyles = Collections.unmodifiableList(
            new ArrayList<String>() {{
                add("#");
                add("##");
            }});

    private final List<String> underlineStyles = Collections.unmodifiableList(
            new ArrayList<String>() {{
                add("-");
                add("==");
            }});

    private final List<Integer> lineSpacings = Collections.unmodifiableList(
            new ArrayList<Integer>() {{
                add(1);
                add(2);
                add(3);
            }});

    private final List<String> lineMarkers = Collections.unmodifiableList(
            new ArrayList<String>() {{
                add("-");
                add("->");
                add("=>");
                add("*");
                add("+");
            }});

    private String databaseName;
    private String databaseCollectionName;

    private String assignmentDirectoryPath;


    public String getHeadingStyle() {
        return headingStyle;
    }

    public void setHeadingStyle(String headingStyle) {
        if (headingStyles.contains(headingStyle)) {
            this.headingStyle = headingStyle;
        } else {
            this.headingStyle = "";
        }
    }

    public String getUnderlineStyle() {
        return underlineStyle;
    }

    public void setUnderlineStyle(String underlineStyle) {
        if (underlineStyles.contains(underlineStyle)) {
            this.underlineStyle = underlineStyle;
        } else {
            this.underlineStyle = "";
        }
    }

    public int getLineSpacing() {
        return lineSpacing;
    }

    public void setLineSpacing(int lineSpacing) {
        if (lineSpacings.contains(lineSpacing)) {
            this.lineSpacing = lineSpacing;
        } else {
            this.lineSpacing = 1;
        }
    }

    public String getLineMarker() {
        return lineMarker;
    }

    public void setLineMarker(String lineMarker) {
        if (lineSpacings.contains(lineSpacing)) {
            this.lineMarker = lineMarker;
        } else {
            this.lineMarker = "-";
        }
    }

    private String headingStyle;
    private String underlineStyle;
    private int lineSpacing;
    private String lineMarker;

    public String getAssignmentDirectoryPath() {
        return assignmentDirectoryPath;
    }

    public void setAssignmentDirectoryPath(String assignmentDirectoryPath) {
        this.assignmentDirectoryPath = assignmentDirectoryPath;
    }

    public String getDatabaseCollectionName() {
        return this.databaseCollectionName;
    }

    public Assignment() {
        this.studentIds = new ArrayList<String>();
        this.assignmentHeadings = new ArrayList<String>();
        studentIdAndFeedbackDocumentMap = new HashMap<String, FeedbackDocument>();
    }

    public void setFeedbackDocument(String studentId, FeedbackDocument feedbackDocument) {
        studentIdAndFeedbackDocumentMap.put(studentId, feedbackDocument);
    }

    public static Assignment loadAssignment(String filePath) {
        Assignment loadedAssignment = null;
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            loadedAssignment = (Assignment) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return loadedAssignment;
    }

    public String getAssignmentTitle() {
        return assignmentTitle;
    }

    public void setAssignmentTitle(String assignmentTitle) {
        this.assignmentTitle = assignmentTitle;
        this.databaseName = assignmentTitle.replace(" ", "-").toLowerCase() + ".db";
        this.databaseCollectionName = assignmentTitle.replace(" ", "-").toLowerCase() + "-feedback-docs";
    }

    public List<String> getAssignmentHeadings() {
        return assignmentHeadings;
    }

    public void setAssignmentHeadings(String assignmentHeadings) {
        this.assignmentHeadings = new ArrayList<String>(Arrays.asList(assignmentHeadings.split("\n")));
        // Remove empty lines
        this.assignmentHeadings = this.assignmentHeadings.stream().filter(heading -> !heading.trim().isEmpty()).collect(Collectors.toList());
        System.out.println("Converting to list: " + this.assignmentHeadings);
    }

    public List<String> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(File studentManifestFile) {
        System.out.println("Loading file: " + studentManifestFile);
        this.studentIds = new ArrayList<String>();
        this.feedbackDocuments = new ArrayList<FeedbackDocument>();

        try (BufferedReader reader = new BufferedReader(new FileReader(studentManifestFile))) {
            while (reader.ready()) {
                String studentId = reader.readLine().trim();
                FeedbackDocument feedbackDocument = new FeedbackDocument(this, studentId);

                this.studentIds.add(studentId);
                this.feedbackDocuments.add(feedbackDocument);

                this.studentIdAndFeedbackDocumentMap.put(studentId, feedbackDocument);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Student ids: " + studentIds);
    }

    public List<FeedbackDocument> getFeedbackDocuments() {
        return new ArrayList<FeedbackDocument>(this.studentIdAndFeedbackDocumentMap.values());
    }

    public void saveAssignmentDetails(String fileName) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(assignmentDirectoryPath + File.separator + fileName + ".fht");
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setFeedbackDocuments(List<FeedbackDocument> feedbackDocuments) {
        this.feedbackDocuments = feedbackDocuments;
        feedbackDocuments.forEach(feedbackDocument -> {
            studentIdAndFeedbackDocumentMap.put(feedbackDocument.getStudentId(), feedbackDocument);
        });
    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    public AssignmentConfig getAssignmentConfig() {
        return null;
    }

    public FeedbackDocument getFeedbackDocumentForStudent(String studentId) {
        return studentIdAndFeedbackDocumentMap.get(studentId);
    }


}
