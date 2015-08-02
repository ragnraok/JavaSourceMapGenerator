package com.ragnarok.javasourcemapgenerator.visitor;


import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.util.TreeScanner;

/**
 * Created by ragnarok on 15/8/2.
 */
public class SourceTreeVisitor extends TreeScanner<Void, Void> {

    @Override
    public Void visitImport(ImportTree node, Void aVoid) {
        return super.visitImport(node, aVoid);
    }

    @Override
    public Void visitClass(ClassTree node, Void aVoid) {
        return super.visitClass(node, aVoid);
    }
}
