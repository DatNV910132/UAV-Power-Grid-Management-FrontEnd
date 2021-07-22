package com.dji.sdk.sample.ui.activity.incident;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dji.sdk.sample.JsonPlaceHolderApi;
import com.dji.sdk.sample.R;
import com.dji.sdk.sample.SHA256;
import com.dji.sdk.sample.adapter.SpinFixerAdapter;
import com.dji.sdk.sample.model.Employee;
import com.dji.sdk.sample.model.Incident;
import com.dji.sdk.sample.model.Link;
import com.dji.sdk.sample.ui.activity.StartAppActivity;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

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
 * The type Add incident activity.
 */
public class AddIncidentActivity extends AppCompatActivity {

    private final String SHARED_PREFERENCES_NAME = "login";
    private final String ID_USER = "id_user";
    private final String ROLE_USER = "role_user";
    private JsonPlaceHolderApi jsonPlaceHolderApi_Data;
    private JsonPlaceHolderApi jsonPlaceHolderApi_Inci;
    private String idimage;
    private String idpole;
    private String id_user;
    private String idfixer;
    private List<Employee> listfixer = new ArrayList<>();

    private PhotoView mpv_addinci_image;
    private EditText medt_addinci_name;
    private EditText medt_addinci_des;
    private EditText medt_addinci_date;
    private EditText medt_addinci_level;
    private Spinner msp_addinci_idfix;
    private Button mbtn_addinci_save;
    private Button mbtn_addinci_cancel;
    private TextView mtv_addinci_addStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_incident);

        //Get intent lấy giá trị từ việc startactivity gọi sang activity này
        //lấy thông tin id của ảnh và id của cột điện
        Intent get = getIntent();
        idimage = get.getStringExtra("idimage");
        idpole = get.getStringExtra("idpole");

        //Get thông tin của người dùng đã đăng nhập vào hệ thống
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, this.MODE_PRIVATE);
        id_user = sharedPreferences.getString(ID_USER, "");
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
        jsonPlaceHolderApi_Data = retrofit.create(JsonPlaceHolderApi.class);

        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.URLIncidentService))
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        jsonPlaceHolderApi_Inci = retrofit2.create(JsonPlaceHolderApi.class);

        // link các trường giao diện khai báo đến layout giao diện đã thiết kế
        mpv_addinci_image = findViewById(R.id.photoview_addinci_image);
        medt_addinci_name = findViewById(R.id.edt_addinci_name);
        medt_addinci_des = findViewById(R.id.edt_addinci_des);
        medt_addinci_date = findViewById(R.id.edt_addinci_date);
        medt_addinci_level = findViewById(R.id.edt_addinci_level);
        msp_addinci_idfix = findViewById(R.id.sp_addinci_idfix);
        mbtn_addinci_save = findViewById(R.id.btn_addinci_save);
        mbtn_addinci_cancel = findViewById(R.id.btn_addinci_cancel);
        mtv_addinci_addStatus = findViewById(R.id.tv_addinci_addstatus);

        //Lấy ảnh và hiển thị ảnh vào trong giao diện
        String imageUrl = getResources().getString(R.string.URLDataDrone)+"photos/byte/" + idimage;
        Picasso.get().load(imageUrl).into(mpv_addinci_image);

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
                        //Lấy tất cả thông tin người dùng là người sửa chữa để hiển thị lên spinner lựa chọn
                        Call<List<Employee>> getuserbyrole = jsonPlaceHolderApi_Data.getEmpbyRole("Fixer",code);
                        getuserbyrole.enqueue(new Callback<List<Employee>>() {
                            @Override
                            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                                listfixer = response.body();
                                SpinFixerAdapter adapter = new SpinFixerAdapter(getApplicationContext(), R.layout.sp_item_addinci_fixxer, listfixer);
                                adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                                msp_addinci_idfix.setAdapter(adapter);
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

        //Sự kiện chọn một người sửa chữa nào đó trong spinner và lưu lại để thêm vào thông tin sự cố
        msp_addinci_idfix.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                idfixer = listfixer.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Sự kiến ấn vào button "cancel", trở về trang trước
        mbtn_addinci_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //sự kiện chọn button "Lưu" để lưu thông tin của sự cố
        mbtn_addinci_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (medt_addinci_name.getText().toString().equals("") ||
                        medt_addinci_des.getText().toString().equals("") ||
                        medt_addinci_date.getText().toString().equals("") ||
                        medt_addinci_level.getText().toString().equals("")) {
                    mtv_addinci_addStatus.setText("Bạn cần nhập đầy đủ thông tin");
                } else {
                    Incident incident = new Incident();
                    incident.setName(medt_addinci_name.getText().toString());
                    incident.setDate(medt_addinci_date.getText().toString());
                    incident.setDes(medt_addinci_des.getText().toString());
                    incident.setLevel(medt_addinci_level.getText().toString());
                    incident.setStatus("norepair");
                    incident.setIdfix(idfixer);
                    incident.setImage(idimage);
                    incident.setIdpole(idpole);
                    incident.setIddetect(id_user);

                    //Call API để post dữ liệu sự cố lên server
                    Call<String> calladd = jsonPlaceHolderApi_Inci.addInci(incident);
                    calladd.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
//                                mtv_addinci_addStatus.setText(t.getMessage());
                        }
                    });

                    //Startactivity bằng intent để chuyển về màn hình trang chủ
                    Intent i = new Intent(getApplicationContext(), StartAppActivity.class);
                    startActivity(i);
                }
            }
        });

    }
}
