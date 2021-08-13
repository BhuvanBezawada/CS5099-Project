package controller;

import java.util.Objects;

public class Pair<T> {

    private T itemOne;
    private T itemTwo;

    public Pair(T itemOne, T itemTwo) {
        this.itemOne = itemOne;
        this.itemTwo = itemTwo;
    }

    public T getItemOne() {
        return this.itemOne;
    }

    public T getItemTwo() {
        return this.itemTwo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return Objects.equals(itemOne, pair.itemOne) && Objects.equals(itemTwo, pair.itemTwo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemOne, itemTwo);
    }

    @Override
    public String toString() {
        return "Pair{" +
                "phrase1=" + itemOne +
                ", phrase2=" + itemTwo +
                '}';
    }
}
