package com.dji.sdk.sample.ui.activity.dji;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dji.sdk.sample.R;
import com.dji.sdk.sample.adapter.DJIListFeaturesAdapter;
import com.dji.sdk.sample.demo.camera.MediaPlaybackView;
import com.dji.sdk.sample.internal.controller.DJISampleApplication;
import com.dji.sdk.sample.internal.utils.ToastUtils;
import com.dji.sdk.sample.ui.activity.StartAppActivity;

import java.util.ArrayList;
import java.util.List;

import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJIError;
import dji.common.util.CommonCallbacks;
import dji.sdk.base.BaseProduct;
import dji.sdk.media.MediaFile;
import dji.sdk.media.MediaManager;

import static dji.midware.data.manager.P3.ServiceManager.getContext;

/**
 * The type List features activity.
 */
public class ListFeaturesActivity extends AppCompatActivity implements MediaManager.VideoPlaybackStateListener {

    private static final String TAG = ListFeaturesActivity.class.getName();
    private MediaManager mediaManager;
    private List<MediaFile> DJIMediaList = new ArrayList<>();
    private final int SHOW_TOAST = 1;
    private final int SHOW_PROGRESS_DIALOG = 2;
    private final int HIDE_PROGRESS_DIALOG = 3;
    private final int GET_THUMBNAILS = 8;
    private final int FETCH_FILE_LIST = 6;
    private RecyclerView rv_list_features;
    private Button mbtn_lf_gohome;
    /**
     * The List features.
     */
    List<String> list_features;
    /**
     * The Dji list features adapter.
     */
    DJIListFeaturesAdapter djiListFeaturesAdapter;
    private boolean isDialogAllowable = false;
    private ProgressDialog dialog;

    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_TOAST:
                    ToastUtils.setResultToToast((String) msg.obj);
                    break;
                case SHOW_PROGRESS_DIALOG:
                    showProgressDialog();
                    break;
                case HIDE_PROGRESS_DIALOG:
                    hideProgressDialog();
                    break;
                case FETCH_FILE_LIST:
                    getFileList();
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    private void showProgressDialog() {
        if (dialog != null && isDialogAllowable) {
            dialog.show();
        }
    }

    private void hideProgressDialog() {
        if (null != dialog && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void getFileList() {
        DJIMediaList.clear();
        mediaManager = DJISampleApplication.getProductInstance().getCamera().getMediaManager();
        if (mediaManager != null) {

            mediaManager.refreshFileListOfStorageLocation(SettingsDefinitions.StorageLocation.SDCARD, new CommonCallbacks.CompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    if (djiError == null) {

                        List<MediaFile> medias = mediaManager.getSDCardFileListSnapshot();
                        Log.d(TAG, "fetchMediaList onSuccess ");
                        if (DJIMediaList != null) {
                            DJIMediaList.clear();
                        }
                        for (MediaFile media : medias) {
                            DJIMediaList.add(media);
                        }
                        ToastUtils.setResultToToast(String.valueOf(DJIMediaList.size()));
                        handler.removeMessages(GET_THUMBNAILS);
                    } else {
                        handler.sendMessage(handler.obtainMessage(SHOW_TOAST, djiError.getDescription()));
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_features);
        initDJIMedia();
        rv_list_features = findViewById(R.id.rv_list_features);
        mbtn_lf_gohome = findViewById(R.id.btn_lf_backhome);
        createProgressDialog();
        list_features = new ArrayList<>();
        list_features.add("LiveStream từ Drone");
        list_features.add("Thiết lập hành trình bay");
        list_features.add("Điều khiển bay");
        list_features.add("Lấy dữ liệu thủ công");
        list_features.add("Lấy dữ liệu tự động");
        djiListFeaturesAdapter = new DJIListFeaturesAdapter(list_features);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rv_list_features.setLayoutManager(layoutManager);
        rv_list_features.setAdapter(djiListFeaturesAdapter);

        mbtn_lf_gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListFeaturesActivity.this, StartAppActivity.class);
                startActivity(i);
            }
        });
    }

