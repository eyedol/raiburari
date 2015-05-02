/*
 * Copyright (c) 2015. Henry Addo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.addhen.raiburari.domain.repository;

import java.util.List;

import rx.Observable;

/**
 * Base Interface that represents Repository for fetching {@link com.addhen.raiburari.domain.entity.Entity}
 * related data. It provides a list and a singe item of {@link com.addhen.raiburari.domain.entity.Entity}
 *
 * @author Henry Addo
 */
public interface Repository<E> {

    /**
     * Get an {@link rx.Observable} which will emit a List of {@link com.addhen.raiburari.domain.entity.Entity}.
     */
    Observable<List<E>> getEntities();

    /**
     * Get an {@link rx.Observable} which will emit a {@link com.addhen.raiburari.domain.entity.Entity}.
     *
     * @param entityId The entity id used to retrieve the entity data.
     */
    Observable<E> getEntity(final int entityId);
}
