package com.dji.sdk.sample;

import android.util.Log;

import com.dji.sdk.sample.adapter.EPAdapter;
import com.dji.sdk.sample.model.ElectricPole;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
 * The type Connect server.
 */
public class ConnectServer {

    /**
     * The Json place holder api.
     */
    public JsonPlaceHolderApi jsonPlaceHolderApi;
    /**
     * The List elecpole.
     */
    public List<ElectricPole> list_elecpole = new ArrayList<>();
    /**
     * The Formatter.
     */
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * The Ok http client.
     */
//Khai báo các biến đầu vào để Call API từ server tương ứng tại baseURL
    OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();
    /**
     * The Retrofit.
     */
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://119.17.214.75:8081/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build();

    /**
     * Update ep server.
     *
     * @param id           the id
     * @param electricPole the electric pole
     */
    public void Update_EP_Server(String id, ElectricPole electricPole){
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<String> call = jsonPlaceHolderApi.updateEP(id, electricPole);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
//                Log.d("Update EP", response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
//                Log.e("Update EP", "Lỗi: " + t.getMessage());
            }
        });

    }
}
