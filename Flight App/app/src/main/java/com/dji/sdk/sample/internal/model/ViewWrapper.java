package com.dji.sdk.sample.internal.model;

import android.view.View;

/**
 * The type View wrapper.
 */
public class ViewWrapper {

    private int titleId;
    private View view;

    /**
     * Instantiates a new View wrapper.
     *
     * @param layoutView the layout view
     * @param titleId    the title id
     */
    public ViewWrapper(View layoutView, int titleId) {
        view = layoutView;
        this.titleId = titleId;
    }

    /**
     * Gets title id.
     *
     * @return the title id
     */
    public int getTitleId() {
        return titleId;
    }

    /**
     * Gets view.
     *
     * @return the view
     */
    public View getView() {
        return view;
    }
}
