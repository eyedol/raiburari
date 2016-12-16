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
import com.squareup.javapoet.CodeBlock;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * @author Henry Addo
 */
public class TransformAnnotatedField {

  private VariableElement mVariableElement;

  private TypeElement mTypeElement;

  private String mFieldName;

  private String mCanonicalClassName = null;

  public TransformAnnotatedField(VariableElement field, Transform transform)
      throws ProcessingException {

    if (field.getModifiers().contains(Modifier.PRIVATE)) {
      throw new ProcessingException(field,
          "Field %s is declared as private. Field must have at least package visibility",
          field.getSimpleName().toString());
    }

    if (field.getModifiers().contains(Modifier.PROTECTED)) {
      throw new ProcessingException(field,
          "Field %s is declared as protected. Field must have at least package visibility",
          field.getSimpleName().toString());
    }

    if (field.getModifiers().contains(Modifier.FINAL)) {
      throw new ProcessingException(field, "Field %s is declared as final, that is not allowed!",
          field.getSimpleName().toString());
    }

    if (field.getModifiers().contains(Modifier.STATIC)) {
      throw new ProcessingException(field, "Field %s is declared as static. That is not supported!",
          field.getSimpleName().toString());
    }

    mFieldName = transform.name();
    if (mFieldName == null || mFieldName.length() == 0) {
      throw new ProcessingException(field, "The name of the field being transformed into is "
          + "not specified in the in the annotated field %s", field.getSimpleName().toString());
    }

    String transformer = transform.transformer();
    if (transformer != null || transformer.length() > 0) {
      mCanonicalClassName = transformer;
    }
    mVariableElement = field;
  }

  public VariableElement getField() {
    return mVariableElement;
  }

  public String getFieldName() {
    return mFieldName;
  }

  public void generateFieldAssignmentCode(CodeBlock.Builder builder, String src, String dest) {
    if (mCanonicalClassName != null && mCanonicalClassName.length() > 0) {
      builder.addStatement("$L $L = new $L()", mCanonicalClassName, mFieldName + "Transformer",
          mCanonicalClassName);
      builder.addStatement("$L.$L = $L.transform($L.$L)", dest, mFieldName,
          mFieldName + "Transformer", src, mVariableElement.getSimpleName().toString());
    } else {
      builder.addStatement("$L.$L = $L.$L", dest, mFieldName, src,
          mVariableElement.getSimpleName().toString());
    }
  }

  public String getQualifiedSurroundingClassName() {
    TypeElement typeElement = (TypeElement) mVariableElement.getEnclosingElement();
    return typeElement.getQualifiedName().toString();
  }

  public String getElementName() {
    return mVariableElement.getSimpleName().toString();
  }
}
