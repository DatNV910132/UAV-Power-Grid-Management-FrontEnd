package com.dji.sdk.sample.internal.view;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dji.sdk.sample.R;

/**
 * The type Base app activation view.
 */
public abstract class BaseAppActivationView extends FrameLayout implements PresentableView{

    /**
     * The Binding state tv.
     */
    protected TextView bindingStateTV;
    /**
     * The App activation state tv.
     */
    protected TextView appActivationStateTV;
    /**
     * The Adsb state tv.
     */
    protected TextView adsbStateTV;

    /**
     * Instantiates a new Base app activation view.
     *
     * @param context the context
     */
    public BaseAppActivationView(Context context) {
        super(context);
        init(context);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @NonNull
    @Override
    public String getHint() {
        return this.getClass().getSimpleName() + ".java";
    }

    private void init(Context context) {
        setClickable(true);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);

        layoutInflater.inflate(R.layout.view_app_activation, this, true);

        appActivationStateTV = (TextView) findViewById(R.id.tv_activation_state_info);
        bindingStateTV = (TextView) findViewById(R.id.tv_binding_state_info);
        adsbStateTV = (TextView) findViewById(R.id.tv_adsb_info);
    }

}
