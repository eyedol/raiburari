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

package com.addhen.android.raiburari.sample.app.presentation.di.modules;

import com.addhen.android.raiburari.domain.usecase.Usecase;
import com.addhen.android.raiburari.presentation.di.qualifier.ActivityScope;
import com.addhen.android.raiburari.sample.app.data.repository.UserDataRepository;
import com.addhen.android.raiburari.sample.app.domain.repository.UserRepository;
import com.addhen.android.raiburari.sample.app.domain.usecase.GetUserListUsecase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides user related classes
 *
 * @author Henry Addo
 */
@Module
public class UserModule {

    public UserModule() {
    }

    @Provides
    @ActivityScope
    @Named("userList")
    Usecase provideGetUserListUseCase(
            GetUserListUsecase getUserListUseCase) {
        return getUserListUseCase;
    }


    @Provides
    @ActivityScope
    UserRepository provideUserRepository(UserDataRepository userDataRepository) {
        return userDataRepository;
    }
}
