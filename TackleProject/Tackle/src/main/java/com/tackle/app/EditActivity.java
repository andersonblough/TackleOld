package com.tackle.app;

import android.content.ContentUris;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tackle.app.Dialogs.CategoryPickerDialog;
import com.tackle.app.Dialogs.DatePickerFragment;
import com.tackle.app.Dialogs.DeleteTackleItemDialog;
import com.tackle.app.Dialogs.NumberPickerFragment;
import com.tackle.app.Dialogs.SetTitleDialog;
import com.tackle.app.Dialogs.TimePickerFragment;
import com.tackle.app.adapters.EditPagerAdapter;
import com.tackle.app.data.TackleContract;
import com.tackle.app.data.TackleEvent;
import com.tackle.app.fragments.EditFragments.DateTimeFragment;
import com.tackle.app.fragments.EditFragments.ItemsFragment;
import com.tackle.app.fragments.EditFragments.NotesFragment;
import com.tackle.app.fragments.EditFragments.RemindersFragment;
import com.tackle.app.fragments.EditFragments.ShareFragment;

import java.util.Calendar;

/**
 * Created by Bill on 1/15/14.
 */
public class EditActivity extends ActionBarActivity implements
        DatePickerFragment.DateChangeListener,
        NumberPickerFragment.CountChangedListener,
        TimePickerFragment.TimeChangeListener,
        CategoryPickerDialog.CategorySelectedListener,
        DeleteTackleItemDialog.DeleteItemListener,
        SetTitleDialog.TitleChangeListener{

    private ViewPager mViewPager;
    private EditPagerAdapter pagerAdapter;
    private GridView mPagerTabBar;
    private TextView mTitleView;
    private ImageView mMonthImage;

    private Cursor mCursor;
    private long mID;
    private String mTitle;

    private DateTimeFragment mDateTimeFragment;
    private RemindersFragment mRemindersFragment;
    private NotesFragment mNotesFragment;
    private ShareFragment mShareFragment;
    private ItemsFragment mItemsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        setUpCursor();
        setUpActionBar();

        if (savedInstanceState != null){
            FragmentManager fm = getSupportFragmentManager();
            mDateTimeFragment = (DateTimeFragment) fm.getFragment(savedInstanceState, DateTimeFragment.class.getName());
            mRemindersFragment = (RemindersFragment) fm.getFragment(savedInstanceState, RemindersFragment.class.getName());
            mNotesFragment = (NotesFragment) fm.getFragment(savedInstanceState, NotesFragment.class.getName());
            mShareFragment = (ShareFragment) fm.getFragment(savedInstanceState, ShareFragment.class.getName());
            mItemsFragment = (ItemsFragment) fm.getFragment(savedInstanceState, ItemsFragment.class.getName());
            mTitle = savedInstanceState.getString("title");
        }
        else {
            if (mCursor.moveToFirst()){
                mTitle = mCursor.getString(mCursor.getColumnIndex(TackleContract.TackleEvent.NAME));
            }
            mDateTimeFragment = new DateTimeFragment(mCursor);
            mRemindersFragment = new RemindersFragment(mCursor);
            mNotesFragment = new NotesFragment(mCursor);
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

        //set the initial view based on type
        mCursor.moveToFirst();
        int type = mCursor.getInt(mCursor.getColumnIndex(TackleContract.TackleEvent.TYPE));
        int positon;
        switch (type){
            case TackleEvent.Type.TODO:
                positon = 0;
                break;
            case TackleEvent.Type.LIST:
                positon = 4;
                break;
            case TackleEvent.Type.NOTE:
                positon = 2;
                break;
            case TackleEvent.Type.EVENT:
                positon = 0;
                break;
            default:
                positon = 0;
                break;
        }
        mViewPager.setCurrentItem(positon);

        //set the mTitleView and the click listener to adjust the mTitleView
        mTitleView = (TextView) findViewById(R.id.tv_title);
        mTitleView.setText(mTitle);
        mTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetTitleDialog titleDialog = new SetTitleDialog(mTitleView.getText().toString());
                titleDialog.show(getSupportFragmentManager(), "mTitle");
            }
        });
        mMonthImage = (ImageView) findViewById(R.id.month_image);
        if (mCursor.moveToFirst()){
            setMonthImage(mCursor.getLong(mCursor.getColumnIndex(TackleContract.TackleEvent.START_DATE)));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCursor.close();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("title", mTitle);
        FragmentManager fm = getSupportFragmentManager();
        fm.putFragment(outState, DateTimeFragment.class.getName(), mDateTimeFragment);
        fm.putFragment(outState, RemindersFragment.class.getName(), mRemindersFragment);
        fm.putFragment(outState, NotesFragment.class.getName(), mNotesFragment);
        fm.putFragment(outState, ShareFragment.class.getName(), mShareFragment);
        fm.putFragment(outState, ItemsFragment.class.getName(), mItemsFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                setResult(RESULT_OK);
                finish();
                overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_right);
                break;
            case R.id.delete:
                DeleteTackleItemDialog deleteDialog = new DeleteTackleItemDialog();
                deleteDialog.show(getSupportFragmentManager(), "delete");
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Edit");
        //actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_bg));

    }

    private void setUpCursor(){
        mID = getIntent().getLongExtra("id", 0);
        if (mID > 0){
            Uri uri = ContentUris.withAppendedId(TackleContract.TackleEvent.CONTENT_URI, mID);
            mCursor = getContentResolver().query(uri, null, null, null, null);
        }
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
            setMonthImage(dateTime);
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

    @Override
    public void onCategorySelected(long id) {
        mNotesFragment.setCategory(id);
    }

    @Override
    public void onDeleteItem() {
        Uri uri = ContentUris.withAppendedId(TackleContract.TackleEvent.CONTENT_URI, mID);
        getContentResolver().delete(uri, null, null);
        setResult(RESULT_OK);
        finish();
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_right);
    }

    @Override
    public void onTitleChange(String title) {
        mTitle = title;
        mTitleView.setText(mTitle);
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

    public void setMonthImage(long dateTime){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(dateTime);
        int month = c.get(Calendar.MONTH);

        TypedArray months = getResources().obtainTypedArray(R.array.months);
        mMonthImage.setImageDrawable(months.getDrawable(month));

    }
}
