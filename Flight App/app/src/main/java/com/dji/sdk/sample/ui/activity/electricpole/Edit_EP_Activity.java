package com.dji.sdk.sample.ui.activity.electricpole;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dji.sdk.sample.ConnectServer;
import com.dji.sdk.sample.JsonPlaceHolderApi;
import com.dji.sdk.sample.R;
import com.dji.sdk.sample.model.ElectricPole;
import com.dji.sdk.sample.ui.activity.StartAppActivity;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The type Edit ep activity.
 */
public class Edit_EP_Activity extends AppCompatActivity {

    private EditText medt_epedit_name;
    private EditText medt_epedit_des;
    private EditText medt_epedit_latitude;
    private EditText medt_epedit_longitude;
    private EditText medt_epedit_datebuild;
    private EditText medt_epedit_datemain;
    private TextView mtv_epedit_status;
    private Button mbtn_epedit_delete;
    private Button mbtn_epedit_map;
    private Button mbtn_epedit_done;
    private Button mbtn_epedit_edit;
    private Button mbtn_epedit_save;
    private Button mbtn_epedit_cancel;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private ElectricPole electricPole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__ep);

        //Get Intent để lấy giá trị từ intent truyền vào của activity gọi đến để
        // lấy ra id của cột điện cần chỉnh sửa
        Intent intent = getIntent();
        String id = intent.getStringExtra("id_pole");

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

        //khai báo một thông báo xác nhận để xác nhận lưu thông tin từ người dùng
        AlertDialog.Builder show = new AlertDialog.Builder(this);

        // link các trường giao diện khai báo đến layout giao diện đã thiết kế
        medt_epedit_name = findViewById(R.id.edt_epedit_name);
        medt_epedit_des = findViewById(R.id.edt_epedit_des);
        medt_epedit_latitude = findViewById(R.id.edt_epedit_latitude);
        medt_epedit_longitude = findViewById(R.id.edt_epedit_longitude);
        medt_epedit_datebuild = findViewById(R.id.edt_epedit_datebuild);
        medt_epedit_datemain = findViewById(R.id.edt_epedit_datemain);
        mtv_epedit_status = findViewById(R.id.tv_epedit_status);
        mbtn_epedit_map = findViewById(R.id.btn_epedit_map);
        mbtn_epedit_delete = findViewById(R.id.btn_epedit_delete);
        mbtn_epedit_done = findViewById(R.id.btn_epedit_done);
        mbtn_epedit_edit = findViewById(R.id.btn_epedit_edit);
        mbtn_epedit_save = findViewById(R.id.btn_epedit_save);
        mbtn_epedit_cancel = findViewById(R.id.btn_epedit_cancel);

        //SetVisible sự xuất hiện các itemview trong giao diện khi create giao diện
        mbtn_epedit_save.setVisibility(View.INVISIBLE);
        mbtn_epedit_cancel.setVisibility(View.INVISIBLE);
        mbtn_epedit_edit.setVisibility(View.VISIBLE);
        mbtn_epedit_done.setVisibility(View.VISIBLE);

        //Bắt sự kiện click vào button xóa để xóa thông tin cột điện
        mbtn_epedit_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<String> call = jsonPlaceHolderApi.deleteEP(id);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        //Log.d("Delete EP", response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        //Log.e("Delete EP", t.getMessage());
                    }
                });

                //Chuyển đến trang home khi xóa xong một cột điện bằng Intent
                Intent i = new Intent(getApplicationContext(), StartAppActivity.class);
                startActivity(i);
            }
        });

        // Lấy thông tin người dùng
        Call<ElectricPole> call = jsonPlaceHolderApi.getEPbyID(id);
        call.enqueue(new Callback<ElectricPole>() {
            @Override
            public void onResponse(Call<ElectricPole> call, Response<ElectricPole> response) {
                electricPole = response.body();

                //Gán thông tin người dùng cho các phần từ trên giao diện
                medt_epedit_name.setText(electricPole.getPole_Name());
                medt_epedit_des.setText(electricPole.getDescription());
                medt_epedit_latitude.setText(electricPole.getPole_Latitude().toString());
                medt_epedit_longitude.setText(electricPole.getPole_Longitude().toString());
                medt_epedit_datebuild.setText(electricPole.getBuildTime());
                medt_epedit_datemain.setText(electricPole.getEp_MaintenanceTime());
            }

            @Override
            public void onFailure(Call<ElectricPole> call, Throwable t) {
                Log.e("User Manager", "Lỗi: " + t.getMessage());
            }
        });

        //Sự kiện ấn vào nút "xong", trở lại trang trước
        mbtn_epedit_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Sự kiện ấn vào nút "sửa"
        //Hiển thị các trường lưu và cancel và cho phép sửa thông tin trong edittext
        mbtn_epedit_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mbtn_epedit_edit.setVisibility(View.INVISIBLE);
                mbtn_epedit_done.setVisibility(View.INVISIBLE);
                mbtn_epedit_save.setVisibility(View.VISIBLE);
                mbtn_epedit_cancel.setVisibility(View.VISIBLE);
                medt_epedit_name.setEnabled(true);
                medt_epedit_des.setEnabled(true);
                medt_epedit_latitude.setEnabled(true);
                medt_epedit_longitude.setEnabled(true);
                medt_epedit_datebuild.setEnabled(true);
                medt_epedit_datemain.setEnabled(true);
            }
        });

        //Sự kiện ấn vào button "cancel"
        mbtn_epedit_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                medt_epedit_name.setText(electricPole.getPole_Name());
                medt_epedit_des.setText(electricPole.getDescription());
                medt_epedit_latitude.setText(electricPole.getPole_Latitude().toString());
                medt_epedit_longitude.setText(electricPole.getPole_Longitude().toString());
                medt_epedit_datebuild.setText(electricPole.getBuildTime().toString());
                medt_epedit_datemain.setText(electricPole.getEp_MaintenanceTime().toString());
                mbtn_epedit_edit.setVisibility(View.VISIBLE);
                mbtn_epedit_done.setVisibility(View.VISIBLE);
                mbtn_epedit_save.setVisibility(View.INVISIBLE);
                mbtn_epedit_cancel.setVisibility(View.INVISIBLE);
                medt_epedit_name.setEnabled(false);
                medt_epedit_des.setEnabled(false);
                medt_epedit_latitude.setEnabled(false);
                medt_epedit_longitude.setEnabled(false);
                medt_epedit_datebuild.setEnabled(false);
                medt_epedit_datemain.setEnabled(false);
            }
        });

        //Sự kiện ấn vào button "Lưu"
        mbtn_epedit_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (medt_epedit_name.getText().toString().equals("") ||
                        medt_epedit_des.getText().toString().equals("") ||
                        medt_epedit_latitude.getText().toString().equals("") ||
                        medt_epedit_longitude.getText().toString().equals("") ||
                        medt_epedit_datebuild.getText().toString().equals("") ||
                        medt_epedit_datemain.getText().toString().equals("")) {
                    mtv_epedit_status.setText("Bạn cần nhập đầy đủ thông tin");
                } else {
                    show.setIcon(android.R.drawable.ic_dialog_alert);
                    show.setTitle("Lưu thông tin");
                    show.setMessage("Bạn có muốn lưu thông tin không ?");

                    //Nếu chọn "Yes" thì thực hiện
                    show.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            electricPole.setPole_Name(medt_epedit_name.getText().toString());
                            electricPole.setDescription(medt_epedit_des.getText().toString());
                            electricPole.setPole_Latitude(Double.parseDouble(medt_epedit_latitude.getText().toString()));
                            electricPole.setPole_Longitude(Double.parseDouble(medt_epedit_longitude.getText().toString()));
                            electricPole.setBuildTime(medt_epedit_datebuild.getText().toString());
                            electricPole.setEp_MaintenanceTime(medt_epedit_datemain.getText().toString());
                            ConnectServer connectServer = new ConnectServer();
                            connectServer.Update_EP_Server(id, electricPole);
                            mtv_epedit_status.setText("Sửa thông tin cột điện thành công");
                            mbtn_epedit_edit.setVisibility(View.VISIBLE);
                            mbtn_epedit_done.setVisibility(View.VISIBLE);
                            mbtn_epedit_save.setVisibility(View.INVISIBLE);
                            mbtn_epedit_cancel.setVisibility(View.INVISIBLE);
                            medt_epedit_name.setEnabled(false);
                            medt_epedit_des.setEnabled(false);
                            medt_epedit_latitude.setEnabled(false);
                            medt_epedit_longitude.setEnabled(false);
                            medt_epedit_datebuild.setEnabled(false);
                            medt_epedit_datemain.setEnabled(false);
                        }
                    });

                    //Nếu chọn "No" thì thực hiện
                    show.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            medt_epedit_name.setText(electricPole.getPole_Name());
                            medt_epedit_des.setText(electricPole.getDescription());
                            medt_epedit_latitude.setText(electricPole.getPole_Latitude().toString());
                            medt_epedit_longitude.setText(electricPole.getPole_Longitude().toString());
                            medt_epedit_datebuild.setText(electricPole.getBuildTime().toString());
                            medt_epedit_datemain.setText(electricPole.getEp_MaintenanceTime().toString());
                            mbtn_epedit_edit.setVisibility(View.VISIBLE);
                            mbtn_epedit_done.setVisibility(View.VISIBLE);
                            mbtn_epedit_save.setVisibility(View.INVISIBLE);
                            mbtn_epedit_cancel.setVisibility(View.INVISIBLE);
                            medt_epedit_name.setEnabled(false);
                            medt_epedit_des.setEnabled(false);
                            medt_epedit_latitude.setEnabled(false);
                            medt_epedit_longitude.setEnabled(false);
                            medt_epedit_datebuild.setEnabled(false);
                            medt_epedit_datemain.setEnabled(false);
                        }
                    });
                    show.show();
                }
            }
        });

        //Chế độ Map để hiển thị vị trí cột điện trên bản đồ
        mbtn_epedit_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mtv_epedit_status.setText("Chuyển sang chế độ Map");
            }
        });
    }
}
