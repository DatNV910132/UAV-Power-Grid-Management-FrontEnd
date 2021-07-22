package com.dji.sdk.sample.ui.fragment.incident;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dji.sdk.sample.JsonPlaceHolderApi;
import com.dji.sdk.sample.R;

import java.util.List;
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
public class AutoCheckInciFragment extends Fragment {

    private JsonPlaceHolderApi jsonPlaceHolderApi;

    private Button mbtn_autocheckinci;

    /**
     * Instantiates a new Auto check inci fragment.
     */
    public AutoCheckInciFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_auto_check_inci, container, false);

        mbtn_autocheckinci = root.findViewById(R.id.btn_autocheckinci);

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

        mbtn_autocheckinci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<List<String>> call = jsonPlaceHolderApi.autoinci();
                call.enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        List<String> result = response.body();
                        if (result.isEmpty()) {
                            Toast.makeText(getContext(), "Không phát hiện sự cố bất thường nào", Toast.LENGTH_SHORT).show();
                        } else {
                            FragmentManager fm = getFragmentManager();
                            FragmentTransaction ft_rep = fm.beginTransaction();
                            ft_rep.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                            ft_rep.replace(R.id.nav_host_fragment, new ListConfirmInciFragment());
                            ft_rep.commit();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {

                    }
                });
            }
        });


        return root;
    }
}
