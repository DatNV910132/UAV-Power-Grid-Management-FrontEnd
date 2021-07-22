package com.dji.sdk.sample.ui.fragment.electricpole;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.dji.sdk.sample.JsonPlaceHolderApi;
import com.dji.sdk.sample.R;
import com.dji.sdk.sample.model.ElectricPole;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddEPFragment extends Fragment {

    private EditText medt_epadd_name;
    private EditText medt_epadd_des;
    private EditText medt_epadd_latitude;
    private EditText medt_epadd_longitude;
    private EditText medt_epadd_datebuild;
    private EditText medt_epadd_datemain;
    private TextView mtv_epadd_status;
    private Button mbtn_epadd_save;
    private Button mbtn_epadd_cancel;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private ElectricPole electricPole;

    /**
     * Instantiates a new Add ep fragment.
     */
    public AddEPFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_add_e_p, container, false);

        //Khai báo các biến đầu vào để Call API từ server tương ứng tại baseURL
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.URLDataService))
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        medt_epadd_name = root.findViewById(R.id.edt_epadd_name);
        medt_epadd_des = root.findViewById(R.id.edt_epadd_des);
        medt_epadd_latitude = root.findViewById(R.id.edt_epadd_latitude);
        medt_epadd_longitude = root.findViewById(R.id.edt_epadd_longitude);
        medt_epadd_datebuild = root.findViewById(R.id.edt_epadd_datebuild);
        medt_epadd_datemain = root.findViewById(R.id.edt_epadd_datemain);
        mtv_epadd_status = root.findViewById(R.id.tv_epadd_status);
        mbtn_epadd_save = root.findViewById(R.id.btn_epadd_save);
        mbtn_epadd_cancel = root.findViewById(R.id.btn_epadd_cancel);
        
        return root;
    }
}
