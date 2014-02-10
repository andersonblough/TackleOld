package com.tackle.app.fragments.EditFragments;

import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tackle.app.Dialogs.CategoryPickerDialog;
import com.tackle.app.R;
import com.tackle.app.data.TackleContract;

/**
 * Created by Bill on 1/15/14.
 */
public class NotesFragment extends Fragment {

    private Cursor mCursor;

    private TextView categoryText;
    private EditText notesText;

    private String notes;
    private long catID;

    public NotesFragment(){}

    public NotesFragment(Cursor cursor){
        super();
        mCursor = cursor;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null){
            notes = savedInstanceState.getString("notes");
            catID = savedInstanceState.getLong("id");
        }
        else {
            mCursor.moveToFirst();
            notes = mCursor.getString(mCursor.getColumnIndex(TackleContract.TackleEvent.NOTES));
            catID = mCursor.getLong(mCursor.getColumnIndex(TackleContract.TackleEvent.CATEGORY_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_fragment_notes, container, false);

        //Set up view
        categoryText = (TextView) view.findViewById(R.id.category_text);
        notesText = (EditText) view.findViewById(R.id.note_text);


        categoryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryPickerDialog picker = new CategoryPickerDialog();
                picker.show(getFragmentManager(), "picker");
            }
        });

        //set up values
        setCategory(catID);
        if (!TextUtils.isEmpty(notes)){
            notesText.setText(notes);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("notes", notes);
        outState.putLong("id", catID);
    }

    public void setCategory(long id){
        catID = id;
        Uri uri = ContentUris.withAppendedId(TackleContract.Categories.CONTENT_URI, id);
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        categoryText.setText(cursor.getString(cursor.getColumnIndex(TackleContract.Categories.CATEGORY_NAME)));
        int tint = Color.parseColor(cursor.getString(cursor.getColumnIndex(TackleContract.Categories.COLOR)));
        categoryText.setTextColor(tint);
    }
}
