package com.dji.sdk.sample.demo.missionoperator;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dji.sdk.sample.R;
import com.dji.sdk.sample.demo.missionmanager.MissionBaseView;
import com.dji.sdk.sample.internal.controller.DJISampleApplication;
import com.dji.sdk.sample.internal.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dji.common.error.DJIError;
import dji.common.flightcontroller.FlightControllerState;
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
import dji.keysdk.FlightControllerKey;
import dji.keysdk.KeyManager;
import dji.sdk.base.BaseProduct;
import dji.sdk.flightcontroller.FlightController;
import dji.sdk.mission.MissionControl;
import dji.sdk.mission.waypoint.WaypointMissionOperator;
import dji.sdk.mission.waypoint.WaypointMissionOperatorListener;
import dji.sdk.products.Aircraft;

import static dji.keysdk.FlightControllerKey.HOME_LOCATION_LATITUDE;
import static dji.keysdk.FlightControllerKey.HOME_LOCATION_LONGITUDE;

/**
 * Class for waypoint mission.
 */
public class WaypointMissionOperatorView extends MissionBaseView {

    private static final double ONE_METER_OFFSET = 0.00000899322;
    private static final String TAG = WaypointMissionOperatorView.class.getSimpleName();
    private WaypointMissionOperator waypointMissionOperator;
    private WaypointMission mission;
    private WaypointMissionOperatorListener listener;
    private final int WAYPOINT_COUNT = 5;

    /**
     * Instantiates a new Waypoint mission operator view.
     *
     * @param context the context
     */
    public WaypointMissionOperatorView(Context context) {
        super(context);
    }

    //region Mission Action Demo
    @Override
    public void onClick(View v) {
        if (waypointMissionOperator == null) {
            waypointMissionOperator = MissionControl.getInstance().getWaypointMissionOperator();
        }
        switch (v.getId()) {
            case R.id.btn_simulator:
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
                break;
            case R.id.btn_set_maximum_altitude:
                if (getFlightController() != null) {
                    flightController.setMaxFlightHeight(500, new CommonCallbacks.CompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            ToastUtils.setResultToToast(djiError == null ? "Max Flight Height is set to 500m!" : djiError.getDescription());
                        }
                    });
                }
                break;

            case R.id.btn_set_maximum_radius:
                if (getFlightController() != null) {
                    flightController.setMaxFlightRadius(500, new CommonCallbacks.CompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            ToastUtils.setResultToToast(djiError == null ? "Max Flight Radius is set to 500m!" : djiError.getDescription());
                        }
                    });
                }
                break;
            case R.id.btn_load:
                // Example of loading a Mission
