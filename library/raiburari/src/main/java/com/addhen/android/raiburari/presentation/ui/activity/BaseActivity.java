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

import com.addhen.android.raiburari.R;
import com.addhen.android.raiburari.presentation.BaseApplication;
import com.addhen.android.raiburari.presentation.di.component.ApplicationComponent;
import com.addhen.android.raiburari.presentation.di.module.ActivityModule;
import com.addhen.android.raiburari.presentation.model.NavDrawerItem;
import com.addhen.android.raiburari.presentation.model.UserProfile;
import com.addhen.android.raiburari.presentation.state.ApplicationState;
import com.addhen.android.raiburari.presentation.ui.adapter.NavDrawerAdapter;
import com.addhen.android.raiburari.presentation.ui.listener.NavDrawerListener;
import com.addhen.android.raiburari.presentation.ui.widget.BezelImageView;
import com.addhen.android.raiburari.presentation.util.Utility;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
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

    public TextView mUserName;

    public TextView mUserEmail;

    public BezelImageView mUserPhoto;

    public ImageView mUserBackground;

    private ListView mList;

    private Toolbar mToolbar;

    private View mHeader;

    private TextView mTitleFooter;

    private ImageView mIconFooter;

    private int mColorName = 0;

    private int mColorIcon = 0;

    private int mColorSeparator = 0;

    private int mColorDefault = 0;

    private int mColorSelected = 0;

    private int mCurrentPosition = 1;

    private int mNewSelectorResId = 0;

    private boolean mRemoveAlpha = false;

    private DrawerLayout mDrawerLayout;

    private FrameLayout mRelativeDrawer;

    private RelativeLayout mFooterDrawer;

    private NavDrawerAdapter mNavigationAdapter;

    private ActionBarDrawerToggleCompat mDrawerToggle;

    private NavDrawerListener mNavigationListener;

    private List<Integer> mNavDrawerItemExtras = new ArrayList<>();

    public static final String CURRENT_POSITION = "CURRENT_POSITION";

    private ActionBar mActionBarToolbar;

    @Inject
    ApplicationState mApplicationState;

    // Prompt user to login
    protected View mLoginLayout;

    // Show user profile
    protected View mUserProfileLayout;

    /**
     * Initialize nav drawer menu items.
     */
    protected abstract void initNavDrawerItems(Bundle savedInstanceState);


    public BaseActivity(int layout, int menu, int drawerLayoutId) {
        mLayout = layout;
        mMenu = menu;
        mDrawerLayoutId = drawerLayoutId;
    }

    public BaseActivity(int layout, int menu) {
        this(layout, menu, 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationComponent().inject(this);
        injectViews();
        if (mLayout != 0) {
            setContentView(mLayout);
            initNavDrawer(savedInstanceState);
        }

        mActionBarToolbar = getSupportActionBar();

        if (mActionBarToolbar != null) {
            mActionBarToolbar.setDisplayHomeAsUpEnabled(true);
            mActionBarToolbar.setHomeButtonEnabled(true);
        }
    }

    private void initNavDrawer(Bundle savedInstanceState) {
        // Navigation drawer is enabled
        if (mDrawerLayoutId != 0) {
            if (savedInstanceState != null) {
                setNavDrawerItemCurrentPosition(savedInstanceState.getInt(CURRENT_POSITION));
            }

            mList = (ListView) findViewById(R.id.navdrawer_list);
            mList.setOnItemClickListener(new DrawerItemClickListener());

            mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

            mDrawerToggle = new ActionBarDrawerToggleCompat(this, mDrawerLayout, mToolbar);
            mDrawerLayout.setDrawerListener(mDrawerToggle);

            mTitleFooter = (TextView) this.findViewById(R.id.titleFooter);
            mIconFooter = (ImageView) this.findViewById(R.id.iconFooter);

            mFooterDrawer = (RelativeLayout) this.findViewById(R.id.footerDrawer);
            mFooterDrawer.setOnClickListener(onClickFooterDrawer);

            mRelativeDrawer = (FrameLayout) this.findViewById(R.id.relativeDrawer);

            if (mToolbar != null) {
                setSupportActionBar(mToolbar);
            }

            if (Utility.isLollipopOrHigher()) {
                Resources.Theme theme = this.getTheme();
                TypedArray typedArray = theme
                        .obtainStyledAttributes(new int[]{android.R.attr.colorPrimary});
                mDrawerLayout.setStatusBarBackground(typedArray.getResourceId(0, 0));
                setElevationToolBar(15);
            }

            if (mList != null) {
                setupNavDrawer(savedInstanceState);
            }
            if (savedInstanceState == null) {
                mNavigationListener.onNavDrawerItemClick(mCurrentPosition, R.id.container);
            }
            setCheckedItemNavigation(mCurrentPosition, true);
        }
    }

    /**
     * Sets the base elevation of this view, in pixels.
     */
    protected void setElevationToolBar(float elevation) {
        if (Utility.isLollipopOrHigher()) {
            if (getActionBarToolbar() != null) {
                getActionBarToolbar().setElevation(elevation);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_POSITION, mCurrentPosition);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mDrawerLayout != null) {
            boolean drawerOpen = isNavDrawerOpen();
            mNavigationListener.onPrepareOptionsMenusNavDrawer(menu, mCurrentPosition, drawerOpen);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mDrawerToggle != null) {
            mDrawerToggle.syncState();
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

    protected void setActionBarTitle(String title) {
        if (mActionBarToolbar != null) {
            mActionBarToolbar.setTitle(title);
        }
    }

    protected void closeNavDrawer() {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
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
     * Adds a {@link Fragment} to this activity's layout.
     *
     * @param containerViewId The container view where to add the fragment.
     * @param fragment        The fragment to be added.
     */
    protected void addFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Adds a {@link Fragment} to this activity's layout.
     *
     * @param containerViewId The container view where to add the fragment.
     * @param fragment        The fragment to be added.
     * @param tag             The tag for the fragment
     */
    protected void replaceFragment(int containerViewId, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment, tag);
        fragmentTransaction.commit();
    }

    /**
     * Adds a {@link Fragment} to this activity's layout.
     *
     * @param containerViewId The container view where to add the fragment.
     * @param fragment        The fragment to be added.
     */
    protected void replaceFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment);
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

    private class ActionBarDrawerToggleCompat extends ActionBarDrawerToggle {

        public ActionBarDrawerToggleCompat(Activity activity, DrawerLayout drawerLayout,
                Toolbar toolbar) {
            super(
                    activity,
                    drawerLayout, toolbar,
                    R.string.open_navdrawer,
                    R.string.close_navdrawer);
        }

        @Override
        public void onDrawerClosed(View view) {
            supportInvalidateOptionsMenu();
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            supportInvalidateOptionsMenu();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);

        if (mDrawerToggle != null) {
            mDrawerToggle.onConfigurationChanged(newConfig);
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            int mPosition = (position - 1);

            if (position != 0) {
                mNavigationListener.onNavDrawerItemClick(mPosition, R.id.container);
                setNavDrawerItemCurrentPosition(mPosition);
                setCheckedItemNavigation(mPosition, true);
            }

            mDrawerLayout.closeDrawer(mRelativeDrawer);
        }
    }

    private OnClickListener onClickUserPhoto = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mNavigationListener.onNavDrawerUserAvatarClick(v);
            mDrawerLayout.closeDrawer(mRelativeDrawer);
        }
    };

    private View.OnClickListener onClickFooterDrawer = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mNavigationListener.onNavDrawerFooterClick(v);
            mDrawerLayout.closeDrawer(mRelativeDrawer);
        }
    };

    private void setupNavDrawer(Bundle savedInstanceState) {
        createUserDefaultHeader();
        showLoginOrUserProfile();
        mNavigationAdapter = new NavDrawerAdapter(this);
        initNavDrawerItems(savedInstanceState);
        setAdapterNavigation();
    }

    protected void showLoginOrUserProfile() {
        mUserProfileLayout = findViewById(R.id.layout_user_profile);
        mLoginLayout = findViewById(R.id.layout_user_login);

        if (mApplicationState.getUserProfile() != null) {
            showUserProfile(mApplicationState.getUserProfile());
        } else {
            if (mLoginLayout == null) {
                return;
            }
            mLoginLayout.setVisibility(VISIBLE);
            mUserProfileLayout.setVisibility(GONE);
        }
    }

    private void showUserProfile(UserProfile userProfile) {
        if (userProfile != null) {
            mUserName.setText(userProfile.getFullName());
            mUserEmail.setText(userProfile.getUsername());
            if (userProfile.getAvatar() != null) {
                Picasso.with(this).load(userProfile.getAvatar()).into(mUserPhoto);
            }
        }
    }

    private void setAdapterNavigation() {

        if (mNavigationListener == null) {
            throw new RuntimeException(getString(R.string.start_navigation_listener));
        }
        mList.setAdapter(mNavigationAdapter);
    }

    /**
     * Create user default header
     */
    private void createUserDefaultHeader() {
        mHeader = getLayoutInflater().inflate(R.layout.navdrawer_header, mList, false);

        mUserName = (TextView) mHeader.findViewById(R.id.user_username);
        mUserEmail = (TextView) mHeader.findViewById(R.id.user_role);

        mUserPhoto = (BezelImageView) mHeader.findViewById(R.id.user_profile_image);
        mUserPhoto.setOnClickListener(onClickUserPhoto);

        mUserBackground = (ImageView) mHeader.findViewById(R.id.profile_cover_image);
        mList.addHeaderView(mHeader);
    }

    /**
     * Sets NavDrawer Adapter items
     *
     * @param navDrawerItems      The list items.
     * @param navDrawerItemExtras The navdrawer list items.
     */
    protected void setNavDrawerAdapterItems(List<NavDrawerItem> navDrawerItems,
            List<Integer> navDrawerItemExtras) {
        if (mNavigationAdapter != null) {
            mNavigationAdapter.setNavDrawerItems(navDrawerItems, mRemoveAlpha, navDrawerItemExtras);
        }
    }

    /**
     * Sets NavDrawer Adapter items with default set extra settings
     *
     * @param navDrawerItems The list items.
     */
    protected void setNavDrawerAdapterItems(List<NavDrawerItem> navDrawerItems) {
        mNavDrawerItemExtras.add(0, mNewSelectorResId);
        mNavDrawerItemExtras.add(1, mColorDefault);
        mNavDrawerItemExtras.add(2, mColorIcon);
        mNavDrawerItemExtras.add(3, mColorName);
        mNavDrawerItemExtras.add(4, mColorSeparator);
        setNavDrawerAdapterItems(navDrawerItems, mNavDrawerItemExtras);
    }

    /**
     * Starting listener navigation
     *
     * @param navigationListener listener.
     */
    protected void setNavDrawerListener(NavDrawerListener navigationListener) {
        this.mNavigationListener = navigationListener;
    }

    ;

    /**
     * First item of the position selected from the list
     *
     * @param position ...
     */
    protected void setNavDrawerDefaultStartPosition(int position) {
        this.mCurrentPosition = position;
    }

    /**
     * Position in the last clicked item list
     *
     * @param position ...
     */
    private void setNavDrawerItemCurrentPosition(int position) {
        this.mCurrentPosition = position;
    }

    /**
     * Get the current position of the NavDrawer Item
     */
    protected int getNavDrawerItemCurrentPosition() {
        return this.mCurrentPosition;
    }

    /**
     * Select item clicked
     *
     * @param position item position.
     * @param checked  true to check.
     */
    public void setCheckedItemNavigation(int position, boolean checked) {
        this.mNavigationAdapter.setChecked(position, checked);
    }

    /**
     * Information footer list item
     *
     * @param title item footer name.
     * @param icon  item footer icon.
     */
    public void setNavDrawerFooterItem(@NonNull String title, @NonNull int icon) {
        mTitleFooter.setText(title);

        if (icon == 0) {
            mIconFooter.setVisibility(View.GONE);
        } else {
            mIconFooter.setImageResource(icon);
        }
    }

    /**
     * Information footer list item
     *
     * @param title     item footer name.
     * @param icon      item footer icon.
     * @param colorName item footer name color.
     * @param colorIcon item footer icon color.
     */
    public void setNavDrawerFooterItem(@NonNull String title, @NonNull int icon, int colorName,
            int colorIcon) {

        mTitleFooter.setText(title);
        if (colorName > 0) {
            mTitleFooter.setTextColor(getResources().getColor(colorName));
        }
        if (icon == 0) {
            mIconFooter.setVisibility(View.GONE);
        } else {
            mIconFooter.setImageResource(icon);
            if (colorIcon > 0) {
                mIconFooter.setColorFilter(getResources().getColor(colorIcon));
            }
        }
    }

    /**
     * Information footer list item
     *
     * @param title item footer name.
     * @param icon  item footer icon.
     */
    public void setNavDrawerFooterItem(@NonNull int title, @NonNull int icon) {

        mTitleFooter.setText(getString(title));

        if (icon == 0) {
            mIconFooter.setVisibility(View.GONE);
        } else {
            mIconFooter.setImageResource(icon);
        }
    }

    /**
     * Information footer list item
     *
     * @param title     item footer name.
     * @param icon      item footer icon.
     * @param colorName item footer name color.
     * @param colorIcon item footer icon color.
     */
    public void setNavDrawerFooterItem(@NonNull int title, @NonNull int icon, int colorName,
            int colorIcon) {
        setNavDrawerFooterItem(getString(title), icon, colorName, colorIcon);
    }

    /**
     * If not want to use the footer item just put false
     *
     * @param visible true or false.
     */
    public void setFooterNavigationVisible(boolean visible) {
        this.mFooterDrawer.setVisibility((visible) ? View.VISIBLE : View.GONE);
    }

    /**
     * Item color selected in the list - name and icon (use before the setNavDrawerAdapter)
     *
     * @param colorId color id.
     */
    public void setColorSelectedItemNavigation(int colorId) {
        this.mColorSelected = colorId;
    }

    /**
     * Footer icon color
     *
     * @param colorId color id.
     */
    public void setFooterIconColorNavigation(int colorId) {
        this.mIconFooter.setColorFilter(getResources().getColor(colorId));
    }

    /**
     * Item color default in the list - name and icon (use before the setNavDrawerAdapter)
     *
     * @param colorId color id.
     */
    public void setColorDefaultItemNavigation(int colorId) {
        this.mColorDefault = colorId;
    }

    /**
     * Icon item color in the list - icon (use before the setNavDrawerAdapter)
     *
     * @param colorId color id.
     */
    public void setColorIconItemNavigation(int colorId) {
        this.mColorIcon = colorId;
    }

    /**
     * Separator item subHeader color in the list - icon (use before the setNavDrawerAdapter)
     *
     * @param colorId color id.
     */
    public void setColorSeparatorItemSubHeaderNavigation(int colorId) {
        this.mColorSeparator = colorId;
    }

    /**
     * Name item color in the list - name (use before the setNavDrawerAdapter)
     *
     * @param colorId color id.
     */
    public void setColorNameItemNavigation(int colorId) {
        this.mColorName = colorId;
    }

    /**
     * New selector navigation
     *
     * @param resId ID of the drawable selector
     */
    public void setNavDrawerNewSelector(@NonNull int resId) {
        this.mNewSelectorResId = resId;
    }

    /**
     * Sets a new title for the NavDrawer
     *
     * @param position item position.
     * @param name     title
     */
    public void setNavDrawerTitle(int position, String name) {
        this.mNavigationAdapter.setTitle(position, name);
    }

    /**
     * Sets NavDrawer title
     *
     * @param position item position.
     * @param resId    the resource Id of the title
     */
    public void setNavDrawerTitle(int position, int resId) {
        this.mNavigationAdapter.setTitle(position, getString(resId));
    }

    /**
     * New name item
     *
     * @param position item position.
     * @param icon     new icon
     */
    public void setIcon(int position, int icon) {
        this.mNavigationAdapter.setIcon(position, icon);
    }

    /**
     * New information item navigation
     *
     * @param position item position.
     * @param name     new name
     * @param icon     new icon
     * @param counter  new counter
     */
    public void setNavDrawerItem(int position, int name, int icon, int counter) {
        this.mNavigationAdapter.setNavDrawerItem(position, getString(name), icon, counter);
    }

    /**
     * New information item navigation
     *
     * @param position item position.
     * @param name     new name
     * @param icon     new icon
     * @param counter  new counter
     */

    public void setNavDrawerItem(int position, String name, int icon, int counter) {
        this.mNavigationAdapter.setNavDrawerItem(position, name, icon, counter);
    }

    /**
     * New counter value
     *
     * @param position item position.
     * @param value    new counter value.
     */
    public void setNavDrawerCounterValue(int position, int value) {
        this.mNavigationAdapter.setCounter(position, value);
    }

    /**
     * Remove alpha item navigation (use before the setNavDrawerAdapter)
     */
    public void removeNavDrawerAlpha() {
        this.mRemoveAlpha = !mRemoveAlpha;
    }

    /**
     * Remove default Header
     */
    public void showDefaultHeader() {
        if (mHeader == null) {
            throw new RuntimeException(getString(R.string.header_not_created));
        }

        mList.addHeaderView(mHeader);
    }

    /**
     * Remove default Header
     */
    private void removeDefaultHeader() {
        if (mHeader == null) {
            throw new RuntimeException(getString(R.string.header_not_created));
        }

        mList.removeHeaderView(mHeader);
    }

    /**
     * Add custom Header
     *
     * @param v ...
     */
    public void addCustomHeader(View v) {
        if (v == null) {
            throw new RuntimeException(getString(R.string.custom_header_not_created));
        }

        removeDefaultHeader();
        mList.addHeaderView(v);
    }

    /**
     * Remove default Header
     *
     * @param v ...
     */
    public void removeCustomHeader(View v) {
        if (v == null) {
            throw new RuntimeException(getString(R.string.custom_header_not_created));
        }

        mList.removeHeaderView(v);
    }

    /**
     * Get NavDrawer's {@link ListView}
     */
    public ListView getListView() {
        return this.mList;
    }

    /**
     * Gets toolbar
     */
    public Toolbar getActionBarToolbar() {
        return this.mToolbar;
    }

    /**
     * Open drawer
     */
    public void openDrawer() {
        mDrawerLayout.openDrawer(mRelativeDrawer);
    }

    /**
     * Close drawer
     */
    public void closeDrawer() {
        mDrawerLayout.closeDrawer(mRelativeDrawer);
    }

    @Override
    public void onBackPressed() {

        boolean drawerOpen = isNavDrawerOpen();
        if (drawerOpen) {
            mDrawerLayout.closeDrawer(mRelativeDrawer);
        } else {
            super.onBackPressed();
        }
    }

    private boolean isNavDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mRelativeDrawer);
    }
}
