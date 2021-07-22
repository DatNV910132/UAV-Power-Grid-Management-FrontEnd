package com.dji.sdk.sample.ui.fragment.incident;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dji.sdk.sample.JsonPlaceHolderApi;
import com.dji.sdk.sample.R;
import com.dji.sdk.sample.SHA256;
import com.dji.sdk.sample.adapter.SpinFixerAdapter;
import com.dji.sdk.sample.model.Employee;
import com.dji.sdk.sample.model.Incident;
import com.dji.sdk.sample.model.Link;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.security.NoSuchAlgorithmException;
import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import dji.thirdparty.rx.internal.operators.BackpressureUtils;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmInciFragment extends Fragment {

    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private JsonPlaceHolderApi jsonPlaceHolderApi_Data;
    private List<Employee> listfixer = new ArrayList<>();
    private String idinci = "";
    private Incident incident;
    private final String SHARED_PREFERENCES_NAME = "login";
    private final String ID_USER = "id_user";
    private final String ROLE_USER = "role_user";

    private PhotoView mptv_confinci_image;
    private EditText medt_confinci_name;
    private EditText medt_confinci_des;
    private EditText medt_confinci_date;
    private Spinner msp_confinci_level;
    private Spinner msp_confinci_idfix;
    private Button mbtn_confinci_save;
    private Button mbtn_confinci_cancel;
    private Button mbtn_confinci_delete;

    /**
     * Instantiates a new Confirm inci fragment.
     */
    public ConfirmInciFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_confirm_inci, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_NAME, getContext().MODE_PRIVATE);
        String id_role = sharedPreferences.getString(ROLE_USER, "");

        Bundle bundle = getArguments();
        if (bundle != null) {
            idinci = bundle.getString("idinci");
        }

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
        jsonPlaceHolderApi_Data = retrofit.create(JsonPlaceHolderApi.class);

        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.URLIncidentService))
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        jsonPlaceHolderApi = retrofit1.create(JsonPlaceHolderApi.class);

        mptv_confinci_image = root.findViewById(R.id.photoview_confinci_image);
        medt_confinci_name = root.findViewById(R.id.edt_confinci_name);
        medt_confinci_des = root.findViewById(R.id.edt_confinci_des);
        medt_confinci_date = root.findViewById(R.id.edt_confinci_date);
        msp_confinci_level = root.findViewById(R.id.sp_confinci_level);
        msp_confinci_idfix = root.findViewById(R.id.sp_confinci_idfix);
        mbtn_confinci_save = root.findViewById(R.id.btn_confinci_save);
        mbtn_confinci_cancel = root.findViewById(R.id.btn_confinci_cancel);
        mbtn_confinci_delete = root.findViewById(R.id.btn_confinci_delete);

        String level[] = {
                "Mức độ nguy hiểm 1",
                "Mức độ nguy hiểm 2",
                "Mức độ nguy hiểm 3",
                "Mức độ nguy hiểm 4"};
        ArrayAdapter<String> adapterlevel = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, level);
        adapterlevel.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);
        //Thiết lập adapter cho Spinner
        msp_confinci_level.setAdapter(adapterlevel);

        Call<Link> link1 = jsonPlaceHolderApi_Data.getlinkbyUrl("employees/role/");
        link1.enqueue(new Callback<Link>() {
            @Override
            public void onResponse(Call<Link> call, Response<Link> response) {
                    String code = null;
                    Link l = response.body();
                    String idurl = l.get_id();
                    String primarycode = idurl + "." + id_role;
                    try {
                        code = SHA256.toHexString(SHA256.getSHA(primarycode));
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    if(code != null) {
                        Call<List<Employee>> getuserbyrole = jsonPlaceHolderApi_Data.getEmpbyRole("Fixer",code);
                        getuserbyrole.enqueue(new Callback<List<Employee>>() {
                            @Override
                            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                                listfixer = response.body();
                                SpinFixerAdapter adapter = new SpinFixerAdapter(getContext(), R.layout.sp_item_addinci_fixxer, listfixer);
                                adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                                msp_confinci_idfix.setAdapter(adapter);
                            }

                            @Override
                            public void onFailure(Call<List<Employee>> call, Throwable t) {

                            }
                        });
                    }
            }

            @Override
            public void onFailure(Call<Link> call, Throwable t) {

            }
        });

        Call<Incident> getinci = jsonPlaceHolderApi.getincibyid(idinci);
        getinci.enqueue(new Callback<Incident>() {
            @Override
            public void onResponse(Call<Incident> call, Response<Incident> response) {
                incident = response.body();
                String imageUrl = getResources().getString(R.string.URLDataDrone)+"photos/byte/" + incident.getImage();

                //Loading image using Picasso
                Picasso.get().load(imageUrl).into(mptv_confinci_image);
                medt_confinci_name.setText(incident.getName());
                medt_confinci_des.setText(incident.getDes());
                medt_confinci_date.setText(incident.getDate());
            }

            @Override
            public void onFailure(Call<Incident> call, Throwable t) {

            }
        });

        mbtn_confinci_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<String> deleteinci = jsonPlaceHolderApi.deleteinci(idinci);
                deleteinci.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft_rep = fm.beginTransaction();
                ft_rep.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                ft_rep.replace(R.id.nav_host_fragment, new ListConfirmInciFragment());
                ft_rep.commit();
            }
        });

        mbtn_confinci_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft_rep = fm.beginTransaction();
                ft_rep.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                ft_rep.replace(R.id.nav_host_fragment, new ListConfirmInciFragment());
                ft_rep.commit();
            }
        });

        mbtn_confinci_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (medt_confinci_name.getText().toString().equals("") ||
                        medt_confinci_des.getText().toString().equals("")
                ) {
                    Toast.makeText(getContext(), "Bạn cần nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    new AlertDialog.Builder(getActivity())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Lưu thông tin")
                            .setMessage("Bạn có muốn lưu thông tin không ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    incident.setName(medt_confinci_name.getText().toString());
                                    incident.setDes(medt_confinci_des.getText().toString());

                                    if (msp_confinci_level.getSelectedItemPosition() == 0) {
                                        incident.setLevel("1");
                                    }

                                    if (msp_confinci_level.getSelectedItemPosition() == 1) {
                                        incident.setLevel("2");
                                    }
                                    if (msp_confinci_level.getSelectedItemPosition() == 2) {
                                        incident.setLevel("3");
                                    }
                                    if (msp_confinci_level.getSelectedItemPosition() == 3) {
                                        incident.setLevel("4");
                                    }
                                    incident.setStatus("norepair");
                                    incident.setIdfix(listfixer.get(msp_confinci_idfix.getSelectedItemPosition()).getId());

                                    Call<String> update = jsonPlaceHolderApi.modiffyincident(incident.getId(), incident);
                                    update.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            Log.d("update", response.body());
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {

                                        }
                                    });
                                    FragmentManager fm = getFragmentManager();
                                    FragmentTransaction ft_rep = fm.beginTransaction();
                                    ft_rep.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                                    ft_rep.replace(R.id.nav_host_fragment, new ListConfirmInciFragment());
                                    ft_rep.commit();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    FragmentManager fm = getFragmentManager();
                                    FragmentTransaction ft_rep = fm.beginTransaction();
                                    ft_rep.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                                    ft_rep.replace(R.id.nav_host_fragment, new ListConfirmInciFragment());
                                    ft_rep.commit();
                                }
                            })
                            .show();
                }
            }
        });

        return root;
    }
}
