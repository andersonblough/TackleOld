package com.tackle.app.fragments.EditFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tackle.app.R;

/**
 * Created by Bill on 1/15/14.
 */
public class ShareFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.edit_fragment_share, container, false);
    }
}
