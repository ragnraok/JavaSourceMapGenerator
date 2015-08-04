package com.ragnarok.javasourcemapgenerator;

import java.io.FileNotFoundException;

/**
 * Created by ragnarok on 15/8/5.
 */
public class Main {
    
    public static void main(String[] args) {
        JavaFileScanner fileScanner = new JavaFileScanner("src");
        try {
            fileScanner.scanAllJavaSources();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static class TestInnerClass {
        class TestInnerClassClass {

        }
    }
}
