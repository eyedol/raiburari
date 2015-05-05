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

package com.addhen.android.raiburari.sample.app.data.repository.datasource;

import com.addhen.android.raiburari.sample.app.data.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class DummyDataSource implements UserDataSource {

    @Override
    public Observable<List<UserEntity>> getUserEntityList() {
        return Observable.create(new Observable.OnSubscribe<List<UserEntity>>() {
            @Override
            public void call(Subscriber<? super List<UserEntity>> subscriber) {
                UserEntity userEntity = new UserEntity();
                userEntity._id = 1l;
                userEntity.description = "Hey there";
                userEntity.fullName = "Henry Addo";
                userEntity.email = "addhenemail";

                UserEntity userEntity2 = new UserEntity();
                userEntity2._id = 2l;
                userEntity2.email = "emailaddress";
                userEntity2.description = "Hey there";
                userEntity2.fullName = "Ebony Mathis";

                UserEntity userEntity3 = new UserEntity();
                userEntity3._id = 3l;
                userEntity3.email = "emailaddress";
                userEntity3.description = "Hey there";
                userEntity3.fullName = "FindReels";

                List<UserEntity> listUser = new ArrayList<>();
                listUser.add(userEntity);
                listUser.add(userEntity2);
                listUser.add(userEntity3);
                subscriber.onNext(listUser);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<UserEntity> getUserEntityDetails(int userId) {
        return null;
    }
}
