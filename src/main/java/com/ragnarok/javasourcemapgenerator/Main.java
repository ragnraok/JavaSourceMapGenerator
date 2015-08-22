package com.ragnarok.javasourcemapgenerator;

import com.ragnarok.javasourcemapgenerator.util.Log;

import java.io.FileNotFoundException;

/**
 * Created by ragnarok on 15/8/5.
 */
public class Main {
    
    public static final String TAG = "JavaSourceMapGenerator.Main";
    
    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();
        String path = args[0];
        String outputFileName = args[1];
        Log.i(TAG, "directory: %s", path);
        JavaFileScanner fileScanner = new JavaFileScanner(path, 4);
        try {
            ClassNameMaps classNameMaps = fileScanner.scanAllJavaSources();
            SourceMapWriter.writeClassNameMaps(outputFileName, classNameMaps);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "scan failed: %s", e.getMessage());
        }
        long endTime = System.currentTimeMillis();
        Log.i(TAG, "totally use %dms", endTime - startTime);
    }
}
