package com.dji.sdk.sample.ui.activity.dji;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dji.sdk.sample.JsonPlaceHolderApi;
import com.dji.sdk.sample.R;
import com.dji.sdk.sample.SHA256;
import com.dji.sdk.sample.adapter.SpinMissionAdapter;
import com.dji.sdk.sample.demo.missionoperator.WaypointMissionOperatorView;
import com.dji.sdk.sample.internal.controller.DJISampleApplication;
import com.dji.sdk.sample.internal.utils.ToastUtils;
import com.dji.sdk.sample.model.Link;
import com.dji.sdk.sample.model.Mission;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import dji.common.error.DJIError;
import dji.common.flightcontroller.FlightControllerState;
import dji.common.flightcontroller.FlightMode;
import dji.common.flightcontroller.simulator.InitializationData;
import dji.common.mission.waypoint.Waypoint;
import dji.common.mission.waypoint.WaypointAction;
import dji.common.mission.waypoint.WaypointActionType;
import dji.common.mission.waypoint.WaypointMission;
import dji.common.mission.waypoint.WaypointMissionDownloadEvent;
import dji.common.mission.waypoint.WaypointMissionExecutionEvent;
import dji.common.mission.waypoint.WaypointMissionFinishedAction;
import dji.common.mission.waypoint.WaypointMissionFlightPathMode;
import dji.common.mission.waypoint.WaypointMissionGotoWaypointMode;
import dji.common.mission.waypoint.WaypointMissionHeadingMode;
import dji.common.mission.waypoint.WaypointMissionState;
import dji.common.mission.waypoint.WaypointMissionUploadEvent;
import dji.common.model.LocationCoordinate2D;
import dji.common.util.CommonCallbacks;
import dji.sdk.base.BaseProduct;
import dji.sdk.flightcontroller.FlightController;
import dji.sdk.mission.MissionControl;
import dji.sdk.mission.waypoint.WaypointMissionOperator;
import dji.sdk.mission.waypoint.WaypointMissionOperatorListener;
import dji.sdk.products.Aircraft;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.dji.sdk.sample.internal.utils.ModuleVerificationUtil.getFlightController;

/**
 * The type Waypoint mission activity.
 */
public class WaypointMissionActivity extends AppCompatActivity {

    /**
     * The Flight controller.
     */
    protected FlightController flightController;

    /**
     * The Simulator btn.
     */
    protected Button simulatorBtn;
    /**
     * The Max altitude btn.
     */
    protected Button maxAltitudeBtn;
    /**
     * The Max radius btn.
     */
    protected Button maxRadiusBtn;

    /**
     * The Load btn.
     */
    protected Button loadBtn;
    /**
     * The Upload btn.
     */
    protected Button uploadBtn;
    /**
     * The Start btn.
     */
    protected Button startBtn;
    /**
     * The Stop btn.
     */
    protected Button stopBtn;
    /**
     * The Pause btn.
     */
    protected Button pauseBtn;
    /**
     * The Resume btn.
     */
    protected Button resumeBtn;
    /**
     * The Download btn.
     */
    protected Button downloadBtn;

    /**
     * The Mission push info tv.
     */
    protected TextView missionPushInfoTV;
    /**
     * The Fc push info tv.
     */
    protected TextView FCPushInfoTV;
    /**
     * The Progress bar.
     */
    protected ProgressBar progressBar;

