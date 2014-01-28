package com.tackle.app.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tackle.app.fragments.EditFragments.DateTimeFragment;
import com.tackle.app.fragments.EditFragments.ItemsFragment;
import com.tackle.app.fragments.EditFragments.NotesFragment;
import com.tackle.app.fragments.EditFragments.RemindersFragment;
import com.tackle.app.fragments.EditFragments.ShareFragment;

import java.util.ArrayList;

/**
 * Created by Bill on 1/15/14.
 */
public class EditPagerAdapter extends FragmentPagerAdapter {

    Fragment[] fragments;

    public EditPagerAdapter(FragmentManager fm, Fragment[] fragments){
        super(fm);
        this.fragments = fragments;

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
