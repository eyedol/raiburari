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
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class DummyDataSource implements UserDataSource {

  @Override public Observable<List<UserEntity>> getUserEntityList() {
    return Observable.create(new ObservableOnSubscribe<List<UserEntity>>() {
      @Override public void subscribe(ObservableEmitter<List<UserEntity>> subscriber) {
        UserEntity userEntity = new UserEntity();
        userEntity.id = 1l;
        userEntity.description = "Hey there";
        userEntity.fullName = "Henry Addo";
        userEntity.email = "addhenemail";

        UserEntity userEntity2 = new UserEntity();
        userEntity2.id = 2l;
        userEntity2.email = "emailaddress";
        userEntity2.description = "Hey there";
        userEntity2.fullName = "Ebony Mathis";

        UserEntity userEntity3 = new UserEntity();
        userEntity3.id = 3l;
        userEntity3.email = "emailaddress";
        userEntity3.description = "Hey there";
        userEntity3.fullName = "FindReels";

        UserEntity userEntity4 = new UserEntity();
        userEntity4.id = 4l;
        userEntity4.email = "emailaddress";
        userEntity4.description = "Hey there 4";
        userEntity4.fullName = "FindReels";

        UserEntity userEntity5 = new UserEntity();
        userEntity5.id = 5l;
        userEntity5.email = "emailaddress";
        userEntity5.description = "Hey there 4";
        userEntity5.fullName = "FindReels";

        UserEntity userEntity6 = new UserEntity();
        userEntity6.id = 6l;
        userEntity6.email = "emailaddress";
        userEntity6.description = "Hey there 4";
        userEntity6.fullName = "FindReels";

        UserEntity userEntity7 = new UserEntity();
        userEntity7.id = 7l;
        userEntity7.email = "emailaddress";
        userEntity7.description = "Hey there 4";
        userEntity7.fullName = "FindReels";

        UserEntity userEntity8 = new UserEntity();
        userEntity8.id = 8l;
        userEntity8.email = "emailaddress";
        userEntity8.description = "Hey there 4";
        userEntity8.fullName = "FindReels";

        List<UserEntity> listUser = new ArrayList<>();
        listUser.add(userEntity);
        listUser.add(userEntity2);
        listUser.add(userEntity3);
        subscriber.onNext(listUser);
        subscriber.onComplete();
      }
    });
  }

  @Override public Observable<UserEntity> getUserEntityDetails(Long userId) {
    return Observable.just(null);
  }
}
