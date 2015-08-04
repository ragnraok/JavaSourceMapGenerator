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
                String qualifiedName = null;
                if (outerClassName != null) {
                    qualifiedName = buildClassName(this.outerClassName, classTree.getSimpleName().toString());
                } else {
                    qualifiedName = buildClassName(this.packageName, classTree.getSimpleName().toString());
                }
                currentClassName = qualifiedName;
                this.classNameMaps.addClass(qualifiedName);
            } else if (this.outerClassName != null) {
                currentClassName = buildClassName(this.outerClassName, classTree.getSimpleName().toString());
            }
            for (Tree member : classTree.getMembers()) {
                if (member instanceof JCTree.JCClassDecl) {
                    JCTree.JCClassDecl classDecl = (JCTree.JCClassDecl) member;
                    String simpleName = classDecl.getSimpleName().toString();
                    String qualifiedName = null;
                    if (this.outerClassName != null) {
                        qualifiedName = buildClassName(this.outerClassName, simpleName);
                    } else {
                        qualifiedName = buildClassName(this.currentClassName, simpleName);
                    }
                    this.classNameMaps.addClass(qualifiedName);
                    new ClassTreeVisitor().inspectClass(this.packageName, this.classNameMaps, classDecl, qualifiedName, true);
                }
            }
        }
    }
    
    public ClassNameMaps getResult() {
        return this.classNameMaps;
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
