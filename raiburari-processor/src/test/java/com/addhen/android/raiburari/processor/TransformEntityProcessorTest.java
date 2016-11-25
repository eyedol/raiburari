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

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

/**
 * @author Henry Addo
 */
public class TransformEntityProcessorTest {

    public static final String MOCK_USER_ENTITY_CLASS_NAME = "MockUserEntity";

    public static final String MOCK_LOCATION_ENTITY_CLASS_NAME = "MockLocationEntity";

    public static final String PACKAGE = "package mock;";

    public static final String MOCK_ENTITIES_PACKAGE
            = "com.addhen.android.raiburari.processor.mock.";

    @Test
    public void testProcessorCodeGenerator() {
        StringBuilder builder = new StringBuilder(PACKAGE);
        builder.append("import com.addhen.android.raiburari.annotations.Transform;");
        builder.append("import com.addhen.android.raiburari.annotations.TransformEntity;");
        builder.append(
                "@TransformEntity(to = " + MOCK_ENTITIES_PACKAGE
                        + "MockUser.class) public class "
                        + MOCK_USER_ENTITY_CLASS_NAME
                        + " {");
        builder.append("public " + MOCK_USER_ENTITY_CLASS_NAME + "(){}");
        builder.append("@Transform(name = \"fullName\") public String fullName;");
        builder.append("}");

        Compilation compilation2 = javac()
                .withProcessors(new TransformEntityProcessor())
                .compile(JavaFileObjects
                        .forSourceString(MOCK_USER_ENTITY_CLASS_NAME,
                                builder.toString()));
        assertThat(compilation2).succeeded();
        assertThat(compilation2)
                .generatedSourceFile("mock.MockUserEntityTransformer")
                .hasSourceEquivalentTo(
                        JavaFileObjects
                                .forResource("MockUserEntityTransformer.java"));
    }

    @Test
    public void testProcessorCodeGeneratorWithMapObject() {

        StringBuilder builder = new StringBuilder("package mock;");
        builder.append("import com.addhen.android.raiburari.annotations.Transform;");
        builder.append("import com.addhen.android.raiburari.annotations.TransformEntity;");
        builder.append(
                "@TransformEntity(to =" + MOCK_ENTITIES_PACKAGE
                        + "MockLocation.class) public class "
                        + MOCK_LOCATION_ENTITY_CLASS_NAME
                        + " {");
        builder.append("public " + MOCK_LOCATION_ENTITY_CLASS_NAME + "(){}");
        builder.append("@Transform(name = \"locationName\") public String locationName;");
        builder.append("}");

        Compilation compilation2 = javac()
                .withProcessors(new TransformEntityProcessor())
                .compile(JavaFileObjects
                        .forSourceString(MOCK_LOCATION_ENTITY_CLASS_NAME,
                                builder.toString()));
        assertThat(compilation2).succeeded();
        assertThat(compilation2)
                .generatedSourceFile("mock.MockLocationEntityTransformer")
                .hasSourceEquivalentTo(
                        JavaFileObjects
                                .forResource("MockLocationEntityTransformer.java"));
    }
}
