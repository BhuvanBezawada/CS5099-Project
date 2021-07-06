package database;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
import java.util.Map;

public class TestPhraseDatabase {

    GraphDatabaseService graphDb;

    public TestPhraseDatabase() {
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File("data/phrases"));
    }

    public static void main(String[] args) {
        TestPhraseDatabase phraseDatabase = new TestPhraseDatabase();
//        phraseDatabase.setup();
//        phraseDatabase.findVariations();
//        phraseDatabase.findFollowedBy();
//        phraseDatabase.findPairedWith();
//        phraseDatabase.findMutuallyExclusive();
        phraseDatabase.findPrecededBy();
    }

    private void setup() {
        try (Transaction tx = graphDb.beginTx()) {

            // Create 5 phrase nodes
            Node node1 = graphDb.createNode(Label.label("Phrase"));
            node1.setProperty("text", "This was an excellent practical!");
            node1.setProperty("sentiment", "Very positive");

            Node node2 = graphDb.createNode(Label.label("Phrase"));
            node2.setProperty("text", "A truly exceptional report and practical showing broad knowledge, deep understanding, independent assessment and original analysis");
            node2.setProperty("sentiment", "Very positive");

            Node node3 = graphDb.createNode(Label.label("Phrase"));
            node3.setProperty("text", "A coding practical report showing little grasp of the issues, with no code");
            node3.setProperty("sentiment", "Negative");

            Node node4 = graphDb.createNode(Label.label("Phrase"));
            node4.setProperty("text", "A coding practical containing clear and well-structured code achieving almost all required functionality, together with a clear report showing a good level of understanding");
            node4.setProperty("sentiment", "Positive");

            Node node5 = graphDb.createNode(Label.label("Phrase"));
            node5.setProperty("text", "This was a good practical, but there are some areas for improvement");
            node5.setProperty("sentiment", "Neutral");

            Node node6 = graphDb.createNode(Label.label("Phrase"));
            node6.setProperty("text", "A truly exceptional essay showing that you have understood the problem very well, with independent assessment and original analysis");
            node6.setProperty("sentiment", "Very positive");

            Relationship pairedRelationship = node1.createRelationshipTo(node2, PhraseRelationship.FREQUENTLY_PAIRED_WITH);
            Relationship mutuallyExclusiveRelationship = node1.createRelationshipTo(node3, PhraseRelationship.MUTUALLY_EXCLUSIVE_FROM);
            Relationship followedByRelationship = node4.createRelationshipTo(node5, PhraseRelationship.FOLLOWED_BY);
            Relationship variationOfRelationship = node2.createRelationshipTo (node6, PhraseRelationship.VARIATION_OF);
            Relationship precededByRelationship = node5.createRelationshipTo(node4, PhraseRelationship.PRECEDED_BY);

            // Database operations go here
            tx.success();
            System.out.println("Created graph");
        } catch (Exception e) {
            e.printStackTrace();
        }
        graphDb.shutdown();
    }

    public void findVariations() {
        Result result = graphDb.execute(
                "MATCH (p1:Phrase) -[r:VARIATION_OF]-> (p2:Phrase) " +
                        "RETURN p1.text, p2.text");

        System.out.println("Got a result: \n" + result.resultAsString());
    }

    public void findFollowedBy() {
        Result result = graphDb.execute(
                "MATCH (p1:Phrase) -[r:FOLLOWED_BY]-> (p2:Phrase) " +
                        "RETURN p1.text, p2.text");

        System.out.println("Got a result: \n" + result.resultAsString());
    }


    public void findPairedWith() {
        Result result = graphDb.execute(
                "MATCH (p1:Phrase) -[r:FREQUENTLY_PAIRED_WITH]-> (p2:Phrase) " +
                        "RETURN p1.text, p2.text");

        System.out.println("Got a result: \n" + result.resultAsString());
    }

    public void findMutuallyExclusive() {
        Result result = graphDb.execute(
                "MATCH (p1:Phrase) -[r:MUTUALLY_EXCLUSIVE_FROM]-> (p2:Phrase) " +
                        "RETURN p1.text, p2.text");

        System.out.println("Got a result: \n" + result.resultAsString());
    }

    public void findPrecededBy() {
        Result result = graphDb.execute(
                "MATCH (p1:Phrase) -[r:PRECEDED_BY]-> (p2:Phrase) " +
                        "RETURN p1.text, p2.text");

        //System.out.println("Got a result: \n" + result.resultAsString());
        System.out.println("Columns? \n" + result.columns());

        Map<String, Object> phrasePair = result.stream().findFirst().get();
        System.out.println(phrasePair.get(result.columns().get(0)));
        System.out.println(phrasePair.get(result.columns().get(1)));
    }

}
