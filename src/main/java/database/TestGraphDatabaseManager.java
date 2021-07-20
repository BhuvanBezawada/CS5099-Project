package database;

import model.Phrase;

import java.util.ArrayList;
import java.util.List;

public class TestGraphDatabaseManager {
    public static void main(String[] args) {
        GraphDatabaseManager graphDatabaseManager = new GraphDatabaseManager();
        graphDatabaseManager.openOrCreateGraphDatabase("data/testing_phrases");

        List<String> testHeadings = new ArrayList<String>();
        testHeadings.add("Introduction");
        testHeadings.add("Design and Implementation");
        testHeadings.add("Overall Comments");

        graphDatabaseManager.setUpGraphDbForAssignment(testHeadings);
        Phrase phrase = new Phrase("Well done!");
        Phrase phrase2 = new Phrase("This was some good quality work");
        graphDatabaseManager.addPhraseForHeading("Overall Comments", phrase);
        graphDatabaseManager.addPhraseForHeading("Overall Comments", phrase2);

        System.out.println("Before phrase update:");
        List<Phrase> overall_comments = graphDatabaseManager.getPhrasesForHeading("Overall Comments");
        overall_comments.forEach(phrase1 -> {
            System.out.println(phrase1.getPhraseAsString() + ": " + phrase1.getUsageCount());
        });

        phrase.incrementUsageCount();
        graphDatabaseManager.updatePhrase("Overall Comments", phrase);

        System.out.println("After phrase update:");
        List<Phrase> overall_comments1 = graphDatabaseManager.getPhrasesForHeading("Overall Comments");
        overall_comments1.forEach(phrase1 -> {
            System.out.println(phrase1.getPhraseAsString() + ": " + phrase1.getUsageCount());
        });
    }


    private void testSetOperations() {
        List<String> testHeadings = new ArrayList<String>();
        testHeadings.add("Introduction");
        testHeadings.add("Design and Implementation");
        testHeadings.add("Overall Comments");

        List<String> testHeadingsCopy = new ArrayList<>(testHeadings);

        List<String> testing = new ArrayList<String>();
        testing.add("Introduction");
        testing.add("Overall Comments");
        testing.add("A new section");

        List<String> testingCopy = new ArrayList<String>(testing);

        testHeadings.removeAll(testing);
        testingCopy.removeAll(testHeadingsCopy);
        List<String> removedList = new ArrayList<>(testHeadings);
        List<String> addedList = new ArrayList<>(testingCopy);

        System.out.println("Things removed: " + removedList);
        System.out.println("Things added: " + addedList);
    }
}
