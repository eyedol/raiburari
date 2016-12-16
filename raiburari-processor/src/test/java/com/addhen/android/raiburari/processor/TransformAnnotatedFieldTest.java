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
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.BDDMockito.given;

/**
 * @author Henry Addo
 */
public class TransformAnnotatedFieldTest {

  @Rule public ExpectedException expectedException = ExpectedException.none();

  // Allow chained method calls without throwing NPE
  @Mock(answer = RETURNS_DEEP_STUBS) VariableElement mVariableElement;

  @Mock Transform mTransform;

  @Before public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test public void testShouldCheckIfAnnotatedClassIsPrivate() throws ProcessingException {
    expectedException.expect(ProcessingException.class);
    expectedException.expectMessage(
        "Field null is declared as private. Field must have at least package visibility");
    given(mTransform.name()).willReturn("name");
    given(mVariableElement.getModifiers().contains(Modifier.PRIVATE)).willReturn(true);
    new TransformAnnotatedField(mVariableElement, mTransform);
  }

  @Test public void testShouldCheckIfAnnotatedClassIsProtected() throws ProcessingException {
    expectedException.expect(ProcessingException.class);
    expectedException.expectMessage(
        "Field null is declared as protected. Field must have at least package visibility");
    given(mTransform.name()).willReturn("name");
    given(mVariableElement.getModifiers().contains(Modifier.PROTECTED)).willReturn(true);
    new TransformAnnotatedField(mVariableElement, mTransform);
  }

  @Test public void testShouldCheckIfAnnotatedClassIsFinal() throws ProcessingException {
    expectedException.expect(ProcessingException.class);
    expectedException.expectMessage("Field null is declared as final, that is not allowed!");
    given(mTransform.name()).willReturn("name");
    given(mVariableElement.getModifiers().contains(Modifier.FINAL)).willReturn(true);
    new TransformAnnotatedField(mVariableElement, mTransform);
  }

  @Test public void testShouldCheckIfAnnotatedClassIsStatic() throws ProcessingException {
    expectedException.expect(ProcessingException.class);
    expectedException.expectMessage("Field null is declared as static. That is not supported!");
    given(mTransform.name()).willReturn("name");
    given(mVariableElement.getModifiers().contains(Modifier.STATIC)).willReturn(true);
    new TransformAnnotatedField(mVariableElement, mTransform);
  }

  @Test public void testShouldCheckAnnotatedClassNameIsNull() throws ProcessingException {
    expectedException.expect(ProcessingException.class);
    expectedException.expectMessage(
        "The name of the field being transformed into is not specified in the in the annotated field null");
    given(mTransform.name()).willReturn(null);
    given(mVariableElement.getModifiers().contains(Modifier.PRIVATE)).willReturn(false);
    new TransformAnnotatedField(mVariableElement, mTransform);
  }
}

