package view;

public enum PhraseType {
    CUSTOM("Custom"),
    FREQUENTLY_USED("Frequently Used"),
    INSIGHTS("Insights"),
    EXCLUDED("Excluded");

    public final String phraseTypeAsString;

    PhraseType(String phraseTypeAsString) {
        this.phraseTypeAsString = phraseTypeAsString;
    }

    public String getPhraseTypeAsString() {
        return phraseTypeAsString;
    }
}
