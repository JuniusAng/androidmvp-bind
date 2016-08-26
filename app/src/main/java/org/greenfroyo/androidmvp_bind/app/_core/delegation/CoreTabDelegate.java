package org.greenfroyo.androidmvp_bind.app._core.delegation;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;

import org.greenfroyo.androidmvp_bind.R;

/**
 * Created by junius.ang on 8/24/2016.
 */
public class CoreTabDelegate<T extends CoreDelegateDependency> extends CoreActivityDelegate<T>{
    /**
     * Default implementation for tab is fixed mode, you are not endorsed to change the text appearance for tab as it is already defined by the style
     * Make sure to set view pager
     * @param coreDelegateDependency
     */
    public static<T extends CoreDelegateDependency> CoreTabDelegate  createDefaultImpl(T coreDelegateDependency){
        CoreTabDelegate coreTabDelegate = new CoreTabDelegate(coreDelegateDependency);
        coreTabDelegate.setScrollMode(TabLayout.MODE_FIXED);
        return coreTabDelegate;
    }


    public static<T extends CoreDelegateDependency> CoreTabDelegate createFullImpl(T coreDelegateDependency, @TabLayout.Mode int tabScrollMode, int appBarScrollMode, ViewPager viewPager){
        CoreTabDelegate coreTabDelegate = new CoreTabDelegate(coreDelegateDependency);
        coreTabDelegate.setScrollMode(tabScrollMode);
        AppBarLayout.LayoutParams p = (AppBarLayout.LayoutParams)coreTabDelegate.getTabLayout().getLayoutParams();
        if(appBarScrollMode != -1) {
            p.setScrollFlags(appBarScrollMode);
        }
        coreTabDelegate.getTabLayout().setupWithViewPager(viewPager);
        return coreTabDelegate;
    }



    protected TabLayout vTabLayout;

    public CoreTabDelegate(T mCoreDelegateDependency) {
        super(mCoreDelegateDependency);
        getLayoutInflater().inflate(R.layout.layer_core_tab, getCoreDelegateDependency().getAppBarLayout(), true);
        vTabLayout = (TabLayout) getCoreDelegateDependency().getAppBarLayout().findViewById(R.id.core_tab);
        AppBarLayout.LayoutParams p = getAppBarLayoutParam(vTabLayout);
        p.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        vTabLayout.setLayoutParams(p);
    }

    public TabLayout getTabLayout() {
        return vTabLayout;
    }

    public void setCustomViewToTab(View v, int index, int tabGravity){
        if(vTabLayout.getTabCount() < index){
            throw new IndexOutOfBoundsException("tab specified is larger than maximum tab count");
        }
        vTabLayout.getTabAt(index).setCustomView(v);
        if(tabGravity != -1) {
            vTabLayout.setTabGravity(tabGravity);
        }
        else{
            vTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        }
    }

    public void setCustomViewToTab(@LayoutRes int layoutRes, int index, int tabGravity){
        if(vTabLayout.getTabCount() < index){
            throw new IndexOutOfBoundsException("tab specified is larger than maximum tab count");
        }
        vTabLayout.getTabAt(index).setCustomView(layoutRes);
        if(tabGravity != -1) {
            vTabLayout.setTabGravity(tabGravity);
        }
        else{
            vTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        }
    }

    public void setViewPager(ViewPager viewPager){
        vTabLayout.setupWithViewPager(viewPager);
    }
    /**
     *
     * @param scrollMode TabLayout.MODE_SCROLLABLE || TabLayout.MODE_FIXED
     */
    public void setScrollMode(@TabLayout.Mode int scrollMode){
        vTabLayout.setTabMode(scrollMode);
    }
    public void createDummyTab(int count){
        for(int x = 0; x< count ; x++) {
            vTabLayout.addTab(vTabLayout.newTab().setText("Tab "+(x+1)));
        }
    }

    public static class PageTitle implements CharSequence {
        private CharSequence title;
        private int imageId;

        public PageTitle(CharSequence title) {
            this.title = title;
        }

        public CharSequence getTitle() {
            return title;
        }

        public PageTitle setTitle(CharSequence title) {
            this.title = title;
            return this;
        }

        public int getImageId() {
            return imageId;
        }

        public PageTitle setImageId(int imageId) {
            this.imageId = imageId;
            return this;
        }

        @Override
        public int length() {
            return title == null ? 0 : title.length();
        }

        @Override
        public char charAt(int index) {
            return title == null ? ' ' : title.charAt(index);
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return title == null ? null : title.subSequence(start, end);
        }

        @NonNull
        @Override
        public String toString() {
            return title == null ? "" : title.toString();
        }
    }
}
