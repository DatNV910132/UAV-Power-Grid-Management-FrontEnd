package com.dji.sdk.sample.ui.fragment.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dji.sdk.sample.JsonPlaceHolderApi;
import com.dji.sdk.sample.R;
import com.dji.sdk.sample.SHA256;
import com.dji.sdk.sample.model.Link;
import com.dji.sdk.sample.ui.activity.user.UserEditActivity;
import com.dji.sdk.sample.adapter.UserManagerListAdapter;
import com.dji.sdk.sample.model.Employee;

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
 * The type User mana fragment.
 */
public class UserManaFragment extends Fragment {

    private final String SHARED_PREFERENCES_NAME = "login";
    private final String ROLE_USER = "role_user";

    private TextView mtv_sum_emp;
    private TextView mtv_pilot_emp;
    private TextView mtv_fix_emp;
    private TextView mtv_mana_empl;
    private ListView mlist_emp;
    private Button mbtn_add_user;
    private int sum = 0, sum_pilot = 0, sum_mana = 0, sum_fix = 0;

    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private ArrayList<Employee> list_employee = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_user_mana, container, false);
        // Inflate the layout for this fragment
        mtv_sum_emp = root.findViewById(R.id.tv_sum_emp);
        mtv_mana_empl = root.findViewById(R.id.tv_sys_emp);
        mtv_fix_emp = root.findViewById(R.id.tv_emp_fix);
        mtv_pilot_emp = root.findViewById(R.id.tv_pilot_emp);
        mbtn_add_user = root.findViewById(R.id.btn_user_manager_add);
        mlist_emp = root.findViewById(R.id.lv_emp);

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


        mbtn_add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft_rep = fm.beginTransaction();
                ft_rep.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                ft_rep.replace(R.id.nav_host_fragment, new AddUserFragment());
                ft_rep.commit();
            }
        });

        Call<Link> linkgetuserbyid = jsonPlaceHolderApi.getlinkbyUrl("/employees/getall/");
        linkgetuserbyid.enqueue(new Callback<Link>() {
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
                    //Lấy thông tin của tất cả người dùng
                    Call<List<Employee>> getalluser = jsonPlaceHolderApi.getAllEmp(code);
                    getalluser.enqueue(new Callback<List<Employee>>() {
                        @Override
                        public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                            List<Employee> list = response.body();
                            for (Employee emp : list) {
                                if (emp.getIdrole().equals("5e86f1e756de7131e29ed620")) {
                                    sum_pilot++;
                                    list_employee.add(emp);
                                }
                                if (emp.getIdrole().equals("5e86f1ac56de7131e29ed61e")) {
                                    sum++;
                                }
                                if (emp.getIdrole().equals("5e86f1d656de7131e29ed61f")) {
                                    sum_mana++;
                                    list_employee.add(emp);
                                }
                                if (emp.getIdrole().equals("5e86f20e56de7131e29ed621")) {
                                    sum_fix++;
                                    list_employee.add(emp);
                                }
                            }
                            mtv_sum_emp.setText(Integer.toString((list.size() - sum)));
                            mtv_pilot_emp.setText(Integer.toString(sum_pilot));
                            mtv_mana_empl.setText(Integer.toString(sum_mana));
                            mtv_fix_emp.setText(Integer.toString(sum_fix));
                            UserManagerListAdapter adapter = new UserManagerListAdapter(getActivity(), R.layout.list_item_employee, list_employee);
                            mlist_emp.setAdapter(adapter);
                        }

                        @Override
                        public void onFailure(Call<List<Employee>> call, Throwable t) {
                            Log.e("User Manager", "Lỗi: " + t.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Link> call, Throwable t) {

            }
        });

        mlist_emp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Employee e = list_employee.get(i);
                Intent intent = new Intent(getContext(), UserEditActivity.class);
                intent.putExtra("id", e.getId());
                startActivity(intent);

            }
        });

        mtv_mana_empl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Employee> list_mana_emp = new ArrayList<>();
                for (Employee emp : list_employee) {
                    if (emp.getIdrole().equals("5e86f1d656de7131e29ed61f")) {
                        list_mana_emp.add(emp);
                    }
                }
                UserManagerListAdapter adapter = new UserManagerListAdapter(getActivity(), R.layout.list_item_employee, list_mana_emp);
                mlist_emp.setAdapter(adapter);
            }
        });
        mtv_fix_emp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Employee> list_fix_emp = new ArrayList<>();
                for (Employee emp : list_employee) {
                    if (emp.getIdrole().equals("5e86f20e56de7131e29ed621")) {
                        list_fix_emp.add(emp);
                    }
                }
                UserManagerListAdapter adapter = new UserManagerListAdapter(getActivity(), R.layout.list_item_employee, list_fix_emp);
                mlist_emp.setAdapter(adapter);
            }
        });
        mtv_pilot_emp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Employee> list_pilot_emp = new ArrayList<>();
                for (Employee emp : list_employee) {
                    if (emp.getIdrole().equals("5e86f1e756de7131e29ed620")) {
                        list_pilot_emp.add(emp);
                    }
                }
                UserManagerListAdapter adapter = new UserManagerListAdapter(getActivity(), R.layout.list_item_employee, list_pilot_emp);
                mlist_emp.setAdapter(adapter);
            }
        });

        mtv_sum_emp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserManagerListAdapter adapter = new UserManagerListAdapter(getActivity(), R.layout.list_item_employee, list_employee);
                mlist_emp.setAdapter(adapter);
            }
        });
        return root;
    }


}
