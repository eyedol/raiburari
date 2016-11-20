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

package com.addhen.android.raiburari.sample.app.data.entity;

import com.addhen.android.raiburari.annotations.Transform;
import com.addhen.android.raiburari.annotations.TransformEntity;
import com.addhen.android.raiburari.data.entity.DataEntity;
import com.addhen.android.raiburari.sample.app.domain.entity.User;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
@TransformEntity(to = User.class)
public class UserEntity extends DataEntity {

    @Transform(name = "fullName")
    public String fullName;

    @Transform(name = "email")
    public String email;

    @Transform(name = "description")
    public String description;

    public UserEntity() {
        // No-op
    }
}
