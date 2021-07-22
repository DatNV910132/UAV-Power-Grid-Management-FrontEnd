package com.dji.sdk.sample.internal.controller;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import dji.sdk.base.BaseProduct;
import dji.sdk.products.Aircraft;
import dji.sdk.products.HandHeld;
import dji.sdk.sdkmanager.DJISDKManager;

/**
 * Main application
 */
public class DJISampleApplication extends Application {

    /**
     * The constant TAG.
     */
    public static final String TAG = DJISampleApplication.class.getName();

    private static BaseProduct product;
    private static Bus bus = new Bus(ThreadEnforcer.ANY);
    private static Application app = null;

    /**
     * Gets instance of the specific product connected after the
     * API KEY is successfully validated. Please make sure the
     * API_KEY has been added in the Manifest
     *
     * @return the product instance
     */
    public static synchronized BaseProduct getProductInstance() {
        product = DJISDKManager.getInstance().getProduct();
        return product;
    }

    /**
     * Is aircraft connected boolean.
     *
     * @return the boolean
     */
    public static boolean isAircraftConnected() {
        return getProductInstance() != null && getProductInstance() instanceof Aircraft;
    }

    /**
     * Is hand held connected boolean.
     *
     * @return the boolean
     */
    public static boolean isHandHeldConnected() {
        return getProductInstance() != null && getProductInstance() instanceof HandHeld;
    }

    /**
     * Gets aircraft instance.
     *
     * @return the aircraft instance
     */
    public static synchronized Aircraft getAircraftInstance() {
        if (!isAircraftConnected()) {
            return null;
        }
        return (Aircraft) getProductInstance();
    }

    /**
     * Gets hand held instance.
     *
     * @return the hand held instance
     */
    public static synchronized HandHeld getHandHeldInstance() {
        if (!isHandHeldConnected()) {
            return null;
        }
        return (HandHeld) getProductInstance();
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static Application getInstance() {
        return DJISampleApplication.app;
    }

    /**
     * Gets event bus.
     *
     * @return the event bus
     */
    public static Bus getEventBus() {
        return bus;
    }

    @Override
    protected void attachBaseContext(Context paramContext) {
        super.attachBaseContext(paramContext);
        MultiDex.install(this);
        com.secneo.sdk.Helper.install(this);
        app = this;
    }
}