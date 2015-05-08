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

package com.addhen.android.raiburari.sample.app.presentation.model.mapper;

import com.addhen.android.raiburari.presentation.di.qualifier.ActivityScope;
import com.addhen.android.raiburari.sample.app.domain.entity.User;
import com.addhen.android.raiburari.sample.app.presentation.model.UserModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.inject.Inject;

/**
 * @author Henry Addo
 */
@ActivityScope
public class UserModelMapper {

    @Inject
    public UserModelMapper() {
    }

    /**
     * Transform a {@link User} into an {@link UserModel}.
     *
     * @param user Object to be transformed.
     * @return {@link UserModel}.
     */
    public UserModel map(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Cannot transform a null value");
        }
        UserModel userModel = new UserModel();
        userModel._id = user._id;
        userModel.description = user.description;
        userModel.fullName = user.fullName;
        userModel.email = user.email;
        return userModel;
    }

    /**
     * Transform a Collection of {@link User} into a Collection of {@link UserModel}.
     *
     * @param usersCollection Objects to be transformed.
     * @return List of {@link UserModel}.
     */
    public Collection<UserModel> map(Collection<User> usersCollection) {
        Collection<UserModel> userModelsCollection;

        if (usersCollection != null && !usersCollection.isEmpty()) {
            userModelsCollection = new ArrayList<>();
            for (User user : usersCollection) {
                userModelsCollection.add(map(user));
            }
        } else {
            userModelsCollection = Collections.emptyList();
        }

        return userModelsCollection;
    }
}
