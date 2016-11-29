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

package com.addhen.android.raiburari.sample.app.presentation.presenter;

import com.addhen.android.raiburari.domain.exception.DefaultErrorHandler;
import com.addhen.android.raiburari.domain.exception.ErrorHandler;
import com.addhen.android.raiburari.domain.usecase.DefaultSubscriber;
import com.addhen.android.raiburari.domain.usecase.Usecase;
import com.addhen.android.raiburari.presentation.di.qualifier.ActivityScope;
import com.addhen.android.raiburari.presentation.presenter.Presenter;
import com.addhen.android.raiburari.sample.app.domain.entity.User;
import com.addhen.android.raiburari.sample.app.presentation.exception.ErrorMessageFactory;
import com.addhen.android.raiburari.sample.app.presentation.model.UserModel;
import com.addhen.android.raiburari.sample.app.presentation.model.mapper.UserModelMapper;
import com.addhen.android.raiburari.sample.app.presentation.ui.view.UserListView;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
@ActivityScope
public class UserListPresenter extends DefaultSubscriber<List<User>> implements Presenter {

    private UserListView viewListView;

    private final Usecase getUserListUseCase;

    private final UserModelMapper userModelDataMapper;

    @Inject
    public UserListPresenter(@Named("userList") Usecase getUserListUserCase,
            UserModelMapper userModelDataMapper) {
        this.getUserListUseCase = getUserListUserCase;
        this.userModelDataMapper = userModelDataMapper;
    }

    public void setView(@NonNull UserListView view) {
        this.viewListView = view;
    }

    @Override
    public void resume() {
        initialize();
    }

    @Override
    public void pause() {
    }

    @Override
    public void destroy() {
        this.getUserListUseCase.unsubscribe();
    }

    /**
     * Initializes the presenter by start retrieving the user list.
     */
    public void initialize() {
        this.loadUserList();
    }

    /**
     * Loads all users.
     */
    private void loadUserList() {
        this.hideViewRetry();
        this.showViewLoading();
        this.getUserList();
    }

    private void showViewLoading() {
        this.viewListView.showLoading();
    }

    private void hideViewLoading() {
        this.viewListView.hideLoading();
    }

    private void showViewRetry() {
        this.viewListView.showRetry();
    }

    private void hideViewRetry() {
        this.viewListView.hideRetry();
    }

    private void showErrorMessage(ErrorHandler errorBundle) {
        String errorMessage = ErrorMessageFactory.create(this.viewListView.getAppContext(),
                errorBundle.getException());
        this.viewListView.showError(errorMessage);
    }

    private void showUsersCollectionInView(List<User> usersCollection) {
        final Collection<UserModel> userModelsCollection =
                this.userModelDataMapper.map(usersCollection);
        this.viewListView.showUserList(userModelsCollection);
    }

    private void getUserList() {
        this.getUserListUseCase.execute(this);
    }

    @Override
    public void onComplete() {
        this.hideViewLoading();
    }

    @Override
    public void onError(Throwable e) {
        this.hideViewLoading();
        this.showErrorMessage(new DefaultErrorHandler((Exception) e));
        this.showViewRetry();
    }

    @Override
    public void onNext(List<User> users) {
        this.showUsersCollectionInView(users);
    }
}


