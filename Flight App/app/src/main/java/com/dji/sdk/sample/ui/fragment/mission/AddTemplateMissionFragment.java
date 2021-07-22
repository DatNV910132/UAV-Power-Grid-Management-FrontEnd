package com.dji.sdk.sample.ui.fragment.mission;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dji.sdk.sample.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTemplateMissionFragment extends Fragment {

    public AddTemplateMissionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_template_mission, container, false);
    }
}
