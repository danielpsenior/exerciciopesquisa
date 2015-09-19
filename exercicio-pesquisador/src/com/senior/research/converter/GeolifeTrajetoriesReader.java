package com.senior.research.converter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 * Reads the values from the Geolife Trajetories dataset. Available in:
 * http://research.microsoft.com/apps/pubs/?id=152883.
 * 
 * @author Daniel Pereira
 */
public class GeolifeTrajetoriesReader {

    public static void read(File folder, File output, GeolifeTrajetoriesConverter converter) {
        if (!folder.exists()) {
            throw new IllegalArgumentException(String.format("Folder does not exist: %s", folder.getName()));
        }

        if (!folder.isDirectory()) {
            throw new IllegalArgumentException(String.format("%s is not a directory", folder.getName()));
        }

        if (!output.exists()) {
            boolean created;
            try {
                created = output.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (!created) {
                throw new IllegalArgumentException(String.format("File could not be created : %s", output.getName()));
            }
        }

        readRecursive(folder, output, converter);

    }

    private static void readRecursive(File folder, File output, GeolifeTrajetoriesConverter converter) {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                System.out.println("Folder: " + file.getName());
                readRecursive(file, output, converter);
            } else {
                StringBuilder sb = converter.convertCenario01(file);
                try {
                    FileWriter writer = new FileWriter(output, true);
                    writer.append(sb);
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
