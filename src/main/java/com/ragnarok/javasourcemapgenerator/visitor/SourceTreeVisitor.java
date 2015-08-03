package com.ragnarok.javasourcemapgenerator.visitor;


import com.ragnarok.javasourcemapgenerator.ClassNameMaps;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.TreeScanner;

/**
 * Created by ragnarok on 15/8/2.
 */
public class SourceTreeVisitor extends TreeScanner<Void, Void> {
    
    private String packageName = null;
    
    private ClassTreeVisitor classTreeVisitor = new ClassTreeVisitor();
    
    private ClassNameMaps result = new ClassNameMaps();

    @Override
    public Void visitCompilationUnit(CompilationUnitTree node, Void aVoid) {
        packageName = node.getPackageName().toString();
        return super.visitCompilationUnit(node, aVoid);
    }

    @Override
    public Void visitClass(ClassTree node, Void aVoid) {
        classTreeVisitor.inspectClass(this.packageName, result, node, null, false);
        this.result = classTreeVisitor.getResult();
        return null;
    }
    
    public ClassNameMaps getResult() {
        return this.result;
    }
}
