package com.dji.sdk.sample.demo.missionmanager;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dji.sdk.sample.R;
import com.dji.sdk.sample.internal.utils.ToastUtils;
import com.dji.sdk.sample.internal.view.PresentableView;

import dji.common.flightcontroller.FlightMode;
import dji.sdk.flightcontroller.FlightController;

/**
 * Class for basic manager view in mission manager
 */
public abstract class MissionBaseView extends RelativeLayout implements View.OnClickListener, PresentableView {

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

    /**
     * Instantiates a new Mission base view.
     *
     * @param context the context
     */
    public MissionBaseView(Context context) {
        super(context);
        initUI(context);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    private void showLongitudeLatitude() {
        ToastUtils.setResultToText(FCPushInfoTV,
                                   "Home point latitude: "
                                       + homeLatitude
                                       + "\n"
                                       + "Home point longitude: "
                                       + homeLongitude
                                       + "\n"
                                       + "Flight state: "
                                       + (flightState == null ? "" : flightState.name()));
    }

    private void initUI(Context context) {
        setClickable(true);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);

        layoutInflater.inflate(R.layout.view_mission, this, true);

        missionPushInfoTV = (TextView) findViewById(R.id.tv_mission_info);
        FCPushInfoTV = (TextView) findViewById(R.id.tv_fc_info);
        loadBtn = (Button) findViewById(R.id.btn_load);
        uploadBtn = (Button) findViewById(R.id.btn_upload);
        startBtn = (Button) findViewById(R.id.btn_start);
        stopBtn = (Button) findViewById(R.id.btn_stop);
        pauseBtn = (Button) findViewById(R.id.btn_pause);
        resumeBtn = (Button) findViewById(R.id.btn_resume);
        downloadBtn = (Button) findViewById(R.id.btn_download);
        progressBar = (ProgressBar) findViewById(R.id.pb_mission);
        simulatorBtn = (Button) findViewById(R.id.btn_simulator);
        maxAltitudeBtn = (Button) findViewById(R.id.btn_set_maximum_altitude);
        maxRadiusBtn = (Button) findViewById(R.id.btn_set_maximum_radius);

        loadBtn.setOnClickListener(this);
        uploadBtn.setOnClickListener(this);
        startBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);
        pauseBtn.setOnClickListener(this);
        resumeBtn.setOnClickListener(this);
        downloadBtn.setOnClickListener(this);
        simulatorBtn.setOnClickListener(this);
        maxRadiusBtn.setOnClickListener(this);
        maxAltitudeBtn.setOnClickListener(this);
    }

    @NonNull
    @Override
    public String getHint() {
        return this.getClass().getSimpleName() + ".java";
    }
}
