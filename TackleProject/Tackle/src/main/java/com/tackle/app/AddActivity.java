package com.tackle.app;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.tackle.app.data.TackleContract;
import com.tackle.app.views.TackleEditText;

import java.util.Calendar;

/**
 * Created by Bill on 11/18/13.
 */
public class AddActivity extends ActionBarActivity {
    private int mType;
    private long mCategory;
    private long mDate;
    private TackleEditText tackleText;
    private ActionBar actionBar;
    SpinnerAdapter mSpinnerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        initValues();
        setUpMonthImage();

        setUpActionBar();

        tackleText = (TackleEditText) findViewById(R.id.tackle_edit_text);
        tackleText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    if (TextUtils.isEmpty(textView.getText())){
                        return false;
                    }
                    ContentValues values = new ContentValues();
                    values.put(TackleContract.TackleEvent.NAME, textView.getText().toString());
                    values.put(TackleContract.TackleEvent.CATEGORY_ID, mCategory);
                    values.put(TackleContract.TackleEvent.START_DATE, mDate);
                    values.put(TackleContract.TackleEvent.TYPE, mType);
                    values.put(TackleContract.TackleEvent.ALL_DAY, 0);

                    getContentResolver().insert(TackleContract.TackleEvent.CONTENT_URI, values);

                    finish();
                    overridePendingTransition(0,0);
                    return true;

                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2){
            if (resultCode == RESULT_OK){
                finish();

            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0,0);
    }

    private void setUpMonthImage() {
        TypedArray monthImages = getResources().obtainTypedArray(R.array.months);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(mDate);
        int month = cal.get(Calendar.MONTH);

        ImageView monthImage = (ImageView) findViewById(R.id.month_image);
        monthImage.setImageDrawable(monthImages.getDrawable(month));
    }

    private void initValues() {
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            mType = extras.getInt("type");
            mDate = extras.getLong("dateTime");
            mCategory = extras.getLong("category");
        }
        else {
            mType = 1;
            mDate = System.currentTimeMillis();
            mCategory = 1;
        }
        if (mCategory == 0){
            mCategory = 1;
        }



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                overridePendingTransition(0,0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setTackleText(String type) {
        tackleText.setHint("tackle a new " + type);

    }

    private void setUpActionBar() {
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        mSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.types, R.layout.spinner_item_layout);
        actionBar.setListNavigationCallbacks(mSpinnerAdapter, new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int postion, long id) {
                String[] array = getResources().getStringArray(R.array.types);
                setTackleText(array[postion].toLowerCase());
                mType = postion + 1;
                return true;
            }
        });
        actionBar.setSelectedNavigationItem(mType - 1);
    }

    public void onMoreClicked(View view){
        //Toast.makeText(this, "Edit More", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, EditActivity.class);
        startActivityForResult(intent, 2);
    }
}
