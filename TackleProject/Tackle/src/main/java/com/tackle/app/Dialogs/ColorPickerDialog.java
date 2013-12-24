package com.tackle.app.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import com.tackle.app.views.ColorPicker;

/**
 * Created by Bill on 12/17/13.
 */
public class ColorPickerDialog extends DialogFragment {

    private CategoryPickerListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (CategoryPickerListener) activity;
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement CategoryPickerListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        final ColorPicker picker = new ColorPicker(getActivity());
        builder.setView(picker)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String categoryText = picker.mCategoryTitle.getText().toString();
                        int colorRes = picker.mColorRes;
                        if (!categoryText.isEmpty() && colorRes != 0) {
                            mListener.onCategoryPicked(categoryText, colorRes);
                        } else {
                            if (categoryText.isEmpty() && colorRes == 0){
                                Toast.makeText(getActivity(), "Nothing Added", Toast.LENGTH_SHORT).show();
                            }
                            else if (categoryText.isEmpty()) {
                                Toast.makeText(getActivity(), "Please include title", Toast.LENGTH_SHORT).show();
                            }
                            else if (colorRes == 0) {
                                Toast.makeText(getActivity(), "Please choose a color", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }

    public interface CategoryPickerListener{
        public void onCategoryPicked(String category, int color);
    }

}
