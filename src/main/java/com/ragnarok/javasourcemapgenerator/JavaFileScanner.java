package com.ragnarok.javasourcemapgenerator;

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
