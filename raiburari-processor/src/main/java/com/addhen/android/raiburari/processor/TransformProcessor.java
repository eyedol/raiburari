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

import com.google.auto.service.AutoService;

import com.addhen.android.raiburari.annotations.Transform;
import com.addhen.android.raiburari.annotations.TransformField;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public final class TransformProcessor extends AbstractProcessor {

    private Elements mElements;

    private Types mTypes;

    private Messager mMessager;

    private Filer mFiler;

    private Set<TransformAnnotatedClasses> mAnnotatedClasses = new HashSet<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mMessager = processingEnv.getMessager();
        mTypes = processingEnv.getTypeUtils();
        mElements = processingEnv.getElementUtils();
        mFiler = processingEnv.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(Transform.class.getCanonicalName());
        annotations.add(TransformField.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        mMessager.printMessage(Diagnostic.Kind.WARNING, "Generating messages ");
        try {

            // Scan classes
            for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(Transform.class)) {
                // Check if a class has been annotated with @Transform
                if (annotatedElement.getKind() != ElementKind.CLASS) {
                    throw new ProcessingException(annotatedElement,
                            "Only classes can be annotated with @%s",
                            Transform.class.getSimpleName());
                }

                TypeElement typeElement = (TypeElement) annotatedElement;
                TransformAnnotatedClass annotatedClass = new TransformAnnotatedClass(typeElement);
                String canonicalName = annotatedClass.getCanonicalName();
                TransformAnnotatedClasses annotatedClasses = new TransformAnnotatedClasses(
                        canonicalName);
                mAnnotatedClasses.add(annotatedClasses);
            }

            // Generate code
            for (TransformAnnotatedClasses annotatedClasses : mAnnotatedClasses) {
                annotatedClasses.generateClassCode(mElements, mFiler);
            }
            mAnnotatedClasses.clear();
        } catch (ProcessingException e) {
            error(e.getElement(), e.getMessage());
        } catch (IOException e) {
            error(null, e.getMessage());
        }
        return false;
    }

    public void error(Element e, String msg) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, msg, e);
    }
}
