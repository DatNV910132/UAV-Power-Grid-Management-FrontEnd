package com.dji.sdk.sample.ui.fragment.user;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dji.sdk.sample.JsonPlaceHolderApi;
import com.dji.sdk.sample.R;
import com.dji.sdk.sample.SHA256;
import com.dji.sdk.sample.model.Employee;
import com.dji.sdk.sample.model.Link;

import java.security.NoSuchAlgorithmException;
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
public class AddUserFragment extends Fragment {

    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private final String SHARED_PREFERENCES_NAME = "login";
    private final String ID_USER = "id_user";
    private final String ROLE_USER = "role_user";

    private EditText medt_adduser_name;
    private EditText medt_adduser_sex;
    private EditText medt_adduser_birth;
    private EditText medt_adduser_nationid;
    private EditText medt_adduser_address;
    private EditText medt_adduser_mail;
    private EditText medt_adduser_phone;
    private EditText medt_adduser_username;
    private EditText medt_adduser_password;
    private Spinner msp_adduser_role;

    private Button mbtn_adduser_save;
    private Button mbtn_adduser_cancel;
    private TextView mtv_adduser_status;

    /**
     * Instantiates a new Add user fragment.
     */
    public AddUserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_user, container, false);

        //Lấy thông tin của nhân viên đã đăng nhập vào hệ thống
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_NAME, getContext().MODE_PRIVATE);
        String id_user = sharedPreferences.getString(ID_USER, "");
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
        medt_adduser_name = root.findViewById(R.id.edt_adduser_name);
        medt_adduser_sex = root.findViewById(R.id.edt_adduser_sex);
        medt_adduser_birth = root.findViewById(R.id.edt_adduser_birth);
        medt_adduser_nationid = root.findViewById(R.id.edt_adduser_nationid);
        medt_adduser_address = root.findViewById(R.id.edt_adduser_address);
        medt_adduser_phone = root.findViewById(R.id.edt_adduser_phone);
        medt_adduser_mail = root.findViewById(R.id.edt_adduser_mail);
        medt_adduser_username = root.findViewById(R.id.edt_adduser_username);
        medt_adduser_password = root.findViewById(R.id.edt_adduser_pass);
        msp_adduser_role = root.findViewById(R.id.sp_adduser_role);
        mbtn_adduser_save = root.findViewById(R.id.btn_adduser_save);
        mbtn_adduser_cancel = root.findViewById(R.id.btn_adduser_cancel);
        mtv_adduser_status = root.findViewById(R.id.tv_adduser_status);

        String role[] = {
                "Admin",
                "Phi Công",
                "Quản lý chung",
                "Nhân viên sửa chữa"};
        ArrayAdapter<String> adapterlevel = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, role);
        adapterlevel.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);
        //Thiết lập adapter cho Spinner
        msp_adduser_role.setAdapter(adapterlevel);

        mbtn_adduser_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (medt_adduser_name.getText().toString().equals("") ||
                        medt_adduser_sex.getText().toString().equals("") ||
                        medt_adduser_birth.getText().toString().equals("") ||
                        medt_adduser_address.getText().toString().equals("") ||
                        medt_adduser_nationid.getText().toString().equals("") ||
                        medt_adduser_phone.getText().toString().equals("") ||
                        medt_adduser_mail.getText().toString().equals("")) {
                    mtv_adduser_status.setText("Bạn cần nhập đầy đủ thông tin");
                } else {
                    Employee employee = new Employee();

                    employee.setUsername(medt_adduser_username.getText().toString());
                    employee.setPassword(medt_adduser_password.getText().toString());

                    if(msp_adduser_role.getSelectedItemPosition() == 0){
                        employee.setIdrole("5e86f1ac56de7131e29ed61e");
                    }

                    if(msp_adduser_role.getSelectedItemPosition() == 1){
                        employee.setIdrole("5e86f1e756de7131e29ed620");
                    }

                    if(msp_adduser_role.getSelectedItemPosition() == 2){
                        employee.setIdrole("5e86f1d656de7131e29ed61f");
                    }

                    if(msp_adduser_role.getSelectedItemPosition() == 3){
                        employee.setIdrole("5e86f20e56de7131e29ed621");
                    }

                    employee.setName(medt_adduser_name.getText().toString());
                    employee.setSex(medt_adduser_sex.getText().toString());
                    employee.setBirth(medt_adduser_birth.getText().toString());
                    employee.setAddress(medt_adduser_address.getText().toString());
                    employee.setNationality(medt_adduser_nationid.getText().toString());
                    employee.setMail(medt_adduser_mail.getText().toString());
                    employee.setPhone(medt_adduser_phone.getText().toString());

                    Call<Link> link1 = jsonPlaceHolderApi.getlinkbyUrl("employees/delete/{id}");
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
                                    Call<String> adduser = jsonPlaceHolderApi.addUser(employee,code);
                                    adduser.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            String result = response.body();
                                            System.out.println(result);
                                            if (result.equals("false")) {
                                                mtv_adduser_status.setText("Thông tin người dùng đã tồn tại, vui lòng kiểm tra lại");
                                            } else {
                                                Toast.makeText(getContext(),"Thêm nhân viên thành công",Toast.LENGTH_SHORT).show();
                                                FragmentManager fm = getFragmentManager();
                                                FragmentTransaction ft_rep = fm.beginTransaction();
                                                ft_rep.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                                                ft_rep.replace(R.id.nav_host_fragment, new UserManaFragment());
                                                ft_rep.commit();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            Log.e("Add User", "Lỗi: " + t.getMessage());
                                        }
                                    });
                                }
                        }

                        @Override
                        public void onFailure(Call<Link> call, Throwable t) {

                        }
                    });
                }
//                finish();
            }
        });
        mbtn_adduser_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft_rep = fm.beginTransaction();
                ft_rep.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                ft_rep.replace(R.id.nav_host_fragment, new UserManaFragment());
                ft_rep.commit();
            }
        });



        return root;
    }
}