//                mission = createRandomWaypointMission(WAYPOINT_COUNT, 1);
                mission = demo();
                DJIError djiError = waypointMissionOperator.loadMission(mission);
                showResultToast(djiError);

                break;

            case R.id.btn_upload:
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
                break;
            case R.id.btn_start:
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
                break;
            case R.id.btn_stop:
                // Example of stopping a Mission
                waypointMissionOperator.stopMission(new CommonCallbacks.CompletionCallback() {
                    @Override
                    public void onResult(DJIError djiError) {
                        showResultToast(djiError);
                    }
                });
                break;
            case R.id.btn_pause:
                // Example of pausing an executing Mission
                waypointMissionOperator.pauseMission(new CommonCallbacks.CompletionCallback() {
                    @Override
                    public void onResult(DJIError djiError) {
                        showResultToast(djiError);
                    }
                });
                break;
            case R.id.btn_resume:
                // Example of resuming a paused Mission
                waypointMissionOperator.resumeMission(new CommonCallbacks.CompletionCallback() {
                    @Override
                    public void onResult(DJIError djiError) {
                        showResultToast(djiError);
                    }
                });
                break;
            case R.id.btn_download:
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
                break;
            default:
                break;
        }
    }
    //endregion

    //region View Life-Cycle
    @Override
    protected void onAttachedToWindow() {
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
    protected void onDetachedFromWindow() {
        tearDownListener();
        if (flightController != null) {
            flightController.getSimulator().stop(null);
            flightController.setStateCallback(null);
        }
        super.onDetachedFromWindow();
    }
    //endregion

    //region Internal Helper Methods
    private FlightController getFlightController(){
        if (flightController == null) {
            BaseProduct product = DJISampleApplication.getProductInstance();
            if (product != null && product instanceof Aircraft) {
                flightController = ((Aircraft) product).getFlightController();
            } else {
                ToastUtils.setResultToToast("Product is disconnected!");
            }
        }

        return flightController;
    }
    private void updateWaypointMissionState(){
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
    //endregion

    //region Example of Creating a Waypoint Mission

    /**
     * Randomize a WaypointMission
     *
     * @param numberOfWaypoint total number of Waypoint to randomize
     * @param numberOfAction total number of Action to randomize
     */
    private WaypointMission createRandomWaypointMission(int numberOfWaypoint, int numberOfAction) {
        WaypointMission.Builder builder = new WaypointMission.Builder();
        double baseLatitude = 22;
        double baseLongitude = 113;
        Object latitudeValue = KeyManager.getInstance().getValue((FlightControllerKey.create(HOME_LOCATION_LATITUDE)));
        Object longitudeValue =
            KeyManager.getInstance().getValue((FlightControllerKey.create(HOME_LOCATION_LONGITUDE)));
        if (latitudeValue != null && latitudeValue instanceof Double) {
            baseLatitude = (double) latitudeValue;
        }
        if (longitudeValue != null && longitudeValue instanceof Double) {
            baseLongitude = (double) longitudeValue;
        }

        final float baseAltitude = 20.0f;
        builder.autoFlightSpeed(5f);
        builder.maxFlightSpeed(10f);
        builder.setExitMissionOnRCSignalLostEnabled(false);
        builder.finishedAction(WaypointMissionFinishedAction.GO_HOME);
        builder.flightPathMode(WaypointMissionFlightPathMode.NORMAL);
        builder.gotoFirstWaypointMode(WaypointMissionGotoWaypointMode.SAFELY);
        builder.headingMode(WaypointMissionHeadingMode.AUTO);
        builder.repeatTimes(1);
        Random randomGenerator = new Random(System.currentTimeMillis());
        List<Waypoint> waypointList = new ArrayList<>();
        for (int i = 0; i < numberOfWaypoint; i++) {
            final double variation = (Math.floor(i / 4) + 1) * 2 * ONE_METER_OFFSET;
            final float variationFloat = (baseAltitude + (i + 1) * 2);
            final Waypoint eachWaypoint = new Waypoint(baseLatitude + variation * Math.pow(-1, i) * Math.pow(0, i % 2),
                                                       baseLongitude + variation * Math.pow(-1, (i + 1)) * Math.pow(0, (i + 1) % 2),
                                                       variationFloat);
            for (int j = 0; j < numberOfAction; j++) {
                final int randomNumber = randomGenerator.nextInt() % 6;
                switch (randomNumber) {
                    case 0:
                        eachWaypoint.addAction(new WaypointAction(WaypointActionType.STAY, 1));
                        break;
                    case 1:
                        eachWaypoint.addAction(new WaypointAction(WaypointActionType.START_TAKE_PHOTO, 1));
                        break;
                    case 2:
                        eachWaypoint.addAction(new WaypointAction(WaypointActionType.START_RECORD, 1));
                        eachWaypoint.addAction(new WaypointAction(WaypointActionType.STOP_RECORD, 1));
                        break;
                    case 3:
                        eachWaypoint.addAction(new WaypointAction(WaypointActionType.GIMBAL_PITCH,
                                                                  randomGenerator.nextInt() % 45 - 45));
                        break;
                    case 4:
                        eachWaypoint.addAction(new WaypointAction(WaypointActionType.ROTATE_AIRCRAFT,
                                                                  randomGenerator.nextInt() % 180));
                        break;
                    default:
                        eachWaypoint.addAction(new WaypointAction(WaypointActionType.START_TAKE_PHOTO, 1));
                        break;
                }
            }
            waypointList.add(eachWaypoint);
        }
        builder.waypointList(waypointList).waypointCount(waypointList.size());
        return builder.build();
    }
    //endregion

    //region Not important stuff
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

    private void tearDownListener() {
        if (waypointMissionOperator != null && listener != null) {
            // Example of removing listeners
            waypointMissionOperator.removeListener(listener);
        }
    }

    private void showResultToast(DJIError djiError) {
        ToastUtils.setResultToToast(djiError == null ? "Action started!" : djiError.getDescription());
    }

    @Override
    public int getDescription() {
        return R.string.component_listview_waypoint_mission_operator;
    }

    //endregion

    private WaypointMission demo(){
        WaypointMission.Builder builder = new WaypointMission.Builder();
        double baseLatitude = 22;
        double baseLongitude = 113;
        Object latitudeValue = KeyManager.getInstance().getValue((FlightControllerKey.create(HOME_LOCATION_LATITUDE)));
        Object longitudeValue =
                KeyManager.getInstance().getValue((FlightControllerKey.create(HOME_LOCATION_LONGITUDE)));
        if (latitudeValue != null && latitudeValue instanceof Double) {
            baseLatitude = (double) latitudeValue;
        }
        if (longitudeValue != null && longitudeValue instanceof Double) {
            baseLongitude = (double) longitudeValue;
        }
        ToastUtils.setResultToToast(Double.toString(baseLatitude));
        Log.d("base",Double.toString(baseLatitude));
        Log.d("base",Double.toString(baseLongitude));
        ToastUtils.setResultToToast(Double.toString(baseLongitude));
        builder.autoFlightSpeed(5f);
        builder.maxFlightSpeed(10f);
        builder.setExitMissionOnRCSignalLostEnabled(false);
        builder.finishedAction(WaypointMissionFinishedAction.AUTO_LAND);
        builder.flightPathMode(WaypointMissionFlightPathMode.NORMAL);
        builder.gotoFirstWaypointMode(WaypointMissionGotoWaypointMode.SAFELY);
        builder.headingMode(WaypointMissionHeadingMode.AUTO);
        builder.repeatTimes(1);
        List<Waypoint> waypointList = new ArrayList<>();
        final Waypoint Waypoint1 = new Waypoint(21.006658,
                105.843065,
                5);
        Waypoint1.addAction(new WaypointAction(WaypointActionType.START_TAKE_PHOTO,1));
        waypointList.add(Waypoint1);

        final Waypoint Waypoint2 = new Waypoint(21.006716,
                105.842942,
                5);
        Waypoint2.addAction(new WaypointAction(WaypointActionType.START_TAKE_PHOTO,1));
        waypointList.add(Waypoint2);

        final Waypoint Waypoint3 = new Waypoint(21.006716,
                105.843173,
                5);
        Waypoint3.addAction(new WaypointAction(WaypointActionType.START_TAKE_PHOTO,1));
        waypointList.add(Waypoint3);

        final Waypoint Waypoint4 = new Waypoint(21.006774,
                105.843066,
                5);
        Waypoint4.addAction(new WaypointAction(WaypointActionType.START_TAKE_PHOTO,1));
        waypointList.add(Waypoint4);

        builder.waypointList(waypointList).waypointCount(waypointList.size());
        ToastUtils.setResultToToast(String.valueOf(waypointList.size()));
        return builder.build();
    }
}
