package com.digotsoft.uatc.util;

import java.io.File;
import java.net.URL;

/**
 * @author MaSte
 * @created 25-Jan-18
 */
public class Utils {
    
    public static File[] getResourceFolderFiles ( String folder ) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(folder);
        String path = url.getPath();
        return new File(path).listFiles();
    }
    
}
