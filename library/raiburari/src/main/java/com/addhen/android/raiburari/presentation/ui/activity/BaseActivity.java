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

package com.addhen.android.raiburari.presentation.ui.activity;

import com.addhen.android.raiburari.presentation.BaseApplication;
import com.addhen.android.raiburari.R;
import com.addhen.android.raiburari.presentation.di.component.ApplicationComponent;
import com.addhen.android.raiburari.presentation.di.module.ActivityModule;
import com.addhen.android.raiburari.presentation.state.ApplicationState;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * @author Henry Addo
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * Layout resource id
     */
    protected final int mLayout;

    /**
     * Menu resource id
     */
    protected final int mMenu;

    /**
     * Navigation Drawer resource ID
     */
    protected final int mDrawerLayoutId;

    /**
     * Navigation Drawer Item resource ID
     */
    protected final int mDrawerItemsContainerId;

    // User profile
    protected View mLoginLayout;

    protected View mUserProfileLayout;

    // Navigation drawer:
    private DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mDrawerToggle;

    private ActionBar mActionBar = null;

    // Primary toolbar and drawer toggle
    private Toolbar mActionBarToolbar;

    // ViewGroup that habours the navigation drawer items
    private ViewGroup mDrawerItemsContainer;

    private LinearLayout mUserAccountListContainer;

    private ImageView mUserAccountListExpandIndicator;

    private boolean mUserAccountListExpanded = false;

    private ListView mList;

    private View mHeader;

    private TextView mTitleFooter;

    private ImageView mIconFooter;

    private int mColorName = 0;

    private int mColorIcon = 0;

    private int mColorSeparator = 0;

    private int mColorDefault = 0;

    private int mColorSelected = 0;

    private int mCurrentPosition = 1;

    private int mNewSelector = 0;

    private boolean mRemoveAlpha = false;

    private boolean mRemoveSelector = false;

    private List<Integer> mListIcon;

    private List<Integer> mListHeader;

    private List<String> mListNameItem;

    private SparseIntArray mSparseCounter;

    private FrameLayout mRelativeDrawer;

    private RelativeLayout mFooterDrawer;


    @Inject
    ApplicationState mApplicationState;


    public BaseActivity(int layout, int menu, int drawerLayoutId, int drawerItemsContainerId) {
        mLayout = layout;
        mMenu = menu;
        mDrawerLayoutId = drawerLayoutId;
        mDrawerItemsContainerId = drawerItemsContainerId;
    }

    public BaseActivity(int layout, int menu) {
        this(layout, menu, 0, 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationComponent().inject(this);
        injectViews();
        if (mLayout != 0) {
            setContentView(mLayout);
            mUserProfileLayout = findViewById(R.id.layout_user_profile);
            mLoginLayout = findViewById(R.id.layout_user_login);
        }

        mActionBar = getSupportActionBar();

        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
        }

        //TODO put this in a Util function
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                Resources.Theme theme = this.getTheme();
                TypedArray typedArray = theme
                        .obtainStyledAttributes(new int[]{android.R.attr.colorPrimary});
                mDrawerLayout.setStatusBarBackground(typedArray.getResourceId(0, 0));
            } catch (Exception e) {
                e.getMessage();
            }

            setElevationToolBar(15);
        }
    }

    /**
     * public void setElevation (float elevation)
     * Added in API level 21
     * Default value is 15
     *
     * @param elevation Sets the base elevation of this view, in pixels.
     */
    protected void setElevationToolBar(float elevation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (getActionBarToolbar() != null) {
                getActionBarToolbar().setElevation(elevation);
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //createNavDrawer();
        showLoginOrUserProfile();
        if (mDrawerToggle != null) {
            mDrawerToggle.syncState();
        }

        /*View mainContent = findViewById(R.id.main_content);
        if (mainContent != null) {
            fadeIn(mainContent, true);
        }*/
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDrawerToggle != null) {
            mDrawerToggle.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if (isNavDrawerOpen()) {
            closeNavDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        mApplicationState.registerEvent(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        mApplicationState.unregisterEvent(this);
        super.onPause();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getActionBarToolbar();
    }

    protected Toolbar getActionBarToolbar() {
        if (mActionBarToolbar == null) {
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
            if (mActionBarToolbar != null) {
                setSupportActionBar(mActionBarToolbar);
            }
        }
        return mActionBarToolbar;
    }

    protected void setActionBarTitle(String title) {
        if (mActionBar != null) {
            mActionBar.setTitle(title);
        }

    }

    protected boolean isNavDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START);
    }

    protected void closeNavDrawer() {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    private void showLoginOrUserProfile() {
        if (mApplicationState.getUserProfile() != null) {
            //showUserProfile(mApplicationState.getUserProfile());
        } else {
            if (mLoginLayout == null) {
                return;
            }
            mLoginLayout.setVisibility(VISIBLE);
            mUserProfileLayout.setVisibility(GONE);
            mLoginLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    closeNavDrawer();
                }
            });
        }
    }

    private void createNavDrawerItems() {
        if (mDrawerLayoutId == 0) {
            return;
        }

        mDrawerItemsContainer = (ViewGroup) findViewById(mDrawerItemsContainerId);

        if (mDrawerItemsContainer != null) {
            //Remove previous items
            mDrawerItemsContainer.removeAllViews();
            /*for (NavDrawerItem view : mNavDrawerItemViews) {
                mDrawerItemsContainer.addView(view.getView());
            }*/
        }
    }

    private void setupNavdrawer() {
        if (mDrawerLayoutId == 0) {
            return;
        }
        mDrawerLayout = (DrawerLayout) findViewById(mDrawerLayoutId);

    }

    /**
     * Shows a {@link android.widget.Toast} message.
     *
     * @param message A message resource
     */
    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mMenu != 0) {
            getMenuInflater().inflate(mMenu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item);
    }

    protected android.view.View fadeIn(final android.view.View view, final boolean animate) {
        if (view != null) {
            if (animate) {
                view.startAnimation(AnimationUtils.loadAnimation(this,
                        android.R.anim.fade_in));
            } else {

                view.clearAnimation();
            }
        }

        return view;

    }

    protected android.view.View fadeOut(final android.view.View view, final boolean animate) {
        if (view != null) {
            if (animate) {
                view.startAnimation(AnimationUtils.loadAnimation(this,
                        android.R.anim.fade_out));
            } else {
                view.clearAnimation();
            }
        }
        return view;

    }

    protected <V extends android.view.View> V setViewGone(final V view) {
        return setViewGone(view, true);
    }

    protected <V extends android.view.View> V setViewGone(final V view, final boolean gone) {
        if (view != null) {
            if (gone) {
                if (GONE != view.getVisibility()) {

                    fadeOut(view, true);

                    view.setVisibility(GONE);
                }
            } else {
                if (VISIBLE != view.getVisibility()) {
                    view.setVisibility(VISIBLE);

                    fadeIn(view, true);

                }
            }
        }
        return view;
    }

    /**
     * Replace every field annotated with ButterKnife annotations like @InjectView with the proper
     * value.
     */
    private void injectViews() {
        ButterKnife.inject(this);
    }

    /**
     * Adds a {@link Fragment} to this activity's layout.
     *
     * @param containerViewId The container view where to add the fragment.
     * @param fragment        The fragment to be added.
     * @param tag             The tag for the fragment
     */
    protected void addFragment(int containerViewId, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.add(containerViewId, fragment, tag);
        fragmentTransaction.commit();
    }

    /**
     * Get the Main Application component for dependency injection.
     *
     * @return {@link com.addhen.android.raiburari.presentation.di.component.ApplicationComponent}
     */
    public ApplicationComponent getApplicationComponent() {
        return ((BaseApplication) getApplication()).getApplicationComponent();
    }

    /**
     * Get an Activity module for dependency injection.
     *
     * @return {@link com.addhen.android.raiburari.presentation.di.module.ActivityModule}
     */
    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }
}
