package com.tackle.app.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tackle.app.fragments.CalendarFragment;

/**
 * Created by Bill on 11/12/13.
 */
public class InfinitePageAdapter extends FragmentPagerAdapter {

    CalendarFragment[] fragList;

    public InfinitePageAdapter(FragmentManager fm, CalendarFragment[] fragList){
        super(fm);
        this.fragList = fragList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragList[position];
    }

    @Override
    public int getCount() {
        return fragList.length;
    }
}
