package com.senior.research.tests;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.bson.Document;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.senior.research.converter.GeolifeTrajetoriesConverter;
import com.senior.research.converter.GeolifeTrajetoriesReader;
import com.senior.research.converter.MongoGeolifeTrajetoriesConverter;

/**
 * 
 * All the MongoDB queries used in the study.
 * 
 * @author Daniel Pereira
 */
public class MongoDBQueriesTest {

    private static final String INPUT = "resources/01";
    private static final String OUTPUT = "resources/mongo_data.json";
    private static MongoClient client;
    private static MongoCollection<Document> taxiroute;

    @BeforeClass
    public static void beforeClass() {
        client = new MongoClient();
        MongoDatabase database = client.getDatabase("geodb");
        taxiroute = database.getCollection("taxiroute");
    }

    @Before
    public void beforeTest() {
        File folder = new File(INPUT);
        File output = new File(OUTPUT);
        if (output.exists()) {
            assertTrue(output.delete());
        }
        GeolifeTrajetoriesConverter converter = new MongoGeolifeTrajetoriesConverter();
        GeolifeTrajetoriesReader.read(folder, output, converter);
    }

    @After
    public void afterTest() {
        File output = new File(OUTPUT);
        if (output.exists()) {
            output.delete();
        }
    }

    @AfterClass
    public static void afterClass() {
        client.close();
    }

    /**
     * Tests the insertion of data into MongoDB. This test assumes tha a mongo server is running on localhost using the
     * default port. It also assumes that the taxiroute collection on geodb database is empty and both normal an
     * 2dsphere indexes were
     * created on the 'coordinates' field.
     * 
     * @throws Exception if anything goes wrong :(
     */
    @Test
    public void testCenario01() throws Exception {
        long total = 0;
        BufferedReader reader = new BufferedReader(new FileReader(OUTPUT));
        String line = null;
        int count = 0;
        Document documentToInsert;

        while ((line = reader.readLine()) != null) {
            documentToInsert = Document.parse(line);
            long begin = System.nanoTime();
            taxiroute.insertOne(documentToInsert);
            long end = System.nanoTime();
            total += (end - begin);

            count++;

            if (count % 100000 == 0) {
                System.out.println(count + " registers inserted.");
            }
        }

        reader.close();
        System.out.println("Total time spent: " + (total / 1000000) + " ms");
    }

}
