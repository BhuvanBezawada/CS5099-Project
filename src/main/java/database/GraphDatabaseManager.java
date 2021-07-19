package database;

import model.Phrase;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
import java.util.List;

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
            tx.success();
        }
    }

    public void addPhraseToDbForHeading(String heading, Phrase phrase) {
        addNewNode(phrase);

        try (Transaction tx = graphDb.beginTx()){
            graphDb.execute(
                    "MATCH (h:Heading {heading: '" + heading + "'}) " +
                    "MATCH (p:Phrase {phrase: '" + phrase.getPhraseAsString() + "'})" +
                    "CREATE (h)-[rel:CONTAINS]->(p)");

            tx.success();

            System.out.println("Linked phrase to heading.");
        }
    }

    public void getPhrasesForHeading(String heading) {
        try (Transaction tx = graphDb.beginTx()) {
            Result result = graphDb.execute("MATCH (h:Heading {heading: '" + heading + "'}) " +
                    "-[rel:CONTAINS]->(p:Phrase)" +
                    "RETURN h, p");

//            Result result = graphDb.execute("MATCH (p:Phrase) RETURN p");

            System.out.println(result.resultAsString());
        }
    }
}
