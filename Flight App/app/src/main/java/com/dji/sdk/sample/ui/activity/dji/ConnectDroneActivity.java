package com.dji.sdk.sample.ui.activity.dji;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dji.sdk.sample.R;
import com.dji.sdk.sample.internal.controller.DJISampleApplication;
import com.dji.sdk.sample.internal.view.MainContent;

import java.util.concurrent.atomic.AtomicBoolean;

import dji.common.error.DJIError;
import dji.common.realname.AppActivationState;
import dji.common.useraccount.UserAccountState;
import dji.common.util.CommonCallbacks;
import dji.keysdk.DJIKey;
import dji.keysdk.KeyManager;
import dji.keysdk.ProductKey;
import dji.keysdk.callback.KeyListener;
import dji.sdk.base.BaseProduct;
import dji.sdk.products.Aircraft;
import dji.sdk.realname.AppActivationManager;
import dji.sdk.sdkmanager.DJISDKManager;
import dji.sdk.useraccount.UserAccountManager;

import static dji.midware.data.manager.P3.ServiceManager.getContext;

/**
 * The type Connect drone activity.
 */
public class ConnectDroneActivity extends AppCompatActivity {
    /**
     * The constant TAG.
     */
    public static final String TAG = MainContent.class.getName();
    private TextView mtv_name_aircraft;
    private TextView mtv_connection_status;
    private TextView mtv_product_info;
    private TextView mtv_model_available;
    private EditText medt_bridge_ip;
    private Button mbtn_open_fun;

    private Button btm_WPM;
    private Button btn_demo1;
    private Button btn_demo2;

    private TextView mtv_version;

