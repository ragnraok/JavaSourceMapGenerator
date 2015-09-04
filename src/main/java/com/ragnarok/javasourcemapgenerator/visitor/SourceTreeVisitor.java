package com.ragnarok.javasourcemapgenerator.visitor;


import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.ragnarok.javasourcemapgenerator.ClassNameMaps;
import com.ragnarok.javasourcemapgenerator.util.Log;

/**
 * Created by ragnarok on 15/8/2.
 */
public class SourceTreeVisitor extends VoidVisitorAdapter {
    //TODO: Enum parse support
    
    public static final String TAG = "JavaSourceMapGenerator.SourceTreeVisitor";
    
    private String packageName = null;
    
    private ClassTreeVisitor classTreeVisitor = new ClassTreeVisitor();
    
    private ClassNameMaps result = new ClassNameMaps();


    @Override
    public void visit(CompilationUnit node, Object arg) {
        if (node == null || node.getPackage() == null || node.getPackage().getName() == null) {
            return;
        }
        if (packageName == null) {
            packageName = node.getPackage().getName().toString();
        }
        super.visit(node, arg);
        Log.d(TAG, "visit CompilationUnit, packageName: %s", packageName);
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration node, Object arg) {
        if (node == null) {
            return;
        }
        if (packageName == null && node.getParentNode() instanceof CompilationUnit) {
            CompilationUnit compilationUnit = (CompilationUnit) node.getParentNode();
            packageName = compilationUnit.getPackage().getName().getName();
        }
        if (node.getName() != null) {
            Log.d(TAG, "visit ClassOrInterfaceDeclaration, packageName: %s, className: %s", packageName, node.getName());
            classTreeVisitor.inspectTypeDeclaration(this.packageName, result, node, null, false);
            result = classTreeVisitor.getResult();
        }
        
    }

    @Override
    public void visit(AnnotationDeclaration node, Object arg) {
        if (node == null) {
            super.visit(node, arg);
        }
        if (packageName == null && node.getParentNode() instanceof CompilationUnit) {
            CompilationUnit compilationUnit = (CompilationUnit) node.getParentNode();
            packageName = compilationUnit.getPackage().getName().getName();
        }
        classTreeVisitor.inspectTypeDeclaration(this.packageName, result, node, null, false);
        result = classTreeVisitor.getResult();
    }
    
    @Override
    public void visit(EnumDeclaration node, Object arg) {
        if (node == null) {
            super.visit(node, arg);
        }
        if (packageName == null && node.getParentNode() instanceof CompilationUnit) {
            CompilationUnit compilationUnit = (CompilationUnit) node.getParentNode();
            packageName = compilationUnit.getPackage().getName().getName();
        }
        classTreeVisitor.inspectTypeDeclaration(this.packageName, result, node, null, false);
        result = classTreeVisitor.getResult();
        
    }

    public ClassNameMaps getResult() {
        return this.result;
    }
}
