package com.tackle.app.fragments.EditFragments;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tackle.app.Dialogs.DatePickerFragment;
import com.tackle.app.Dialogs.NumberPickerFragment;
import com.tackle.app.Dialogs.RemindersDialog;
import com.tackle.app.R;
import com.tackle.app.data.TackleContract;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Bill on 1/16/14.
 */
public class RemindersFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    long untilDate;
    int choice;
    int weekVisibility;
    int untilVisibility;
    boolean[] daysChosen;
    int untilPos;
    int untilCount;

    private long mID;

    private GridView freqGrid;
    private GridView weekGrid;
    private GridView untilGrid;
    private LinearLayout remindersList;
    private TextView addReminder;

    private UntilAdapter untilAdapter;
    private ReminderAdapter reminderAdapter;

    RelativeLayout weekLayout;
    RelativeLayout untilLayout;

    private Cursor mCursor;

    public RemindersFragment(Cursor cursor) {
        super();
        mCursor = cursor;
    }

    public RemindersFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        setRetainInstance(true);

        //set up values
        if (savedState != null) {
            // restore from state
            choice = savedState.getInt("choice");
            weekVisibility = savedState.getInt("weekVisible");
            daysChosen = savedState.getBooleanArray("daysChosen");
            untilPos = savedState.getInt("untilPos");
            untilVisibility = savedState.getInt("untilVisible");
            untilDate = savedState.getLong("untilDate");
            untilCount = savedState.getInt("untilCount");

        } else {
            // first time, set up everything
            untilDate = System.currentTimeMillis();
            choice = -1;
            weekVisibility = View.GONE;
            untilVisibility = View.GONE;
            daysChosen = new boolean[]{false, false, false, false, false, false, false};
            untilCount = 1;

        }

        //create until adapter
        untilAdapter = new UntilAdapter();
        // create reminder adapter
        reminderAdapter = new ReminderAdapter();

        //set up the id for the event
        if (mCursor.moveToFirst()) {
            mID = mCursor.getLong(mCursor.getColumnIndex(TackleContract.TackleEvent._ID));
        }
        Bundle args = new Bundle();
        args.putLong("id", mID);
        getActivity().getSupportLoaderManager().initLoader(1, args, this);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.edit_fragment_reminders, container, false);

        //Only things to do with the view

        //set up the 3 Grid Views
        freqGrid = (GridView) view.findViewById(R.id.freq);
        weekGrid = (GridView) view.findViewById(R.id.weekGrid);
        untilGrid = (GridView) view.findViewById(R.id.untilGrid);

        //set up the 2 layouts
        weekLayout = (RelativeLayout) view.findViewById(R.id.weekDays);
        untilLayout = (RelativeLayout) view.findViewById(R.id.until);

        //set up the reminders list
        remindersList = (LinearLayout) view.findViewById(R.id.reminder_container);
        addReminder = (TextView) view.findViewById(R.id.add_reminder);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //set Visibility of week and until layouts
        weekLayout.setVisibility(weekVisibility);
        untilLayout.setVisibility(untilVisibility);

        //set adapters for grid views
        ArrayAdapter freqAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.frequencies, R.layout.picker_text);
        freqGrid.setAdapter(freqAdapter);

        String[] week = {"S", "M", "T", "W", "T", "F", "S"};
        ArrayAdapter weekAdapter = new ArrayAdapter(getActivity(), R.layout.picker_text, week);
        weekGrid.setAdapter(weekAdapter);

        untilGrid.setAdapter(untilAdapter);

        //add reminders to list and set click listener
        addReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // temporarily add reminders to database
                /*ContentValues reminder = new ContentValues();
                reminder.put(TackleContract.Reminders.EVENT_ID, mID);
                reminder.put(TackleContract.Reminders.MINUTES, 45);
                getActivity().getContentResolver().insert(TackleContract.Reminders.CONTENT_URI, reminder);
                */
                RemindersDialog remindersDialog = new RemindersDialog(mID);
                remindersDialog.show(getActivity().getSupportFragmentManager(), "reminders");
            }
        });

        //set up grid views with values
        if (choice != -1) {
            freqGrid.setItemChecked(choice, true);
        }

        for (int i = 0; i < daysChosen.length; i++) {
            weekGrid.setItemChecked(i, daysChosen[i]);
        }

        switch (untilPos) {
            case 0:
                untilGrid.setItemChecked(untilPos, true);
                break;
            case 1:
                untilGrid.setItemChecked(untilPos, true);
                Date date = new Date(untilDate);
                DateFormat formater = new SimpleDateFormat("M/dd/yyyy");
                untilAdapter.setUntil(formater.format(date));
                break;
            case 2:
                untilGrid.setItemChecked(untilPos, true);
                if (untilCount == 1) {
                    untilAdapter.setCount("Once");
                } else {
                    untilAdapter.setCount(String.valueOf(untilCount) + " times");
                }

        }

        //set up ItemClickListeners for the grid views
        freqGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == choice) {
                    freqGrid.setItemChecked(i, false);
                    choice = -1;
                    untilVisibility = View.GONE;
                } else {
                    freqGrid.setItemChecked(i, true);
                    choice = i;
                    untilVisibility = View.VISIBLE;

                }
                if (choice == 1) {
                    weekVisibility = View.VISIBLE;
                } else {
                    weekVisibility = View.GONE;
                }
                weekLayout.setVisibility(weekVisibility);
                untilLayout.setVisibility(untilVisibility);
            }
        });

        weekGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (daysChosen[position]) {
                    daysChosen[position] = false;
                } else {
                    daysChosen[position] = true;
                }
            }
        });

        untilGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                untilPos = position;
                switch (position) {
                    case 0:
                        untilAdapter.setUntil("Until");
                        untilDate = System.currentTimeMillis();
                        break;
                    case 1:
                        DatePickerFragment dialog = new DatePickerFragment(untilDate);
                        dialog.show(getFragmentManager(), DatePickerFragment.UNTILDATE);
                        break;
                    case 2:
                        NumberPickerFragment numPicker = new NumberPickerFragment();
                        numPicker.show(getFragmentManager(), "numPicker");
                        break;
                }

            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("choice", choice);
        outState.putInt("weekVisible", weekVisibility);
        outState.putBooleanArray("daysChosen", daysChosen);
        outState.putInt("untilPos", untilPos);
        outState.putInt("untilVisible", untilVisibility);
        outState.putLong("untilDate", untilDate);
        outState.putInt("untilCount", untilCount);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void setUntilDate(long dateTime) {
        untilDate = dateTime;
        Date date = new Date(untilDate);
        DateFormat formater = new SimpleDateFormat("M/dd/yyyy");
        untilAdapter.setUntil(formater.format(date));
    }

    public void setCountValue(int count) {
        untilCount = count;
        if (untilCount == 1) {
            untilAdapter.setCount("Once");
        } else {
            untilAdapter.setCount(String.valueOf(untilCount) + " times");
        }

    }

    private void setRemindersList() {
        remindersList.removeAllViews();
        final int count = reminderAdapter.getCount();
        for (int i = 0; i < count; i++) {
            View v = reminderAdapter.getView(i, null, null);
            remindersList.addView(v);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = new String[]{TackleContract.Reminders.ID, TackleContract.Reminders.EVENT_ID, TackleContract.Reminders.MINUTES};
        String selection = TackleContract.Reminders.EVENT_ID + "=" + String.valueOf(bundle.getLong("id"));
        CursorLoader cursorLoader = new CursorLoader(getActivity(), TackleContract.Reminders.CONTENT_URI, projection, selection, null, TackleContract.Reminders.MINUTES + " ASC");
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        //Toast.makeText(getActivity(), String.valueOf(cursor.getCount()), Toast.LENGTH_SHORT).show();
        reminderAdapter.setCursor(cursor);
        setRemindersList();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private class UntilAdapter extends BaseAdapter {
        String until = "Until";
        String count = "Count";

        public void setUntil(String until) {
            this.until = until;
            count = "Count";
            notifyDataSetChanged();
        }

        public void setCount(String count) {
            this.count = count;
            until = "Until";
            untilDate = System.currentTimeMillis();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            TextView view = (TextView) convertView;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = (TextView) inflater.inflate(R.layout.picker_text, viewGroup, false);
            }

            switch (position) {
                case 0:
                    view.setText("Forever");
                    break;
                case 1:
                    view.setText(until);
                    break;
                case 2:
                    view.setText(count);
                    break;
            }
            return view;
        }
    }

    private class ReminderAdapter extends BaseAdapter {
        private Cursor c;

        public void setCursor(Cursor cursor) {
            if (c != null) {
                c.close();
                c = null;
            }
            c = cursor;
        }

        public Cursor getCursor() {
            return c;
        }

        @Override
        public int getCount() {
            if (c == null) {
                return 0;
            }
            return c.getCount();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.cell_reminder, viewGroup, false);
            }

            TextView reminderLabel = (TextView) view.findViewById(R.id.reminder_text);
            ImageView remove = (ImageView) view.findViewById(R.id.remove);

            if (c.moveToPosition(position)) {
                int minBefore = c.getInt(c.getColumnIndex(TackleContract.Reminders.MINUTES));
                String reminderText = "";
                if (minBefore == 0) {
                    reminderText = "On time";
                } else if (minBefore < 60) {
                    reminderText = minBefore + " minutes";
                } else if (minBefore == 60) {
                    reminderText = "1 hour";
                } else if (minBefore < (24 * 60)) {
                    int hours = minBefore / 60;
                    reminderText = hours + " hours";
                } else if (minBefore == (24 * 60)) {
                    reminderText = "1 day";
                } else if (minBefore == (2 * 24 * 60)) {
                    reminderText = "2 days";
                } else if (minBefore == (7 * 24 * 60)) {
                    reminderText = "1 week";
                }
                reminderLabel.setText(reminderText);
                final int pos = c.getPosition();

                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(getActivity(), String.valueOf(pos), Toast.LENGTH_SHORT).show();
                        deleteView(pos);
                    }
                });
            }
            final long id = c.getLong(c.getColumnIndex(TackleContract.Reminders.ID));
            view.setTag(id);
            view.setPadding(12, 2, 12, 2);

            return view;
        }
    }

    private void deleteView(int position) {
        Cursor cursor = reminderAdapter.getCursor();
        cursor.moveToPosition(position);
        long id = cursor.getLong(cursor.getColumnIndex(TackleContract.Reminders.ID));
        Uri uri = ContentUris.withAppendedId(TackleContract.Reminders.CONTENT_URI, id);
        getActivity().getContentResolver().delete(uri, null, null);
    }


}
