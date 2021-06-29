package model;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AssignmentConfig implements Serializable {

    private String assignmentTitle;
    private List<String> assignmentHeadings;
    private String assignmentConfigFilePath;

    public AssignmentConfig(String assignmentConfigFilePath) {
        this.assignmentConfigFilePath = assignmentConfigFilePath;
        this.setupAssignmentConfig();
    }

    public String getAssignmentTitle() {
        return assignmentTitle;
    }

    public List<String> getAssignmentHeadings() {
        return assignmentHeadings;
    }

    private void setupAssignmentConfig() {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(new File(assignmentConfigFilePath)))) {
            while (fileReader.ready()) {
                String line = fileReader.readLine().trim();

                if (line.startsWith("assignment-title:")) {
                    assignmentTitle = line.split(":")[1].trim();
                    assignmentTitle = assignmentTitle.substring(1, assignmentTitle.length()-1);
                } else if (line.startsWith("assignment-headings:")) {
                    String assignmentHeadingsString = line.split(":")[1].trim();

                    assignmentHeadings = Arrays.asList(
                            assignmentHeadingsString
                                    .substring(1, assignmentHeadingsString.length()-1)
                                    .split(",")
                    );

                    assignmentHeadings = assignmentHeadings.stream().map(String::trim).collect(Collectors.toList());
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }

        System.out.println("Got assignment title as: " + assignmentTitle);
        System.out.println("Got assignment headings as: " + assignmentHeadings);
    }
}
