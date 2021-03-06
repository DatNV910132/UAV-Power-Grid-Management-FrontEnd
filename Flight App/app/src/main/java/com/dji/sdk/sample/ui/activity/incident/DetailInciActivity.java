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

        //L???y ra th??ng tin c???a role c???a user ????ng nh???p v??o thi???t b???
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        String id_role = sharedPreferences.getString(ROLE_USER, "");

        //Khai b??o c??c bi???n ?????u v??o ????? Call API t??? server t????ng ???ng t???i baseURL
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

        //khai b??o m???t th??ng b??o x??c nh???n ????? x??c nh???n l??u th??ng tin t??? ng?????i d??ng
        AlertDialog.Builder show = new AlertDialog.Builder(this);

        // link c??c tr?????ng giao di???n khai b??o ?????n layout giao di???n ???? thi???t k???
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
                "M???c ????? nguy hi???m 1",
                "M???c ????? nguy hi???m 2",
                "M???c ????? nguy hi???m 3",
                "M???c ????? nguy hi???m 4"};
        ArrayAdapter<String> adapterlevel = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, level);
        adapterlevel.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);
        //Thi???t l???p adapter cho Spinner level
        msp_detailinci_level.setAdapter(adapterlevel);

        String status[] = {
                "Ch??a s???a ch???a",
                "??ang s???a ch???a",
                "???? s???a ch???a",
                "Ch??? x??c nh???n"};
        ArrayAdapter<String> adapterstatus = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, status);
        adapterstatus.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);
        //Thi???t l???p adapter cho Spinner status
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
                        //L???y th??ng tin c???a ng?????i s???a ch???a ????? ????a v??o spinner ng?????i s???a ch???a
                        Call<List<Employee>> getempbyrole = jsonPlaceHolderApi_Data.getEmpbyRole("Fixer", code);
                        getempbyrole.enqueue(new Callback<List<Employee>>() {
                            @Override
                            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                                listfixer = response.body();
                                SpinFixerAdapter adapter = new SpinFixerAdapter(getApplicationContext(), R.layout.sp_item_addinci_fixxer, listfixer);
                                adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                                //Thi???t l???p adapter cho Spinner ng?????i s???a ch???a
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


        //L???y th??ng tin c???a s??? c??? ????? hi???n th??? l??n giao di???n
        Call<Incident> getbyid = jsonPlaceHolderApi.getincibyid(idinci);
        getbyid.enqueue(new Callback<Incident>() {
            @Override
            public void onResponse(Call<Incident> call, Response<Incident> response) {
                incident = response.body();
                //L???y id ????? hi???n th??? ???nh l??n giao di???n
                String imageUrl = getResources().getString(R.string.URLDataDrone)+"photos/byte/" + incident.getImage();
                Picasso.get().load(imageUrl).into(mpv_detailinci_image);
                //hi???n th??? c??c tr?????ng kh??c
                medt_detailinci_name.setText(incident.getName());
                medt_detailinci_des.setText(incident.getDes());
                medt_detailinci_date.setText(incident.getDate());
                medt_detailinci_level.setText(incident.getLevel());

                //Hi???n th??? tr?????ng tr???ng th??i
                if (incident.getStatus().equals("norepair")) {
                    medt_detailinci_status.setText("Ch??a s???a ch???a");
                }
                if (incident.getStatus().equals("repairing")) {
                    medt_detailinci_status.setText("??ang s???a ch???a");
                }
                if (incident.getStatus().equals("repaired")) {
                    medt_detailinci_status.setText("???? s???a ch???a");
                }
                if (incident.getStatus().equals("wait")) {
                    medt_detailinci_status.setText("Ch??? x??c nh???n");
                }

                //L???y th??ng tin c???t ??i???n x???y ra s??? c??? ????
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
                                //L???y th??ng tin ng?????i ph??t hi???n ra s??? c???
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

                                //L???y th??ng tin ng?????i ch???u tr??ch nhi???m x??? l?? s??? c???
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

        //Ch??? ????? xem s??? c??? tr??n b???n ????? google Map
        mbtn_detailinci_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Ch??? ????? B???n ?????", Toast.LENGTH_SHORT).show();
            }
        });

        //S??? ki???n ch???n button "Xong" ????? tr??? l???i trang tr?????cs
        mbtn_detailinci_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //S??? ki???n ch???n button "cancel"
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

                //Hi???n th??? ???nh
                String imageUrl = getResources().getString(R.string.URLDataDrone)+"photos/byte/" + incident.getImage();
                Picasso.get().load(imageUrl).into(mpv_detailinci_image);
                medt_detailinci_name.setText(incident.getName());
                medt_detailinci_des.setText(incident.getDes());
                medt_detailinci_date.setText(incident.getDate());
                medt_detailinci_level.setText(incident.getLevel());
                if (incident.getStatus().equals("norepair")) {
                    medt_detailinci_status.setText("Ch??a s???a ch???a");
                }
                if (incident.getStatus().equals("repairing")) {
                    medt_detailinci_status.setText("??ang s???a ch???a");
                }
                if (incident.getStatus().equals("repaired")) {
                    medt_detailinci_status.setText("???? s???a ch???a");
                }
                if (incident.getStatus().equals("wait")) {
                    medt_detailinci_status.setText("Ch??? x??c nh???n");
                }

                //L???y th??ng tin v??? c???t ??i???n x???y ra s??? c???
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

                                //l???y th??ng tin nh??n vi??n ph??t hi???n ra s??? c??? ????
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

                                //l???y th??ng tin nh??n vi??n ch???u tr??ch nhi???m s???a ch???a s??? c??? ????
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

        //S??? ki???n ch???n button "s???a" chuy???n sang ch??? ????? s???a th??ng tin c???a s??? c???
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

        //S??? ki???n s???a v?? ch???n button "L??u"
        mbtn_detailinci_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (medt_detailinci_name.getText().toString().equals("") ||
                        medt_detailinci_des.getText().toString().equals("") ||
                        medt_detailinci_date.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "B???n c???n nh???p ?????y ????? th??ng tin", Toast.LENGTH_LONG).show();
                } else {
                    show.setIcon(android.R.drawable.ic_dialog_alert);
                    show.setTitle("L??u th??ng tin");
                    show.setMessage("B???n c?? mu???n l??u th??ng tin kh??ng ?");

                    //N???u ch???n "Yes"
                    show.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            incident.setName(medt_detailinci_name.getText().toString());
                            incident.setDes(medt_detailinci_des.getText().toString());
                            incident.setDate(medt_detailinci_date.getText().toString());

                            //Ch???n level
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

                            //Ch???n Status
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

                            //L???y th??ng tin Id c???a ng?????i ch???u tr??ch nhi???m s???a ch???a s??? c???
                            incident.setIdfix(listfixer.get(msp_detailinci_idfixer.getSelectedItemPosition()).getId());

                            //Call API update s??? c??? l??n h??? th???ng
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

                            //L???y th??ng tin s??? c??? theo ID c???a s??? c??? ????? hi???n th??? sau khi update s??? c???
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
                                        medt_detailinci_status.setText("Ch??a s???a ch???a");
                                    }
                                    if (incident.getStatus().equals("repairing")) {
                                        medt_detailinci_status.setText("??ang s???a ch???a");
                                    }
                                    if (incident.getStatus().equals("repaired")) {
                                        medt_detailinci_status.setText("???? s???a ch???a");
                                    }
                                    if (incident.getStatus().equals("wait")) {
                                        medt_detailinci_status.setText("Ch??? x??c nh???n");
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

                            // t??y ch???n hi???n th??? tr??n giao di???n sau khi s???a th??nh c??ng
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

                    //N???u ch???n "No"
                    show.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            //T??y ch???n hi???n th??? tr??n giao di???n n???u ch???n "no"
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

                            //Hi???n th??? ???nh v?? c??c th??ng tin kh??c c???a s??? c???
                            String imageUrl = getResources().getString(R.string.URLDataDrone)+"photos/byte/" + incident.getImage();
                            Picasso.get().load(imageUrl).into(mpv_detailinci_image);
                            medt_detailinci_name.setText(incident.getName());
                            medt_detailinci_des.setText(incident.getDes());
                            medt_detailinci_date.setText(incident.getDate());
                            medt_detailinci_level.setText(incident.getLevel());
                            if (incident.getStatus().equals("norepair")) {
                                medt_detailinci_status.setText("Ch??a s???a ch???a");
                            }
                            if (incident.getStatus().equals("repairing")) {
                                medt_detailinci_status.setText("??ang s???a ch???a");
                            }
                            if (incident.getStatus().equals("repaired")) {
                                medt_detailinci_status.setText("???? s???a ch???a");
                            }
                            if (incident.getStatus().equals("wait")) {
                                medt_detailinci_status.setText("Ch??? x??c nh???n");
                            }

                            //L???y th??ng tin c???a c???t ??i???n x???y ra s??? c???
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
                                            //L???y th??ng tin c???a nh??n vi??n ph??t hi???n ra s??? c???
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

                                            //L???y th??ng tin c???a nh??n vi??n ch???u tr??ch nhi???m s???a ch???a
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
