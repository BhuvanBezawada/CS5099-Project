package model;

import java.util.Objects;

public class Phrase {

    private String phraseAsString;
    private Sentiment sentiment;
    private int usageCount;

    public Phrase(String phraseAsString) {
        this.phraseAsString = phraseAsString;
    }

    public Sentiment getSentiment() {
        return this.sentiment;
    }

    public void setUsageCount(int usageCount) {
        this.usageCount = usageCount;
    }

    public void setSentiment(Sentiment sentiment) {
        this.sentiment = sentiment;
    }

    public String getPhraseAsString() {
        return this.phraseAsString;
    }

    public int getUsageCount() {
        return this.usageCount;
    }

    public void incrementUsageCount() {
        this.usageCount++;
    }

    public void decrementUsageCount() {
        if (this.usageCount > 0) {
            this.usageCount--;
        }
    }

    @Override
    public String toString() {
        return phraseAsString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phrase phrase = (Phrase) o;
        return Objects.equals(phraseAsString, phrase.phraseAsString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phraseAsString);
    }
}
