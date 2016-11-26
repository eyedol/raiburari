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

import com.addhen.android.raiburari.annotations.TransformEntity;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * @author Henry Addo
 */
public class TransformEntityAnnotatedClasses {

    private static final String SUFFIX = "Transformer";

    private static final String METHOD_NAME = "transform";

    private static final String COLLECTION_VARIABLE_SUFFIX = "Collection";

    private String mQualifiedClassName;

    private Set<TransformEntityAnnotatedClass> mItems = new LinkedHashSet<>();


    public TransformEntityAnnotatedClasses(String qualifiedClassName) {
        mQualifiedClassName = qualifiedClassName;
    }

    public void add(TransformEntityAnnotatedClass transformAnnotatedClass) {
        mItems.add(transformAnnotatedClass);
    }

    public void generateClassCode(Messager messager, Elements elements, Filer filer)
            throws IOException {
        TypeElement superClassName = elements.getTypeElement(mQualifiedClassName);

        for (TransformEntityAnnotatedClass item : mItems) {
            TransformEntity annotation = item.getElement().getAnnotation(TransformEntity.class);
            String transformerClassName = item.getElement().getSimpleName() + SUFFIX;
            String packageName = getPackageName(elements, item.getElement());
            if (superClassName != null) {
                String variableName = makeFirstCharLowerCase(
                        superClassName.getSimpleName().toString().toLowerCase());
                String paramVariableName = makeFirstCharLowerCase(
                        item.getElement().getSimpleName().toString());

                // Generate map for single object
                MethodSpec.Builder method = MethodSpec.methodBuilder(METHOD_NAME)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(TypeName.get(item.getElement().asType()), paramVariableName)
                        .returns(TypeName.get(superClassName.asType()));
                method.beginControlFlow("if ($L == null)", paramVariableName)
                        .addStatement("throw new IllegalArgumentException($S)",
                                String.format("%s is null!", paramVariableName))
                        .endControlFlow();
                method.addStatement("$T $L = new $T()", superClassName.asType(), variableName,
                        superClassName.asType());
                method.addCode(generateFieldAssignmentCode(item, paramVariableName, variableName,
                        packageName));
                method.addStatement("return $L", variableName);

                MethodSpec mapMethod = method.build();

                // Generate collection method
                ClassName collection = ClassName.get("java.util", "Collection");
                ClassName arrayList = ClassName.get("java.util", "ArrayList");
                TypeName collectionOfObject = ParameterizedTypeName
                        .get(collection, TypeName.get(item.getElement().asType()));

                ClassName list = ClassName.get("java.util", "List");
                TypeName listOfObjects = ParameterizedTypeName
                        .get(list, TypeName.get(superClassName.asType()));

                String colVariableName = makeFirstCharLowerCase(
                        variableName + COLLECTION_VARIABLE_SUFFIX);

                String listVariableName = makeFirstCharLowerCase(variableName + "List");

                MethodSpec.Builder methodCollection = MethodSpec.methodBuilder(METHOD_NAME)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(collectionOfObject, colVariableName)
                        .returns(listOfObjects)
                        .addStatement("$T $L = new $T<>()", listOfObjects,
                                listVariableName, arrayList)
                        .beginControlFlow("for ($T $L : $L)", item.getElement().asType(),
                                paramVariableName, colVariableName)
                        .addStatement("$T $L = $N($L)", superClassName.asType(), variableName,
                                mapMethod, paramVariableName)
                        .beginControlFlow("if ($L != null)", variableName)
                        .addStatement("$L.add($L)", listVariableName, variableName)
                        .endControlFlow()
                        .endControlFlow()
                        .addStatement("return $L", listVariableName);

                MethodSpec constructor = MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC).build();

                TypeSpec typeSpec = TypeSpec.classBuilder(transformerClassName)
                        .addJavadoc("Transforms a {@link $T} into an {@link $T}\n",
                                TypeName.get(item.getElement().asType()),
                                ClassName.get(superClassName))
                        .addMethod(constructor)
                        .addMethod(mapMethod)
                        .addMethod(methodCollection.build())
                        .build();

                JavaFile.builder(packageName, typeSpec).build().writeTo(filer);
            }
        }
    }

    private CodeBlock generateFieldAssignmentCode(TransformEntityAnnotatedClass clazz, String src,
            String dest, String packName) {
        CodeBlock.Builder blockBuilder = CodeBlock.builder();
        for (TransformAnnotatedField field : clazz.getTransformAnnotatedElements()) {
            field.generateFieldAssignmentCode(blockBuilder, src, dest);
        }
        return blockBuilder.build();
    }

    private String getPackageName(Elements elements, TypeElement superClassName) {
        PackageElement pkg = elements.getPackageOf(superClassName);
        return pkg.isUnnamed() ? null : pkg.getQualifiedName().toString();
    }

    private static String makeFirstCharLowerCase(String inputString) {
        return inputString.substring(0, 1).toLowerCase() + inputString.substring(1);
    }
}
