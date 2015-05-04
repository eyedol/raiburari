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

package com.addhen.raiburari.sample.domain.usecase;

import com.addhen.android.domain.executor.PostExecutionThread;
import com.addhen.android.domain.executor.ThreadExecutor;
import com.addhen.android.domain.usecase.Usecase;
import com.addhen.raiburari.sample.domain.repository.UserRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author Henry Addo
 */
public class GetUserListUsecase extends Usecase {

    private final UserRepository mUserRepository;

    @Inject
    protected GetUserListUsecase(UserRepository userRepository, ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mUserRepository = userRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return mUserRepository.getEntities();
    }
}
