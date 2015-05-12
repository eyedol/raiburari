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

package com.addhen.android.raiburari.sample.app.data.repository;

import com.addhen.android.raiburari.sample.app.data.entity.UserEntity;
import com.addhen.android.raiburari.sample.app.data.entity.mapper.UserEntityDataMapper;
import com.addhen.android.raiburari.sample.app.data.repository.datasource.UserDataSource;
import com.addhen.android.raiburari.sample.app.data.repository.datasource.UserDataStoreFactory;
import com.addhen.android.raiburari.sample.app.domain.entity.User;
import com.addhen.android.raiburari.sample.app.domain.repository.UserRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * {@link UserRepository} for retrieving user data.
 */
public class UserDataRepository implements UserRepository {

    private final UserDataStoreFactory userDataStoreFactory;

    private final UserEntityDataMapper userEntityDataMapper;

    final UserDataSource userDataStore;

    private final Func1<List<UserEntity>, List<User>> userListEntityMapper =
            new Func1<List<UserEntity>, List<User>>() {
                @Override
                public List<User> call(List<UserEntity> userEntities) {
                    return UserDataRepository.this.userEntityDataMapper.map(userEntities);
                }
            };

    private final Func1<UserEntity, User>
            userDetailsEntityMapper = new Func1<UserEntity, User>() {
        @Override
        public User call(UserEntity userEntity) {
            return UserDataRepository.this.userEntityDataMapper.map(userEntity);
        }
    };

    /**
     * Constructs a {@link UserRepository}.
     *
     * @param dataStoreFactory     A factory to construct different data source implementations.
     * @param userEntityDataMapper {@link UserEntityDataMapper}.
     */
    @Inject
    public UserDataRepository(UserDataStoreFactory dataStoreFactory,
            UserEntityDataMapper userEntityDataMapper) {
        this.userDataStoreFactory = dataStoreFactory;
        this.userEntityDataMapper = userEntityDataMapper;
        userDataStore = userDataStoreFactory.createDummyDataSource();
    }

    @Override
    public Observable<List<User>> getEntities() {
        return userDataStore.getUserEntityList().map(userListEntityMapper);
    }

    @Override
    public Observable<Long> addEntity(User entity) {
        return null;
    }

    @Override
    public Observable<Long> updateEntity(User entity) {
        return null;
    }

    @Override
    public Observable<Long> deleteEntity(Long id) {
        return null;
    }

    @Override
    public Observable<User> getEntity(Long userId) {
        return userDataStore.getUserEntityDetails(userId).map(userDetailsEntityMapper);
    }

}
