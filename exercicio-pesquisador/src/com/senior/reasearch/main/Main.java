package com.senior.reasearch.main;

import java.io.File;

import com.senior.research.converter.GeolifeTrajetoriesConverter;
import com.senior.research.converter.GeolifeTrajetoriesReader;
import com.senior.research.converter.MySQLGeoLifeTrajetoriesConverter;

public class Main {

    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException(String.format("The number of arguments are invalid[expcted: 2, found: %d]", args.length));
        }
        File folder = new File(args[0]);
        File output = new File(args[1]);
        GeolifeTrajetoriesConverter converter = new MySQLGeoLifeTrajetoriesConverter();

        GeolifeTrajetoriesReader.read(folder, output, converter);
    }

}
