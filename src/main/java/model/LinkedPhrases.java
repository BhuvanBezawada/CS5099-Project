package model;

public class LinkedPhrases {

    private Phrase first;
    private Phrase second;
    private int count;

    public LinkedPhrases(Phrase first, Phrase second, int count) {
        this.first = first;
        this.second = second;
        this.count = count;
    }

    public Phrase getFirst() {
        return first;
    }

    public Phrase getSecond() {
        return second;
    }

    public int getCount() {
        return count;
    }
}
