package com.tackle.app.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tackle.app.fragments.EditFragments.DateTimeFragment;
import com.tackle.app.fragments.EditFragments.ItemsFragment;
import com.tackle.app.fragments.EditFragments.NotesFragment;
import com.tackle.app.fragments.EditFragments.RemindersFragment;
import com.tackle.app.fragments.EditFragments.ShareFragment;

/**
 * Created by Bill on 1/15/14.
 */
public class EditPagerAdapter extends FragmentStatePagerAdapter {

    Fragment[] fragments;

    public EditPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new Fragment[5];
        fragments[0] = new DateTimeFragment();
        fragments[1] = new RemindersFragment();
        fragments[2] = new NotesFragment();
        fragments[3] = new ShareFragment();
        fragments[4] = new ItemsFragment();
    }

    @Override
    public Fragment getItem(int i) {
        return fragments[i];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
