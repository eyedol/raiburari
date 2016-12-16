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

package com.addhen.android.raiburari.sample.app.presentation.view;

import com.addhen.android.raiburari.sample.app.presentation.model.UserModel;
import java.util.Collection;

/**
 * @author Henry Addo
 */
public interface UserListView extends LoadDataView {

  /**
   * Render a user list in the UI.
   *
   * @param userModelCollection The collection of {@link UserModel} that will be shown.
   */
  void showUserList(Collection<UserModel> userModelCollection);
}
