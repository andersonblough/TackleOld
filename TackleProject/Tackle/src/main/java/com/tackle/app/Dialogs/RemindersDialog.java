package com.tackle.app.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.tackle.app.R;
import com.tackle.app.data.TackleContract;

/**
 * Created by Bill on 2/18/14.
 */
public class RemindersDialog extends DialogFragment {

    private GridView mReminderGrid;
    private int[] minutes = {0, 120, 5, 180, 10, 240, 15, 360, 20, 720, 30, 1440, 45, 2880, 60, 10080};
    private LayoutInflater inflater;
    private View view;
    private long mID;

    public RemindersDialog(long id) {
        super();
        mID = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.dialog_reminders, null, false);
        mReminderGrid = (GridView) view.findViewById(R.id.reminder_grid);
        mReminderGrid.setAdapter(new ReminderAdapter());

        mReminderGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContentResolver cr = getActivity().getContentResolver();
                ContentValues reminder = new ContentValues();
                reminder.put(TackleContract.Reminders.EVENT_ID, mID);
                reminder.put(TackleContract.Reminders.MINUTES, minutes[position]);
                cr.insert(TackleContract.Reminders.CONTENT_URI, reminder);
                dismiss();
            }
        });

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setView(view).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }

    private class ReminderAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return minutes.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = inflater.inflate(R.layout.reminder_text, parent, false);
            }
            TextView minTV = (TextView) view.findViewById(R.id.minutes);
            TextView post = (TextView) view.findViewById(R.id.post);

            int min = minutes[position];

            if (min == 0) {
                minTV.setText("On");
                post.setText("time");
            } else if (min < 60) {
                minTV.setText(String.valueOf(min));
                post.setText("minutes");
            } else if (min == 60) {
                minTV.setText("1");
                post.setText("hour");
            } else if (min < 1440) {
                minTV.setText(String.valueOf(min / 60));
                post.setText("hours");
            } else if (min == 1440) {
                minTV.setText("1");
                post.setText("day");
            } else if (min == 2880) {
                minTV.setText("2");
                post.setText("days");
            } else if (min == 10080) {
                minTV.setText("1");
                post.setText("week");
            }

            return view;
        }
    }
}
