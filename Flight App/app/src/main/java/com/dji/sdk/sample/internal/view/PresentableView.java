package com.dji.sdk.sample.internal.view;


import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

/**
 * Basic required interface of a DemoView
 */
public interface PresentableView {
    /**
     * Returns string id for the description of this View. This might be shown inside the View itself.
     *
     * @return the description
     */
    @StringRes
    int getDescription();

    /**
     * Return the hint to user on how to find this View in code
     *
     * @return hint
     */
    @NonNull
    String getHint();
}
