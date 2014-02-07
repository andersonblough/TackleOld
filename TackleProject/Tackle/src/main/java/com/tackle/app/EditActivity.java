package com.tackle.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.tackle.app.Dialogs.DatePickerFragment;
import com.tackle.app.Dialogs.NumberPickerFragment;
import com.tackle.app.Dialogs.TimePickerFragment;
import com.tackle.app.adapters.EditPagerAdapter;
import com.tackle.app.fragments.EditFragments.DateTimeFragment;
import com.tackle.app.fragments.EditFragments.ItemsFragment;
import com.tackle.app.fragments.EditFragments.NotesFragment;
import com.tackle.app.fragments.EditFragments.RemindersFragment;
import com.tackle.app.fragments.EditFragments.ShareFragment;

/**
 * Created by Bill on 1/15/14.
 */
public class EditActivity extends ActionBarActivity implements DatePickerFragment.DateChangeListener, NumberPickerFragment.CountChangedListener, TimePickerFragment.TimeChangeListener {
    private ViewPager mViewPager;
    private EditPagerAdapter pagerAdapter;
    private GridView mPagerTabBar;

    private DateTimeFragment mDateTimeFragment;
    private RemindersFragment mRemindersFragment;
    private NotesFragment mNotesFragment;
    private ShareFragment mShareFragment;
    private ItemsFragment mItemsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        setUpActionBar();

        if (savedInstanceState != null){
            FragmentManager fm = getSupportFragmentManager();
            mDateTimeFragment = (DateTimeFragment) fm.getFragment(savedInstanceState, DateTimeFragment.class.getName());
            mRemindersFragment = (RemindersFragment) fm.getFragment(savedInstanceState, RemindersFragment.class.getName());
            mNotesFragment = (NotesFragment) fm.getFragment(savedInstanceState, NotesFragment.class.getName());
            mShareFragment = (ShareFragment) fm.getFragment(savedInstanceState, ShareFragment.class.getName());
            mItemsFragment = (ItemsFragment) fm.getFragment(savedInstanceState, ItemsFragment.class.getName());
        }
        else {
            mDateTimeFragment = new DateTimeFragment();
            mRemindersFragment = new RemindersFragment();
            mNotesFragment = new NotesFragment();
            mShareFragment = new ShareFragment();
            mItemsFragment = new ItemsFragment();
        }

        Fragment[] fragments = {mDateTimeFragment, mRemindersFragment, mNotesFragment, mShareFragment, mItemsFragment};

        mPagerTabBar = (GridView) findViewById(R.id.pager_tab_bar);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(4);
        pagerAdapter = new EditPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(pagerAdapter);

        final PagerTabAdapter pagerTabAdapter = new PagerTabAdapter();
        mPagerTabBar.setAdapter(pagerTabAdapter);
        mPagerTabBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mViewPager.setCurrentItem(position);

            }
        });
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                pagerTabAdapter.setSelectedPosition(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        FragmentManager fm = getSupportFragmentManager();
        fm.putFragment(outState, DateTimeFragment.class.getName(), mDateTimeFragment);
        fm.putFragment(outState, RemindersFragment.class.getName(), mRemindersFragment);
        fm.putFragment(outState, NotesFragment.class.getName(), mNotesFragment);
        fm.putFragment(outState, ShareFragment.class.getName(), mShareFragment);
        fm.putFragment(outState, ItemsFragment.class.getName(), mItemsFragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                setResult(RESULT_OK);
                finish();
                overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_right);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_bg));

    }

    @Override
    public void onDateChanged(long dateTime, String tag) {
        if (tag.equals(DatePickerFragment.UNTILDATE)){
            if (dateTime != -1){
                mRemindersFragment.setUntilDate(dateTime);
            }
        }
        else if (tag.equals(DatePickerFragment.STARTDATE)){
            mDateTimeFragment.setStartDate(dateTime);
        }
        else if (tag.equals(DatePickerFragment.ENDDATE)){
            mDateTimeFragment.setEndDate(dateTime);
        }


    }

    @Override
    public void onCountChanged(int count) {
        mRemindersFragment.setCountValue(count);
    }

    @Override
    public void onTimeChanged(long dateTime, String tag) {
        if (tag.equals(TimePickerFragment.STARTTIME)){
            mDateTimeFragment.setStartTime(dateTime);
        }
        else if (tag.equals(TimePickerFragment.ENDTIME)){
            mDateTimeFragment.setEndTime(dateTime);
        }
    }

    private class PagerTabAdapter extends BaseAdapter {

        private int selectedPosition;
        private int[] images = {R.drawable.ic_pager_tab_date,
                                R.drawable.ic_pager_tab_reminder,
                                R.drawable.ic_pager_tab_notes,
                                R.drawable.ic_pager_tab_share,
                                R.drawable.ic_pager_tab_more};

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        public void setSelectedPosition(int position){
            selectedPosition = position;
            notifyDataSetChanged();
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            View view = convertView;
            if (convertView == null){
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.cell_pager_tab_bar, viewGroup, false);
            }
            ImageView iv = (ImageView) view.findViewById(R.id.cellImage);
            iv.setImageResource(images[position]);

            if (position == selectedPosition){
                view.setBackgroundResource(android.R.color.white);
            }
            else {
                view.setBackgroundResource(android.R.color.transparent);
            }
            return view;
        }
    }
}
