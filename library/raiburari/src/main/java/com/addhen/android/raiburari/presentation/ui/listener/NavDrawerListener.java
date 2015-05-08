package com.addhen.android.raiburari.presentation.ui.listener;

import android.view.Menu;
import android.view.View;

/**
 * @author Henry Addo
 */
public interface NavDrawerListener {

    /**
     * Item click Navigation (ListView.OnItemClickListener)
     *
     * @param position          position of the item that was clicked.
     * @param layoutContainerId Default layout. Tell the replace - FragmentManager.beginTransaction().replace(layoutContainerId,
     *                          yourFragment).commit()
     */
    void onNavDrawerItemClick(int position, int layoutContainerId);

    /**
     * Prepare options menu navigation (onPrepareOptionsMenu(Menu menu))
     *
     * @param menu     menu.
     * @param position last position of the item that was clicked.
     * @param visible  use to hide the menu when the navigation is open.
     */
    void onPrepareOptionsMenusNavDrawer(Menu menu, int position, boolean visible);

    /**
     * Click footer item navigation
     *
     * @param v view.
     */
    void onNavDrawerFooterClick(View v);

    /**
     * Click user photo navigation
     *
     * @param v view.
     */
    void onNavDrawerUserAvatarClick(View v);

}
