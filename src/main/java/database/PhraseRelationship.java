package database;

import org.neo4j.graphdb.RelationshipType;

public enum PhraseRelationship implements RelationshipType {
    FOLLOWED_BY,
    PRECEDED_BY,
    MUTUALLY_EXCLUSIVE_FROM,
    VARIATION_OF,
    FREQUENTLY_PAIRED_WITH
}
