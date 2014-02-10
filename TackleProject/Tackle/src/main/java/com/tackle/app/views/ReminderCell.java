package com.tackle.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tackle.app.R;

/**
 * Created by User on 2/9/14.
 */
public class ReminderCell extends LinearLayout {

    public ReminderCell(Context context) {
        super(context);
    }

    public ReminderCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.cell_reminder, this, true);

        TextView reminderText = (TextView) findViewById(R.id.reminder_text);
        ImageView remove = (ImageView) findViewById(R.id.remove);

    }

    public ReminderCell(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public interface RemoveReminderListener{
        public void onRemoveReminder(View view);
    }
}
