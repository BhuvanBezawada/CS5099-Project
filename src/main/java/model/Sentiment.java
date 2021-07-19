package model;

public enum Sentiment {
    VERY_POSITIVE("Very Positive"),
    POSITIVE("Positive"),
    NEUTRAL("Neutral"),
    NEGATIVE("Negative");

    public final String sentimentAsString;

    Sentiment(String phraseTypeAsString) {
        this.sentimentAsString = phraseTypeAsString;
    }

    public String getSentimentAsString() {
        return sentimentAsString;
    }
}
