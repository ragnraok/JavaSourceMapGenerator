package com.ragnarok.javasourcemapgenerator;

import com.github.javaparser.ast.CompilationUnit;
import com.ragnarok.javasourcemapgenerator.visitor.SourceTreeVisitor;
import com.sun.source.tree.CompilationUnitTree;

import java.io.FileNotFoundException;

/**
 * Created by ragnarok on 15/8/5.
 */
public class SourceClassParser {
    
    private String filePath;
    
    private JavaSourceReader sourceReader;
    
    private ClassNameMaps result;
    
    public SourceClassParser(String filePath) {
        this.filePath = filePath;
        sourceReader = new JavaSourceReader(this.filePath);
    }
    
    public ClassNameMaps parse() {
        result = new ClassNameMaps();
        CompilationUnit parseResult = null;
        try {
            parseResult = sourceReader.readSource();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (parseResult != null) {
            SourceTreeVisitor sourceTreeVisitor = new SourceTreeVisitor();
            sourceTreeVisitor.visit(parseResult, null);
            parseResult.accept(sourceTreeVisitor, null);
            result.addAll(sourceTreeVisitor.getResult());
        }
        return result;
    }
}