    /**
     * The Msp mission.
     */
    public Spinner msp_mission;
    public TextView mtv_missionname;
    public TextView mtv_missiondes;
    public TextView mtv_missionspeed;
    public TextView mtv_missionmaxspeed;
    public TextView mtv_missiongotofirst;
    public TextView mtv_missionlostRCS;
    public TextView mtv_missionfinishaction;
    public TextView mtv_missionflightpathmode;
    public TextView mtv_missionrepeattimes;
    public TextView mtv_missionpoint;
    public Button mbtn_viewmap;
    private static final double ONE_METER_OFFSET = 0.00000899322;
    private static final String TAG = WaypointMissionOperatorView.class.getSimpleName();
    private WaypointMissionOperator waypointMissionOperator;
    private WaypointMission mission;
    private WaypointMissionOperatorListener listener;
    private final int WAYPOINT_COUNT = 5;
    /**
     * The Home latitude.
     */
    protected double homeLatitude = 181;
    /**
     * The Home longitude.
     */
    protected double homeLongitude = 181;
    /**
     * The Flight state.
     */
    protected FlightMode flightState = null;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private final String SHARED_PREFERENCES_NAME = "login";
    private final String ROLE_USER = "role_user";
    private List<Mission> listmision = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waypoint_mission);

        // link các trường giao diện khai báo đến layout giao diện đã thiết kế
        missionPushInfoTV = findViewById(R.id.tv_wpm_mission_info);
        FCPushInfoTV = findViewById(R.id.tv_wpm_fc_info);
        loadBtn = findViewById(R.id.btn_wpm_load);
        uploadBtn = findViewById(R.id.btn_wpm_upload);
        startBtn = findViewById(R.id.btn_wpm_start);
        stopBtn = findViewById(R.id.btn_wpm_stop);
        pauseBtn = findViewById(R.id.btn_wpm_pause);
        resumeBtn = findViewById(R.id.btn_wpm_resume);
        downloadBtn = findViewById(R.id.btn_wpm_download);
        progressBar = findViewById(R.id.pb_wpm_mission);
        simulatorBtn = findViewById(R.id.btn_wpm_simulator);
        maxAltitudeBtn = findViewById(R.id.btn_wpm_set_maximum_altitude);
        maxRadiusBtn = findViewById(R.id.btn_wpm_set_maximum_radius);
        msp_mission = findViewById(R.id.sp_wpm);
        mtv_missionname = findViewById(R.id.tv_itemwpm_name);
        mtv_missiondes = findViewById(R.id.tv_itemwpm_des);
        mtv_missionspeed = findViewById(R.id.tv_itemwpm_speed);
        mtv_missionmaxspeed = findViewById(R.id.tv_itemwpm_maxspeed);
        mtv_missiongotofirst = findViewById(R.id.tv_itemwpm_gotofirst);
        mtv_missionlostRCS = findViewById(R.id.tv_itemwpm_lostRCS);
        mtv_missionfinishaction = findViewById(R.id.tv_itemwpm_finishaction);
        mtv_missionflightpathmode = findViewById(R.id.tv_itemwpm_flightpathmode);
        mtv_missionrepeattimes = findViewById(R.id.tv_itemwpm_repeattimes);
        mtv_missionpoint = findViewById(R.id.tv_itemwpm_point);
        mbtn_viewmap = findViewById(R.id.btn_wpm_viewmap);
        mbtn_viewmap.setEnabled(false);

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
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<Link> linkgetuserbyid = jsonPlaceHolderApi.getlinkbyUrl("/mission/getall/");
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
                    Call<List<Mission>> getallmission = jsonPlaceHolderApi.getallmission(code);
                    getallmission.enqueue(new Callback<List<Mission>>() {
                        @Override
                        public void onResponse(Call<List<Mission>> call, Response<List<Mission>> response) {
                            listmision = response.body();
                            SpinMissionAdapter adapter = new SpinMissionAdapter(getApplicationContext(), R.layout.sp_item_addinci_fixxer, (ArrayList<Mission>) listmision);
                            adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                            msp_mission.setAdapter(adapter);
                        }

                        @Override
                        public void onFailure(Call<List<Mission>> call, Throwable t) {
                            Log.e("User Manager", "Lỗi: " + t.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Link> call, Throwable t) {

            }
        });

        msp_mission.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Mission misionselect = listmision.get(i);
                mtv_missionname.setText("Tên: " + misionselect.getName());
                mtv_missiondes.setText("Mô tả: " + misionselect.getDes());
                mtv_missionspeed.setText("Tốc độ bay: " + misionselect.getAutoFlightSpeed());
                mtv_missionmaxspeed.setText("Tốc độ tối đa: " + misionselect.getMaxFlightSpeed());
                mtv_missiongotofirst.setText("Bay đến điểm bắt đầu: " + misionselect.getGotoFirstWaypointMode());
                if (misionselect.isSetExitMissionOnRCSignalLostEnabled()) {
                    mtv_missionlostRCS.setText("Mất kết nối RCS: Stop Mission");
                } else {
                    mtv_missionlostRCS.setText("Mất kết nối RCS: Continue Mission");
                }
                mtv_missionfinishaction.setText("Kết thúc: " + misionselect.getFinishedAction());
                mtv_missionflightpathmode.setText("Đường bay: " + misionselect.getFlightPathMode());
                mtv_missionrepeattimes.setText("Số lần bay: " + misionselect.getRepeatTimes());
                mtv_missionpoint.setText("Số điểm bay: " + misionselect.getPoleandpointmission().size());

                mbtn_viewmap.setEnabled(true);
                mbtn_viewmap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ToastUtils.setResultToToast("Chuyển sang chế độ Map");
                    }
                });
            }
        });

        if (waypointMissionOperator == null) {
            waypointMissionOperator = MissionControl.getInstance().getWaypointMissionOperator();
        }

        simulatorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFlightController() != null) {
                    flightController.getSimulator()
                            .start(InitializationData.createInstance(new LocationCoordinate2D(22, 113), 10, 10),
                                    new CommonCallbacks.CompletionCallback() {
                                        @Override
                                        public void onResult(DJIError djiError) {
                                            showResultToast(djiError);
                                        }
                                    });
                }
            }
        });

        maxAltitudeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFlightController() != null) {
                    flightController.setMaxFlightHeight(500, new CommonCallbacks.CompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            ToastUtils.setResultToToast(djiError == null ? "Max Flight Height is set to 500m!" : djiError.getDescription());
                        }
                    });
                }
            }
        });

        maxRadiusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFlightController() != null) {
                    flightController.setMaxFlightRadius(500, new CommonCallbacks.CompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            ToastUtils.setResultToToast(djiError == null ? "Max Flight Radius is set to 500m!" : djiError.getDescription());
                        }
                    });
                }
            }
        });

        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mission = demo();
                DJIError djiError = waypointMissionOperator.loadMission(mission);
                showResultToast(djiError);
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Example of uploading a Mission
                if (WaypointMissionState.READY_TO_RETRY_UPLOAD.equals(waypointMissionOperator.getCurrentState())
                        || WaypointMissionState.READY_TO_UPLOAD.equals(waypointMissionOperator.getCurrentState())) {
                    waypointMissionOperator.uploadMission(new CommonCallbacks.CompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            showResultToast(djiError);
                        }
                    });
                } else {
                    ToastUtils.setResultToToast("Not ready!");
                }
            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Example of starting a Mission
                if (mission != null) {
                    waypointMissionOperator.startMission(new CommonCallbacks.CompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            showResultToast(djiError);
                        }
                    });
                } else {
                    ToastUtils.setResultToToast("Prepare Mission First!");
                }
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Example of stopping a Mission
                waypointMissionOperator.stopMission(new CommonCallbacks.CompletionCallback() {
                    @Override
                    public void onResult(DJIError djiError) {
                        showResultToast(djiError);
                    }
                });
            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Example of pausing an executing Mission
                waypointMissionOperator.pauseMission(new CommonCallbacks.CompletionCallback() {
                    @Override
                    public void onResult(DJIError djiError) {
                        showResultToast(djiError);
                    }
                });
            }
        });

        resumeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Example of resuming a paused Mission
                waypointMissionOperator.resumeMission(new CommonCallbacks.CompletionCallback() {
                    @Override
                    public void onResult(DJIError djiError) {
                        showResultToast(djiError);
                    }
                });
            }
        });

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Example of downloading an executing Mission
                if (WaypointMissionState.EXECUTING.equals(waypointMissionOperator.getCurrentState()) ||
                        WaypointMissionState.EXECUTION_PAUSED.equals(waypointMissionOperator.getCurrentState())) {
                    waypointMissionOperator.downloadMission(new CommonCallbacks.CompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            showResultToast(djiError);
                        }
                    });
                } else {
                    ToastUtils.setResultToToast("Mission can be downloaded when the mission state is EXECUTING or EXECUTION_PAUSED!");
                }
            }
        });

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        BaseProduct product = DJISampleApplication.getProductInstance();

        if (product == null || !product.isConnected()) {
            ToastUtils.setResultToToast("Disconnect");
            return;
        } else {
            if (product instanceof Aircraft) {
                flightController = ((Aircraft) product).getFlightController();
            }

            if (flightController != null) {

                flightController.setStateCallback(new FlightControllerState.Callback() {
                    @Override
                    public void onUpdate(@NonNull FlightControllerState flightControllerState) {
                        homeLatitude = flightControllerState.getHomeLocation().getLatitude();
                        homeLongitude = flightControllerState.getHomeLocation().getLongitude();
                        flightState = flightControllerState.getFlightMode();

                        updateWaypointMissionState();
                    }
                });

            }
        }
        waypointMissionOperator = MissionControl.getInstance().getWaypointMissionOperator();
        setUpListener();
    }

    @Override
    public void onDetachedFromWindow() {
        tearDownListener();
        if (flightController != null) {
            flightController.getSimulator().stop(null);
            flightController.setStateCallback(null);
        }
        super.onDetachedFromWindow();
    }

    private void tearDownListener() {
        if (waypointMissionOperator != null && listener != null) {
            // Example of removing listeners
            waypointMissionOperator.removeListener(listener);
        }
    }

    private void setUpListener() {
        // Example of Listener
        listener = new WaypointMissionOperatorListener() {
            @Override
            public void onDownloadUpdate(@NonNull WaypointMissionDownloadEvent waypointMissionDownloadEvent) {
                // Example of Download Listener
                if (waypointMissionDownloadEvent.getProgress() != null
                        && waypointMissionDownloadEvent.getProgress().isSummaryDownloaded
                        && waypointMissionDownloadEvent.getProgress().downloadedWaypointIndex == (WAYPOINT_COUNT - 1)) {
                    ToastUtils.setResultToToast("Download successful!");
                }
                updateWaypointMissionState();
            }

            @Override
            public void onUploadUpdate(@NonNull WaypointMissionUploadEvent waypointMissionUploadEvent) {
                // Example of Upload Listener
                if (waypointMissionUploadEvent.getProgress() != null
                        && waypointMissionUploadEvent.getProgress().isSummaryUploaded
                        && waypointMissionUploadEvent.getProgress().uploadedWaypointIndex == (WAYPOINT_COUNT - 1)) {
                    ToastUtils.setResultToToast("Upload successful!");
                }
                updateWaypointMissionState();
            }

            @Override
            public void onExecutionUpdate(@NonNull WaypointMissionExecutionEvent waypointMissionExecutionEvent) {
                // Example of Execution Listener
                Log.d(TAG,
                        (waypointMissionExecutionEvent.getPreviousState() == null
                                ? ""
                                : waypointMissionExecutionEvent.getPreviousState().getName())
                                + ", "
                                + waypointMissionExecutionEvent.getCurrentState().getName()
                                + (waypointMissionExecutionEvent.getProgress() == null
                                ? ""
                                : waypointMissionExecutionEvent.getProgress().targetWaypointIndex));
                updateWaypointMissionState();
            }

            @Override
            public void onExecutionStart() {
                ToastUtils.setResultToToast("Execution started!");
                updateWaypointMissionState();
            }

            @Override
            public void onExecutionFinish(@Nullable DJIError djiError) {
                ToastUtils.setResultToToast("Execution finished!");
                updateWaypointMissionState();
            }
        };

        if (waypointMissionOperator != null && listener != null) {
            // Example of adding listeners
            waypointMissionOperator.addListener(listener);
        }
    }

    private void updateWaypointMissionState() {
        if (waypointMissionOperator != null && waypointMissionOperator.getCurrentState() != null) {
            ToastUtils.setResultToText(FCPushInfoTV,
                    "home point latitude: "
                            + homeLatitude
                            + "\nhome point longitude: "
                            + homeLongitude
                            + "\nFlight state: "
                            + flightState.name()
                            + "\nCurrent Waypointmission state : "
                            + waypointMissionOperator.getCurrentState().getName());
        } else {
            ToastUtils.setResultToText(FCPushInfoTV,
                    "home point latitude: "
                            + homeLatitude
                            + "\nhome point longitude: "
                            + homeLongitude
                            + "\nFlight state: "
                            + flightState.name());
        }
    }

    private WaypointMission demo() {
        WaypointMission.Builder builder = new WaypointMission.Builder();
        builder.autoFlightSpeed(5f);
        builder.maxFlightSpeed(10f);
        builder.setExitMissionOnRCSignalLostEnabled(false);
        builder.finishedAction(WaypointMissionFinishedAction.AUTO_LAND);
        builder.flightPathMode(WaypointMissionFlightPathMode.NORMAL);
        builder.gotoFirstWaypointMode(WaypointMissionGotoWaypointMode.SAFELY);
        builder.headingMode(WaypointMissionHeadingMode.AUTO);
        builder.repeatTimes(1);
        List<Waypoint> waypointList = new ArrayList<>();
        final Waypoint Waypoint1 = new Waypoint(20.988062 + ONE_METER_OFFSET,
                105.832935 + ONE_METER_OFFSET,
                2);
        Waypoint1.addAction(new WaypointAction(WaypointActionType.START_TAKE_PHOTO, 1));
        waypointList.add(Waypoint1);

        final Waypoint Waypoint2 = new Waypoint(20.988014 + ONE_METER_OFFSET,
                105.832968 + ONE_METER_OFFSET,
                2);
        Waypoint2.addAction(new WaypointAction(WaypointActionType.START_TAKE_PHOTO, 1));
        waypointList.add(Waypoint2);

        final Waypoint Waypoint3 = new Waypoint(20.987953 + ONE_METER_OFFSET,
                105.833012 + ONE_METER_OFFSET,
                2);
        Waypoint3.addAction(new WaypointAction(WaypointActionType.START_TAKE_PHOTO, 1));
        waypointList.add(Waypoint3);

        builder.waypointList(waypointList).waypointCount(waypointList.size());
        ToastUtils.setResultToToast(String.valueOf(waypointList.size()));
        return builder.build();
    }

    private WaypointMission loadfromserver(Mission mission) {
        WaypointMission.Builder builder = new WaypointMission.Builder();
        builder.autoFlightSpeed(mission.getAutoFlightSpeed());
        builder.maxFlightSpeed(mission.getMaxFlightSpeed());
        builder.setExitMissionOnRCSignalLostEnabled(mission.isSetExitMissionOnRCSignalLostEnabled());
        if (mission.getFinishedAction().equals("NO_ACTION")) {
            builder.finishedAction(WaypointMissionFinishedAction.NO_ACTION);
        }
        if (mission.getFinishedAction().equals("GO_HOME")) {
            builder.finishedAction(WaypointMissionFinishedAction.GO_HOME);
        }
        if (mission.getFinishedAction().equals("AUTO_LAND")) {
            builder.finishedAction(WaypointMissionFinishedAction.AUTO_LAND);
        }
        if (mission.getFinishedAction().equals("GO_FIRST_WAYPOINT")) {
            builder.finishedAction(WaypointMissionFinishedAction.GO_FIRST_WAYPOINT);
        }
        if (mission.getFinishedAction().equals("CONTINUE_UNTIL_END")) {
            builder.finishedAction(WaypointMissionFinishedAction.CONTINUE_UNTIL_END);
        }
        if (mission.getFlightPathMode().equals("NORMAL")) {
            builder.flightPathMode(WaypointMissionFlightPathMode.NORMAL);
        }
        if (mission.getFlightPathMode().equals("CURVED")) {
            builder.flightPathMode(WaypointMissionFlightPathMode.CURVED);
        }
        if (mission.getGotoFirstWaypointMode().equals("SAFELY")) {
            builder.gotoFirstWaypointMode(WaypointMissionGotoWaypointMode.SAFELY);
        }
        if (mission.getGotoFirstWaypointMode().equals("POINT_TO_POINT")) {
            builder.gotoFirstWaypointMode(WaypointMissionGotoWaypointMode.POINT_TO_POINT);
        }
        if (mission.getHeadingMode().equals("AUTO")) {
            builder.headingMode(WaypointMissionHeadingMode.AUTO);
        }
        if (mission.getHeadingMode().equals("USING_INITIAL_DIRECTION")) {
            builder.headingMode(WaypointMissionHeadingMode.USING_INITIAL_DIRECTION);
        }
        if (mission.getHeadingMode().equals("CONTROL_BY_REMOTE_CONTROLLER")) {
            builder.headingMode(WaypointMissionHeadingMode.CONTROL_BY_REMOTE_CONTROLLER);
        }
        if (mission.getHeadingMode().equals("USING_WAYPOINT_HEADING")) {
            builder.headingMode(WaypointMissionHeadingMode.USING_WAYPOINT_HEADING);
        }
        if (mission.getHeadingMode().equals("TOWARD_POINT_OF_INTEREST")) {
            builder.headingMode(WaypointMissionHeadingMode.TOWARD_POINT_OF_INTEREST);
        }
        builder.repeatTimes(mission.getRepeatTimes());
        List<Waypoint> waypointList = new ArrayList<>();
        for (String idpapmission : mission.getPoleandpointmission()) {
            Call<Link> linkgetuserbyid = jsonPlaceHolderApi.getlinkbyUrl("/mission/getall/");
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
                        Call<List<Mission>> getallmission = jsonPlaceHolderApi.getallmission(code);
                        getallmission.enqueue(new Callback<List<Mission>>() {
                            @Override
                            public void onResponse(Call<List<Mission>> call, Response<List<Mission>> response) {
                                listmision = response.body();
                                SpinMissionAdapter adapter = new SpinMissionAdapter(getApplicationContext(), R.layout.sp_item_addinci_fixxer, (ArrayList<Mission>) listmision);
                                adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                                msp_mission.setAdapter(adapter);
                            }

                            @Override
                            public void onFailure(Call<List<Mission>> call, Throwable t) {
                                Log.e("User Manager", "Lỗi: " + t.getMessage());
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<Link> call, Throwable t) {

                }
            });

        }

        return builder.build();
    }

    private void showResultToast(DJIError djiError) {
        ToastUtils.setResultToToast(djiError == null ? "Action started!" : djiError.getDescription());
    }
}
