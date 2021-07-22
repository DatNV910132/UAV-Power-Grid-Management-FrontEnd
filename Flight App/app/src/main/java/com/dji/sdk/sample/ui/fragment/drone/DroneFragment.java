package com.dji.sdk.sample.ui.fragment.drone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dji.sdk.sample.R;

/**
 * The type Drone fragment.
 */
public class DroneFragment extends Fragment {

    private TextView mtv_sum_drone;
    private TextView mtv_onl_drone;
    private TextView mtv_wait_drone;
    private TextView mtv_error_drone;
    private Button mbtn_add_drone;
    private ListView mlv_drone;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_drone, container, false);

        //Link các trường giao diện đã khai báo với layout giao diện đã thiết kế
        mtv_sum_drone = root.findViewById(R.id.tv_sum_drone);
        mtv_onl_drone = root.findViewById(R.id.tv_onl_drone);
        mtv_wait_drone = root.findViewById(R.id.tv_wait_drone);
        mtv_error_drone = root.findViewById(R.id.tv_error_drone);
        mbtn_add_drone = root.findViewById(R.id.btn_add_drone);
        mlv_drone = root.findViewById(R.id.lv_drone);



        return root;
    }
}