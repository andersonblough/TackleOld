package com.tackle.app.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.tackle.app.R;

/**
 * Created by Bill on 1/24/14.
 */
public class NumberPickerFragment extends DialogFragment{

    EditText occurences;
    int count;
    CountChangedListener mListener;

    public NumberPickerFragment(){
        this(1);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (CountChangedListener) activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement CountChamgedListener");
        }
    }

    public NumberPickerFragment(int count){
        this.count = count;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_num_picker, null, false);

        occurences = (EditText) view.findViewById(R.id.count);
        if (count > 1){
            occurences.setText(String.valueOf(count));
        }

        occurences.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                occurences.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(occurences, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
            }
        });
        occurences.requestFocus();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setTitle("Number of Occurrences")
                .setView(view)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (TextUtils.isEmpty(occurences.getText())){
                            count = 1;
                        }
                        else {
                            count = Integer.parseInt(occurences.getText().toString());
                        }

                        mListener.onCountChanged(count);

                    }
                });
        return builder.create();
    }

    public interface CountChangedListener{
        public void onCountChanged(int count);
    }
}
