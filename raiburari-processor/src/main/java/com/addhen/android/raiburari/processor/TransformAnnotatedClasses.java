/*
 * Copyright (c) 2016 Henry Addo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.addhen.android.raiburari.processor;

import com.addhen.android.raiburari.annotations.Transform;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * @author Henry Addo
 */
public class TransformAnnotatedClasses {

    private static final String SUFFIX = "Transformer";

    private String mQualifiedClassName;

    private Set<TransformAnnotatedClass> mItems = new LinkedHashSet<>();

    public TransformAnnotatedClasses(String qualifiedClassName) {
        mQualifiedClassName = qualifiedClassName;
    }

    public void add(TransformAnnotatedClass transformAnnotatedClass) {
        mItems.add(transformAnnotatedClass);
    }

    public void generateClassCode(Elements elements, Filer filer)
            throws IOException {
        TypeElement superClassName = elements.getTypeElement(mQualifiedClassName);
        String transformerClassName = superClassName.getSimpleName() + SUFFIX;
        //String qualifiedTransformerClassName = mQualifiedClassName + SUFFIX;
        String packageName = getPackageName(elements, superClassName);

        //PackageElement pkg = elements.getPackageOf(superClassName);
        //String packageName = pkg.isUnnamed() ? null : pkg.getQualifiedName().toString();
        /*MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE).build();
        TypeSpec transformerClass = TypeSpec.classBuilder(transformerClassName)
                .addJavadoc("Generated class to work with Entity transformation for $T\n",
                        ClassName.get(superClassName))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(constructor)
                .build();*/

        MethodSpec.Builder method = MethodSpec.methodBuilder("map")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.get(superClassName.asType()), "object")
                .returns(TypeName.get(superClassName.asType()));

        method.beginControlFlow("if (object == null)")
                .addStatement("throw new IllegalArgumentException($S)", "object is null!")
                .endControlFlow();

        for (TransformAnnotatedClass item : mItems) {
            method.addStatement("return new $L()", item.getElement().getQualifiedName().toString());
        }

        method.addStatement("throw new IllegalArgumentException($S + object)", "Unknown object = ");

        TypeSpec typeSpec = TypeSpec.classBuilder(transformerClassName)
                .addMethod(method.build())
                .build();

        JavaFile.builder(packageName, typeSpec).build().writeTo(filer);
    }

    /**
     * Check the Element if it's a class and returns the corresponding TypeElement
     *
     * @param e The element to check
     * @return The {@link TypeElement} representing the annotated class
     * @throws ProcessingException If element is not a CLASS
     */
    private TypeElement checkAndGetClass(Element e) throws ProcessingException {
        if (e.getKind() != ElementKind.CLASS) {
            throw new ProcessingException(e,
                    "%s is annotated with @%s but only classes can be annotated with this annotation",
                    e.toString(), Transform.class.getSimpleName());
        }
        return (TypeElement) e;
    }

    private String getPackageName(Elements elements, TypeElement superClassName) {
        PackageElement pkg = elements.getPackageOf(superClassName);
        return pkg.isUnnamed() ? null : pkg.getQualifiedName().toString();
    }
}
