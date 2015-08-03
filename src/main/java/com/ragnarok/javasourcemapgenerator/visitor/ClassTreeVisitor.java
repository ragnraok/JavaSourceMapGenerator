package com.ragnarok.javasourcemapgenerator.visitor;

import com.ragnarok.javasourcemapgenerator.ClassNameMaps;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.tree.JCTree;

/**
 * Created by ragnarok on 15/8/4.
 */
public class ClassTreeVisitor {
    
    public static final String TAG = "JavaSourceMapGenerator.ClassTreeVisitor";
    
    private String packageName = null;

    private String outerClassName = null; // fully qualified

    private String currentClassName = null; // fully qualified
    
    private ClassNameMaps classNameMaps = null;
    
    public void inspectClass(String packageName, ClassNameMaps classNameMaps, ClassTree classTree, String outerClassName, boolean ignoreSelf) {
        this.packageName = packageName;
        this.classNameMaps = classNameMaps;
        this.outerClassName = outerClassName;

        if (classTree.getKind() == Tree.Kind.CLASS || classTree.getKind() == Tree.Kind.INTERFACE 
                || classTree.getKind() == Tree.Kind.ANNOTATION_TYPE) {
            if (!ignoreSelf) {
                String qualifiedName = parseClassNameFromSimpleName(classTree.getSimpleName().toString());
                this.classNameMaps.addClass(qualifiedName);
            } else if (outerClassName != null) {
                currentClassName = buildClassName(this.outerClassName, classTree.getSimpleName().toString());
            }
            for (Tree member : classTree.getMembers()) {
                if (member instanceof JCTree.JCClassDecl) {
                    JCTree.JCClassDecl classDecl = (JCTree.JCClassDecl) member;
                    String simpleName = classDecl.getSimpleName().toString();
                    String qualifiedName = parseClassNameFromSimpleName(simpleName);
                    this.classNameMaps.addClass(qualifiedName);
                    new ClassTreeVisitor().inspectClass(this.packageName, this.classNameMaps, classDecl, currentClassName, true);
                }
            }
        }
    }
    
    public ClassNameMaps getResult() {
        return this.classNameMaps;
    }
    
    private String parseClassNameFromSimpleName(String simpleName) {
        String qualifiedName = null;
        if (outerClassName != null) {
            qualifiedName = buildClassName(this.outerClassName, simpleName);
        } else {
            qualifiedName = buildClassName(this.packageName, simpleName);
        }
        return qualifiedName;
    }

    private String buildClassName(String prefix, String simpleName) {
        simpleName = simpleName.replace(".", "");
        if (prefix.endsWith(".")) {
            return prefix + simpleName;
        } else  {
            return prefix + "." + simpleName;
        }
    }
}
