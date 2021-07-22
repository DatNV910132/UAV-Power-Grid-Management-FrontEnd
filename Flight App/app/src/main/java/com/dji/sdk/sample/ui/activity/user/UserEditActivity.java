package com.dji.sdk.sample.ui.activity.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dji.sdk.sample.JsonPlaceHolderApi;
import com.dji.sdk.sample.R;
import com.dji.sdk.sample.SHA256;
import com.dji.sdk.sample.model.Employee;
import com.dji.sdk.sample.model.Link;
import com.dji.sdk.sample.ui.activity.StartAppActivity;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The type User edit activity.
 */
public class UserEditActivity extends AppCompatActivity {

    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private final String SHARED_PREFERENCES_NAME = "login";
    private final String ROLE_USER = "role_user";

    private EditText medt_useredit_name;
    private EditText medt_useredit_sex;
    private EditText medt_useredit_birth;
    private EditText medt_useredit_nationid;
    private EditText medt_useredit_address;
    private EditText medt_useredit_mail;
    private EditText medt_useredit_phone;
    private Button mbtn_useredit_edit;
    private Button mbtn_useredit_done;
    private Button mbtn_useredit_save;
    private Button mbtn_useredit_cancel;
    private Button mbtn_useredit_delete;
    private TextView mtv_useredit_status;
    private Employee employee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);

        //Lấy ra thông tin của role của user đăng nhập vào thiết bị
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        String id_role = sharedPreferences.getString(ROLE_USER, "");

        //Get Intent để lấy giá trị từ intent truyền vào của activity gọi đến để
        // lấy ra id của nhân viên cần chỉnh sửa
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

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

        //Link các trường khai báo với layout giao diện đã thiết kế
        medt_useredit_name = findViewById(R.id.edt_useredit_name);
        medt_useredit_sex = findViewById(R.id.edt_useredit_sex);
        medt_useredit_birth = findViewById(R.id.edt_useredit_birth);
        medt_useredit_nationid = findViewById(R.id.edt_useredit_nationid);
        medt_useredit_address = findViewById(R.id.edt_useredit_address);
        medt_useredit_phone = findViewById(R.id.edt_useredit_phone);
        medt_useredit_mail = findViewById(R.id.edt_useredit_mail);
        mbtn_useredit_edit = findViewById(R.id.btn_useredit_edit);
        mbtn_useredit_save = findViewById(R.id.btn_useredit_save);
        mbtn_useredit_cancel = findViewById(R.id.btn_useredit_cancel);
        mbtn_useredit_done = findViewById(R.id.btn_useredit_done);
        mtv_useredit_status = findViewById(R.id.tv_useredit_status);
        mbtn_useredit_save.setVisibility(View.INVISIBLE);
        mbtn_useredit_cancel.setVisibility(View.INVISIBLE);
        mbtn_useredit_delete = findViewById(R.id.btn_useredit_delete);

        //khai báo một thông báo xác nhận để xác nhận lưu thông tin từ người dùng
        AlertDialog.Builder show = new AlertDialog.Builder(this);

        //Sự kiện chọn button "Xóa" người dùng
        mbtn_useredit_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Link> link = jsonPlaceHolderApi.getlinkbyUrl("employees/delete/{id}");
                link.enqueue(new Callback<Link>() {
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
                               Call<String> deleteAPI = jsonPlaceHolderApi.deleteUser(id, code);
                               deleteAPI.enqueue(new Callback<String>() {
                                   @Override
                                   public void onResponse(Call<String> call, Response<String> response) {
//                        Log.d("Delete User", response.body());
                                   }

                                   @Override
                                   public void onFailure(Call<String> call, Throwable t) {
//                        Log.e("Delete User", t.getMessage());
                                   }
                               });
                           }
                    }

                    @Override
                    public void onFailure(Call<Link> call, Throwable t) {

                    }
                });
                Toast.makeText(getApplicationContext(),"Xóa Nhân viên Thành công",Toast.LENGTH_SHORT).show();

                //Chuyển sang activity khác
                Intent i = new Intent(getApplicationContext(), StartAppActivity.class);
                startActivity(i);
            }
        });

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
                        Call<Employee> getuserbyid = jsonPlaceHolderApi.getEmpbyID(id, code);
                        getuserbyid.enqueue(new Callback<Employee>() {
                            @Override
                            public void onResponse(Call<Employee> call, Response<Employee> response) {
                                employee = response.body();
                                //Hiển thị thông tin người dùng ra giao diện
                                medt_useredit_name.setText(employee.getName());
                                medt_useredit_address.setText(employee.getAddress());
                                medt_useredit_sex.setText(employee.getSex());
                                medt_useredit_birth.setText(employee.getBirth());
                                medt_useredit_nationid.setText(employee.getNationality());
                                medt_useredit_phone.setText(employee.getPhone());
                                medt_useredit_mail.setText(employee.getMail());
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


        mbtn_useredit_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Sự kiện chọn button sửa thông tin người dùng
        mbtn_useredit_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Tùy chọn các trường hiển thị của giao diện
                mbtn_useredit_edit.setVisibility(View.INVISIBLE);
                mbtn_useredit_done.setVisibility(View.INVISIBLE);
                mbtn_useredit_save.setVisibility(View.VISIBLE);
                mbtn_useredit_cancel.setVisibility(View.VISIBLE);
                medt_useredit_name.setEnabled(true);
                medt_useredit_sex.setEnabled(true);
                medt_useredit_birth.setEnabled(true);
                medt_useredit_nationid.setEnabled(true);
                medt_useredit_address.setEnabled(true);
                medt_useredit_phone.setEnabled(true);
                medt_useredit_mail.setEnabled(true);
            }
        });

        //Sự kiện chọn button "Cancel"
        mbtn_useredit_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Set dữ liệu hiển thị lại và các layout hiển thị trên giao diện
                medt_useredit_name.setText(employee.getName());
                medt_useredit_address.setText(employee.getAddress());
                medt_useredit_sex.setText(employee.getSex());
                medt_useredit_birth.setText(employee.getBirth());
                medt_useredit_nationid.setText(employee.getNationality());
                medt_useredit_phone.setText(employee.getPhone());
                medt_useredit_mail.setText(employee.getMail());
                medt_useredit_name.setEnabled(false);
                medt_useredit_sex.setEnabled(false);
                medt_useredit_birth.setEnabled(false);
                medt_useredit_address.setEnabled(false);
                medt_useredit_nationid.setEnabled(false);
                medt_useredit_phone.setEnabled(false);
                medt_useredit_mail.setEnabled(false);
                mbtn_useredit_cancel.setVisibility(View.INVISIBLE);
                mbtn_useredit_save.setVisibility(View.INVISIBLE);
                mbtn_useredit_edit.setVisibility(View.VISIBLE);
                mbtn_useredit_done.setVisibility(View.VISIBLE);
            }
        });

        //Sự kiện chọn button "Lưu" thông tin người dùng (nhân viên)
        mbtn_useredit_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (medt_useredit_name.getText().toString().equals("") ||
                        medt_useredit_sex.getText().toString().equals("") ||
                        medt_useredit_birth.getText().toString().equals("") ||
                        medt_useredit_address.getText().toString().equals("") ||
                        medt_useredit_nationid.getText().toString().equals("") ||
                        medt_useredit_phone.getText().toString().equals("") ||
                        medt_useredit_mail.getText().toString().equals("")) {
                    mtv_useredit_status.setText("Bạn cần nhập đầy đủ thông tin");
                } else {
                            show.setIcon(android.R.drawable.ic_dialog_alert);
                    show.setTitle("Lưu thông tin");
                    show.setMessage("Bạn có muốn lưu thông tin không ?");

                    //nếu chọn "yes" trong thông báo
                    show.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    employee.setName(medt_useredit_name.getText().toString());
                                    employee.setSex(medt_useredit_sex.getText().toString());
                                    employee.setBirth(medt_useredit_birth.getText().toString());
                                    employee.setAddress(medt_useredit_address.getText().toString());
                                    employee.setNationality(medt_useredit_nationid.getText().toString());
                                    employee.setMail(medt_useredit_mail.getText().toString());
                                    employee.setPhone(medt_useredit_phone.getText().toString());

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
                                                    //Call API update nhân viên
                                                    Call<String> updateuser = jsonPlaceHolderApi.updateUser(id, employee,code);
                                                    updateuser.enqueue(new Callback<String>() {
                                                        @Override
                                                        public void onResponse(Call<String> call, Response<String> response) {
//                                            Log.d("Update User", response.body());
                                                        }

                                                        @Override
                                                        public void onFailure(Call<String> call, Throwable t) {
//                                            Log.e("Update User", "Lỗi: " + t.getMessage());
                                                        }
                                                    });
                                                }
                                        }

                                        @Override
                                        public void onFailure(Call<Link> call, Throwable t) {

                                        }
                                    });

                                    mtv_useredit_status.setText("Sửa thông tin người dùng thành công");

                                    //Tùy chọn hiển thị các itemview trên giao diện
                                    mbtn_useredit_cancel.setVisibility(View.INVISIBLE);
                                    mbtn_useredit_save.setVisibility(View.INVISIBLE);
                                    mbtn_useredit_edit.setVisibility(View.VISIBLE);
                                    mbtn_useredit_done.setVisibility(View.VISIBLE);
                                    medt_useredit_name.setEnabled(false);
                                    medt_useredit_sex.setEnabled(false);
                                    medt_useredit_birth.setEnabled(false);
                                    medt_useredit_address.setEnabled(false);
                                    medt_useredit_nationid.setEnabled(false);
                                    medt_useredit_phone.setEnabled(false);
                                    medt_useredit_mail.setEnabled(false);
                                }
                            });

                    //Nếu chọn "No"
                    //Get lại giá trị của người dùng và tùy chọn hiển thị giao diện
                    show.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    medt_useredit_name.setText(employee.getName());
                                    medt_useredit_address.setText(employee.getAddress());
                                    medt_useredit_sex.setText(employee.getSex());
                                    medt_useredit_birth.setText(employee.getBirth());
                                    medt_useredit_nationid.setText(employee.getNationality());
                                    medt_useredit_phone.setText(employee.getPhone());
                                    medt_useredit_mail.setText(employee.getMail());
                                    mbtn_useredit_cancel.setVisibility(View.INVISIBLE);
                                    mbtn_useredit_save.setVisibility(View.INVISIBLE);
                                    mbtn_useredit_edit.setVisibility(View.VISIBLE);
                                    mbtn_useredit_done.setVisibility(View.VISIBLE);
                                    medt_useredit_name.setEnabled(false);
                                    medt_useredit_sex.setEnabled(false);
                                    medt_useredit_birth.setEnabled(false);
                                    medt_useredit_address.setEnabled(false);
                                    medt_useredit_nationid.setEnabled(false);
                                    medt_useredit_phone.setEnabled(false);
                                    medt_useredit_mail.setEnabled(false);
                                }
                            });
                    show.show();
                }

            }
        });
    }
}
