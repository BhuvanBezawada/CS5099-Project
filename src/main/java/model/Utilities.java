package controller;

import model.Phrase;

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

    public static <T> List<Pair<T>> getPairs(List<T> newList) {
        List<Pair<T>> pairsList = new ArrayList<Pair<T>>();

        for (int i = 0; i < newList.size()-1; i++) {
            pairsList.add(new Pair<T>(newList.get(i), newList.get(i+1)));
        }

        return pairsList;
    }

    public static <T> List<Pair<T>> getPairsToRemove(List<Pair<T>> oldList, List<Pair<T>> newList) {
        return getRemovalsFromList(oldList, newList);
    }

    public static <T> List<Pair<T>> getPairsToAdd(List<Pair<T>> oldList, List<Pair<T>> newList) {
        return getAdditionsToList(oldList, newList);
    }


}
