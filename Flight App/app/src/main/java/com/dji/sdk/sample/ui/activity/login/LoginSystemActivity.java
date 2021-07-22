package com.dji.sdk.sample.ui.activity.login;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dji.sdk.sample.AES;
import com.dji.sdk.sample.JsonPlaceHolderApi;
import com.dji.sdk.sample.R;
import com.dji.sdk.sample.model.Employee;
import com.dji.sdk.sample.ui.activity.HomeActivity;
import com.dji.sdk.sample.ui.activity.login.ForgetInfoActivity;

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
 * The type Login system activity.
 */
public class LoginSystemActivity extends AppCompatActivity {

    private final String SHARED_PREFERENCES_NAME = "login";
    private final String ID_USER = "id_user";
    private final String NAME_USER = "name_user";
    private final String MAIL_USER = "mail_user";
    private final String ROLE_USER = "role_user";
    private final String secretKey = "@datdeptrai@@@anhxinhdep@@@910132!!!";
    private static final String[] REQUIRED_PERMISSION_LIST_LOGN = new String[]{
            Manifest.permission.VIBRATE, // Gimbal rotation
            Manifest.permission.INTERNET, // API requests
            Manifest.permission.ACCESS_WIFI_STATE, // WIFI connected products
            Manifest.permission.ACCESS_COARSE_LOCATION, // Maps
            Manifest.permission.ACCESS_NETWORK_STATE, // WIFI connected products
            Manifest.permission.ACCESS_FINE_LOCATION, // Maps
            Manifest.permission.CHANGE_WIFI_STATE, // Changing between WIFI and USB connection
            Manifest.permission.WRITE_EXTERNAL_STORAGE, // Log files
            Manifest.permission.BLUETOOTH, // Bluetooth connected products
            Manifest.permission.BLUETOOTH_ADMIN, // Bluetooth connected products
            Manifest.permission.READ_EXTERNAL_STORAGE, // Log files
            Manifest.permission.READ_PHONE_STATE, // Device UUID accessed upon registration
            Manifest.permission.RECORD_AUDIO // Speaker accessory
    };
    private static final int REQUEST_PERMISSION_CODE_LOGIN = 12345;
    private List<String> missingPermission = new ArrayList<>();
    private EditText edt_username;
    private EditText edit_password;
    private Button btn_login_sys;
    private TextView mtv_false_status;
    private TextView mtv_forget_password;

    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_system);

        //Kiểm tra các quyền đã được cấp cho ứng dụng hay chưa
        checkAndRequestPermissions();

        // link các trường giao diện khai báo đến layout giao diện đã thiết kế
        edt_username = findViewById(R.id.edt_sys_username);
        edit_password = findViewById(R.id.edt_sys_password);
        btn_login_sys = findViewById(R.id.btn_sys_login);
        mtv_false_status = findViewById(R.id.tv_status_login);
        mtv_forget_password = findViewById(R.id.tv_forget_password);

        //SET sharedpreferences để lưu các thông tin của người dùng đăng nhập vào hệ thống
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

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

        //Sự kiện chọn button quên mật khẩu
        mtv_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ForgetInfoActivity.class);
                startActivity(i);
            }
        });

        //Sự kiện đăng nhập vào hệ thống
        btn_login_sys.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if (edt_username.getText().toString().equals("") && edit_password.getText().toString().equals("")) {
                    mtv_false_status.setText("Vui lòng nhập tài khoản và mất khẩu");
                } else if (edt_username.getText().toString().equals("")) {
                    mtv_false_status.setText("Vui lòng nhập tài khoản");
                } else if (edit_password.getText().toString().equals("") && !edt_username.getText().toString().equals("")) {
                    mtv_false_status.setText("Vui lòng nhập mất khẩu");
                }
                String user = edt_username.getText().toString();
                String pass = AES.encrypt(edit_password.getText().toString(), secretKey);
                if (!edt_username.getText().toString().equals("")) {
                    Call<Employee> call = jsonPlaceHolderApi.getEmpbyUsername(user);
                    call.enqueue(new Callback<Employee>() {
                        @Override
                        public void onResponse(Call<Employee> call, Response<Employee> response) {

                            if (!response.isSuccessful()) {
                                mtv_false_status.setText("Tài khoản không tồn tại");
                                return;
                            }
                            Employee emp = response.body();
                            if (emp.getPassword().equals(pass)) {

                                //Lưu thông tin người dùng đăng nhập vào sharedpreferences
                                editor.putString(ID_USER, emp.getId());
                                editor.putString(NAME_USER, emp.getName());
                                editor.putString(MAIL_USER,emp.getMail());
                                editor.putString(ROLE_USER,emp.getIdrole());
                                editor.commit();

                                //chuyển về activity trang home
                                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(i);
                            } else {
                                mtv_false_status.setText("Mật khẩu không đúng, vui lòng kiểm tra lại");
                            }
                        }

                        @Override
                        public void onFailure(Call<Employee> call, Throwable t) {
                            mtv_false_status.setText("Tài khoản không tồn tại");
                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // Nếu người dùng đăng nhập thành công thì chuyển đển trang Home luôn
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, getApplicationContext().MODE_PRIVATE);
        String id_user_restart = sharedPreferences.getString(ID_USER, "");
        if (!id_user_restart.equals("")) {
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
        }
    }

    /**
     * Result of runtime permission request
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Check for granted permission and remove from missing list
        if (requestCode == REQUEST_PERMISSION_CODE_LOGIN) {
            for (int i = grantResults.length - 1; i >= 0; i--) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    missingPermission.remove(permissions[i]);
                }
            }
        }
    }


    //endregion
    //region Registration n' Permissions Helpers

    /**
     * Checks if there is any missing permissions, and
     * requests runtime permission if needed.
     */
    private void checkAndRequestPermissions() {
        // Check for permissions
        for (String eachPermission : REQUIRED_PERMISSION_LIST_LOGN) {
            if (ContextCompat.checkSelfPermission(this, eachPermission) != PackageManager.PERMISSION_GRANTED) {
                missingPermission.add(eachPermission);
            }
        }
        // Request for missing permissions
        if (missingPermission.isEmpty()) {
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,
                    missingPermission.toArray(new String[missingPermission.size()]),
                    REQUEST_PERMISSION_CODE_LOGIN);
        }
    }
}
