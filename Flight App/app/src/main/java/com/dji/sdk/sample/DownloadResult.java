package com.dji.sdk.sample;

import android.graphics.Bitmap;
import android.os.Environment;
import android.widget.Toast;

import com.dji.sdk.sample.internal.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import dji.common.error.DJIError;
import dji.sdk.media.DownloadListener;

/**
 * The type Download result.
 *
 * @param <B> the type parameter
 */
public class DownloadResult<B> implements DownloadListener<B> {


    @Override
    public void onStart() {
        ToastUtils.setResultToToast("Bắt đầu download");

    }

    @Override
    public void onRateUpdate(long total, long current, long arg2) {

    }

    @Override
    public void onProgress(long total, long current) {

    }

    @Override
    public void onSuccess(B string) {



        if (string instanceof Bitmap) {
            Bitmap bitmap = (Bitmap) string;
            ToastUtils.setResultToToast("Success! The bitmap's byte count is: " + bitmap.getByteCount());
        } else if (string instanceof java.lang.String) {
            ToastUtils.setResultToToast("Lưu file thành công với đường dẫn: " + string.toString());
            String[] local = ((String) string).split("/");
            ToastUtils.setResultToToast(local[4]);
            if (string.toString().contains("DJI_Image")) {
                File file = new File((string.toString()));
                String[] files = file.list();
//                File upload = new File(string.toString()+files[files.length-1]);
                for(String i : files){
                    String in = string.toString() + i;
                    ToastUtils.setResultToToast(in);
                }
            }
        }
    }

    @Override
    public void onFailure(DJIError djiError) {

    }
}
