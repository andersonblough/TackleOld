package com.tackle.app.fragments.EditFragments;

import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tackle.app.Dialogs.CategoryPickerDialog;
import com.tackle.app.R;
import com.tackle.app.data.TackleContract;

/**
 * Created by Bill on 1/15/14.
 */
public class NotesFragment extends Fragment {

    LinearLayout categoryLayout;
    TextView categoryText;

    private long catID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_fragment_notes, container, false);

        categoryLayout = (LinearLayout) view.findViewById(R.id.category_picker);
        categoryText = (TextView) view.findViewById(R.id.category_text);
        categoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryPickerDialog picker = new CategoryPickerDialog();
                picker.show(getFragmentManager(), "picker");
            }
        });
        return view;
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
