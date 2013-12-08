package com.tackle.app;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
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

import com.tackle.app.Weather.JSONWeatherParser;
import com.tackle.app.Weather.Weather;
import com.tackle.app.Weather.WeatherHTTPClient;
import com.tackle.app.fragments.DateHeaderFragment;
import com.tackle.app.fragments.DayHeaderFragment;
import com.tackle.app.fragments.DayViewFragment;
import com.tackle.app.fragments.WeekViewFragment;
import com.tackle.app.views.QuoteView;
import com.tackle.app.views.SelectableImageView;

import org.json.JSONException;

import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final int MILLI_PER_SECOND = 1000;
    private static final int SEC_PER_HOUR = 3600;
    private static final int THREE_HOURS = (3 * SEC_PER_HOUR * MILLI_PER_SECOND);

    private static final int VIEW_STATE_WEEK = 0;
    private static final int VIEW_STATE_DAY = 1;

    private QuoteView quoteView;

    public static final String WEEK_VIEW = "week view";
    public static final String DAY_VIEW = "day view";

    /**
     * Fragment for displaying the week view
     */
    private WeekViewFragment mWeekViewFragment;

    /**
     * Fragment for displaying the day view
     */
    private DayViewFragment mDayViewFragment;

    private DateHeaderFragment dateHeaderFragment;
    private DayHeaderFragment dayHeaderFragment;
    private int mViewState;
    private long mSelectedDay;
    private int mCategory;


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
                setDate(data.getLongExtra("result", 1));
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().setCustomAnimations(R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                        .show(mDayViewFragment).hide(mWeekViewFragment).commit();
                setViewState(VIEW_STATE_DAY);
            }
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpActionBar();
        setContentView(R.layout.activity_main);

        mDayViewFragment = new DayViewFragment();
        mWeekViewFragment = new WeekViewFragment();

        // set the date as the current date
        //setDate(System.currentTimeMillis());
        // add the fragments to the current view
        mSelectedDay = System.currentTimeMillis();
        setUpMonthImage(mSelectedDay);
        setUpDateHeader();

        runnable.run();

        mViewState = VIEW_STATE_WEEK;

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

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    private void initDate() {
        mSelectedDay = System.currentTimeMillis();
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

        //TODO: check if the date is current and if it has changed

        showDateHeader();


    }

    private void showDateHeader() {
        FragmentManager manager = getFragmentManager();
        if (mViewState == VIEW_STATE_DAY){
            manager.beginTransaction().show(mDayViewFragment)
                    .hide(mWeekViewFragment).commit();

        }
        if (mViewState == VIEW_STATE_WEEK){
            manager.beginTransaction().show(mWeekViewFragment)
                    .hide(mDayViewFragment).commit();

        }
    }

    private void setUpDateHeader() {
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction()
                .add(R.id.fragment_date_bar, mWeekViewFragment, WEEK_VIEW).hide(mWeekViewFragment)
                .add(R.id.fragment_date_bar, mDayViewFragment, DAY_VIEW).hide(mDayViewFragment)
                .commit();
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (currentDay()){
            menu.removeItem(R.id.today);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.today:
                setDate(System.currentTimeMillis());
                return true;
            case R.id.month:
                Intent intent = new Intent(this, MonthActivity.class);
                intent.putExtra("date", mSelectedDay);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.slide_in_right, android.R.anim.fade_out);
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

        long time = mWeekViewFragment.getWeekDay(day);

        mDayViewFragment.setDate(time);

        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().setCustomAnimations(R.animator.card_flip_right_in, R.animator.card_flip_right_out)
                .show(mDayViewFragment).hide(mWeekViewFragment).commit();
        setUpMonthImage(time);
        setViewState(VIEW_STATE_DAY);

    }


    public void switchToWeek(View view){
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().setCustomAnimations(R.animator.card_flip_right_in, R.animator.card_flip_right_out)
                .show(mWeekViewFragment).hide(mDayViewFragment).commit();
        setUpMonthImage(mSelectedDay);
        setViewState(VIEW_STATE_WEEK);
    }

    public void addItem(View view){
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);

    }

    public void goToCalendar(View view){
        Intent intent = new Intent(this, MonthActivity.class);
        intent.putExtra("date", mSelectedDay);
        startActivityForResult(intent, 1);
        overridePendingTransition(R.anim.slide_in_right, android.R.anim.fade_out);
    }

    private void setViewState(int viewState) {
        mViewState = viewState;
    }

    /**
     * private Method to set date and update all content related to date
     */
    private void setDate(long dateTime){
        mSelectedDay = dateTime;

        // check if fragments exists
        if (mWeekViewFragment == null) mWeekViewFragment = new WeekViewFragment();
        if (mDayViewFragment == null) mDayViewFragment = new DayViewFragment();

        //initialize set date for fragments
        mWeekViewFragment.setDateTime(mSelectedDay);
        mDayViewFragment.setDate(mSelectedDay);

        //update the month image and text
        setUpMonthImage(mSelectedDay);

        //update menu to display 'TODAY' option if appropriate
        invalidateOptionsMenu();

        // TODO: initialize the loader to update the list view based on the date


    }

    /**
     * private Method to set up the Month image and Text
     */
    private void setUpMonthImage(long date) {
        ImageView imageView = (ImageView) findViewById(R.id.month_image);
        TextView monthText = (TextView) findViewById(R.id.tv_month);

        TypedArray monthImages = getResources().obtainTypedArray(R.array.months);
        Calendar cal  = Calendar.getInstance();
        cal.setTimeInMillis(date);
        int month  = cal.get(Calendar.MONTH);
        String monthTitle = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
        String year = String.valueOf(cal.get(Calendar.YEAR));

        imageView.setImageDrawable(monthImages.getDrawable(month));
        monthText.setText(monthTitle + " " + year);
    }

    /**
     *private Method to check the current day
     */
    private boolean currentDay() {
        Calendar today = Calendar.getInstance();
        Calendar selected = Calendar.getInstance();
        selected.setTimeInMillis(mSelectedDay);
        return (today.get(Calendar.YEAR) == selected.get(Calendar.YEAR) && today.get(Calendar.DAY_OF_YEAR) == selected.get(Calendar.DAY_OF_YEAR));
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            double latitude = 0;
            double longitude = 0;

            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null){
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }

            String locale = "lat=" + String.valueOf(latitude) + "&lon=" + String.valueOf(longitude);

            Toast.makeText(getBaseContext(), "Run Started", Toast.LENGTH_SHORT).show();

            JSONWeatherTask weatherTask = new JSONWeatherTask();

            weatherTask.execute(new String[]{locale});
            handler.postDelayed(runnable, THREE_HOURS);

        }
    };

    private class JSONWeatherTask extends AsyncTask<String, Void, Weather[]> {

        @Override
        protected Weather[] doInBackground(String... params) {
            Weather[] forecast = new Weather[5];
            String data = new WeatherHTTPClient().getWeatherData(params[0]);
            JSONWeatherParser parser = new JSONWeatherParser();

            if (data != null){
                //System.out.println(data);

                for (int i = 0; i < 5; i++){
                    try {
                        Weather weather = parser.getWeather(data, i);
                        forecast[i] = weather;

                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            return forecast;
        }

        @Override
        protected void onPostExecute(Weather[] forecast) {
            super.onPostExecute(forecast);
            initWeatherIds(forecast);
        }

    }

    private void initWeatherIds(Weather[] forecast){
        int[] weatherIds = new int[5];
        if (forecast != null){
            for (int i = 0; i < forecast.length; i++){
                Weather weather = forecast[i];
                if (weather != null){
                    int weatherID = weather.getWeatherId();
                    if (weatherID >=200 && weatherID <= 232){
                        weatherIds[i] = R.drawable.weather_thunderstorm;
                    }
                    else if (weatherID >= 300 && weatherID <= 522){
                        weatherIds[i] = R.drawable.weather_rain;
                    }
                    else if (weatherID >= 600 && weatherID <= 621){
                        weatherIds[i] = R.drawable.weather_snow;
                    }
                    else if (weatherID == 800){
                        weatherIds[i] = R.drawable.weather_clear;
                    }
                    else if (weatherID >= 801 && weatherID <= 803){
                        weatherIds[i] = R.drawable.weather_partlycloudy;
                    }
                    else if (weatherID == 804){
                        weatherIds[i] = R.drawable.weather_cloudy;
                    }
                }
            }
        }
        mWeekViewFragment.setWeather(weatherIds);
        mDayViewFragment.setWeather(weatherIds);
    }

}
