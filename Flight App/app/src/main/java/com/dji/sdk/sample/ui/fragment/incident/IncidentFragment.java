package com.dji.sdk.sample.ui.fragment.incident;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dji.sdk.sample.JsonPlaceHolderApi;
import com.dji.sdk.sample.R;
import com.dji.sdk.sample.ui.activity.incident.DetailInciActivity;
import com.dji.sdk.sample.adapter.ListInciAdapter;
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
 * The type Incident fragment.
 */
public class IncidentFragment extends Fragment {

    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private List<Incident> listincident;
    private List<Incident> listrepairing = new ArrayList<>();
    private List<Incident> listnorepair = new ArrayList<>();
    private List<Incident> listwait = new ArrayList<>();
    private List<Incident> listrepaired = new ArrayList<>();

    private TextView mtv_sum_inci;
    private TextView mtv_repair_inci;
    private TextView mtv_norepair_inci;
    private TextView mtv_waitconfirm_inci;
    private TextView mtv_repaired_inci;
    private Spinner msp_level_inci;
    private ListView mlv_inci;

    @Override
    public void onResume() {
        super.onResume();
        Call<List<Incident>> getallinci = jsonPlaceHolderApi.getallinci();
        getallinci.enqueue(new Callback<List<Incident>>() {
            @Override
            public void onResponse(Call<List<Incident>> call, Response<List<Incident>> response) {
                listincident = response.body();
                int sum = 0, repairing = 0, norepair = 0, wait = 0, repaired = 0;
                sum = listincident.size();
                for (Incident incident : listincident) {
                    if (incident.getStatus().equals("repairing")) {
                        listrepairing.add(incident);
                        repairing++;
                    }
                    if (incident.getStatus().equals("norepair")) {
                        listnorepair.add(incident);
                        norepair++;
                    }
                    if (incident.getStatus().equals("repaired")) {
                        listrepaired.add(incident);
                        repaired++;
                    }
                    if (incident.getStatus().equals("wait")) {
                        listrepairing.add(incident);
                        wait++;
                    }
                }
                ListInciAdapter adapter = new ListInciAdapter(getContext(), R.layout.inci_item_list, (ArrayList<Incident>) listincident);
                mlv_inci.setAdapter(adapter);
                mtv_sum_inci.setText(Integer.toString(sum));
                mtv_repair_inci.setText(Integer.toString(repairing));
                mtv_waitconfirm_inci.setText(Integer.toString(wait));
                mtv_repaired_inci.setText(Integer.toString(repaired));
                mtv_norepair_inci.setText(Integer.toString(norepair));
                mlv_inci.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(getContext(), DetailInciActivity.class);
                        intent.putExtra("idinci", listincident.get(i).getId());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Incident>> call, Throwable t) {

            }
        });
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_incident, container, false);

        mtv_sum_inci = root.findViewById(R.id.tv_sum_inci);
        mtv_repair_inci = root.findViewById(R.id.tv_repair_inci);
        mtv_repaired_inci = root.findViewById(R.id.tv_repaired_inci);
        mtv_norepair_inci = root.findViewById(R.id.tv_norepair_inci);
        mtv_waitconfirm_inci = root.findViewById(R.id.tv_waitconfirm_inci);
        msp_level_inci = root.findViewById(R.id.sp_mana_inci);
        mlv_inci = root.findViewById(R.id.lv_inci);


