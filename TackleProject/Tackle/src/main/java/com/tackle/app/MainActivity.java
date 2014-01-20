package com.tackle.app;

import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tackle.app.Dialogs.ColorPickerDialog;
import com.tackle.app.Dialogs.DeleteDialog;
import com.tackle.app.Weather.JSONWeatherParser;
import com.tackle.app.Weather.Weather;
import com.tackle.app.Weather.WeatherHTTPClient;
import com.tackle.app.adapters.TackleListAdapter;
import com.tackle.app.data.TackleContract;
import com.tackle.app.data.TackleEvent;
import com.tackle.app.fragments.DateHeaderFragment;
import com.tackle.app.fragments.DayHeaderFragment;
import com.tackle.app.fragments.DayViewFragment;
import com.tackle.app.fragments.NavigationDrawerFragment;
import com.tackle.app.fragments.WeekViewFragment;
import com.tackle.app.views.QuoteView;

import org.json.JSONException;

import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, LoaderManager.LoaderCallbacks<Cursor>, ColorPickerDialog.CategoryPickerListener, DeleteDialog.DeleteCategoryListener {

    private static final int MILLI_PER_SECOND = 1000;
    private static final int SEC_PER_HOUR = 3600;
    private static final int THREE_HOURS = (3 * SEC_PER_HOUR * MILLI_PER_SECOND);

    public static final int VIEW_STATE_WEEK = 0;
    public static final int VIEW_STATE_DAY = 1;

    private static final int CATEGORY_LOADER = 100;
    private static final int TACKLE_ITEMS_LOADER = 110;

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

    private TackleListAdapter mTackleListAdapter;

    private DateHeaderFragment dateHeaderFragment;
    private DayHeaderFragment dayHeaderFragment;
    public int mViewState;
    public long mSelectedDay;
    private long mTempDate;
    private long mCategory;
    private int[] weatherIds;


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
                mTempDate = mSelectedDay;
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

        if (savedInstanceState != null){
            mSelectedDay = savedInstanceState.getLong("selectedDay");
            mTempDate = savedInstanceState.getLong("tempDate");
            mCategory = savedInstanceState.getLong("category");
            mViewState = savedInstanceState.getInt("viewState");
            weatherIds = savedInstanceState.getIntArray("weather");

        }
        else {
            mSelectedDay = System.currentTimeMillis();
            mViewState = VIEW_STATE_WEEK;
            mCategory = -1;
            runnable.run();
        }

        // set the date as the current date
        //setDate(System.currentTimeMillis());
        // add the fragments to the current view
        setUpMonthImage(mSelectedDay);

        setUpDateHeader(savedInstanceState);

        getSupportLoaderManager().initLoader(CATEGORY_LOADER, null, this);
        getSupportLoaderManager().initLoader(TACKLE_ITEMS_LOADER, null, this);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle(mCategory);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout), mCategory);

        setUpQuote();

        ListView listView = (ListView) findViewById(R.id.tackle_list);

        mTackleListAdapter = new TackleListAdapter(this, null, false);
        listView.setAdapter(mTackleListAdapter);

        View emptyView = findViewById(R.id.empty);
        listView.setEmptyView(emptyView);

        View header = new View(this);
        header.setMinimumHeight(16);
        header.setClickable(false);

        listView.addHeaderView(header);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                startActivityForResult(intent, 2);
                overridePendingTransition(R.anim.slide_in_right, android.R.anim.fade_out);
            }
        });

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

        quoteView = (QuoteView) findViewById(R.id.quotes);
        quoteView.quote.setText(quotes[position]);
        quoteView.author.setText("- " + authors[position]);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //TODO: check if the date is current and if it has changed


        showDateHeader();


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("selectedDay", mSelectedDay);
        outState.putInt("viewState", mViewState);
        outState.putLong("tempDate", mTempDate);
        outState.putLong("category", mCategory);
        outState.putIntArray("weather", weatherIds);


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

    private void setUpDateHeader(Bundle savedInstanceState) {
        mDayViewFragment = new DayViewFragment();
        mWeekViewFragment = new WeekViewFragment();

        FragmentManager manager = getFragmentManager();
        if (savedInstanceState == null){
            manager.beginTransaction().add(R.id.container_week, mWeekViewFragment).hide(mWeekViewFragment).commit();
            manager.beginTransaction().add(R.id.container_day, mDayViewFragment).hide(mDayViewFragment).commit();
            if (weatherIds != null){
                mWeekViewFragment.setWeather(weatherIds);
                mDayViewFragment.setWeather(weatherIds);
            }
        }
        else {
            manager.beginTransaction().replace(R.id.container_week, mWeekViewFragment).hide(mWeekViewFragment).commit();
            manager.beginTransaction().replace(R.id.container_day, mDayViewFragment).hide(mDayViewFragment).commit();
        }
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(true);
        //actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_bg));

    }

    @Override
    public void onNavigationDrawerItemSelected(long id) {
        mTitle = getTitle(id);
        getSupportLoaderManager().restartLoader(TACKLE_ITEMS_LOADER, null, this);

    }

    public String getTitle(long id){
        if (id == -1){
            mCategory = id;
            return "All";
        }
        else {
            mCategory = id;
            Uri uri = Uri.parse(TackleContract.Categories.CONTENT_URI + "/" + mCategory);
            Cursor c = getContentResolver().query(uri, null, null, null, null);
            c.moveToFirst();
            String title = c.getString(c.getColumnIndex(TackleContract.Categories.CATEGORY_NAME));
            c.close();
            return title;
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
            case R.id.add_todo:
                addTackleItem(TackleEvent.Type.TODO);
                return true;
            case R.id.add_list:
                addTackleItem(TackleEvent.Type.LIST);
                return true;
            case R.id.add_note:
                addTackleItem(TackleEvent.Type.NOTE);
                return true;
            case R.id.add_event:
                addTackleItem(TackleEvent.Type.EVENT);
                return true;
            case R.id.action_settings:
                return true;
            case R.id.today:
                setDate(System.currentTimeMillis());
                mTempDate = mSelectedDay;
                getSupportLoaderManager().restartLoader(TACKLE_ITEMS_LOADER, null, this);
                return true;
            case R.id.month:
                Intent intent = new Intent(this, MonthActivity.class);
                intent.putExtra("date", mSelectedDay);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.slide_in_right, android.R.anim.fade_out);
        }
        return super.onOptionsItemSelected(item);
    }

    private void addTackleItem(int type) {

        Intent intent = new Intent(this, AddActivity.class);
        intent.putExtra("type", type);
        if (mCategory == -1){
            intent.putExtra("category", 1);
        }
        else {
            intent.putExtra("category", mCategory);
        }
        intent.putExtra("dateTime", mSelectedDay);
        startActivity(intent);
        overridePendingTransition(0,0);

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
        mTempDate = mSelectedDay;
        mSelectedDay = time;

        mDayViewFragment.setDate(mSelectedDay);


        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().setCustomAnimations(R.animator.card_flip_right_in, R.animator.card_flip_right_out)
                .show(mDayViewFragment).hide(mWeekViewFragment).commit();
        setUpMonthImage(time);
        setViewState(VIEW_STATE_DAY);

    }


    public void switchToWeek(View view){

        mSelectedDay = mTempDate;
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().setCustomAnimations(R.animator.card_flip_right_in, R.animator.card_flip_right_out)
                .hide(mDayViewFragment).show(mWeekViewFragment).commit();
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
      getSupportLoaderManager().restartLoader(TACKLE_ITEMS_LOADER, null, this);
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

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        CursorLoader cursorLoader = null;
        switch (loaderID){
            case CATEGORY_LOADER:
                cursorLoader = new CursorLoader(this, TackleContract.Categories.CONTENT_URI, null, null, null, null);
                break;
            case TACKLE_ITEMS_LOADER:
                String[] projection = {TackleContract.TackleEvent._ID, TackleContract.TackleEvent.NAME, TackleContract.TackleEvent.TYPE, TackleContract.TackleEvent.START_DATE, TackleContract.TackleEvent.STATUS, TackleContract.TackleEvent.CATEGORY_ID};
                switch (mViewState){
                    case VIEW_STATE_DAY:
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(mSelectedDay);
                        cal.set(Calendar.HOUR_OF_DAY, 0);
                        cal.set(Calendar.MINUTE, 0);
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.MILLISECOND, 0);
                        long startTime = cal.getTimeInMillis();
                        long endTime = startTime + (24 * SEC_PER_HOUR * MILLI_PER_SECOND);
                        String selection;
                        String[] selectionArgs;

                        if (mCategory == -1){
                            selection = TackleContract.TackleEvent.START_DATE + " >= ? AND " + TackleContract.TackleEvent.START_DATE + " <= ?";
                            selectionArgs = new String[]{String.valueOf(startTime), String.valueOf(endTime)};
                        }
                        else {
                            selection = TackleContract.TackleEvent.START_DATE + " >= ? AND " + TackleContract.TackleEvent.START_DATE + " <= ? AND " + TackleContract.TackleEvent.CATEGORY_ID + " = ?";
                            selectionArgs = new String[]{String.valueOf(startTime), String.valueOf(endTime), String.valueOf(mCategory)};
                        }

                        cursorLoader = new CursorLoader(this, TackleContract.TackleEvent.CONTENT_URI, projection, selection, selectionArgs, TackleContract.TackleEvent.START_DATE + " ASC");
                        break;
                    case VIEW_STATE_WEEK:
                        Calendar week = Calendar.getInstance();
                        week.setTimeInMillis(mSelectedDay);
                        week.set(Calendar.HOUR_OF_DAY, 0);
                        week.set(Calendar.MINUTE, 0);
                        week.set(Calendar.SECOND, 0);
                        week.set(Calendar.MILLISECOND, 0);
                        long weekStart = week.getTimeInMillis();
                        long weekEnd = weekStart + (5 * 24 * SEC_PER_HOUR * MILLI_PER_SECOND);
                        String weekSelection;
                        String[] weekSelectionArgs;

                        if (mCategory == -1){
                            weekSelection = TackleContract.TackleEvent.START_DATE + " >= ? AND " + TackleContract.TackleEvent.START_DATE + " <= ?";
                            weekSelectionArgs = new String[]{String.valueOf(weekStart), String.valueOf(weekEnd)};
                        }
                        else {
                            weekSelection = TackleContract.TackleEvent.START_DATE + " >= ? AND " + TackleContract.TackleEvent.START_DATE + " <= ? AND " + TackleContract.TackleEvent.CATEGORY_ID + " = ?";
                            weekSelectionArgs = new String[]{String.valueOf(weekStart), String.valueOf(weekEnd), String.valueOf(mCategory)};
                        }
                        cursorLoader = new CursorLoader(this, TackleContract.TackleEvent.CONTENT_URI, projection, weekSelection, weekSelectionArgs, TackleContract.TackleEvent.START_DATE + " ASC");
                        break;

                    default:
                        cursorLoader = new CursorLoader(this, TackleContract.TackleEvent.CONTENT_URI, null, null, null, null);
                        break;

                }

        }
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> cursorLoader, Cursor cursor) {
        switch (cursorLoader.getId()){
            case CATEGORY_LOADER:
                mNavigationDrawerFragment.mDrawerAdapter.setDateTime(mSelectedDay);
                mNavigationDrawerFragment.mDrawerAdapter.setViewState(mViewState);
                mNavigationDrawerFragment.mDrawerAdapter.swapCursor(cursor);
                break;
            case TACKLE_ITEMS_LOADER:
                mTackleListAdapter.swapCursor(cursor);
                mNavigationDrawerFragment.mDrawerAdapter.setDateTime(mSelectedDay);
                mNavigationDrawerFragment.mDrawerAdapter.setViewState(mViewState);
                mNavigationDrawerFragment.mDrawerAdapter.notifyDataSetChanged();
                break;
        }

        Cursor c;
        long start;
        long end;
        String selection = TackleContract.TackleEvent.START_DATE + " >= ? AND " + TackleContract.TackleEvent.START_DATE + " <= ?";
        String[] selectionArgs = new String[2];
        String[] projection = {TackleContract.TackleEvent._ID, TackleContract.TackleEvent.START_DATE};

        switch (mViewState){
            case VIEW_STATE_DAY:
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(mSelectedDay);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                start = cal.getTimeInMillis();
                end = start + (24 * SEC_PER_HOUR * MILLI_PER_SECOND);
                selectionArgs = new String[]{String.valueOf(start), String.valueOf(end)};
                break;
            case VIEW_STATE_WEEK:
                Calendar week = Calendar.getInstance();
                week.setTimeInMillis(mSelectedDay);
                week.set(Calendar.HOUR_OF_DAY, 0);
                week.set(Calendar.MINUTE, 0);
                week.set(Calendar.SECOND, 0);
                week.set(Calendar.MILLISECOND, 0);
                start = week.getTimeInMillis();
                end = start + (5 * 24 * SEC_PER_HOUR * MILLI_PER_SECOND);
                selectionArgs = new String[]{String.valueOf(start), String.valueOf(end)};
                break;
        }

        c = getContentResolver().query(TackleContract.TackleEvent.CONTENT_URI, projection, selection, selectionArgs, null);
        c.moveToFirst();
        mNavigationDrawerFragment.count.setText(String.valueOf(c.getCount()));
        c.close();
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> cursorLoader) {
        switch (cursorLoader.getId()){
            case CATEGORY_LOADER:
                mNavigationDrawerFragment.mDrawerAdapter.swapCursor(null);
                break;
            case TACKLE_ITEMS_LOADER:
                mTackleListAdapter.swapCursor(null);
                break;
        }

    }

    @Override
    public void onCategoryPicked(String category, int color) {

        String colorString = getResources().getString(color);

        ContentValues values = new ContentValues();
        values.put(TackleContract.Categories.CATEGORY_NAME, category);
        values.put(TackleContract.Categories.COLOR, colorString);
        getContentResolver().insert(TackleContract.Categories.CONTENT_URI, values);

    }

    @Override
    public void onDeleteCategory(long id) {
        getContentResolver().delete(TackleContract.Categories.CONTENT_URI, TackleContract.Categories._ID + "=" + id, null);
        getContentResolver().delete(TackleContract.TackleEvent.CONTENT_URI, TackleContract.TackleEvent.CATEGORY_ID + "=" + id, null);
        if (id == mCategory){
            mNavigationDrawerFragment.selectItem(1, -1);
        }
    }

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
        weatherIds = new int[5];
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

    public void newCategory(View view){
        DialogFragment categoryPicker = new ColorPickerDialog();
        categoryPicker.show(getSupportFragmentManager(), "CategoryPicker");
    }

}
