package database;

import controller.Pair;
import controller.Utilities;
import model.Phrase;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
import java.util.*;
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

            // Setup nodes for the headings
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

            // Setup a node for the custom phrases
            createCustomPhrasesNode();

            tx.success();
        } catch (TransactionFailureException e) {
            e.printStackTrace();
        }
    }

    public void managePhraseLinks(List<String> oldList, List<String> newList) {
        
        List<Pair<String>> oldPairs = Utilities.getPairs(oldList);
        List<Pair<String>> newPairs = Utilities.getPairs(newList);

        List<Pair<String>> pairsToRemove = Utilities.getPairsToRemove(oldPairs, newPairs);
        List<Pair<String>> pairsToAdd = Utilities.getPairsToAdd(oldPairs, newPairs);

        System.out.println("Pairs to remove/update: " + pairsToRemove);
        System.out.println("Pairs to add: " + pairsToAdd);

        pairsToRemove.forEach(pair -> {
            if (linkExists(pair.getItemOne(), pair.getItemTwo())) {
                updateLinkUsageCount(pair.getItemOne(), pair.getItemTwo(), -1);
            } // else the node has been deleted, so no need to do anything
        });

        pairsToAdd.forEach(pair -> {
            if (linkExists(pair.getItemOne(), pair.getItemTwo())) {
                updateLinkUsageCount(pair.getItemOne(), pair.getItemTwo(), 1);
            } else {
                createFollowedByLink(pair.getItemOne(), pair.getItemTwo());
            }
        });

        //List<Phrase> phrasesForHeading = getPhrasesForHeading(heading);

//        for (int i = 0; i < phrases.size() - 1; i++) {
//            Phrase first = new Phrase(phrases.get(i));
//            Phrase second = new Phrase(phrases.get(i+1));
//
//            // If both are in the db, check if they are linked otherwise link them
//            if (phrasesForHeading.contains(first) && phrasesForHeading.contains(second)) {
//                if (linkExists(first, second)) {
//                    // Link exists, so update link usage count
//                    updateLinkUsageCount(first, second);
//                } else {
//                    // Link does not exist, so create one
//                    createFollowedByLink(first, second);
//                }
//            }
//        }
    }

    private void updateLinkUsageCount(String first, String second, int update) {
        Map<String, Object> params = new HashMap<>();
        params.put( "phrase1", first);
        params.put( "phrase2", second);
        params.put( "update", update);

        String query = "MATCH (p1:Phrase {phrase: $phrase1})" +
                "-[rel:FOLLOWED_BY]->(p2:Phrase {phrase: $phrase2})" +
                "SET rel.usage = rel.usage + $update";

        try (Transaction tx = graphDb.beginTx()){
            graphDb.execute(query, params);

            tx.success();

            System.out.println("Linked phrases with followed_by relationship");
        }
    }

    private boolean linkExists(String first, String second) {
        Map<String, Object> params = new HashMap<>();
        params.put( "phrase1", first);
        params.put( "phrase2", second);

        String query = "MATCH (p1:Phrase {phrase: $phrase1})" +
                "-[rel:FOLLOWED_BY]->(p2:Phrase {phrase: $phrase2})" +
                "RETURN p1.phrase, p2.phrase";

        boolean retVal = false;

        try (Transaction tx = graphDb.beginTx()) {
            Result results = graphDb.execute(query, params);

            retVal = results.hasNext();

            System.out.println("LINK EXISTENCE CHECK : \n" + results.resultAsString());
        }

        return retVal;
    }

    private void createFollowedByLink(String first, String second) {
        Map<String, Object> params = new HashMap<>();
        params.put( "phrase1", first);
        params.put( "phrase2", second);

        String query = "MATCH (p1:Phrase {phrase: $phrase1}) " +
                "MATCH (p2:Phrase {phrase: $phrase2}) " +
                "CREATE (p1)-[rel:FOLLOWED_BY]->(p2)" +
                "SET rel.usage = 1";

        try (Transaction tx = graphDb.beginTx()){
            graphDb.execute(query, params);

            tx.success();

            System.out.println("Linked phrases with followed_by relationship");
        }
    }

    public void getLinkedPhrases(String heading) {
        Map<String, Object> params = new HashMap<>();
        params.put( "heading", heading);

        String query = "MATCH (h:Heading {heading: $heading})" +
                "MATCH (p1:Phrase) -[rel:FOLLOWED_BY]-> (p2:Phrase)" +
                "RETURN h, p1.phrase, p2.phrase, rel.usage";

        try (Transaction tx = graphDb.beginTx()) {
            Result results = graphDb.execute(query, params);

            System.out.println("LINKED PHRASES RESULTS : \n" + results.resultAsString());
        }
    }


    private void createCustomPhrasesNode() {
        Node node = graphDb.createNode();
        node.addLabel(Label.label("Custom"));
        node.setProperty("nodeName", "customPhrases");
    }

    public void addPhraseToCustomNode(Phrase phrase) {
        // Put custom phrase into the db
        addNewNode(phrase);

        Map<String, Object> params = new HashMap<>();
        params.put("nodeName", "customPhrases");
        params.put("phrase", phrase.getPhraseAsString());
        params.put("usageCount", phrase.getUsageCount());

        String query = "MATCH (n:Custom {nodeName: $nodeName}) " +
                "MATCH (p:Phrase {phrase: $phrase, usageCount: $usageCount}) " +
                "CREATE (n)-[rel:CONTAINS]->(p)";

        try (Transaction tx = graphDb.beginTx()){
            graphDb.execute(query, params);

            tx.success();

            System.out.println("Linked phrase to custom phrases node.");
        }
    }

    // Get custom phrases
    public List<Phrase> getCustomPhrases() {

        Map<String, Object> params = new HashMap<>();
        params.put( "nodeName", "customPhrases");

        String query = "MATCH (n:Custom {nodeName: $nodeName})" +
                "-[rel:CONTAINS]->(p:Phrase)" +
                "RETURN n, p";

        try (Transaction tx = graphDb.beginTx()) {
            Result results = graphDb.execute(query, params);

            List<Phrase> customPhrasesList = new ArrayList<>();
            //System.out.println("CUSTOM PHRASES RESULTS :" + results.resultAsString());

            results.stream().forEach(result -> {
                System.out.println("result" + result);
                Node node = (Node) result.get("p");
                Phrase phrase = new Phrase(node.getProperty("phrase").toString());
                phrase.setUsageCount(Integer.parseInt(node.getProperty("usageCount").toString()));
                customPhrasesList.add(phrase);
            });

            // Sort phrases into ascending order
            Collections.sort(customPhrasesList);

            System.out.println("custom phrases list: " + customPhrasesList);

            return customPhrasesList;
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
        phrasesForHeading.addAll(getCustomPhrases()); // add custom phrases, so we can search if they are being used?

        List<String> removedPhrases = Utilities.getRemovalsFromList(oldPhrases, newPhrases);
        List<String> addedPhrases = Utilities.getAdditionsToList(oldPhrases, newPhrases);

        System.out.println("[DEBUG] phrases to remove: " + removedPhrases);
        System.out.println("[DEBUG] phrases to be added: " + addedPhrases);

        // Filter out phrases we have identified as those to remove
        List<Phrase> filteredForRemoval = phrasesForHeading
                .stream()
                .filter(phrase -> removedPhrases.contains(phrase.getPhraseAsString()))
                .collect(Collectors.toList());

        // Check the usage count of the phrases to remove
        filteredForRemoval.forEach(phraseToRemove -> {
            System.out.println("This needs to be removed: " + phraseToRemove);
            System.out.println("num usages: " + phraseToRemove.getUsageCount());
            if (phraseToRemove.getUsageCount() == 1) {
                System.out.println("removing for good");
                removePhrase(heading, phraseToRemove.getPhraseAsString());
            } else {
                System.out.println("decrementing usage");
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
            Phrase phrase = new Phrase(phraseToAdd);
            phrase.incrementUsageCount();
            addPhraseForHeading(heading, phrase);
            System.out.println("[DEBUG] added new phrase: p: " + phrase + " c:" + phrase.getUsageCount());
        });

        System.out.println("[DEBUG] updated phrases for heading: " + heading);
    }


    public void updatePhrase(String heading, Phrase phrase) {

        Map<String, Object> params = new HashMap<>();
        params.put( "heading", heading);
        params.put("phrase", phrase.getPhraseAsString());
        params.put("usageCount", phrase.getUsageCount());

        String query = "MATCH (h:Heading {heading: $heading}) " +
                "MATCH (p:Phrase {phrase: $phrase}) " +
                "SET p.usageCount = $usageCount";


        try (Transaction tx = graphDb.beginTx()){
            graphDb.execute(query, params);

            tx.success();

            System.out.println("[DEBUG] updated phrase usage count: " + phrase + " - count = " + phrase.getUsageCount());
        }
    }


    public void removePhrase(String heading, String phrase) {
        Map<String, Object> params = new HashMap<>();
        params.put( "heading", heading);
        params.put("phrase", phrase);

        String query = "MATCH (h:Heading {heading: $heading}) " +
                "MATCH (p:Phrase {phrase: $phrase})" +
                "DETACH DELETE p";

        try (Transaction tx = graphDb.beginTx()){
            graphDb.execute(query, params);

            tx.success();

            System.out.println("[DEBUG] deleted phrase: '" + phrase + "' from heading: " + heading);
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
                "RETURN h, p";

        try (Transaction tx = graphDb.beginTx()) {
            Result results = graphDb.execute(query, params);

            //System.out.println("[DEBUG] results found for heading: " + heading + " = " + results.resultAsString());

            List<Phrase> phrasesForHeading = new ArrayList<>();

            //System.out.println("In get phrases for heading RESULTS :"  + results.resultAsString());

            results.stream().forEach(result -> {
                System.out.println("result" + result);
                Node node = (Node) result.get("p");
                Phrase phrase = new Phrase(node.getProperty("phrase").toString());
                phrase.setUsageCount(Integer.parseInt(node.getProperty("usageCount").toString()));
                phrasesForHeading.add(phrase);
            });

            // Sort phrases into ascending order
            Collections.sort(phrasesForHeading);

            System.out.println("phrases for heading: '" + heading + "' list: " + phrasesForHeading);

            return phrasesForHeading;
        }
    }
}
