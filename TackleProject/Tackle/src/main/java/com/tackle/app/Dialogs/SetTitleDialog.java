package com.tackle.app.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.tackle.app.R;

/**
 * Created by User on 2/9/14.
 */
public class SetTitleDialog extends DialogFragment {
    private String mTitle;
    private TitleChangeListener mListener;

    public SetTitleDialog(String title){
        super();
        mTitle = title;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (TitleChangeListener) activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement TitleChangedListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final EditText titleText = new EditText(getActivity());
        LinearLayout ll = new LinearLayout(getActivity());
        ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ll.setGravity(Gravity.CENTER_VERTICAL);
        ll.setPadding(16, 16, 16, 16);

        titleText.setTextColor(getResources().getColor(R.color.black));
        titleText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        titleText.setText(mTitle);

        ll.addView(titleText);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setView(ll).setTitle("Edit Title").setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String newTitle = titleText.getText().toString();
                mListener.onTitleChange(newTitle);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        return builder.create();
    }

    public interface TitleChangeListener{
        public void onTitleChange(String title);
    }
}
