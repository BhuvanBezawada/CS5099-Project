package database;


import org.dizitart.no2.*;
import org.dizitart.no2.filters.Filters;

import java.util.ArrayList;
import java.util.Date;

public class TestDatabase {
    public static void main(String[] args) {
        Nitrite db = Nitrite.builder()
                .compressed()
                .filePath("./test.db")
                .openOrCreate("user", "password");


        // Create a Nitrite Collection
        NitriteCollection collection = db.getCollection("test");

        // Create an Object Repository
        //ObjectRepository<Employee> repository = db.getRepository(Employee.class);

        Document doc = Document.createDocument("firstName", "John")
                .put("lastName", "Doe")
                .put("birthDay", new Date())
                .put("data", new byte[] {1, 2, 3})
                .put("fruits", new ArrayList<String>() {{ add("apple"); add("orange"); add("banana"); }})
                .put("note", "a quick brown fox jump over the lazy dog");


        // insert the document
        collection.insert(doc);

        // find a document
        Cursor cursor = collection.find(Filters.eq("firstName", "John"));


        System.out.println("Doc found!");
        System.out.println(cursor.firstOrDefault());

    }
}
