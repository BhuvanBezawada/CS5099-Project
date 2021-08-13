package model;

/**
 * Sentiment Enum.
 */
public enum Sentiment {
    // Enum values
    VERY_POSITIVE("Very positive"),
    POSITIVE("Positive"),
    NEUTRAL("Neutral"),
    NEGATIVE("Negative"),
    VERY_NEGATIVE("Very negative");

    // Instance variable
    public final String sentimentAsString;

    /**
     * Constructor.
     *
     * @param sentimentTypeAsString Th sentiment as a string.
     */
    Sentiment(String sentimentTypeAsString) {
        this.sentimentAsString = sentimentTypeAsString;
    }

    /**
     * Get the string representation of the sentiment.
     *
     * @return The string representation of the sentiment.
     */
    public String getSentimentAsString() {
        return sentimentAsString;
    }
}
