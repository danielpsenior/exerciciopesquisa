package com.senior.research.converter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class MongoGeolifeTrajetoriesConverter implements GeolifeTrajetoriesConverter {

    @Override
    public StringBuilder convertCenario01(File fileToConvert) {
        StringBuilder outputValue = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileToConvert));
            String line = null;

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                outputValue.append("{\"taxi\" : ");
                outputValue.append(values[0]);
                outputValue.append(",\"date\" : ISODate(\"");
                outputValue.append(values[1].replaceFirst(" ", "T"));
                outputValue.append("Z\"),");
                outputValue.append("\"coordinates\" : [");
                outputValue.append(values[2]);
                outputValue.append(",");
                outputValue.append(values[3]);
                outputValue.append("]");
                outputValue.append("}");
                outputValue.append("\n");
            }
            reader.close();
        } catch (Exception e) {
            //life must goes on...
            e.printStackTrace();
        }

        return outputValue;
    }
}
