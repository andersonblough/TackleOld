package com.tackle.app;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

/**
 * Created by Bill on 11/18/13.
 */
public class AddActivity extends ActionBarActivity {
    private String type;
    private EditText tackleText;
    private ActionBar actionBar;
    SpinnerAdapter mSpinnerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        initValues();
        setUpActionBar();

        tackleText = (EditText) findViewById(R.id.tackle_edit_text);
        //tackleText.hasFocus();
        //setTackleText(type);
        //InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        //imm.showSoftInput(tackleText, InputMethodManager.SHOW_IMPLICIT);

    }

    private void initValues() {
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            type = extras.getString("type");
        }
        else {
            type = "to-do";
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
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
                return true;
            }
        });
        String[] types = getResources().getStringArray(R.array.types);
        for (int i = 0; i < types.length; i++){
            if (types[i].equalsIgnoreCase(type)){
                actionBar.setSelectedNavigationItem(i);

            }
        }
    }

    public void onMoreClicked(View view){
        Toast.makeText(this, "Edit More", Toast.LENGTH_SHORT).show();
    }
}
