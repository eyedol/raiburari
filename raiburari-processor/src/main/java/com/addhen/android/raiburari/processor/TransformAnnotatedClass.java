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

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;

/**
 * @author Henry Addo
 */
public class TransformAnnotatedClass {

    private TypeElement mTypeElement;

    private String mCanonicalClassName;

    private String mSimpleClassName;

    public TransformAnnotatedClass(TypeElement typeElement)
            throws ProcessingException {
        mTypeElement = typeElement;

        // Visibility
        if (typeElement.getModifiers().contains(Modifier.PRIVATE)) {
            throw new ProcessingException(typeElement,
                    "Private classes can not contain @%s annotated fields",
                    Transform.class.getSimpleName());
        }

        // No abstract
        if (typeElement.getModifiers().contains(Modifier.ABSTRACT)) {
            throw new ProcessingException(typeElement,
                    "The class %s is abstract. You can't annotate abstract classes with @%",
                    typeElement.getQualifiedName().toString(), Transform.class.getSimpleName());
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
                    Transform.class.getSimpleName());
        }

        Transform annotation = typeElement.getAnnotation(Transform.class);

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

        TransformAnnotatedClass that = (TransformAnnotatedClass) o;

        return mTypeElement.equals(that.mTypeElement);
    }

    @Override
    public int hashCode() {
        return mTypeElement.hashCode();
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
}
