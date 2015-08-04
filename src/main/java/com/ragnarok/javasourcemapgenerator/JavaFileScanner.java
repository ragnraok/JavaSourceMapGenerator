package com.ragnarok.javasourcemapgenerator;

import com.ragnarok.javasourcemapgenerator.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by ragnarok on 15/8/2.
 */
public class JavaFileScanner {
    
    public static final String TAG = "JavaSourceMapGenerator.JavaFileScanner";

    private String dir;

    private ArrayList<String> allJavaSourcePaths = new ArrayList<>();
    
    public JavaFileScanner(String dir) {
        this.dir = dir;
    }
    
    public ClassNameMaps scanAllJavaSources() throws FileNotFoundException {
        long startTime = System.currentTimeMillis();
        initJavaSourcePaths();
        Log.d(TAG, "source paths: %s\n", allJavaSourcePaths.toString());
        ClassNameMaps result = new ClassNameMaps();
        if (allJavaSourcePaths.size() > 0) {
            for (String path : allJavaSourcePaths) {
                SourceClassParser sourceClassParser = new SourceClassParser(path);
                result.addAll(sourceClassParser.parse());
            }
        }
        long endTime = System.currentTimeMillis();
        Log.i(TAG, "parse finish, used: %dms", endTime - startTime);
        return result;
    }

    private void initJavaSourcePaths() throws FileNotFoundException {
        File rootPath = new File(dir);
        if (!rootPath.exists()) {
            throw new FileNotFoundException(String.format("Directory %s not exist!", dir));
        }
        allJavaSourcePaths.clear();
        initJavaSourcePathsRecursive(rootPath);
    }

    private void initJavaSourcePathsRecursive(File rootPath) {
        File[] children = rootPath.listFiles();
        if (children != null && children.length > 0) {
            for (File child : children) {
                if (child.isFile() && child.getAbsolutePath().endsWith(".java")) {
                    String path = child.getAbsolutePath();
                    allJavaSourcePaths.add(path);
                }
                initJavaSourcePathsRecursive(child);
            }
        }
    }
}
