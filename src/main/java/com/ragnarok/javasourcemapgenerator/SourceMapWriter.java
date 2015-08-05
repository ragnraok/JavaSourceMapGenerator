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
        FileOutputStream fileOutputStream = null;
        PrintWriter writer = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            writer = new PrintWriter(fileOutputStream);
            
            for (String className : classNameMaps.getAllClassNames()) {
                writer.println(className);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("writeClassNameMaps error: %s", e.getMessage());
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (writer != null) {
                writer.close();
            }
        }
    }
}
