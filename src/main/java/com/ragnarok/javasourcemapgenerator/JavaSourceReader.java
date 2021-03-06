package com.ragnarok.javasourcemapgenerator;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;
import com.sun.tools.javac.api.JavacTool;
import com.sun.tools.javac.file.JavacFileManager;
import com.sun.tools.javac.util.Context;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by ragnarok on 15/8/2.
 */
public class JavaSourceReader {
    private String filePath = null;
    private String filename = null;

    public JavaSourceReader(String filepath) {
        this.filePath = filepath;
    }

    public CompilationUnit readSource() throws FileNotFoundException {
        File file = new File(this.filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("try parse a not exist source file: " + this.filePath);
        }

        this.filename = file.getName();
        try {
            CompilationUnit result = JavaParser.parse(file, "UTF-8", false);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getFilename() {
        return this.filename;
    }
}
