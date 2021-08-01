package controller;

import java.util.ArrayList;
import java.util.List;

public class Utilities {

    public static <T> List<T> getAdditionsToList(List<T> oldList, List<T> newList) {
        List<T> oldListCopy = new ArrayList<>(oldList);
        List<T> newListCopy = new ArrayList<>(newList);
        newListCopy.removeAll(oldListCopy);
        return newListCopy;
    }

    public static <T> List<T> getRemovalsFromList(List<T> oldList, List<T> newList) {
        List<T> oldListCopy = new ArrayList<>(oldList);
        List<T> newListCopy = new ArrayList<>(newList);
        oldListCopy.removeAll(newListCopy);
        return oldListCopy;
    }


    public static <T> List<T> getIntersection(List<T> oldList, List<T> newList) {
        List<T> oldListCopy = new ArrayList<>(oldList);
        List<T> newListCopy = new ArrayList<>(newList);
        newListCopy.retainAll(oldListCopy);
        return newListCopy;
    }

}
