package com.dji.sdk.sample.ui.fragment.mission;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dji.sdk.sample.JsonPlaceHolderApi;
import com.dji.sdk.sample.R;
import com.dji.sdk.sample.SHA256;
import com.dji.sdk.sample.adapter.PAPMissionAdapter;
import com.dji.sdk.sample.internal.utils.ToastUtils;
import com.dji.sdk.sample.model.Link;
import com.dji.sdk.sample.model.PoleAndPointMission;

import java.security.NoSuchAlgorithmException;
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
 * The type Child mission fragment.
 */
public class ChildMissionFragment extends Fragment {


    private final String SHARED_PREFERENCES_NAME = "login";
    private final String ROLE_USER = "role_user";

    private TextView mtv_sum_papmission;
    private TextView mtv_use_papmission;
    private Button mbtn_add_papmission;
    private ListView mlv_papmission;

    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private ArrayList<PoleAndPointMission> list_papmission = new ArrayList<>();

    /**
     * Instantiates a new Child mission fragment.
     */
    public ChildMissionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_child_mission, container, false);

        //Link với giao diện missionFragment
        mtv_sum_papmission = root.findViewById(R.id.tv_sum_childmission);
        mtv_use_papmission = root.findViewById(R.id.tv_use_childmission);
        mbtn_add_papmission = root.findViewById(R.id.btn_add_childmission);
        mlv_papmission = root.findViewById(R.id.lv_childmission);

        //Lấy ra thông tin của role của user đăng nhập vào thiết bị
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_NAME, getContext().MODE_PRIVATE);
        String id_role = sharedPreferences.getString(ROLE_USER, "");

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

        mbtn_add_papmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft_rep = fm.beginTransaction();
                ft_rep.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                ft_rep.replace(R.id.nav_host_fragment, new AddPoleAPointMissionFragment());
                ft_rep.commit();
            }
        });

        //lấy danh sách hành trình bay
        Call<Link> linkgetmissionall = jsonPlaceHolderApi.getlinkbyUrl("/poleandpointmission/getall/");
        linkgetmissionall.enqueue(new Callback<Link>() {
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
                if (code != null) {
                    //Lấy thông tin của tất cả hành trình
                    Call<List<PoleAndPointMission>> getallpapmission = jsonPlaceHolderApi.getallpoleandpointmission(code);
                    getallpapmission.enqueue(new Callback<List<PoleAndPointMission>>() {
                        @Override
                        public void onResponse(Call<List<PoleAndPointMission>> call, Response<List<PoleAndPointMission>> response) {
                            List<PoleAndPointMission> list = response.body();
                            System.out.println(list.size());
                            System.out.println(list.toString());
                            mtv_sum_papmission.setText(String.valueOf(list.size()));
                            PAPMissionAdapter adapter = new PAPMissionAdapter(getActivity(), R.layout.item_poleandpointmission, (ArrayList<PoleAndPointMission>) list);
                            mlv_papmission.setAdapter(adapter);
                        }

                        @Override
                        public void onFailure(Call<List<PoleAndPointMission>> call, Throwable t) {
                            Log.e("getpoleapointmissions", "Lỗi: " + t.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Link> call, Throwable t) {

            }
        });


        return root;
    }
}
