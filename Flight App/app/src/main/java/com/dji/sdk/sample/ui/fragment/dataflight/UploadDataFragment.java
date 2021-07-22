package com.dji.sdk.sample.ui.fragment.dataflight;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.dji.sdk.sample.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadDataFragment extends Fragment {

    /**
     * Instantiates a new Upload data fragment.
     */
    public UploadDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_upload_data, container, false);

        return root;
    }
}
