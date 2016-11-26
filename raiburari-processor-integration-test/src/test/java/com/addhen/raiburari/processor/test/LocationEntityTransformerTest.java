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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Henry Addo
 */
public class LocationEntityTransformerTest {

    LocationEntityTransformer mLocationEntityTransformer;

    @Before
    public void setUp() {
        mLocationEntityTransformer = new LocationEntityTransformer();
    }

    @Test
    public void testShouldTestObjectFieldTransformation() {
        assertNotNull(mLocationEntityTransformer);
        assertFields();
    }

    private void assertFields() {
        LocationEntity locationEntity = new LocationEntity("Location", new UserEntity("Foo Bar"));
        Location location = mLocationEntityTransformer.transform(locationEntity);
        assertNotNull(location);
        assertEquals("Location", location.locationName);
        assertNotNull(location.user);
        assertEquals("Foo Bar", location.user.fullName);
    }
}
