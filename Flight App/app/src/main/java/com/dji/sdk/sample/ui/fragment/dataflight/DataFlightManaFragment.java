package com.dji.sdk.sample.ui.fragment.dataflight;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dji.sdk.sample.JsonPlaceHolderApi;
import com.dji.sdk.sample.R;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataFlightManaFragment extends Fragment {

    private JsonPlaceHolderApi jsonPlaceHolderApi;

    private TextView mtv_sum_video;
    private TextView mtv_sum_photo;
    private Button mbtn_dataflight_addphoto;
    private Button mbtn_dataflight_addvideo;
    private Button mbtn_dataflight_find;
    private EditText medt_dataflight_day;

    /**
     * Instantiates a new Data flight mana fragment.
     */
    public DataFlightManaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_data_flight_mana, container, false);

        //Link các trường giao diện với các layout giao diện đã thiết kế
        mtv_sum_photo = root.findViewById(R.id.tv_sum_photo);
        mtv_sum_video = root.findViewById(R.id.tv_sum_video);
        mbtn_dataflight_addphoto = root.findViewById(R.id.btn_dataflight_addphoto);
        mbtn_dataflight_addvideo = root.findViewById(R.id.btn_dataflight_addvideo);
        mbtn_dataflight_find = root.findViewById(R.id.btn_dataflight_find);
        medt_dataflight_day = root.findViewById(R.id.edt_dataflight_day);

        //Khai báo các biến đầu vào để Call API từ server tương ứng tại baseURL
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.URLDataDrone))
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        //Đếm sô lượng ảnh trong hệ thống để hiển thị ra giao diện
        Call<Integer> countphoto = jsonPlaceHolderApi.countphoto();
        countphoto.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                mtv_sum_photo.setText(response.body().toString());
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });

        //Đếm số lượng video trong hệ thống để hiện thị ra giao diện
        Call<Integer> countvideo = jsonPlaceHolderApi.countvideo();
        countvideo.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                mtv_sum_video.setText(response.body().toString());
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });

        //Sự kiện chọn button "thêm ảnh" vào hệ thống
        mbtn_dataflight_addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Chuyển sang fragment xử lý thêm dữ liệu ảnh vào hệ thống
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft_rep = fm.beginTransaction();
                ft_rep.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                ft_rep.replace(R.id.nav_host_fragment, new AddPhotoFragment());
                ft_rep.commit();
            }
        });

        //Sự kiện chọn button "thêm video" vào hệ thống
        mbtn_dataflight_addvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Chuyển sang fragment xử lý thêm dữ liệu video vào hệ thống
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft_rep = fm.beginTransaction();
                ft_rep.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                ft_rep.replace(R.id.nav_host_fragment, new AddVideoFragment());
                ft_rep.commit();
            }
        });

        return root;
    }
}
