package database;

import controller.Pair;
import controller.Utilities;
import model.Phrase;

import java.util.ArrayList;
import java.util.List;

public class TestGraphDatabaseManager {

    private GraphDatabaseManager graphDatabaseManager;


    public TestGraphDatabaseManager() {
        graphDatabaseManager = new GraphDatabaseManager();
        graphDatabaseManager.openOrCreateGraphDatabase("data/testing_phrases");
    }

    private void setupHeadings() {
        List<String> testHeadings = new ArrayList<String>();
        testHeadings.add("Introduction");
        testHeadings.add("Design and Implementation");
        testHeadings.add("Overall Comments");

        graphDatabaseManager.setUpGraphDbForAssignment(testHeadings);
    }

    public static void main(String[] args) {

        TestGraphDatabaseManager testGraphDatabaseManager = new TestGraphDatabaseManager();
//        testGraphDatabaseManager.setupHeadings();
        testGraphDatabaseManager.testLinkingPhrases();

//        Phrase phrase = new Phrase("Let's see if this works. I'm going to try and escape *982839//'/\n\n\'!");
//        Phrase phrase2 = new Phrase("This was some good quality work");
//        graphDatabaseManager.addPhraseForHeading("Overall Comments", phrase);
//        graphDatabaseManager.addPhraseForHeading("Overall Comments", phrase2);

//        System.out.println("Before phrase update:");
//        List<Phrase> overall_comments = graphDatabaseManager.getPhrasesForHeading("Overall Comments");
//        overall_comments.forEach(phrase1 -> {
//            System.out.println(phrase1.getPhraseAsString() + ": " + phrase1.getUsageCount());
//        });
//
//        phrase.incrementUsageCount();
//        graphDatabaseManager.updatePhrase("Overall Comments", phrase);
//
//        System.out.println("After phrase update:");
//        List<Phrase> overall_comments1 = graphDatabaseManager.getPhrasesForHeading("Overall Comments");
//        overall_comments1.forEach(phrase1 -> {
//            System.out.println(phrase1.getPhraseAsString() + ": " + phrase1.getUsageCount());
//        });
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


    private void testLinkingPhrases() {
        List<String> oldPhraseList = new ArrayList<String>();

        List<String> newPhraseList = new ArrayList<String>();
        newPhraseList.add("Phrase 1");
        newPhraseList.add("Phrase 3");

//        graphDatabaseManager.addPhraseForHeading("Introduction", new Phrase("Phrase 1"));
//        graphDatabaseManager.addPhraseForHeading("Introduction", new Phrase("Phrase 2"));
//        graphDatabaseManager.addPhraseForHeading("Introduction", new Phrase("Phrase 3"));
//        graphDatabaseManager.addPhraseForHeading("Introduction", new Phrase("Phrase 4"));

        graphDatabaseManager.managePhraseLinks("Introduction", oldPhraseList, newPhraseList);
        graphDatabaseManager.getLinkedPhrases("Introduction");

//        List<Pair<Phrase>> oldPairs = Utilities.getPairs(oldPhraseList);
//        List<Pair<Phrase>> newPairs = Utilities.getPairs(newPhraseList);
//
//        System.out.println(oldPairs);
//        System.out.println(newPairs);
//
//        System.out.println("removals: " + Utilities.getPairsToRemove(oldPairs, newPairs));
//        System.out.println("additions: " + Utilities.getPairsToAdd(oldPairs, newPairs));

//        graphDatabaseManager.addPhraseForHeading("Introduction", new Phrase("Phrase 1"));
//        graphDatabaseManager.addPhraseForHeading("Introduction", new Phrase("Phrase 2"));
//        graphDatabaseManager.addPhraseForHeading("Introduction", new Phrase("Phrase 3"));

//        graphDatabaseManager.linkPhrases("Introduction", phraseList);
//        graphDatabaseManager.getLinkedPhrases();

    }
}
