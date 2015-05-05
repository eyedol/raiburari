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

package com.addhen.android.raiburari.sample.app.data.entity.mapper;

import com.addhen.android.raiburari.sample.app.data.entity.UserEntity;
import com.addhen.android.raiburari.sample.app.domain.entity.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Henry Addo
 */
public class UserEntityDataMapper {

    @Inject
    public UserEntityDataMapper() {
    }

    /**
     * Transform a {@link UserEntity} into an {@link User}.
     *
     * @param userEntity Object to be transformed.
     * @return {@link User} if valid {@link UserEntity} otherwise null.
     */
    public User map(UserEntity userEntity) {
        User user = null;
        if (userEntity != null) {
            user = new User();
            user.description = userEntity.description;
            user.email = userEntity.email;
            user.fullName = userEntity.fullName;
            user._id = userEntity._id;
        }

        return user;
    }

    /**
     * Transform a List of {@link UserEntity} into a Collection of {@link User}.
     *
     * @param userEntityCollection Object Collection to be transformed.
     * @return {@link User} if valid {@link UserEntity} otherwise null.
     */
    public List<User> map(Collection<UserEntity> userEntityCollection) {
        List<User> userList = new ArrayList<>();
        User user;
        for (UserEntity userEntity : userEntityCollection) {
            user = map(userEntity);
            if (user != null) {
                userList.add(user);
            }
        }

        return userList;
    }
}
