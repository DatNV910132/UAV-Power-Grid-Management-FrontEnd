package com.dji.sdk.sample.internal.utils;

import android.graphics.Bitmap;
import dji.common.error.DJIError;
import dji.sdk.media.DownloadListener;

/**
 * The type Download handler.
 *
 * @param <B> the type parameter
 */
public class DownloadHandler<B> implements DownloadListener<B> {

    @Override
    public void onStart() {

    }

    @Override
    public void onRateUpdate(long total, long current, long arg2) {

    }

    @Override
    public void onProgress(long total, long current) {

    }

    @Override
    public void onSuccess(B obj) {
        if (obj instanceof Bitmap) {
            Bitmap bitmap = (Bitmap) obj;
            ToastUtils.setResultToToast("Success! The bitmap's byte count is: " + bitmap.getByteCount());
        } else if (obj instanceof String) {
            ToastUtils.setResultToToast("Lưu file thành công với đường dẫn: " + obj.toString());
        }
    }

    @Override
    public void onFailure(DJIError djiError) {

    }
}
