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
import com.addhen.android.raiburari.annotations.TransformEntity;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * @author Henry Addo
 */
public class TransformEntityAnnotatedClass {

    private TypeElement mTypeElement;

    private String mCanonicalClassName;

    private String mSimpleClassName;

    private Map<String, TransformAnnotatedField> transformAnnotatedElementsMap = new HashMap<>();

    public TransformEntityAnnotatedClass(TypeElement typeElement)
            throws ProcessingException {
        mTypeElement = typeElement;

        // Visibility
        if (typeElement.getModifiers().contains(Modifier.PRIVATE)) {
            throw new ProcessingException(typeElement,
                    "Private classes can not contain @%s annotated fields",
                    TransformEntity.class.getSimpleName());
        }

        // No abstract
        if (typeElement.getModifiers().contains(Modifier.ABSTRACT)) {
            throw new ProcessingException(typeElement,
                    "The class %s is abstract. You can't annotate abstract classes with @%",
                    typeElement.getQualifiedName().toString(),
                    TransformEntity.class.getSimpleName());
        }

        // Constructor check
        boolean isConstructorFound = false;
        for (Element element : typeElement.getEnclosedElements()) {
            if (element.getKind() == ElementKind.CONSTRUCTOR) {
                ExecutableElement constructor = (ExecutableElement) element;
                for (Modifier modifier : constructor.getModifiers()) {
                    if (constructor.getParameters().size() == 0 && constructor.getModifiers()
                            .contains(Modifier.PUBLIC)) {
                        isConstructorFound = true;
                        break;
                    }
                }
            }
        }

        if (!isConstructorFound) {
            throw new ProcessingException(typeElement,
                    "Class %s has %s annotated fields (incl. super class) and therefore"
                            + " must provide a public empty constructor (zero parameters)",
                    typeElement.getQualifiedName().toString(),
                    TransformEntity.class.getSimpleName());
        }

        TransformEntity annotation = typeElement.getAnnotation(TransformEntity.class);

        // Get the full QualifiedTypeName
        try {
            Class<?> clazz = annotation.to();
            mCanonicalClassName = clazz.getCanonicalName();
            mSimpleClassName = clazz.getSimpleName();
        } catch (MirroredTypeException e) {
            DeclaredType classTypeMirror = (DeclaredType) e.getTypeMirror();
            TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
            mCanonicalClassName = classTypeElement.getQualifiedName().toString();
            mSimpleClassName = classTypeElement.getSimpleName().toString();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TransformEntityAnnotatedClass that = (TransformEntityAnnotatedClass) o;

        return mTypeElement.equals(that.mTypeElement);
    }

    @Override
    public int hashCode() {
        return mTypeElement.hashCode();
    }


    /**
     * Scans the class for annotated fields. Also scans inheritance hierarchy.
     */
    public void scanForAnnotatedFields(Types typeUtils, Elements elementUtils)
            throws ProcessingException {

        // Scan inheritance hierarchy recursively to find all annotated fields
        TypeElement currentClass = mTypeElement;
        TypeMirror superClassType;
        Transform annotation = null;
        PackageElement originPackage = elementUtils.getPackageOf(mTypeElement);
        PackageElement superClassPackage;

        Set<VariableElement> annotatedFields = new LinkedHashSet<>();
        Map<String, ExecutableElement> possibleSetterFields = new HashMap<>();

        do {

            // Scan fields
            for (Element e : currentClass.getEnclosedElements()) {
                annotation = e.getAnnotation(Transform.class);
                if (e.getKind() == ElementKind.FIELD && annotation != null) {
                    annotatedFields.add((VariableElement) e);

                } else if (annotation != null) {
                    throw new ProcessingException(e,
                            "%s is of type %s and annotated with @%s, but only Fields or setter "
                                    + "Methods can be annotated with @%s",
                            e.getSimpleName(), e.getKind().toString(),
                            Transform.class.getSimpleName(),
                            Transform.class.getSimpleName());
                }
            }

            superClassType = currentClass.getSuperclass();
            currentClass = (TypeElement) typeUtils.asElement(superClassType);
        } while (superClassType.getKind() != TypeKind.NONE);

        // Check fields
        for (VariableElement e : annotatedFields) {

            annotation = e.getAnnotation(Transform.class);
            currentClass = (TypeElement) e.getEnclosingElement();

            TransformAnnotatedField field = new TransformAnnotatedField(e, annotation);

            // Check field visibility of super class field
            if (currentClass != mTypeElement && !field.getField()
                    .getModifiers()
                    .contains(Modifier.PUBLIC)) {

                superClassPackage = elementUtils.getPackageOf(currentClass);

                if ((superClassPackage != null && originPackage == null) || (
                        superClassPackage == null
                                && originPackage != null) || (superClassPackage != null
                        && !superClassPackage.equals(
                        originPackage)) || (originPackage != null && !originPackage.equals(
                        superClassPackage))) {

                    throw new ProcessingException(e,
                            "The field %s in class %s can not be accessed from it transform class because of "
                                    + "visibility issue. Either move class %s into the same package "
                                    + "as %s or make the field %s public",
                            field.getFieldName(), field.getQualifiedSurroundingClassName(),
                            mTypeElement.getQualifiedName().toString(),
                            field.getQualifiedSurroundingClassName(),
                            field.getFieldName(), Transform.class.getSimpleName());
                }
            }

            TransformAnnotatedField existingTransformAnnotatedField = transformAnnotatedElementsMap
                    .get(field.getFieldName());
            if (existingTransformAnnotatedField != null) {
                throw new ProcessingException(e,
                        "The field %s in class %s is annotated with @%s with transform name = \"%s\" "
                                + "but this transform name is already used by %s in class %s",
                        field.getFieldName(), field.getQualifiedSurroundingClassName(),
                        Transform.class.getSimpleName(), field.getFieldName(),
                        existingTransformAnnotatedField.getElementName(),
                        existingTransformAnnotatedField.getQualifiedSurroundingClassName());
            }

            transformAnnotatedElementsMap.put(field.getFieldName(), field);
        }
    }

    /**
     * Gets the TypeElement representing this class
     *
     * @return the TypeElement
     */
    public TypeElement getElement() {
        return mTypeElement;
    }

    /**
     * Gets the full qualified class name of this annotated class
     *
     * @return full qualified class name
     */
    public String getCanonicalName() {
        return mCanonicalClassName;
    }

    /**
     * Gets the simple class name
     *
     * @return simple class name
     */
    public String getSimpleClassName() {
        return mSimpleClassName;
    }

    /**
     * Get the Elements annotated with {@link Transform}
     *
     * @return annotated elements
     */
    public Collection<TransformAnnotatedField> getTransformAnnotatedElements() {
        return transformAnnotatedElementsMap.values();
    }
}
