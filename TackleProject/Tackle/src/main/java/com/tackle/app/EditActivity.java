package com.tackle.app;

import android.content.Intent;
import android.os.Bundle;
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

import com.tackle.app.adapters.EditPagerAdapter;
import com.tackle.app.views.DateTimePicker;

/**
 * Created by Bill on 1/15/14.
 */
public class EditActivity extends ActionBarActivity {
    private ViewPager mViewPager;
    private GridView mPagerTabBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        setUpActionBar();

        mPagerTabBar = (GridView) findViewById(R.id.pager_tab_bar);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mViewPager.setAdapter(new EditPagerAdapter(getSupportFragmentManager()));
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
