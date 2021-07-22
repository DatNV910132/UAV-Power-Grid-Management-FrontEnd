package com.dji.sdk.sample.ui.fragment.incident;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dji.sdk.sample.JsonPlaceHolderApi;
import com.dji.sdk.sample.R;
import com.dji.sdk.sample.adapter.ListConfirmInciAdapter;
import com.dji.sdk.sample.model.Incident;

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
 * A simple {@link Fragment} subclass.
 */
public class ListConfirmInciFragment extends Fragment {

    private JsonPlaceHolderApi jsonPlaceHolderApi;

    private Button mbtn_confirminci_all;
    private Button mbtn_confirminci_back;
    private ListView mlv_confirminci;
    private List<Incident> list = new ArrayList<>();


    /**
     * Instantiates a new List confirm inci fragment.
     */
    public ListConfirmInciFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        list.clear();
        Call<List<Incident>> getallwaitinci = jsonPlaceHolderApi.getallinciwithstatus("wait");
        getallwaitinci.enqueue(new Callback<List<Incident>>() {
            @Override
            public void onResponse(Call<List<Incident>> call, Response<List<Incident>> response) {
                List<Incident> list = response.body();
                ListConfirmInciAdapter adapter = new ListConfirmInciAdapter(getContext(),R.layout.listitem_confirminci, (ArrayList<Incident>) list);
                mlv_confirminci.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Incident>> call, Throwable t) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_listconfirm_inci, container, false);
        mbtn_confirminci_all = root.findViewById(R.id.btn_confirminci_all);
        mbtn_confirminci_back = root.findViewById(R.id.btn_confirm_back);
        mlv_confirminci = root.findViewById(R.id.lv_confirminci);

        //Khai báo các biến đầu vào để Call API từ server tương ứng tại baseURL
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.URLIncidentService))
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        mbtn_confirminci_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft_rep = fm.beginTransaction();
                ft_rep.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                ft_rep.replace(R.id.nav_host_fragment, new AutoCheckInciFragment());
                ft_rep.commit();
            }
        });

        mbtn_confirminci_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<String> call = jsonPlaceHolderApi.confirminciall();
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String result = response.body();
                        if (result.equals("successful")) {
                            Toast.makeText(getContext(), "Phê duyệt tất cả thành công", Toast.LENGTH_SHORT).show();
                            FragmentManager fm = getFragmentManager();
                            FragmentTransaction ft_rep = fm.beginTransaction();
                            ft_rep.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                            ft_rep.replace(R.id.nav_host_fragment, new IncidentFragment());
                            ft_rep.commit();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
//                        Log.e("Confirminciall",t.getMessage());
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft_rep = fm.beginTransaction();
                        ft_rep.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                        ft_rep.replace(R.id.nav_host_fragment, new IncidentFragment());
                        ft_rep.commit();
                    }
                });
            }
        });

        Call<List<Incident>> getallwaitinci = jsonPlaceHolderApi.getallinciwithstatus("wait");
        getallwaitinci.enqueue(new Callback<List<Incident>>() {
            @Override
            public void onResponse(Call<List<Incident>> call, Response<List<Incident>> response) {
                list = response.body();
                ListConfirmInciAdapter adapter = new ListConfirmInciAdapter(getContext(), R.layout.listitem_confirminci, (ArrayList<Incident>) list);
                mlv_confirminci.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Incident>> call, Throwable t) {

            }
        });

        mlv_confirminci.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FragmentManager fm = getFragmentManager();
                ConfirmInciFragment confirmInciFragment = new ConfirmInciFragment();
                FragmentTransaction ft_rep = fm.beginTransaction();
                ft_rep.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                Bundle bundle = new Bundle();
                bundle.putString("idinci",list.get(i).getId());
                confirmInciFragment.setArguments(bundle);
                ft_rep.replace(R.id.nav_host_fragment, confirmInciFragment);
                ft_rep.commit();
            }
        });

        return root;
    }
}