    private Handler mHandler;
    private Handler mHandlerUI;
    private HandlerThread mHandlerThread = new HandlerThread("Bluetooth");
    private BaseProduct mProduct;
    private DJIKey firmwareKey;
    private KeyListener firmwareVersionUpdater;
    private boolean hasStartedFirmVersionListener = false;
    private AtomicBoolean hasAppActivationListenerStarted = new AtomicBoolean(false);
    private static final int MSG_INFORM_ACTIVATION = 1;
    private static final int ACTIVATION_DALAY_TIME = 1000;
    private AppActivationState.AppActivationStateListener appActivationStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_drone);
        DJISampleApplication.getEventBus().register(this);
        mtv_name_aircraft = findViewById(R.id.tv_name_aircraft);
        mtv_connection_status = findViewById(R.id.tv_connection_status);
        mtv_product_info = findViewById(R.id.tv_product_info);
        mtv_model_available = findViewById(R.id.tv_model_available);
        mbtn_open_fun = findViewById(R.id.btn_open_fun);
        medt_bridge_ip = findViewById(R.id.edt_bridge_ip);

        btm_WPM = findViewById(R.id.btn_wpm);
        btn_demo1 = findViewById(R.id.btn_demo_1);
        btn_demo2 = findViewById(R.id.btn_demo_2);

        btn_demo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), MediaViewActivity.class);
                startActivity(i);
            }
        });

        btn_demo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Kết nối với Drone thành công",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getContext(), ListFeaturesActivity.class);
                startActivity(i);
            }
        });

        btm_WPM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), WaypointMissionActivity.class);
                startActivity(i);
            }
        });

        mbtn_open_fun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Kết nối với Drone thành công",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getContext(), ListFeaturesActivity.class);
                startActivity(i);
            }
        });
        medt_bridge_ip.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event != null
                        && event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (event != null && event.isShiftPressed()) {
                        return false;
                    } else {
                        // the user is done typing.
                        handleBridgeIPTextChange();
                    }
                }
                return false; // pass on to other listeners.
            }
        });
        medt_bridge_ip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.toString().contains("\n")) {
                    // the user is done typing.
                    // remove new line characcter
                    final String currentText = medt_bridge_ip.getText().toString();
                    medt_bridge_ip.setText(currentText.substring(0, currentText.indexOf('\n')));
                    handleBridgeIPTextChange();
                }
            }
        });
        ((TextView) findViewById(R.id.tv_version)).setText(getResources().getString(R.string.sdk_version,
                DJISDKManager.getInstance()
                        .getSDKVersion()));
    }

    private void handleBridgeIPTextChange() {
        // the user is done typing.g
        final String bridgeIP = medt_bridge_ip.getText().toString();
        DJISDKManager.getInstance().enableBridgeModeWithBridgeAppIP(bridgeIP);
        if (!TextUtils.isEmpty(bridgeIP)) {
//            ToastUtils.setResultToToast("BridgeMode ON!\nIP: " + bridgeIP);
        }
    }

    @Override
    public void onAttachedToWindow() {
        Log.d(TAG, "Comes into the onAttachedToWindow");
        refreshSDKRelativeUI();

        mHandlerThread.start();
        final long currentTime = System.currentTimeMillis();
        mHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_INFORM_ACTIVATION:
                        loginToActivationIfNeeded();
                        break;
                }
            }
        };
        mHandlerUI = new Handler(Looper.getMainLooper());
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        removeFirmwareVersionListener();
        removeAppActivationListenerIfNeeded();
        mHandler.removeCallbacksAndMessages(null);
        mHandlerUI.removeCallbacksAndMessages(null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mHandlerThread.quitSafely();
        } else {
            mHandlerThread.quit();
        }
        mHandlerUI = null;
        mHandler = null;
        super.onDetachedFromWindow();
    }

    private void removeAppActivationListenerIfNeeded() {
        if (hasAppActivationListenerStarted.compareAndSet(true, false)) {
            AppActivationManager.getInstance().removeAppActivationStateListener(appActivationStateListener);
        }
    }

    private void loginToActivationIfNeeded() {
        if (AppActivationManager.getInstance().getAppActivationState() == AppActivationState.LOGIN_REQUIRED) {
            UserAccountManager.getInstance()
                    .logIntoDJIUserAccount(getContext(),
                            new CommonCallbacks.CompletionCallbackWith<UserAccountState>() {
                                @Override
                                public void onSuccess(UserAccountState userAccountState) {
//                                    ToastUtils.setResultToToast("Login Successed!");
                                }

                                @Override
                                public void onFailure(DJIError djiError) {
//                                    ToastUtils.setResultToToast("Login Failed!");
                                }
                            });
        }
    }

    private void refreshSDKRelativeUI() {
        mProduct = DJISampleApplication.getProductInstance();
        Log.d(TAG, "mProduct: " + (mProduct == null ? "null" : "unnull"));
        if (null != mProduct ) {
            if (mProduct.isConnected()) {
                mbtn_open_fun.setEnabled(true);
                String str = mProduct instanceof Aircraft ? "DJIAircraft" : "DJIHandHeld";
                mtv_connection_status.setText("Status: " + str + " connected");
                tryUpdateFirmwareVersionWithListener();
                if (mProduct instanceof Aircraft) {
                    addAppActivationListenerIfNeeded();
                }

                if (null != mProduct.getModel()) {
                    mtv_product_info.setText("" + mProduct.getModel().getDisplayName());
//                    mProduct.getName(new CommonCallbacks.CompletionCallbackWith<String>() {
//                        @Override
//                        public void onSuccess(String s) {
//                            mtv_name_aircraft.setText("Name: " + s);
////                        ToastUtils.setResultToToast("db load success ! version : " + s);
//                        }
//
//                        @Override
//                        public void onFailure(DJIError djiError) {
//                            Log.e("GetNameAircraft",djiError.toString());
////                        ToastUtils.setResultToToast("db load success ! get version error : " + djiError.getDescription());
//
//                        }
//                    });
                } else {
                    mtv_product_info.setText(R.string.product_information);
                }
            } else if (mProduct instanceof Aircraft){
                Aircraft aircraft = (Aircraft) mProduct;
                if (aircraft.getRemoteController() != null && aircraft.getRemoteController().isConnected()) {
                    mtv_connection_status.setText(R.string.connection_only_rc);
                    mtv_product_info.setText(R.string.product_information);
                    mbtn_open_fun.setEnabled(false);
                    mtv_model_available.setText("Firmware version:N/A");
                }
            }
        } else {
            mbtn_open_fun.setEnabled(false);
            mtv_product_info.setText(R.string.product_information);
            mtv_connection_status.setText(R.string.connection_loose);
            mtv_model_available.setText("Firmware version:N/A");
        }
    }

    private void addAppActivationListenerIfNeeded() {
        if (AppActivationManager.getInstance().getAppActivationState() != AppActivationState.ACTIVATED) {
            sendDelayMsg(MSG_INFORM_ACTIVATION, ACTIVATION_DALAY_TIME);
            if (hasAppActivationListenerStarted.compareAndSet(false, true)) {
                appActivationStateListener = new AppActivationState.AppActivationStateListener() {

                    @Override
                    public void onUpdate(AppActivationState appActivationState) {
                        if (mHandler != null && mHandler.hasMessages(MSG_INFORM_ACTIVATION)) {
                            mHandler.removeMessages(MSG_INFORM_ACTIVATION);
                        }
                        if (appActivationState != AppActivationState.ACTIVATED) {
                            sendDelayMsg(MSG_INFORM_ACTIVATION, ACTIVATION_DALAY_TIME);
                        }
                    }
                };
                AppActivationManager.getInstance().addAppActivationStateListener(appActivationStateListener);
            }
        }
    }

    private void sendDelayMsg(int msg, long delayMillis) {
        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(msg, delayMillis);
        }
    }

    private void tryUpdateFirmwareVersionWithListener() {
        if (!hasStartedFirmVersionListener) {
            firmwareVersionUpdater = new KeyListener() {
                @Override
                public void onValueChange(final Object o, final Object o1) {
                    mHandlerUI.post(new Runnable() {
                        @Override
                        public void run() {
                            updateVersion();
                        }
                    });
                }
            };
            firmwareKey = ProductKey.create(ProductKey.FIRMWARE_PACKAGE_VERSION);
            if (KeyManager.getInstance() != null) {
                KeyManager.getInstance().addListener(firmwareKey, firmwareVersionUpdater );
            }
            hasStartedFirmVersionListener = true;
        }
        updateVersion();
    }

    private void updateVersion() {
        String version = null;
        if (mProduct != null) {
            version = mProduct.getFirmwarePackageVersion();
        }

        if (TextUtils.isEmpty(version)) {
            mtv_model_available.setText("Firmware version:N/A"); //Firmware version:
        } else {
            mtv_model_available.setText("Firmware version:"+version); //"Firmware version: " +
            removeFirmwareVersionListener();
        }
    }

    private void removeFirmwareVersionListener() {
        if (hasStartedFirmVersionListener) {
            if (KeyManager.getInstance() != null) {
                KeyManager.getInstance().removeListener(firmwareVersionUpdater);
            }
        }
        hasStartedFirmVersionListener = false;
    }
}
