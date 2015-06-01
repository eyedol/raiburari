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

package com.addhen.android.raiburari.sample.app.presentation.ui.fragment;

import com.addhen.android.raiburari.presentation.ui.adapter.ContextMenuAdapter;
import com.addhen.android.raiburari.presentation.ui.fragment.BaseRecyclerViewFragment;
import com.addhen.android.raiburari.presentation.ui.listener.RecyclerViewItemTouchListenerAdapter;
import com.addhen.android.raiburari.presentation.ui.widget.ContextMenu;
import com.addhen.android.raiburari.sample.app.R;
import com.addhen.android.raiburari.sample.app.presentation.di.components.UserComponent;
import com.addhen.android.raiburari.sample.app.presentation.model.UserModel;
import com.addhen.android.raiburari.sample.app.presentation.presenter.UserListPresenter;
import com.addhen.android.raiburari.sample.app.presentation.ui.adapter.UserAdapter;
import com.addhen.android.raiburari.sample.app.presentation.ui.view.UserListView;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Henry Addo
 */
public class MainFragment extends BaseRecyclerViewFragment<UserModel, UserAdapter>
        implements UserListView,
        RecyclerViewItemTouchListenerAdapter.RecyclerViewOnItemClickListener {

    @Inject
    UserListPresenter userListPresenter;

    ContextMenu mContextMenu;

    UserAdapter mUserAdapter;

    private static MainFragment mMainFragment;

    //@InjectView(R.id.toolbar)
    //Toolbar mToolbar;

    //@InjectView(R.id.tab_layout)
    //TabLayout mTabLayout;

    /**
     * BaseFragment
     */
    public MainFragment() {
        super(UserAdapter.class, R.layout.list_users, R.menu.menu_main);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initialize();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.userListPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.userListPresenter.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.userListPresenter.destroy();
    }

    public static MainFragment newInstance() {
        if (mMainFragment == null) {
            mMainFragment = new MainFragment();
        }
        return mMainFragment;
    }


    private void initialize() {
        getComponent(UserComponent.class).inject(this);
        userListPresenter.setView(this);
        //mTabLayout.addTab(mTabLayout.newTab().setText("Today"));
        //mTabLayout.addTab(mTabLayout.newTab().setText("This Week"));
        RecyclerViewItemTouchListenerAdapter itemTouchListenerAdapter
                = new RecyclerViewItemTouchListenerAdapter(
                mBloatedRecyclerView.recyclerView, this);
        mBloatedRecyclerView.addItemDividerDecoration(getActivity());
        mBloatedRecyclerView.recyclerView.addOnItemTouchListener(itemTouchListenerAdapter);
        mUserAdapter = mRecyclerViewAdapter;
        mUserAdapter.setOnUserItemClickListener(new UserAdapter.OnUserItemClickListener() {
            @Override
            public void onMoreClick(View v, int position) {
                if (mContextMenu == null) {
                    manageContextMenu(v, position);
                } else {
                    mContextMenu.hideContextMenu();
                    mContextMenu = null;
                }
            }
        });
    }

    @Override
    public void showUserList(Collection<UserModel> userModelCollection) {
        if (userModelCollection != null) {
            List userList = new ArrayList(userModelCollection);
            mRecyclerViewAdapter.setItems(userList);
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public Context getAppContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void onItemClick(RecyclerView parent, View clickedView, int position) {

    }

    @Override
    public void onItemLongClick(RecyclerView parent, View clickedView, int position) {
        // DO nothing
    }

    public void manageContextMenu(View v, int position) {
        // Set up context menu
        ContextMenuAdapter.ContextMenuItem contextMenuItem = new ContextMenuAdapter.ContextMenuItem(
                "Edit");
        ContextMenuAdapter.ContextMenuItem deleteItem = new ContextMenuAdapter.ContextMenuItem(
                R.drawable.ic_action_add, "Delete");
        List<ContextMenuAdapter.ContextMenuItem> cm = new ArrayList<>();
        cm.add(contextMenuItem);
        cm.add(deleteItem);
        cm.add(ContextMenuAdapter.ContextMenuItem.dividerMenuItem());
        cm.add(new ContextMenuAdapter.ContextMenuItem("Cancel"));

        mContextMenu = new ContextMenu(getAppContext());
        mContextMenu.setContextMenuItems(cm);
        mContextMenu.setOnContextMenuItemClickListener(
                new ContextMenu.OnContextMenuItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        showToast("Item selected");
                    }
                });
        mContextMenu.toggleContextMenuFromView(v, position);
    }
}
