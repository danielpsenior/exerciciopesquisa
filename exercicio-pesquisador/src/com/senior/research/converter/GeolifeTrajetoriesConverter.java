package com.senior.research.converter;

import java.io.File;

/**
 * <p>
 * Converter to the Geolife Trajetories dataset. Available in: http://research.microsoft.com/apps/pubs/?id=152883
 * </p>
 * 
 * @author Daniel Pereira
 * 
 */
public interface GeolifeTrajetoriesConverter {

    /**
     * Convert the specified file.
     * 
     * @param fileToConvert the folder with the files to be converted
     * 
     * @return the output generated in the conversion
     */
    public StringBuilder convertCenario01(File fileToConvert);

}
