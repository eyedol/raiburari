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

package com.addhen.raiburari.processor.test;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Henry Addo
 */
public class UserEntityTransformerTest {

    private UserEntityTransformer mUserEntityTransformer;

    private UserEntity mUserEntity;

    @Before
    public void setUp() {
        mUserEntityTransformer = new UserEntityTransformer();
        assertNotNull(mUserEntityTransformer);
        mUserEntity = new UserEntity("Foo Bar");
    }

    @Test
    public void testShouldTransformSingleEntity() {
        User user = mUserEntityTransformer.transform(mUserEntity);
        assertFields(user);
    }

    @Test
    public void testShouldTransformCollectionEntity() {
        List<User> users = mUserEntityTransformer
                .transform(Arrays.asList(mUserEntity, mUserEntity));
        assertNotNull(users);
        assertEquals(2, users.size());
        assertFields(users.get(0));
    }

    private void assertFields(User user) {
        assertNotNull(user);
        assertEquals("Foo Bar", user.fullName);
    }
}
