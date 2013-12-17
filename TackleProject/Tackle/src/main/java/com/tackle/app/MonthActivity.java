package com.tackle.app;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.tackle.app.Dialogs.PickerDialog;
import com.tackle.app.adapters.InfinitePageAdapter;
import com.tackle.app.fragments.CalendarFragment;

import java.util.Calendar;

/**
 * Created by Bill on 11/11/13.
 */
public class MonthActivity extends ActionBarActivity implements PickerDialog.PickerDateChangeListener {
    private static final int PAGE_MIDDLE = 1;
    private long date;
    private int mSelectedPageIndex = 1;
    CalendarFragment[] fragList;
    ViewPager viewPager;
    int currentMonth, currentYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);

        setUpActionBar();

        if (getIntent() != null){
            date = getIntent().getLongExtra("date", 0);
        }

        fragList = new CalendarFragment[3];
        Calendar curMonth = Calendar.getInstance();
        curMonth.setTimeInMillis(date);
        Calendar prevMonth, nextMonth;
        prevMonth = (Calendar) curMonth.clone();
        nextMonth = (Calendar) curMonth.clone();

        currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        currentYear = Calendar.getInstance().get(Calendar.YEAR);

        prevMonth.set(Calendar.MONTH, prevMonth.get(Calendar.MONTH) - 1);
        nextMonth.set(Calendar.MONTH, nextMonth.get(Calendar.MONTH) + 1);

        fragList[0] = CalendarFragment.newInstance(prevMonth);
        fragList[1] = CalendarFragment.newInstance(curMonth);
        fragList[2] = CalendarFragment.newInstance(nextMonth);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        InfinitePageAdapter pageAdapter = new InfinitePageAdapter(getSupportFragmentManager(), fragList);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int position) {
                mSelectedPageIndex = position;

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                if (arg0 == ViewPager.SCROLL_STATE_IDLE){
                    if (mSelectedPageIndex < PAGE_MIDDLE){
                        fragList[0].onPreviousMonth();
                        fragList[1].onPreviousMonth();
                        fragList[2].onPreviousMonth();
                    }
                    else if (mSelectedPageIndex > PAGE_MIDDLE){
                        fragList[0].onNextMonth();
                        fragList[1].onNextMonth();
                        fragList[2].onNextMonth();
                    }
                    fragList[1].updateUI();
                    viewPager.setCurrentItem(1, false);
                    invalidateOptionsMenu();
                    fragList[0].updateUI();
                    fragList[2].updateUI();
                }

            }
        });
        viewPager.setAdapter(pageAdapter);
        viewPager.setPageMargin(16);
        viewPager.setCurrentItem(1, false);

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.monthview, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Calendar cal = fragList[1].getCalendar();
        if (cal.get(Calendar.YEAR) == currentYear && cal.get(Calendar.MONTH) == currentMonth){
            menu.removeItem(R.id.today);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.today:
                Calendar curMonth = Calendar.getInstance();
                Calendar prevMonth, nextMonth;
                prevMonth = (Calendar) curMonth.clone();
                nextMonth = (Calendar) curMonth.clone();

                prevMonth.set(Calendar.MONTH, prevMonth.get(Calendar.MONTH) - 1);
                nextMonth.set(Calendar.MONTH, nextMonth.get(Calendar.MONTH) + 1);

                fragList[0].setMonth(prevMonth);
                fragList[1].setMonth(curMonth);
                fragList[2].setMonth(nextMonth);

                invalidateOptionsMenu();
                break;
            case android.R.id.home:
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

    public void pickDate(View view){
        Calendar cal = fragList[1].getCalendar();
        DialogFragment dialog = new PickerDialog(cal.getTimeInMillis());
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onPickerDateChanged(long dateTime) {
        Calendar curMonth = Calendar.getInstance();
        curMonth.setTimeInMillis(dateTime);
        Calendar prevMonth, nextMonth;
        prevMonth = (Calendar) curMonth.clone();
        nextMonth = (Calendar) curMonth.clone();

        prevMonth.set(Calendar.MONTH, prevMonth.get(Calendar.MONTH) - 1);
        nextMonth.set(Calendar.MONTH, nextMonth.get(Calendar.MONTH) + 1);

        fragList[0].setMonth(prevMonth);
        fragList[1].setMonth(curMonth);
        fragList[2].setMonth(nextMonth);

        invalidateOptionsMenu();
    }
}
