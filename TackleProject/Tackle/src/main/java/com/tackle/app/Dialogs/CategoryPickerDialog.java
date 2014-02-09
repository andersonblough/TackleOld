package com.tackle.app.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tackle.app.R;
import com.tackle.app.data.TackleContract;

/**
 * Created by Bill on 2/8/14.
 */
public class CategoryPickerDialog extends DialogFragment {

    ListView listView;
    CategorySelectedListener mListener;
    Cursor mCursor;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (CategorySelectedListener) activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement CategorySelectedListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mCursor = getActivity().getContentResolver().query(TackleContract.Categories.CONTENT_URI, null, null, null, null);

        listView = new ListView(getActivity());

        listView.setAdapter(new Adapter(getActivity(), mCursor, false));
        listView.setDivider(new ColorDrawable(getResources().getColor(R.color.light)));
        listView.setDividerHeight(2);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.onCategorySelected(id);
                dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setTitle("Choose a Category")
               .setView(listView);

        return builder.create();
    }

    private class Adapter extends CursorAdapter {

        public Adapter(Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            Holder holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.cat_pick_cell, viewGroup, false);
            holder.label = (TextView) view.findViewById(R.id.category_label);

            view.setTag(holder);

            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            Holder holder = (Holder) view.getTag();
            holder.label.setText(cursor.getString(cursor.getColumnIndex(TackleContract.Categories.CATEGORY_NAME)));

            int tint = Color.parseColor(cursor.getString(cursor.getColumnIndex(TackleContract.Categories.COLOR)));
            holder.label.setTextColor(tint);


        }

        private class Holder{
            TextView label;
        }
    }

    public interface CategorySelectedListener{
        public void onCategorySelected(long id);
    }
}
