package controller;

import model.Phrase;

import javax.rmi.CORBA.Util;
import java.util.ArrayList;
import java.util.List;

public class TestUtils {

    public static void main(String[] args) {
        List<Phrase> oldList = new ArrayList<Phrase>();
        oldList.add(new Phrase("Common"));
        oldList.add(new Phrase("Old 1"));
        oldList.add(new Phrase("Old 2"));

        List<Phrase> newList = new ArrayList<Phrase>();
//        newList.add(new Phrase("Common"));
//        newList.add(new Phrase("Old 1"));
//        newList.add(new Phrase("Old 2"));
//        newList.add(new Phrase("New 1"));
//        newList.add(new Phrase("New 2"));

        List<Phrase> additionsToList = Utilities.getAdditionsToList(oldList, newList);
        System.out.println("Additions " + additionsToList);

        List<Phrase> removalsFromList = Utilities.getRemovalsFromList(oldList, newList);
        System.out.println("Removals " + removalsFromList);

    }
}