        String level[] = {
                "Tất các các mức độ",
                "Mức độ nguy hiểm 1",
                "Mức độ nguy hiểm 2",
                "Mức độ nguy hiểm 3",
                "Mức độ nguy hiểm 4"};
        ArrayAdapter<String> adapterspin = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, level);
        adapterspin.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);
        //Thiết lập adapter cho Spinner
        msp_level_inci.setAdapter(adapterspin);

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

        Call<List<Incident>> getallinci = jsonPlaceHolderApi.getallinci();
        getallinci.enqueue(new Callback<List<Incident>>() {
            @Override
            public void onResponse(Call<List<Incident>> call, Response<List<Incident>> response) {
                listincident = response.body();
                int sum = 0, repairing = 0, norepair = 0, wait = 0, repaired = 0;
                sum = listincident.size();
                for (Incident incident : listincident) {
                    if (incident.getStatus().equals("repairing")) {
                        listrepairing.add(incident);
                        repairing++;
                    }
                    if (incident.getStatus().equals("norepair")) {
                        listnorepair.add(incident);
                        norepair++;
                    }
                    if (incident.getStatus().equals("repaired")) {
                        listrepaired.add(incident);
                        repaired++;
                    }
                    if (incident.getStatus().equals("wait")) {
                        listrepairing.add(incident);
                        wait++;
                    }
                }
                ListInciAdapter adapter = new ListInciAdapter(getContext(), R.layout.inci_item_list, (ArrayList<Incident>) listincident);
                mlv_inci.setAdapter(adapter);
                mtv_sum_inci.setText(Integer.toString(sum));
                mtv_repair_inci.setText(Integer.toString(repairing));
                mtv_waitconfirm_inci.setText(Integer.toString(wait));
                mtv_repaired_inci.setText(Integer.toString(repaired));
                mtv_norepair_inci.setText(Integer.toString(norepair));
                mlv_inci.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(getContext(), DetailInciActivity.class);
                        intent.putExtra("idinci", listincident.get(i).getId());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Incident>> call, Throwable t) {

            }
        });

        mtv_sum_inci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListInciAdapter adapter = new ListInciAdapter(getContext(), R.layout.inci_item_list, (ArrayList<Incident>) listincident);
                mlv_inci.setAdapter(adapter);
                mlv_inci.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(getContext(), DetailInciActivity.class);
                        intent.putExtra("idinci", listincident.get(i).getId());
                        startActivity(intent);
                    }
                });
            }
        });

        mtv_repair_inci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListInciAdapter adapter = new ListInciAdapter(getContext(), R.layout.inci_item_list, (ArrayList<Incident>) listrepairing);
                mlv_inci.setAdapter(adapter);
                mlv_inci.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(getContext(), DetailInciActivity.class);
                        intent.putExtra("idinci", listrepairing.get(i).getId());
                        startActivity(intent);
                    }
                });
            }
        });

        mtv_repaired_inci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListInciAdapter adapter = new ListInciAdapter(getContext(), R.layout.inci_item_list, (ArrayList<Incident>) listrepaired);
                mlv_inci.setAdapter(adapter);
                mlv_inci.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(getContext(), DetailInciActivity.class);
                        intent.putExtra("idinci", listrepaired.get(i).getId());
                        startActivity(intent);
                    }
                });
            }
        });

        mtv_waitconfirm_inci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListInciAdapter adapter = new ListInciAdapter(getContext(), R.layout.inci_item_list, (ArrayList<Incident>) listwait);
                mlv_inci.setAdapter(adapter);
                mlv_inci.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(getContext(), DetailInciActivity.class);
                        intent.putExtra("idinci", listwait.get(i).getId());
                        startActivity(intent);
                    }
                });
            }
        });

        mtv_norepair_inci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListInciAdapter adapter = new ListInciAdapter(getContext(), R.layout.inci_item_list, (ArrayList<Incident>) listnorepair);
                mlv_inci.setAdapter(adapter);
                mlv_inci.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(getContext(), DetailInciActivity.class);
                        intent.putExtra("idinci", listnorepair.get(i).getId());
                        startActivity(intent);
                    }
                });
            }
        });

        msp_level_inci.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0 && listincident != null) {
                    ListInciAdapter adapter = new ListInciAdapter(getContext(), R.layout.inci_item_list, (ArrayList<Incident>) listincident);
                    mlv_inci.setAdapter(adapter);
                }
                if (i == 1) {
                    List<Incident> result = new ArrayList<>();
                    for (Incident incident : listincident) {
                        if (incident.getLevel().equals("1")) {
                            result.add(incident);
                        }
                    }
                    ListInciAdapter adapter = new ListInciAdapter(getContext(), R.layout.inci_item_list, (ArrayList<Incident>) result);
                    mlv_inci.setAdapter(adapter);
                }
                if (i == 2) {
                    List<Incident> result = new ArrayList<>();
                    for (Incident incident : listincident) {
                        if (incident.getLevel().equals("2")) {
                            result.add(incident);
                        }
                    }
                    ListInciAdapter adapter = new ListInciAdapter(getContext(), R.layout.inci_item_list, (ArrayList<Incident>) result);
                    mlv_inci.setAdapter(adapter);
                }
                if (i == 3) {
                    List<Incident> result = new ArrayList<>();
                    for (Incident incident : listincident) {
                        if (incident.getLevel().equals("3")) {
                            result.add(incident);
                        }
                    }
                    ListInciAdapter adapter = new ListInciAdapter(getContext(), R.layout.inci_item_list, (ArrayList<Incident>) result);
                    mlv_inci.setAdapter(adapter);
                }
                if (i == 4) {
                    List<Incident> result = new ArrayList<>();
                    for (Incident incident : listincident) {
                        if (incident.getLevel().equals("4")) {
                            result.add(incident);
                        }
                    }
                    ListInciAdapter adapter = new ListInciAdapter(getContext(), R.layout.inci_item_list, (ArrayList<Incident>) result);
                    mlv_inci.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return root;
    }
}