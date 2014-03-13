package com.tackle.app.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.tackle.app.data.TackleContract;

/**
 * Created by Bill on 2/19/14.
 */
public class ClearItemsDialog extends DialogFragment {
    long[] itemIds;

    public ClearItemsDialog(long[] ids) {
        super();
        itemIds = ids;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setMessage("Clear all Tackled Items?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ContentResolver cr = getActivity().getContentResolver();
                        for (int i = 0; i < itemIds.length; i++) {
                            long id = itemIds[i];
                            Uri uri = ContentUris.withAppendedId(TackleContract.ListItems.CONTENT_URI, id);
                            cr.delete(uri, null, null);
                        }
                    }
                });
        return builder.create();
    }
}
