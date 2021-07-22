package com.dji.sdk.sample.internal.view;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dji.sdk.sample.R;

/**
 * Created by dji on 15/12/28.
 */
public abstract class BasePushDataView extends RelativeLayout implements PresentableView {

    /**
     * The String buffer.
     */
    protected StringBuffer stringBuffer;

    /**
     * The Text view osd.
     */
    protected TextView textViewOSD;

    /**
     * Instantiates a new Base push data view.
     *
     * @param context the context
     */
    public BasePushDataView(Context context) {
        super(context);
        init(context);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        stringBuffer = new StringBuffer();
    }

    @NonNull
    @Override
    public String getHint() {
        return this.getClass().getSimpleName() + ".java";
    }

    private void init(Context context) {
        setClickable(true);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);

        layoutInflater.inflate(R.layout.view_show_osd, this, true);

        textViewOSD = (TextView) findViewById(R.id.text_show_osd);

        textViewOSD.setText(context.getString(getDescription()));
    }

    /**
     * Show string buffer result.
     */
    protected void showStringBufferResult() {
        post(new Runnable() {
            @Override
            public void run() {
                textViewOSD.setText(stringBuffer.toString());
            }
        });
    }
}
