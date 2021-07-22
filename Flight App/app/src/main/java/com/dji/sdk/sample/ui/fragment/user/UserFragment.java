package com.dji.sdk.sample.ui.fragment.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.dji.sdk.sample.JsonPlaceHolderApi;
import com.dji.sdk.sample.R;
import com.dji.sdk.sample.SHA256;
import com.dji.sdk.sample.adapter.UserManagerListAdapter;
import com.dji.sdk.sample.internal.utils.ToastUtils;
import com.dji.sdk.sample.model.Employee;
import com.dji.sdk.sample.model.Link;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The type User fragment.
 */
public class UserFragment extends Fragment {

    private final String SHARED_PREFERENCES_NAME = "login";
    private final String ID_USER = "id_user";
    private final String ROLE_USER = "role_user";

    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private EditText medt_user_name;
    private EditText medt_user_sex;
    private EditText medt_user_birth;
    private EditText medt_user_nationid;
    private EditText medt_user_address;
    private EditText medt_user_mail;
    private EditText medt_user_phone;
    private Button mbtn_user_edit;
    private Button mbtn_user_save;
    private Button mbtn_user_cancel;
    private TextView mtv_user_status;
    private Employee employee;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_user_login, container, false);

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
        medt_user_name = root.findViewById(R.id.edt_user_name);
        medt_user_sex = root.findViewById(R.id.edt_user_sex);
        medt_user_birth = root.findViewById(R.id.edt_user_birth);
        medt_user_nationid = root.findViewById(R.id.edt_user_nationid);
        medt_user_address = root.findViewById(R.id.edt_user_address);
        medt_user_phone = root.findViewById(R.id.edt_user_phone);
        medt_user_mail = root.findViewById(R.id.edt_user_mail);
        mbtn_user_edit = root.findViewById(R.id.btn_user_edit);
        mbtn_user_save = root.findViewById(R.id.btn_user_save);
        mbtn_user_cancel = root.findViewById(R.id.btn_user_cancel);
        mtv_user_status = root.findViewById(R.id.tv_user_status);
        mbtn_user_save.setVisibility(View.INVISIBLE);
        mbtn_user_cancel.setVisibility(View.INVISIBLE);

        Call<Link> linkgetuserbyid = jsonPlaceHolderApi.getlinkbyUrl("/employees/{id}");
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
                    if(code != null) {
                        // Lấy thông tin người dùng
                        Call<Employee> getuser = jsonPlaceHolderApi.getEmpbyID(id_user, code);
                        getuser.enqueue(new Callback<Employee>() {
                            @Override
                            public void onResponse(Call<Employee> call, Response<Employee> response) {
                                employee = response.body();
                                medt_user_name.setText(employee.getName());
                                medt_user_address.setText(employee.getAddress());
                                medt_user_sex.setText(employee.getSex());
                                medt_user_birth.setText(employee.getBirth());
                                medt_user_nationid.setText(employee.getNationality());
                                medt_user_phone.setText(employee.getPhone());
                                medt_user_mail.setText(employee.getMail());
                            }

                            @Override
                            public void onFailure(Call<Employee> call, Throwable t) {
                                Log.e("User Manager", "Lỗi: " + t.getMessage());
                            }
                        });
                    }
            }

            @Override
            public void onFailure(Call<Link> call, Throwable t) {

            }
        });


        mbtn_user_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mbtn_user_edit.setVisibility(View.INVISIBLE);
                mbtn_user_save.setVisibility(View.VISIBLE);
                mbtn_user_cancel.setVisibility(View.VISIBLE);
                medt_user_name.setEnabled(true);
                medt_user_sex.setEnabled(true);
                medt_user_birth.setEnabled(true);
                medt_user_nationid.setEnabled(true);
                medt_user_address.setEnabled(true);
                medt_user_phone.setEnabled(true);
                medt_user_mail.setEnabled(true);
            }
        });

        mbtn_user_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                medt_user_name.setText(employee.getName());
                medt_user_address.setText(employee.getAddress());
                medt_user_sex.setText(employee.getSex());
                medt_user_birth.setText(employee.getBirth());
                medt_user_nationid.setText(employee.getNationality());
                medt_user_phone.setText(employee.getPhone());
                medt_user_mail.setText(employee.getMail());
                medt_user_name.setEnabled(false);
                medt_user_sex.setEnabled(false);
                medt_user_birth.setEnabled(false);
                medt_user_address.setEnabled(false);
                medt_user_nationid.setEnabled(false);
                medt_user_phone.setEnabled(false);
                medt_user_mail.setEnabled(false);
                mbtn_user_cancel.setVisibility(View.INVISIBLE);
                mbtn_user_save.setVisibility(View.INVISIBLE);
                mbtn_user_edit.setVisibility(View.VISIBLE);
                mbtn_user_edit.setVisibility(View.VISIBLE);
            }
        });

        mbtn_user_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (medt_user_name.getText().toString().equals("") ||
                        medt_user_sex.getText().toString().equals("") ||
                        medt_user_birth.getText().toString().equals("") ||
                        medt_user_address.getText().toString().equals("") ||
                        medt_user_nationid.getText().toString().equals("") ||
                        medt_user_phone.getText().toString().equals("") ||
                        medt_user_mail.getText().toString().equals("")) {
                    mtv_user_status.setText("Bạn cần nhập đầy đủ thông tin");
                } else {
                    new AlertDialog.Builder(getActivity())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Lưu thông tin")
                            .setMessage("Bạn có muốn lưu thông tin không ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    employee.setName(medt_user_name.getText().toString());
                                    employee.setSex(medt_user_sex.getText().toString());
                                    employee.setBirth(medt_user_birth.getText().toString());
                                    employee.setAddress(medt_user_address.getText().toString());
                                    employee.setNationality(medt_user_nationid.getText().toString());
                                    employee.setMail(medt_user_mail.getText().toString());
                                    employee.setPhone(medt_user_phone.getText().toString());

                                    Call<Link> linkgetuserbyid = jsonPlaceHolderApi.getlinkbyUrl("employees/update/{id}");
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
                                                if(code != null) {
                                                    //update thông tin nhân viên
                                                    Call<String> updateuser = jsonPlaceHolderApi.updateUser(id_user, employee,code);
                                                    updateuser.enqueue(new Callback<String>() {
                                                        @Override
                                                        public void onResponse(Call<String> call, Response<String> response) {
                                                            Log.d("Update User", response.body());
                                                        }

                                                        @Override
                                                        public void onFailure(Call<String> call, Throwable t) {
                                                            Log.e("Update User", "Lỗi: " + t.getMessage());
                                                        }
                                                    });
                                                }
                                        }

                                        @Override
                                        public void onFailure(Call<Link> call, Throwable t) {

                                        }
                                    });

                                    mtv_user_status.setText("Sửa thông tin người dùng thành công");
                                    mbtn_user_cancel.setVisibility(View.INVISIBLE);
                                    mbtn_user_save.setVisibility(View.INVISIBLE);
                                    mbtn_user_edit.setVisibility(View.VISIBLE);
                                    medt_user_name.setEnabled(false);
                                    medt_user_sex.setEnabled(false);
                                    medt_user_birth.setEnabled(false);
                                    medt_user_address.setEnabled(false);
                                    medt_user_nationid.setEnabled(false);
                                    medt_user_phone.setEnabled(false);
                                    medt_user_mail.setEnabled(false);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    medt_user_name.setText(employee.getName());
                                    medt_user_address.setText(employee.getAddress());
                                    medt_user_sex.setText(employee.getSex());
                                    medt_user_birth.setText(employee.getBirth());
                                    medt_user_nationid.setText(employee.getNationality());
                                    medt_user_phone.setText(employee.getPhone());
                                    medt_user_mail.setText(employee.getMail());
                                    mbtn_user_cancel.setVisibility(View.INVISIBLE);
                                    mbtn_user_save.setVisibility(View.INVISIBLE);
                                    mbtn_user_edit.setVisibility(View.VISIBLE);
                                    mbtn_user_edit.setVisibility(View.VISIBLE);
                                }
                            })
                            .show();
                }

            }
        });

        return root;
    }
}
