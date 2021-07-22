package com.dji.sdk.sample.ui.activity.login;


import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.dji.sdk.sample.JsonPlaceHolderApi;
import com.dji.sdk.sample.R;
import com.dji.sdk.sample.model.Employee;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The type Forget info activity.
 */
public class ForgetInfoActivity extends AppCompatActivity {

    //khóa bí mật để mã hóa mật khẩu
    private final String secretKey = "@datdeptrai@@@anhxinhdep@@@910132!!!";

    private Button mbtn_forget_info;
    private EditText medt_forget_phone;
    private EditText medt_forget_mail;
    private TextView mtv_forget_st;
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_info);

        // link các trường giao diện khai báo đến layout giao diện đã thiết kế
        mbtn_forget_info = findViewById(R.id.btn_forget_info);
        medt_forget_phone = findViewById(R.id.edt_forget_phone);
        medt_forget_mail = findViewById(R.id.edt_forget_email);
        mtv_forget_st = findViewById(R.id.tv_st_forget_info);

        //Khai báo các trường để get API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.URLDataService))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        //Sự kiện chọn button "Lấy lại mật khẩu"
        mbtn_forget_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(medt_forget_phone.getText().toString().equals("") && medt_forget_mail.getText().toString().equals("")){
                    mtv_forget_st.setText("Nhập thông tin điện thoại và Email");
                }
                if(medt_forget_phone.getText().toString().equals("") && !medt_forget_mail.getText().toString().equals("")){
                    mtv_forget_st.setText("Nhập thông tin điện thoại");
                }
                if(medt_forget_mail.getText().toString().equals("") && !medt_forget_phone.getText().toString().equals("")){
                    mtv_forget_st.setText("Nhập thông tin Email");
                }
                if(!medt_forget_phone.getText().toString().equals("") && !medt_forget_mail.getText().toString().equals("")){
                    String phone = medt_forget_phone.getText().toString();
                    String mail = medt_forget_mail.getText().toString();

                    //Kiếm tra số điện thoại đã đăng ký tài khoản nào chưa
                    Call<Employee> call = jsonPlaceHolderApi.getEmpbyPhone(phone,"23c84b2561ec7509bb897577eec7f958815ddb18624b7d4329c5ed4c6c610fd9");
                    call.enqueue(new Callback<Employee>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onResponse(Call<Employee> call, Response<Employee> response) {

                            if (!response.isSuccessful()) {
                                Log.e("Forget Info", "" + response.code());
                                mtv_forget_st.setText("Số điện thoại chưa đăng ký tài khoản nào");
                                return;
                            }
                            Employee emp = response.body();
                            if(emp.getMail().equals(mail)){
                                System.out.println(emp.getPhone());

                                //Call API gửi mail lấy lại mật khẩu cho người dùng
                                Call<String> send = jsonPlaceHolderApi.sendMail(emp.getPhone());
                                send.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        String sms = response.body();
                                        System.out.println(sms);
                                        mtv_forget_st.setText(sms);
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        mtv_forget_st.setText("Lỗi Email");
                                    }
                                });
                            } else {
                                mtv_forget_st.setText("Số điện thoại và mật khẩu không hợp lệ");
                            }
                        }

                        @Override
                        public void onFailure(Call<Employee> call, Throwable t) {
                            Log.e("Forget Info", t.getMessage());
                            mtv_forget_st.setText("Số điện thoại chưa đăng ký tài khoản nào");
                        }
                    });
                }

            }
        });
    }
}
