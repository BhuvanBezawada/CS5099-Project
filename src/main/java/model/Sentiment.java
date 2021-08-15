package model;

import edu.stanford.nlp.pipeline.CoreSentence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * Get the overall sentiment of a list of sentences.
     *
     * @param sentenceList The list of sentences annotated by the NLP library.
     * @return An overall sentiment value.
     */
    public static Sentiment getOverallSentimentOfSentences(List<CoreSentence> sentenceList) {
        // Create a map to store the sentiment counts
        Map<String, Integer> sentimentAndCount = new HashMap<String, Integer>();
        sentimentAndCount.put(Sentiment.VERY_POSITIVE.sentimentAsString, 0);
        sentimentAndCount.put(Sentiment.POSITIVE.sentimentAsString, 0);
        sentimentAndCount.put(Sentiment.NEUTRAL.sentimentAsString, 0);
        sentimentAndCount.put(Sentiment.NEGATIVE.sentimentAsString, 0);
        sentimentAndCount.put(Sentiment.VERY_NEGATIVE.sentimentAsString, 0);

        // Get the sentiment of each sentence
        sentenceList.forEach(sentence -> {
            String sentenceSentiment = sentence.sentiment();
            sentimentAndCount.put(sentenceSentiment, sentimentAndCount.get(sentenceSentiment) + 1);
        });

        // Group the sentiments
        int positiveCounts = sentimentAndCount.get(VERY_POSITIVE.sentimentAsString) + sentimentAndCount.get(POSITIVE.sentimentAsString);
        int negativeCounts = sentimentAndCount.get(VERY_NEGATIVE.sentimentAsString) + sentimentAndCount.get(NEGATIVE.sentimentAsString);
        int neutralCount = sentimentAndCount.get(NEUTRAL.sentimentAsString);

        // Figure out the sentiment with the highest count
        if (positiveCounts > negativeCounts && positiveCounts > neutralCount) {
            return Sentiment.POSITIVE;
        } else if (negativeCounts > positiveCounts && negativeCounts > neutralCount) {
            return Sentiment.NEGATIVE;
        } else if (positiveCounts == neutralCount) {
            return Sentiment.POSITIVE;
        } else if (negativeCounts == neutralCount) {
            return Sentiment.NEGATIVE;
        }

        // Return neutral since everything tied
        return Sentiment.NEUTRAL;
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
