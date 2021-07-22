package com.dji.sdk.sample.ui.fragment.electricpole;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dji.sdk.sample.ui.activity.electricpole.Edit_EP_Activity;
import com.dji.sdk.sample.JsonPlaceHolderApi;
import com.dji.sdk.sample.R;
import com.dji.sdk.sample.adapter.EPAdapter;
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
 * The type Cot dien fragment.
 */
public class CotDienFragment extends Fragment {

    private TextView mtv_sum_pole;
    private TextView mtv_status_pole;
    private TextView mtv_error_pole;
    private Button mbtn_add_pole;
    private Button mbtn_map_pole;
    private ListView mlv_pole;

    private JsonPlaceHolderApi jsonPlaceHolderApi;
    /**
     * The List elecpole.
     */
    ArrayList<ElectricPole> list_elecpole = new ArrayList<>();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_elecpole, container, false);

        mtv_sum_pole = root.findViewById(R.id.tv_sum_pole);
        mtv_status_pole = root.findViewById(R.id.tv_status_pole);
        mtv_error_pole = root.findViewById(R.id.tv_error_pole);
        mbtn_map_pole = root.findViewById(R.id.btn_map_pole);
        mbtn_add_pole = root.findViewById(R.id.btn_add_pole);
        mlv_pole = root.findViewById(R.id.lv_pole);

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

        mbtn_add_pole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft_rep = fm.beginTransaction();
                ft_rep.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                ft_rep.replace(R.id.nav_host_fragment, new AddEPFragment());
                ft_rep.commit();
            }
        });

      //  Lấy thông tin của tất cả cột điện
        Call<List<ElectricPole>> call = jsonPlaceHolderApi.getAllEP();
        call.enqueue(new Callback<List<ElectricPole>>() {
            @Override
            public void onResponse(Call<List<ElectricPole>> call, Response<List<ElectricPole>> response) {
                List<ElectricPole> list = response.body();

                for (ElectricPole ep : list) {
                    list_elecpole.add(ep);
                }
                mtv_sum_pole.setText(Integer.toString(list.size()));
                EPAdapter adapter = new EPAdapter(getActivity(), R.layout.list_item_elecpole, list_elecpole);
                mlv_pole.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<ElectricPole>> call, Throwable t) {
                Log.e("User Manager", "Lỗi: " + t.getMessage());
            }
        });

        mlv_pole.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ElectricPole ep = list_elecpole.get(i);
                Intent intent = new Intent(getActivity(), Edit_EP_Activity.class);
                intent.putExtra("id_pole",ep.get_id());
                startActivity(intent);
            }
        });

        return root;
    }


}
