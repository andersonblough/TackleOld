package com.tackle.app;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tackle.app.fragments.DateHeaderFragment;
import com.tackle.app.fragments.DayHeaderFragment;
import com.tackle.app.views.QuoteView;

import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final int VIEW_STATE_WEEK = 0;
    private static final int VIEW_STATE_DAY = 1;

    private QuoteView quoteView;

    public static final String WEEK_VIEW = "week view";

    private DateHeaderFragment dateHeaderFragment;
    private DayHeaderFragment dayHeaderFragment;
    private int stateView;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                long time = data.getLongExtra("result", 1);
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(time);
                String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
                int year = cal.get(Calendar.YEAR);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                String date = month + " " + String.valueOf(day) + " " + String.valueOf(year);
                Toast.makeText(this, date, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpActionBar();
        setContentView(R.layout.activity_main);
        setUpDateHeader();
        setUpMonthImage();

        stateView = VIEW_STATE_WEEK;

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        setUpQuote();

        ListView listView = (ListView) findViewById(R.id.tackle_list);
        listView.setEmptyView(quoteView);


    }

    private void setUpMonthImage() {
        ImageView imageView = (ImageView) findViewById(R.id.month_image);
        TextView monthText = (TextView) findViewById(R.id.tv_month);

        TypedArray monthImages = getResources().obtainTypedArray(R.array.months);
        int month  = Calendar.getInstance().get(Calendar.MONTH);
        String monthTitle = Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

        imageView.setImageDrawable(monthImages.getDrawable(month));
        monthText.setText(monthTitle + " " + year);
    }

    private void setUpQuote() {
        String[] quotes = getResources().getStringArray(R.array.quotes);
        String[] authors = getResources().getStringArray(R.array.authors);

        Random r = new Random();
        int position = r.nextInt(quotes.length);

        quoteView = (QuoteView) findViewById(R.id.empty);
        quoteView.quote.setText(quotes[position]);
        quoteView.author.setText("- " + authors[position]);
    }

    @Override
    protected void onResume() {
        super.onResume();

        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.fragment_date_bar, dateHeaderFragment, WEEK_VIEW).commit();

    }

    private void setUpDateHeader() {

        if (dateHeaderFragment == null){
            dateHeaderFragment = new DateHeaderFragment();
        }
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(true);
        //actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_bg));

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        //FragmentManager fragmentManager = getSupportFragmentManager();
        //fragmentManager.beginTransaction()
        //       .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
        //        .commit();
        onSectionAttached(position + 1);
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.month:
                Intent intent = new Intent(this, MonthActivity.class);
                startActivityForResult(intent, 1);
        }
        return super.onOptionsItemSelected(item);
    }

    public void dayClicked(View view){
        int day = 0;
        switch (view.getId()){
            case R.id.day1:
                day = 0;
                break;
            case R.id.day2:
                day = 1;
                break;
            case R.id.day3:
                day = 2;
                break;
            case R.id.day4:
                day = 3;
                break;
            case R.id.day5:
                day = 4;
                break;
        }

        long time = dateHeaderFragment.daysOfWeek[day];
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        String date = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US);

        dayHeaderFragment = new DayHeaderFragment(date);

        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().setCustomAnimations(R.animator.card_flip_right_in, R.animator.card_flip_right_out, R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                .add(R.id.fragment_date_bar, dayHeaderFragment).hide(dateHeaderFragment).commit();

    }

    public void switchToWeek(View view){
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().setCustomAnimations(R.animator.card_flip_right_in, R.animator.card_flip_right_out, R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                .show(dateHeaderFragment).detach(dayHeaderFragment).commit();
    }

    public void addItem(View view){
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);

    }

}
