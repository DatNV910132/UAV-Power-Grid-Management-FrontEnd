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
public class AddVideoFragment extends Fragment {

    /**
     * Instantiates a new Add video fragment.
     */
    public AddVideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_video, container, false);
    }
}
