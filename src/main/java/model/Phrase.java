package model;

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
}
