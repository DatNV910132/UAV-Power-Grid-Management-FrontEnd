package com.dji.sdk.sample.ui.activity.incident;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dji.sdk.sample.JsonPlaceHolderApi;
import com.dji.sdk.sample.R;
import com.dji.sdk.sample.SHA256;
import com.dji.sdk.sample.adapter.SpinFixerAdapter;
import com.dji.sdk.sample.model.ElectricPole;
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
 * The type Detail inci activity.
 */
public class DetailInciActivity extends AppCompatActivity {

    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private JsonPlaceHolderApi jsonPlaceHolderApi_Data;
    private List<Employee> listfixer = new ArrayList<>();
    private final String SHARED_PREFERENCES_NAME = "login";
    private final String ROLE_USER = "role_user";
    private Incident incident;

    private PhotoView mpv_detailinci_image;
    private EditText medt_detailinci_name;
    private EditText medt_detailinci_des;
    private EditText medt_detailinci_date;
    private EditText medt_detailinci_level;
    private EditText medt_detailinci_status;
    private EditText medt_detailinci_idpole;
    private EditText medt_detailinci_iddetect;
    private EditText medt_detailinci_idfixer;
    private Spinner msp_detailinci_level;
    private Spinner msp_detailinci_status;
    private Spinner msp_detailinci_idfixer;
    private Button mbtn_detailinci_map;
    private Button mbtn_detailinci_done;
    private Button mbtn_detailinci_edit;
    private Button mbtn_detailinci_save;
    private Button mbtn_detailinci_cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_inci);

        Intent get = getIntent();
        String idinci = get.getStringExtra("idinci");

        //Lấy ra thông tin của role của user đăng nhập vào thiết bị
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
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

        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.URLIncidentService))
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        jsonPlaceHolderApi = retrofit1.create(JsonPlaceHolderApi.class);

        //khai báo một thông báo xác nhận để xác nhận lưu thông tin từ người dùng
        AlertDialog.Builder show = new AlertDialog.Builder(this);

        // link các trường giao diện khai báo đến layout giao diện đã thiết kế
        mpv_detailinci_image = findViewById(R.id.pv_detailinci_image);
        medt_detailinci_name = findViewById(R.id.edt_detailinci_name);
        medt_detailinci_des = findViewById(R.id.edt_detailinci_des);
        medt_detailinci_date = findViewById(R.id.edt_detailinci_date);
        medt_detailinci_level = findViewById(R.id.edt_detailinci_level);
        medt_detailinci_status = findViewById(R.id.edt_detailinci_status);
        medt_detailinci_idpole = findViewById(R.id.edt_detailinci_idpole);
        medt_detailinci_iddetect = findViewById(R.id.edt_detailinci_iddetect);
        medt_detailinci_idfixer = findViewById(R.id.edt_detailinci_idfixer);
        msp_detailinci_status = findViewById(R.id.sp_detailinci_status);
        msp_detailinci_level = findViewById(R.id.sp_detailinci_level);
        msp_detailinci_idfixer = findViewById(R.id.sp_detailinci_idfixer);
        mbtn_detailinci_map = findViewById(R.id.btn_detailinci_map);
        mbtn_detailinci_done = findViewById(R.id.btn_detailinci_done);
        mbtn_detailinci_edit = findViewById(R.id.btn_detailinci_edit);
        mbtn_detailinci_save = findViewById(R.id.btn_detailinci_save);
        mbtn_detailinci_cancel = findViewById(R.id.btn_detailinci_cancel);


        String level[] = {
                "Mức độ nguy hiểm 1",
                "Mức độ nguy hiểm 2",
                "Mức độ nguy hiểm 3",
                "Mức độ nguy hiểm 4"};
        ArrayAdapter<String> adapterlevel = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, level);
        adapterlevel.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);
        //Thiết lập adapter cho Spinner level
        msp_detailinci_level.setAdapter(adapterlevel);

        String status[] = {
                "Chưa sửa chữa",
                "Đang sửa chữa",
                "Đã sửa chữa",
                "Chờ xác nhận"};
        ArrayAdapter<String> adapterstatus = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, status);
        adapterstatus.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);
        //Thiết lập adapter cho Spinner status
        msp_detailinci_status.setAdapter(adapterstatus);

        Call<Link> link2 = jsonPlaceHolderApi_Data.getlinkbyUrl("employees/role/");
        link2.enqueue(new Callback<Link>() {
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
                        //Lấy thông tin của người sửa chữa để đưa vào spinner người sửa chữa
                        Call<List<Employee>> getempbyrole = jsonPlaceHolderApi_Data.getEmpbyRole("Fixer", code);
                        getempbyrole.enqueue(new Callback<List<Employee>>() {
                            @Override
                            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                                listfixer = response.body();
                                SpinFixerAdapter adapter = new SpinFixerAdapter(getApplicationContext(), R.layout.sp_item_addinci_fixxer, listfixer);
                                adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                                //Thiết lập adapter cho Spinner người sửa chữa
                                msp_detailinci_idfixer.setAdapter(adapter);
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


        //Lấy thông tin của sự cố để hiện thị lên giao diện
        Call<Incident> getbyid = jsonPlaceHolderApi.getincibyid(idinci);
        getbyid.enqueue(new Callback<Incident>() {
            @Override
            public void onResponse(Call<Incident> call, Response<Incident> response) {
                incident = response.body();
                //Lấy id để hiển thị ảnh lên giao diện
                String imageUrl = getResources().getString(R.string.URLDataDrone)+"photos/byte/" + incident.getImage();
                Picasso.get().load(imageUrl).into(mpv_detailinci_image);
                //hiển thị các trường khác
                medt_detailinci_name.setText(incident.getName());
                medt_detailinci_des.setText(incident.getDes());
                medt_detailinci_date.setText(incident.getDate());
                medt_detailinci_level.setText(incident.getLevel());

                //Hiển thị trường trạng thái
                if (incident.getStatus().equals("norepair")) {
                    medt_detailinci_status.setText("Chưa sửa chữa");
                }
                if (incident.getStatus().equals("repairing")) {
                    medt_detailinci_status.setText("Đang sửa chữa");
                }
                if (incident.getStatus().equals("repaired")) {
                    medt_detailinci_status.setText("Đã sửa chữa");
                }
                if (incident.getStatus().equals("wait")) {
                    medt_detailinci_status.setText("Chờ xác nhận");
                }

                //Lấy thông tin cột điện xảy ra sự cố đó
                Call<ElectricPole> getpole = jsonPlaceHolderApi_Data.getEPbyID(incident.getIdpole());
                getpole.enqueue(new Callback<ElectricPole>() {
                    @Override
                    public void onResponse(Call<ElectricPole> call, Response<ElectricPole> response) {
                        medt_detailinci_idpole.setText(response.body().getPole_Name());
                    }

                    @Override
                    public void onFailure(Call<ElectricPole> call, Throwable t) {

                    }
                });

                Call<Link> link1 = jsonPlaceHolderApi_Data.getlinkbyUrl("/employees/{id}");
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
                            if (code != null) {
                                //Lấy thông tin người phát hiện ra sự cố
                                Call<Employee> getdetect = jsonPlaceHolderApi_Data.getEmpbyID(incident.getIddetect(), code);
                                getdetect.enqueue(new Callback<Employee>() {
                                    @Override
                                    public void onResponse(Call<Employee> call, Response<Employee> response) {
                                        medt_detailinci_iddetect.setText(response.body().getName());
                                    }

                                    @Override
                                    public void onFailure(Call<Employee> call, Throwable t) {

                                    }
                                });

                                //Lấy thông tin người chịu trách nhiệm xử lý sự cố
                                Call<Employee> getfixer = jsonPlaceHolderApi_Data.getEmpbyID(incident.getIdfix(), code);
                                getfixer.enqueue(new Callback<Employee>() {
                                    @Override
                                    public void onResponse(Call<Employee> call, Response<Employee> response) {
                                        medt_detailinci_idfixer.setText(response.body().getName());
                                    }

                                    @Override
                                    public void onFailure(Call<Employee> call, Throwable t) {

                                    }
                                });
                            }
                    }

                    @Override
                    public void onFailure(Call<Link> call, Throwable t) {

                    }
                });

            }

            @Override
            public void onFailure(Call<Incident> call, Throwable t) {

            }
        });

        //Chế độ xem sự cố trên bản đồ google Map
        mbtn_detailinci_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Chế độ Bản đồ", Toast.LENGTH_SHORT).show();
            }
        });

        //Sự kiện chọn button "Xong" để trở lại trang trướcs
        mbtn_detailinci_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Sự kiện chọn button "cancel"
        mbtn_detailinci_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                medt_detailinci_name.setEnabled(false);
                medt_detailinci_des.setEnabled(false);
                medt_detailinci_date.setEnabled(false);
                medt_detailinci_level.setVisibility(View.VISIBLE);
                medt_detailinci_status.setVisibility(View.VISIBLE);
                medt_detailinci_idfixer.setVisibility(View.VISIBLE);
                msp_detailinci_level.setVisibility(View.INVISIBLE);
                msp_detailinci_status.setVisibility(View.INVISIBLE);
                msp_detailinci_idfixer.setVisibility(View.INVISIBLE);
                mbtn_detailinci_done.setVisibility(View.VISIBLE);
                mbtn_detailinci_edit.setVisibility(View.VISIBLE);
                mbtn_detailinci_save.setVisibility(View.INVISIBLE);
                mbtn_detailinci_cancel.setVisibility(View.INVISIBLE);

                //Hiển thị ảnh
                String imageUrl = getResources().getString(R.string.URLDataDrone)+"photos/byte/" + incident.getImage();
                Picasso.get().load(imageUrl).into(mpv_detailinci_image);
                medt_detailinci_name.setText(incident.getName());
                medt_detailinci_des.setText(incident.getDes());
                medt_detailinci_date.setText(incident.getDate());
                medt_detailinci_level.setText(incident.getLevel());
                if (incident.getStatus().equals("norepair")) {
                    medt_detailinci_status.setText("Chưa sửa chữa");
                }
                if (incident.getStatus().equals("repairing")) {
                    medt_detailinci_status.setText("Đang sửa chữa");
                }
                if (incident.getStatus().equals("repaired")) {
                    medt_detailinci_status.setText("Đã sửa chữa");
                }
                if (incident.getStatus().equals("wait")) {
                    medt_detailinci_status.setText("Chờ xác nhận");
                }

                //Lấy thông tin về cột điện xảy ra sự cố
                Call<ElectricPole> getpole = jsonPlaceHolderApi_Data.getEPbyID(incident.getIdpole());
                getpole.enqueue(new Callback<ElectricPole>() {
                    @Override
                    public void onResponse(Call<ElectricPole> call, Response<ElectricPole> response) {
                        medt_detailinci_idpole.setText(response.body().getPole_Name());
                    }

                    @Override
                    public void onFailure(Call<ElectricPole> call, Throwable t) {

                    }
                });

                Call<Link> link3 = jsonPlaceHolderApi_Data.getlinkbyUrl("/employees/{id}");
                link3.enqueue(new Callback<Link>() {
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

                                //lấy thông tin nhân viên phát hiện ra sự cố đó
                                Call<Employee> getdetect = jsonPlaceHolderApi_Data.getEmpbyID(incident.getIddetect(),code);
                                getdetect.enqueue(new Callback<Employee>() {
                                    @Override
                                    public void onResponse(Call<Employee> call, Response<Employee> response) {
                                        medt_detailinci_iddetect.setText(response.body().getName());
                                    }

                                    @Override
                                    public void onFailure(Call<Employee> call, Throwable t) {

                                    }
                                });

                                //lấy thông tin nhân viên chịu trách nhiệm sửa chữa sự cố đó
                                Call<Employee> getfixer = jsonPlaceHolderApi_Data.getEmpbyID(incident.getIdfix(),code);
                                getfixer.enqueue(new Callback<Employee>() {
                                    @Override
                                    public void onResponse(Call<Employee> call, Response<Employee> response) {
                                        medt_detailinci_idfixer.setText(response.body().getName());
                                    }

                                    @Override
                                    public void onFailure(Call<Employee> call, Throwable t) {

                                    }
                                });
                            }
                    }

                    @Override
                    public void onFailure(Call<Link> call, Throwable t) {

                    }
                });
            }
        });

        //Sự kiện chọn button "sửa" chuyển sang chế độ sửa thông tin của sự cố
        mbtn_detailinci_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                medt_detailinci_name.setEnabled(true);
                medt_detailinci_des.setEnabled(true);
                medt_detailinci_date.setEnabled(true);
                mbtn_detailinci_done.setVisibility(View.INVISIBLE);
                mbtn_detailinci_edit.setVisibility(View.INVISIBLE);
                mbtn_detailinci_save.setVisibility(View.VISIBLE);
                mbtn_detailinci_cancel.setVisibility(View.VISIBLE);
                medt_detailinci_level.setVisibility(View.INVISIBLE);
                medt_detailinci_status.setVisibility(View.INVISIBLE);
                medt_detailinci_idfixer.setVisibility(View.INVISIBLE);
                msp_detailinci_level.setVisibility(View.VISIBLE);
                msp_detailinci_status.setVisibility(View.VISIBLE);
                msp_detailinci_idfixer.setVisibility(View.VISIBLE);
            }
        });

        //Sự kiện sửa và chọn button "Lưu"
        mbtn_detailinci_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (medt_detailinci_name.getText().toString().equals("") ||
                        medt_detailinci_des.getText().toString().equals("") ||
                        medt_detailinci_date.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Bạn cần nhập đầy đủ thông tin", Toast.LENGTH_LONG).show();
                } else {
                    show.setIcon(android.R.drawable.ic_dialog_alert);
                    show.setTitle("Lưu thông tin");
                    show.setMessage("Bạn có muốn lưu thông tin không ?");

                    //Nếu chọn "Yes"
                    show.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            incident.setName(medt_detailinci_name.getText().toString());
                            incident.setDes(medt_detailinci_des.getText().toString());
                            incident.setDate(medt_detailinci_date.getText().toString());

                            //Chọn level
                            if (msp_detailinci_level.getSelectedItemPosition() == 0) {
                                incident.setLevel("1");
                            }

                            if (msp_detailinci_level.getSelectedItemPosition() == 1) {
                                incident.setLevel("2");
                            }
                            if (msp_detailinci_level.getSelectedItemPosition() == 2) {
                                incident.setLevel("3");
                            }
                            if (msp_detailinci_level.getSelectedItemPosition() == 3) {
                                incident.setLevel("4");
                            }

                            //Chọn Status
                            if (msp_detailinci_status.getSelectedItemPosition() == 0) {
                                incident.setStatus("norepair");
                            }
                            if (msp_detailinci_status.getSelectedItemPosition() == 1) {
                                incident.setStatus("repairing");
                            }

                            if (msp_detailinci_status.getSelectedItemPosition() == 2) {
                                incident.setStatus("repaired");
                            }

                            if (msp_detailinci_status.getSelectedItemPosition() == 3) {
                                incident.setStatus("wait");
                            }

                            //Lấy thông tin Id của người chịu trách nhiệm sửa chữa sự cố
                            incident.setIdfix(listfixer.get(msp_detailinci_idfixer.getSelectedItemPosition()).getId());

                            //Call API update sự cố lên hệ thống
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

                            //Lấy thông tin sự cố theo ID của sự cố để hiện thị sau khi update sự cố
                            Call<Incident> getbyid = jsonPlaceHolderApi.getincibyid(idinci);
                            getbyid.enqueue(new Callback<Incident>() {
                                @Override
                                public void onResponse(Call<Incident> call, Response<Incident> response) {
                                    incident = response.body();
                                    String imageUrl = getResources().getString(R.string.URLDataDrone)+"photos/byte/" + incident.getImage();
                                    Picasso.get().load(imageUrl).into(mpv_detailinci_image);
                                    medt_detailinci_name.setText(incident.getName());
                                    medt_detailinci_des.setText(incident.getDes());
                                    medt_detailinci_date.setText(incident.getDate());
                                    medt_detailinci_level.setText(incident.getLevel());
                                    if (incident.getStatus().equals("norepair")) {
                                        medt_detailinci_status.setText("Chưa sửa chữa");
                                    }
                                    if (incident.getStatus().equals("repairing")) {
                                        medt_detailinci_status.setText("Đang sửa chữa");
                                    }
                                    if (incident.getStatus().equals("repaired")) {
                                        medt_detailinci_status.setText("Đã sửa chữa");
                                    }
                                    if (incident.getStatus().equals("wait")) {
                                        medt_detailinci_status.setText("Chờ xác nhận");
                                    }
                                    Call<ElectricPole> getpole = jsonPlaceHolderApi_Data.getEPbyID(incident.getIdpole());
                                    getpole.enqueue(new Callback<ElectricPole>() {
                                        @Override
                                        public void onResponse(Call<ElectricPole> call, Response<ElectricPole> response) {
                                            medt_detailinci_idpole.setText(response.body().getPole_Name());
                                        }

                                        @Override
                                        public void onFailure(Call<ElectricPole> call, Throwable t) {

                                        }
                                    });

                                    Call<Link> link1 = jsonPlaceHolderApi_Data.getlinkbyUrl("employees/delete/{id}");
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
                                                    Call<Employee> getdetect = jsonPlaceHolderApi_Data.getEmpbyID(incident.getIddetect(),code);
                                                    getdetect.enqueue(new Callback<Employee>() {
                                                        @Override
                                                        public void onResponse(Call<Employee> call, Response<Employee> response) {
                                                            medt_detailinci_iddetect.setText(response.body().getName());
                                                        }

                                                        @Override
                                                        public void onFailure(Call<Employee> call, Throwable t) {

                                                        }
                                                    });
                                                    Call<Employee> getfixer = jsonPlaceHolderApi_Data.getEmpbyID(incident.getIdfix(),code);
                                                    getfixer.enqueue(new Callback<Employee>() {
                                                        @Override
                                                        public void onResponse(Call<Employee> call, Response<Employee> response) {
                                                            medt_detailinci_idfixer.setText(response.body().getName());
                                                        }

                                                        @Override
                                                        public void onFailure(Call<Employee> call, Throwable t) {

                                                        }
                                                    });
                                                }
                                        }

                                        @Override
                                        public void onFailure(Call<Link> call, Throwable t) {

                                        }
                                    });

                                }

                                @Override
                                public void onFailure(Call<Incident> call, Throwable t) {

                                }
                            });

                            // tùy chọn hiển thị trên giao diện sau khi sửa thành công
                            medt_detailinci_name.setEnabled(false);
                            medt_detailinci_des.setEnabled(false);
                            medt_detailinci_date.setEnabled(false);
                            medt_detailinci_level.setVisibility(View.VISIBLE);
                            medt_detailinci_status.setVisibility(View.VISIBLE);
                            medt_detailinci_idfixer.setVisibility(View.VISIBLE);
                            msp_detailinci_level.setVisibility(View.INVISIBLE);
                            msp_detailinci_status.setVisibility(View.INVISIBLE);
                            msp_detailinci_idfixer.setVisibility(View.INVISIBLE);
                            mbtn_detailinci_done.setVisibility(View.VISIBLE);
                            mbtn_detailinci_edit.setVisibility(View.VISIBLE);
                            mbtn_detailinci_save.setVisibility(View.INVISIBLE);
                            mbtn_detailinci_cancel.setVisibility(View.INVISIBLE);
                        }
                    });

                    //Nếu chọn "No"
                    show.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            //Tùy chọn hiển thị trên giao diện nếu chọn "no"
                            medt_detailinci_name.setEnabled(false);
                            medt_detailinci_des.setEnabled(false);
                            medt_detailinci_date.setEnabled(false);
                            medt_detailinci_level.setVisibility(View.VISIBLE);
                            medt_detailinci_status.setVisibility(View.VISIBLE);
                            medt_detailinci_idfixer.setVisibility(View.VISIBLE);
                            msp_detailinci_level.setVisibility(View.INVISIBLE);
                            msp_detailinci_status.setVisibility(View.INVISIBLE);
                            msp_detailinci_idfixer.setVisibility(View.INVISIBLE);
                            mbtn_detailinci_done.setVisibility(View.VISIBLE);
                            mbtn_detailinci_edit.setVisibility(View.VISIBLE);
                            mbtn_detailinci_save.setVisibility(View.INVISIBLE);
                            mbtn_detailinci_cancel.setVisibility(View.INVISIBLE);

                            //Hiển thị ảnh và các thông tin khác của sự cố
                            String imageUrl = getResources().getString(R.string.URLDataDrone)+"photos/byte/" + incident.getImage();
                            Picasso.get().load(imageUrl).into(mpv_detailinci_image);
                            medt_detailinci_name.setText(incident.getName());
                            medt_detailinci_des.setText(incident.getDes());
                            medt_detailinci_date.setText(incident.getDate());
                            medt_detailinci_level.setText(incident.getLevel());
                            if (incident.getStatus().equals("norepair")) {
                                medt_detailinci_status.setText("Chưa sửa chữa");
                            }
                            if (incident.getStatus().equals("repairing")) {
                                medt_detailinci_status.setText("Đang sửa chữa");
                            }
                            if (incident.getStatus().equals("repaired")) {
                                medt_detailinci_status.setText("Đã sửa chữa");
                            }
                            if (incident.getStatus().equals("wait")) {
                                medt_detailinci_status.setText("Chờ xác nhận");
                            }

                            //Lấy thông tin của cột điện xảy ra sự cố
                            Call<ElectricPole> getpole = jsonPlaceHolderApi_Data.getEPbyID(incident.getIdpole());
                            getpole.enqueue(new Callback<ElectricPole>() {
                                @Override
                                public void onResponse(Call<ElectricPole> call, Response<ElectricPole> response) {
                                    medt_detailinci_idpole.setText(response.body().getPole_Name());
                                }

                                @Override
                                public void onFailure(Call<ElectricPole> call, Throwable t) {

                                }
                            });

                            Call<Link> link1 = jsonPlaceHolderApi_Data.getlinkbyUrl("/employees/{id}");
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
                                            //Lấy thông tin của nhân viên phát hiện ra sự cố
                                            Call<Employee> getdetect = jsonPlaceHolderApi_Data.getEmpbyID(incident.getIddetect(),code);
                                            getdetect.enqueue(new Callback<Employee>() {
                                                @Override
                                                public void onResponse(Call<Employee> call, Response<Employee> response) {
                                                    medt_detailinci_iddetect.setText(response.body().getName());
                                                }

                                                @Override
                                                public void onFailure(Call<Employee> call, Throwable t) {

                                                }
                                            });

                                            //Lấy thông tin của nhân viên chịu trách nhiệm sửa chữa
                                            Call<Employee> getfixer = jsonPlaceHolderApi_Data.getEmpbyID(incident.getIdfix(),code);
                                            getfixer.enqueue(new Callback<Employee>() {
                                                @Override
                                                public void onResponse(Call<Employee> call, Response<Employee> response) {
                                                    medt_detailinci_idfixer.setText(response.body().getName());
                                                }

                                                @Override
                                                public void onFailure(Call<Employee> call, Throwable t) {

                                                }
                                            });
                                        }
                                }

                                @Override
                                public void onFailure(Call<Link> call, Throwable t) {

                                }
                            });

                        }
                    });
                    show.show();
                }
            }
        });
    }
}
