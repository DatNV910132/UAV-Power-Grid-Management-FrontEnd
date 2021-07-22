package com.dji.sdk.sample.ui.fragment.incident;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dji.sdk.sample.JsonPlaceHolderApi;
import com.dji.sdk.sample.R;
import com.dji.sdk.sample.ui.activity.incident.ViewListImageInciActivity;
import com.dji.sdk.sample.adapter.SpinEPAdapter;
import com.dji.sdk.sample.model.ElectricPole;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The type Check inci fragment.
 */
public class CheckInciFragment extends Fragment {

    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private SpinEPAdapter spinEPAdapter;

    private Spinner msp_elecpole;
    private EditText medt_checkinci_date;
    private RadioGroup mrg_checkinci;
    private RadioButton mrb_true;
    private RadioButton mrb_false;
    private TextView mtv_checkinci_status;
    private Button mbtn_checkinci;
    private String idpole,date;
    private boolean crop;

    private ArrayList<ElectricPole> list_ep = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_check_incident, container, false);

        msp_elecpole = root.findViewById(R.id.sp_checkInci);
        medt_checkinci_date = root.findViewById(R.id.edt_checkInciDate);
        mrg_checkinci = root.findViewById(R.id.rg_checkinci);
        mrb_true = root.findViewById(R.id.rb_crop_true);
        mrb_false = root.findViewById(R.id.rb_crop_false);
        mtv_checkinci_status = root.findViewById(R.id.tv_checkinci_status);
        mbtn_checkinci = root.findViewById(R.id.btn_checkinci);
//        String imageUrl = "http://192.168.1.186:8080/photos/byte/5e586806ae087204b677e9a0";
//
//        //Loading image using Picasso
//        Picasso.get().load(imageUrl).into(mimv_checkimage1);

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

        Call<List<ElectricPole>> call = jsonPlaceHolderApi.getAllEP();
        call.enqueue(new Callback<List<ElectricPole>>() {
            @Override
            public void onResponse(Call<List<ElectricPole>> call, Response<List<ElectricPole>> response) {
                list_ep = (ArrayList<ElectricPole>) response.body();
                spinEPAdapter = new SpinEPAdapter(getContext(),android.R.layout.simple_spinner_item,list_ep);
                spinEPAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                msp_elecpole.setAdapter(spinEPAdapter);

            }

            @Override
            public void onFailure(Call<List<ElectricPole>> call, Throwable t) {

            }
        });

        msp_elecpole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                idpole = list_ep.get(i).get_id();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mrg_checkinci.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int checkradio = radioGroup.getCheckedRadioButtonId();
                if(checkradio == R.id.rb_crop_true){
                    crop = false;
                } else {
                    crop = true;
                }
            }
        });

        mrb_true.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                RadioButton radioButton = (RadioButton) compoundButton;
                crop = false;
            }
        });

        mrb_false.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                RadioButton radioButton = (RadioButton) compoundButton;
                crop = true;
            }
        });

        mbtn_checkinci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!medt_checkinci_date.getText().toString().equals("")){
                    date = medt_checkinci_date.getText().toString();
                   Intent i = new Intent(getContext(), ViewListImageInciActivity.class);
                   i.putExtra("idpole",idpole);
                   i.putExtra("date",date);
                   i.putExtra("crop",crop);
                   startActivity(i);
                } else {
                    mtv_checkinci_status.setText("Bạn cần nhập vào thông tin ngày cần kiểm tra");
                }
            }
        });

        return root;
    }
}