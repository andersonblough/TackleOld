package com.tackle.app.Dialogs;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.tackle.app.data.TackleContract;
import com.tackle.app.data.TackleDatabase;
import com.tackle.app.data.TackleProvider;

/**
 * Created by Bill on 8/6/13.
 */
public class DeleteDialog extends DialogFragment {

    private long id;

    private DeleteCategoryListener mListener;

    public DeleteDialog(long id){
        this.id = id;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (DeleteCategoryListener) activity;
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement DeleteCategoryListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        builder.setMessage("Do you want to delete this Category?")
                .setCancelable(true)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onDeleteCategory(id);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do Nothing //
                    }
                });
        return builder.create();
    }

    public interface DeleteCategoryListener{
        public void onDeleteCategory(long id);
    }
}
