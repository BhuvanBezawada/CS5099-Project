package database;

import model.Phrase;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GraphDatabaseManager {

    GraphDatabaseService graphDb;

    public GraphDatabaseManager() {
    }

    public boolean openOrCreateGraphDatabase(String path) {
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File(path));
        return graphDb != null;
    }

    public void setUpGraphDbForAssignment(List<String> headings) {
        try (Transaction tx = graphDb.beginTx()) {

            for (int i = 0; i < headings.size(); i++) {
                String currentHeading = headings.get(i);

                String nextHeading;
                if (i + 1 >= headings.size()) {
                    nextHeading = "null";
                } else {
                    nextHeading = headings.get(i + 1);
                }

                Node node = graphDb.createNode();
                node.addLabel(Label.label("Heading"));
                node.setProperty("heading", currentHeading);
                node.setProperty("followed_by", nextHeading);
            }

            tx.success();
        } catch (TransactionFailureException e) {
            e.printStackTrace();
        }
    }


    private void addNewNode(Phrase phrase) {
        try (Transaction tx = graphDb.beginTx()) {

            Node phraseNode = graphDb.createNode();
            phraseNode.addLabel(Label.label("Phrase"));
            phraseNode.setProperty("phrase", phrase.getPhraseAsString());
            phraseNode.setProperty("usageCount", phrase.getUsageCount());
            tx.success();
        }
    }


    public void updatePhrasesForHeading(String heading, List<String> oldPhrases, List<String> newPhrases) {
        // Get the current phrases for the heading
        // Find which phrases have been removed and added
            // If the usage count of the phrase was 1 and it has been removed, delete it from the database
            // Otherwise another document is using the phrase so leave it in
        // Make those changes to the database


        List<Phrase> phrasesForHeading = getPhrasesForHeading(heading);


        ArrayList<String> oldPhrasesCopy1 = new ArrayList<String>(oldPhrases);
        ArrayList<String> newPhrasesCopy1 = new ArrayList<String>(newPhrases);

        ArrayList<String> oldPhrasesCopy2 = new ArrayList<String>(oldPhrases);
        ArrayList<String> newPhrasesCopy2 = new ArrayList<String>(newPhrases);

        oldPhrasesCopy1.removeAll(newPhrasesCopy1);
        newPhrasesCopy2.removeAll(oldPhrasesCopy2);

        ArrayList<String> removedPhrases = new ArrayList<String>(oldPhrasesCopy1);
        ArrayList<String> addedPhrases = new ArrayList<String>(newPhrasesCopy2);
        System.out.println("DEBUG: new phrases added: " + addedPhrases);

        // Filter out phrases we have identified as those to remove
        List<Phrase> filteredForRemoval = phrasesForHeading
                .stream()
                .filter(phrase -> removedPhrases.contains(phrase.getPhraseAsString()))
                .collect(Collectors.toList());

        // Check the usage count of the phrases to remove
        filteredForRemoval.forEach(phraseToRemove -> {
            if (phraseToRemove.getUsageCount() == 1) {
                removePhrase(heading, phraseToRemove.getPhraseAsString());
            } else {
                phraseToRemove.decrementUsageCount();
                updatePhrase(heading, phraseToRemove);
            }
        });

        // Filter out phrases that are being reused
        List<Phrase> filteredForAddition = phrasesForHeading
                .stream()
                .filter(phrase -> addedPhrases.contains(phrase.getPhraseAsString()))
                .collect(Collectors.toList());
        List<String> updated = new ArrayList<>();

        // Only add to database if phrase is new, otherwise update counter of existing phrase
        filteredForAddition.forEach(phraseToAdd -> {
            phraseToAdd.incrementUsageCount();
            updatePhrase(heading, phraseToAdd);
            updated.add(phraseToAdd.getPhraseAsString());
        });

        // Add any new phrases
        addedPhrases.removeAll(updated);
        addedPhrases.forEach(phraseToAdd -> {
            addPhraseForHeading(heading, new Phrase(phraseToAdd));
            System.out.println("[DEBUG] added phrase: " + phraseToAdd);
        });

        System.out.println("[DEBUG] updated phrases for heading: " + heading);
    }


    public void updatePhrase(String heading, Phrase phrase) {
        try (Transaction tx = graphDb.beginTx()){
            graphDb.execute(
                    "MATCH (h:Heading {heading: '" + heading + "'}) " +
                            "MATCH (p:Phrase {phrase: '" + phrase.getPhraseAsString() + "'})" +
                            "SET p.usageCount = " + phrase.getUsageCount());

            tx.success();

            System.out.println("Updated phrase usage count.");
        }
    }


    public void removePhrase(String heading, String phrase) {
        Map<String, Object> params = new HashMap<>();
        params.put( "heading", heading);
        params.put("phrase", phrase);

        try (Transaction tx = graphDb.beginTx()){
            graphDb.execute(
                    "MATCH (h:Heading {heading: $heading}) " +
                            "MATCH (p:Phrase {phrase: $phrase})" +
                            "DETACH DELETE p");

            tx.success();

            System.out.println("Deleted phrase from heading.");
        }
    }

    public void addPhraseForHeading(final String heading, Phrase phrase) {
        addNewNode(phrase);

        Map<String, Object> params = new HashMap<>();
        params.put( "heading", heading);
        params.put("phrase", phrase.getPhraseAsString());
        params.put("usageCount", phrase.getUsageCount());

        String query = "MATCH (h:Heading {heading: $heading}) " +
                "MATCH (p:Phrase {phrase: $phrase, usageCount: $usageCount}) " +
                "CREATE (h)-[rel:CONTAINS]->(p)";

        try (Transaction tx = graphDb.beginTx()){
            graphDb.execute(query, params);

            tx.success();

            System.out.println("Linked phrase to heading.");
        }
    }

    public List<Phrase> getPhrasesForHeading(String heading) {

        Map<String, Object> params = new HashMap<>();
        params.put( "heading", heading);

        String query = "MATCH (h:Heading {heading: $heading}) " +
                "-[rel:CONTAINS]->(p:Phrase)" +
                "RETURN p";

        try (Transaction tx = graphDb.beginTx()) {
            Result results = graphDb.execute(query, params);

            //System.out.println("[DEBUG] results found for heading: " + heading + " = " + results.resultAsString());

            List<Phrase> phrasesForHeading = new ArrayList<>();

            results.stream().forEach(result -> {
                System.out.println("result" + result);
                Node node = (Node) result.get("p");
                Phrase phrase = new Phrase(node.getProperty("phrase").toString());
                phrase.setUsageCount(Integer.parseInt(node.getProperty("usageCount").toString()));
                phrasesForHeading.add(phrase);
            });

            System.out.println("list: " + phrasesForHeading);

            return phrasesForHeading;
        }
    }
}
