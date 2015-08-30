package com.ragnarok.javasourcemapgenerator;

import com.ragnarok.javasourcemapgenerator.util.Log;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by ragnarok on 15/8/4.
 */
public class ClassNameMaps {
    
    public static final String TAG = "JavaSourceMapGenerator.FileMaps";
    
    private Set<String> classNamesList = new ConcurrentSkipListSet<>();
    
    public void addAll(ClassNameMaps classNameMaps) {
        for (String className : classNameMaps.getAllClassNames()) {
            classNamesList.add(className);
        }
    } 
    
    public void addClass(String className) {
//        Log.d(TAG, "add Class: %s", className);
        classNamesList.add(className);
    }
    
    public Set<String> getAllClassNames() {
        return classNamesList;
    }
    
}
