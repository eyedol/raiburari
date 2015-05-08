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

package com.addhen.android.raiburari.presentation.model;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class UserProfile extends Model {

    private String mFullName;

    private String mUsername;

    private String mAvatar;

    public UserProfile(String fullName, String username, String avatar) {
        mFullName = fullName;
        mUsername = username;
        mAvatar = avatar;
    }

    public String getFullName() {
        return mFullName;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getAvatar() {
        return mAvatar;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "id='" + _id + '\'' +
                "mFullName='" + mFullName + '\'' +
                ", mUsername='" + mUsername + '\'' +
                ", mAvatar='" + mAvatar + '\'' +
                '}';
    }
}
