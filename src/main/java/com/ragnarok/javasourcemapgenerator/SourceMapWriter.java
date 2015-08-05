package com.ragnarok.javasourcemapgenerator;

import com.ragnarok.javasourcemapgenerator.util.Log;

import java.io.*;

/**
 * Created by ragnarok on 15/8/6.
 */
public class SourceMapWriter {
    
    public static final String TAG = "JavaSourceMapGenerator.SourceMapWriter";
    
    public static void writeClassNameMaps(String outputFileName, ClassNameMaps classNameMaps) {
        File file = new File(outputFileName);
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(file);
            
            for (String className : classNameMaps.getAllClassNames()) {
                Log.i(TAG, "write class: %s", className);
                writer.println(className);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("writeClassNameMaps error: %s", e.getMessage());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
