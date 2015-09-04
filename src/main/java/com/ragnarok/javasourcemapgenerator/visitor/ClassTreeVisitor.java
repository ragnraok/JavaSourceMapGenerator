package com.ragnarok.javasourcemapgenerator.visitor;

import com.github.javaparser.ast.body.*;
import com.ragnarok.javasourcemapgenerator.ClassNameMaps;

/**
 * Created by ragnarok on 15/8/4.
 */
public class ClassTreeVisitor {

    public static final String TAG = "JavaSourceMapGenerator.ClassTreeVisitor";

    private String packageName = null;

    private String outerClassName = null; // fully qualified

    private String currentClassName = null; // fully qualified

    private ClassNameMaps classNameMaps = null;

    public void inspectTypeDeclaration(String packageName, ClassNameMaps classNameMaps, TypeDeclaration classTree, String outerClassName, boolean ignoreSelf) {
        this.packageName = packageName;
        this.classNameMaps = classNameMaps;
        this.outerClassName = outerClassName;

        if (classTree != null) {
            if (!ignoreSelf) {
                String qualifiedName = null;
                if (outerClassName != null) {
                    qualifiedName = buildClassName(this.outerClassName, classTree.getName());
                } else {
                    qualifiedName = buildClassName(this.packageName, classTree.getName());
                }
                currentClassName = qualifiedName;
                this.classNameMaps.addClass(qualifiedName);
            } else if (this.outerClassName != null) {
                currentClassName = buildClassName(this.outerClassName, classTree.getName());
            }
            if (classTree.getMembers() != null) {
                for (BodyDeclaration member : classTree.getMembers()) {
                    if (member instanceof ClassOrInterfaceDeclaration) {
                        ClassOrInterfaceDeclaration classDecl = (ClassOrInterfaceDeclaration) member;
                        String simpleName = classDecl.getName();
                        String qualifiedName = null;
                        if (this.outerClassName != null) {
                            qualifiedName = buildClassName(this.outerClassName, simpleName);
                        } else {
                            qualifiedName = buildClassName(this.currentClassName, simpleName);
                        }
                        this.classNameMaps.addClass(qualifiedName);
                        new ClassTreeVisitor().inspectTypeDeclaration(this.packageName, this.classNameMaps, classDecl, qualifiedName, true);
                    } else if (member instanceof AnnotationDeclaration) {
                        AnnotationDeclaration annotationDeclaration = (AnnotationDeclaration) member;
                        String simpleName = annotationDeclaration.getName();
                        String qualifiedName = null;
                        if (this.outerClassName != null) {
                            qualifiedName = buildClassName(this.outerClassName, simpleName);
                        } else {
                            qualifiedName = buildClassName(this.currentClassName, simpleName);
                        }
                        this.classNameMaps.addClass(qualifiedName);
                    } else if (member instanceof EnumDeclaration) {
                        EnumDeclaration enumDeclaration = (EnumDeclaration) member;
                        String simpleName = enumDeclaration.getName();
                        String qualifiedName = null;
                        if (this.outerClassName != null) {
                            qualifiedName = buildClassName(this.outerClassName, simpleName);
                        } else {
                            qualifiedName = buildClassName(this.currentClassName, simpleName);
                        }
                        this.classNameMaps.addClass(qualifiedName);
                    }
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
        } else {
            return prefix + "." + simpleName;
        }
    }
}
