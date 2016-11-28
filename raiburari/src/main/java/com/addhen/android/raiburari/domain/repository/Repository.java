/*
 * Copyright (c) 2015 Henry Addo
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

package com.addhen.android.raiburari.domain.repository;

import java.util.List;

import rx.Observable;

/**
 * Base Interface that represents Repository for fetching {@link E}
 * related data. It provides a list and a singe item of {@link E}
 *
 * @author Henry Addo
 */
public interface Repository<E> {

    /**
     * Get an {@link Observable} which will emit a List of {@link E}.
     */
    Observable<List<E>> getEntities();

    /**
     * Get an {@link Observable} which will emit a {@link E}.
     *
     * @param entityId The entity id used to retrieve the entity data.
     */
    Observable<E> getEntity(final Long entityId);

    /**
     * Add an {@link E} to a storage.
     *
     * @param entity The entity to be added.
     */
    Observable<Long> addEntity(E entity);

    /**
     * Update an existing {@link E} in a storage.
     *
     * @param entity The entity to be updated.
     */
    Observable<Long> updateEntity(E entity);

    /**
     * Delete an existing {@link E} in a storage.
     *
     * @param id The entity's id to be deleted.
     */
    Observable<Long> deleteEntity(Long id);
}
