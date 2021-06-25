package database;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;

public class TestGraphDatabase {

//    GraphDatabaseFactory graphDbFactory;
//    GraphDatabaseService graphDb;

    GraphDatabaseService graphDb;
    Node firstNode;
    Node secondNode;
    Relationship relationship;

    public TestGraphDatabase() {
//        graphDbFactory = new GraphDatabaseFactory();
//        graphDb = graphDbFactory.newEmbeddedDatabase(new File("data/cars"));
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( new File("data/testing") );
    }

    public void createGraph() {
        try ( Transaction tx = graphDb.beginTx() )
        {

            firstNode = graphDb.createNode(Label.label("Node"));
            firstNode.setProperty( "name", "Dean" );
            secondNode = graphDb.createNode(Label.label("Node"));
            secondNode.setProperty( "name", "Castiel" );

            relationship = firstNode.createRelationshipTo( secondNode, RelationshipType.withName("knows") );
            relationship.setProperty( "time", "20 years" );

            // Database operations go here
            tx.success();
            System.out.println("Created graph");
        }
        graphDb.shutdown();
    }

    public void query() {
        Result result = graphDb.execute(
                "MATCH (n:Node) -[knows]-> (p:Node) " +
                        "RETURN n.name, knows.time, p.name");

        System.out.println("Got a result: " + result.resultAsString());

    }





    public static void main(String[] args) {
//        TestGraphDatabase testGraphDatabase = new TestGraphDatabase();
//        testGraphDatabase.testGraph();
//        testGraphDatabase.queryGraph();
        TestGraphDatabase testGraphDatabase = new TestGraphDatabase();
//        testGraphDatabase.createGraph();
        testGraphDatabase.query();
    }

//    public void testGraph() {
//        graphDb.beginTx();
//        Node car = graphDb.createNode(Label.label("Car"));
//        car.setProperty("make", "tesla");
//        car.setProperty("model", "model3");
//
//        Node owner = graphDb.createNode(Label.label("Person"));
//        owner.setProperty("firstName", "baeldung");
//        owner.setProperty("lastName", "baeldung");
//
//        owner.createRelationshipTo(car, RelationshipType.withName("owner"));
//    }
//
//    public void queryGraph() {
//        Result result = graphDb.execute(
//                "MATCH (c:Car) <-[owner]- (p:Person) " +
//                        "WHERE c.make = 'tesla'" +
//                        "RETURN p.firstName, p.lastName");
//
//        System.out.println("Got a result: " + result.resultAsString());
//    }

}