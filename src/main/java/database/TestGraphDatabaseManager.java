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
        graphDatabaseManager.addPhraseToDbForHeading("Overall Comments", phrase);

        graphDatabaseManager.getPhrasesForHeading("Overall Comments");
    }
}
