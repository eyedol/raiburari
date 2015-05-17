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

package com.addhen.android.raiburari.sample.app.data.repository.datasource;

import com.addhen.android.raiburari.sample.app.data.entity.UserEntity;

import java.util.List;

import rx.Observable;

/**
 * @author Henry Addo
 */
public interface UserDataSource {

    /**
     * Get an {@link Observable} which will emit a List of {@link UserEntity}.
     */
    Observable<List<UserEntity>> getUserEntityList();

    /**
     * Get an {@link Observable} which will emit a {@link UserEntity} by its id.
     *
     * @param userId The id to retrieve user data.
     */
    Observable<UserEntity> getUserEntityDetails(Long userId);
}