    private boolean initDJIMedia() {
        BaseProduct product;
        try {
            product = DJISampleApplication.getProductInstance();
        } catch (Exception exception) {
            product = null;
        }
        if (product == null) {
            DJIMediaList.clear();
            ToastUtils.setResultToToast(getContext().getResources().getString(R.string.playback_disconnected));
            return false;
        } else {
            if (null != DJISampleApplication.getProductInstance().getCamera()
                    && DJISampleApplication.getProductInstance().getCamera().isMediaDownloadModeSupported()) {
                mediaManager = DJISampleApplication.getProductInstance().getCamera().getMediaManager();

                if (null != mediaManager) {
                    if (mediaManager.isVideoPlaybackSupported()) {
                        mediaManager.addMediaUpdatedVideoPlaybackStateListener(this);
                    }
                }

                SettingsDefinitions.CameraMode mode = SettingsDefinitions.CameraMode.MEDIA_DOWNLOAD;

                Log.e(TAG, "Media Test set Camera Mode " + mode);
                DJISampleApplication.getProductInstance()
                        .getCamera()
                        .setMode(mode, new CommonCallbacks.CompletionCallback() {
                            @Override
                            public void onResult(DJIError error) {

                                if (error == null) {
                                    Log.e(TAG, "Media Test set Camera Mode success");
                                    handler.sendMessage(handler.obtainMessage(SHOW_PROGRESS_DIALOG, null));
                                    handler.sendMessageDelayed(handler.obtainMessage(FETCH_FILE_LIST, null),
                                            2000);
                                } else {
                                    Log.e(TAG, "Media Test set Camera Mode failure");
                                    handler.sendMessage(handler.obtainMessage(SHOW_TOAST,
                                            error.getDescription()));
                                }
                            }
                        });
            } else if (null != DJISampleApplication.getProductInstance().getCamera()
                    && !DJISampleApplication.getProductInstance().getCamera().isMediaDownloadModeSupported()) {
                ToastUtils.setResultToToast("Do not support Media list function");
                return false;
            } else {
                ToastUtils.setResultToToast(getContext().getString(R.string.playback_disconnected));
                return false;
            }
        }
        return true;
    }

    private static void addLineToSB(StringBuilder sb, String name, Object value) {
        sb.append(name + ": ").
                append(value == null ? "" : value + "").
                append("\n");
    }

    private void createProgressDialog() {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage(getContext().getString(R.string.Message_Waiting));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        ProgressDialog downloadDialog = new ProgressDialog(getContext());
        downloadDialog.setTitle(R.string.sync_file_title);
        downloadDialog.setIcon(android.R.drawable.ic_dialog_info);
        downloadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        downloadDialog.setCanceledOnTouchOutside(false);
        downloadDialog.setCancelable(false);
    }

    @Override
    public void onUpdate(MediaManager.VideoPlaybackState videoPlaybackState) {
        updateTextView(videoPlaybackState);
    }

    private void updateTextView(MediaManager.VideoPlaybackState currentVideoPlaybackState) {
        final StringBuilder pushInfo = new StringBuilder();

        addLineToSB(pushInfo, "Video Playback State", null);
        if (currentVideoPlaybackState != null) {
            if (currentVideoPlaybackState.getPlayingMediaFile() != null) {
                addLineToSB(pushInfo, "media index", currentVideoPlaybackState.getPlayingMediaFile().getIndex());
                addLineToSB(pushInfo, "media size", currentVideoPlaybackState.getPlayingMediaFile().getFileSize());
                addLineToSB(pushInfo,
                        "media duration",
                        currentVideoPlaybackState.getPlayingMediaFile().getDurationInSeconds());
                addLineToSB(pushInfo,
                        "media created date",
                        currentVideoPlaybackState.getPlayingMediaFile().getDateCreated());
                addLineToSB(pushInfo,
                        "media orientation",
                        currentVideoPlaybackState.getPlayingMediaFile().getVideoOrientation());
            } else {
                addLineToSB(pushInfo, "media index", "None");
            }
            addLineToSB(pushInfo, "media current position", currentVideoPlaybackState.getPlayingPosition());
            addLineToSB(pushInfo, "media current status", currentVideoPlaybackState.getPlaybackStatus());
            pushInfo.append("\n");
            Log.d("GET", pushInfo.toString());
        } else {
            Log.d("GET", "playbackState is null");
        }
    }
}
