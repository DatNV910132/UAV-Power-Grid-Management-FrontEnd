package com.dji.sdk.sample.ui.activity.incident;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.dji.sdk.sample.JsonPlaceHolderApi;
import com.dji.sdk.sample.R;
import com.dji.sdk.sample.adapter.List_ImageCheckInciAdapter;

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
 * The type View list image inci activity.
 */
public class ViewListImageInciActivity extends AppCompatActivity {
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private List<String> listid = new ArrayList<>();

    private ListView mlv_imagecheckinci;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image_inci);

        //Get Intent để lấy các giá trị truyền đi từ activity start gọi đến nó
        //Lấy giá trị id của cột điện, ngày và loại của hình
        Intent i = getIntent();
        String idpole = i.getStringExtra("idpole");
        String date = i.getStringExtra("date");
        boolean crop = i.getBooleanExtra("crop",false);

        // link các trường giao diện khai báo đến layout giao diện đã thiết kế
        mlv_imagecheckinci = findViewById(R.id.lv_viewcheckinci);

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

        //lấy về dữ liệu ảnh tương ứng với các trường được chọn
        Call<List<String>> call = jsonPlaceHolderApi.listimagecheck(idpole,date,crop);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                listid = response.body();
                //hiển thị ảnh ra listview
                List_ImageCheckInciAdapter adapter = new List_ImageCheckInciAdapter(getApplication(), R.layout.item_image_checkinci, (ArrayList<String>) listid);
                mlv_imagecheckinci.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });

        //Sự kiện chọn một ảnh trong listview
        mlv_imagecheckinci.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String idimage = listid.get(i);

                //Gọi intetn, truyền dữ liệu id ảnh và id cột đện vào và truyền đến activity xem ảnh
                Intent intent = new Intent(getApplicationContext(), ViewImageActivity.class);
                intent.putExtra("idimage",idimage);
                intent.putExtra("idpole",idpole);
                startActivity(intent);
                return true;
            }
        });
    }
}
