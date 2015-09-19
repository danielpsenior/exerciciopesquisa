package com.senior.research.tests;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.senior.research.converter.GeolifeTrajetoriesConverter;
import com.senior.research.converter.GeolifeTrajetoriesReader;
import com.senior.research.converter.MySQLGeoLifeTrajetoriesConverter;

/**
 * 
 * All the MySQL queries used in the study.
 * 
 * @author Daniel Pereira
 */
public class MySQLQueriesTest {

    private static final String INPUT = "resources/01";
    private static final String OUTPUT = "resources/mysql_data.sql";
    private static Connection connection;
    private static Statement statement;

    @BeforeClass
    public static void beforeClass() throws Exception {
        Class.forName("org.gjt.mm.mysql.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost/geodb", "root", "root");
        statement = connection.createStatement();
    }

    @Before
    public void beforeTest() {
        File folder = new File(INPUT);
        File output = new File(OUTPUT);
        if (output.exists()) {
            assertTrue(output.delete());
        }
        GeolifeTrajetoriesConverter converter = new MySQLGeoLifeTrajetoriesConverter();
        GeolifeTrajetoriesReader.read(folder, output, converter);
    }

    @After
    public void afterTest() throws Exception {
        File output = new File(OUTPUT);
        if (output.exists()) {
            output.delete();
        }
    }

    @AfterClass
    public static void afterClass() throws Exception {
        statement.close();
        connection.close();
    }

    /**
     * Tests the insertion of data into MySQL. This test assumes tha a MySQL instance is running on localhost using the
     * default port. It also assumes that the taxiroute table on geodb schema is empty and both normal an spatial
     * indexes were
     * created on the 'coordinates' field..
     * 
     * @throws Exception if anything goes wrong :(
     */
    @Test
    public void testCenario01() throws Exception {
        long total = 0;
        BufferedReader reader = new BufferedReader(new FileReader(OUTPUT));
        String line = null;
        int count = 0;

        while ((line = reader.readLine()) != null) {
            long begin = System.nanoTime();
            statement.executeUpdate(line);
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
