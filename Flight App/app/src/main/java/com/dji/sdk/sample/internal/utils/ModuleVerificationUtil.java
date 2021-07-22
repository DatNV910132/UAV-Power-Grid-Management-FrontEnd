package com.dji.sdk.sample.internal.utils;


import androidx.annotation.Nullable;

import com.dji.sdk.sample.internal.controller.DJISampleApplication;

import dji.common.product.Model;
import dji.sdk.accessory.AccessoryAggregation;
import dji.sdk.accessory.beacon.Beacon;
import dji.sdk.accessory.speaker.Speaker;
import dji.sdk.accessory.spotlight.Spotlight;
import dji.sdk.base.BaseProduct;
import dji.sdk.flightcontroller.FlightController;
import dji.sdk.flightcontroller.Simulator;
import dji.sdk.products.Aircraft;
import dji.sdk.products.HandHeld;

/**
 * Created by dji on 16/1/6.
 */
public class ModuleVerificationUtil {
    /**
     * Is product module available boolean.
     *
     * @return the boolean
     */
    public static boolean isProductModuleAvailable() {
        return (null != DJISampleApplication.getProductInstance());
    }

    /**
     * Is aircraft boolean.
     *
     * @return the boolean
     */
    public static boolean isAircraft() {
        return DJISampleApplication.getProductInstance() instanceof Aircraft;
    }

    /**
     * Is hand held boolean.
     *
     * @return the boolean
     */
    public static boolean isHandHeld() {
        return DJISampleApplication.getProductInstance() instanceof HandHeld;
    }

    /**
     * Is camera module available boolean.
     *
     * @return the boolean
     */
    public static boolean isCameraModuleAvailable() {
        return isProductModuleAvailable() && (null != DJISampleApplication.getProductInstance().getCamera());
    }

    /**
     * Is playback available boolean.
     *
     * @return the boolean
     */
    public static boolean isPlaybackAvailable() {
        return isCameraModuleAvailable() && (null != DJISampleApplication.getProductInstance()
                .getCamera()
                .getPlaybackManager());
    }

    /**
     * Is media manager available boolean.
     *
     * @return the boolean
     */
    public static boolean isMediaManagerAvailable() {
        return isCameraModuleAvailable() && (null != DJISampleApplication.getProductInstance()
                .getCamera()
                .getMediaManager());
    }

    /**
     * Is remote controller available boolean.
     *
     * @return the boolean
     */
    public static boolean isRemoteControllerAvailable() {
        return isProductModuleAvailable() && isAircraft() && (null != DJISampleApplication.getAircraftInstance()
                .getRemoteController());
    }

    /**
     * Is flight controller available boolean.
     *
     * @return the boolean
     */
    public static boolean isFlightControllerAvailable() {
        return isProductModuleAvailable() && isAircraft() && (null != DJISampleApplication.getAircraftInstance()
                .getFlightController());
    }

    /**
     * Is compass available boolean.
     *
     * @return the boolean
     */
    public static boolean isCompassAvailable() {
        return isFlightControllerAvailable() && isAircraft() && (null != DJISampleApplication.getAircraftInstance()
                .getFlightController()
                .getCompass());
    }

    /**
     * Is flight limitation available boolean.
     *
     * @return the boolean
     */
    public static boolean isFlightLimitationAvailable() {
        return isFlightControllerAvailable() && isAircraft();
    }

    /**
     * Is gimbal module available boolean.
     *
     * @return the boolean
     */
    public static boolean isGimbalModuleAvailable() {
        return isProductModuleAvailable() && (null != DJISampleApplication.getProductInstance().getGimbal());
    }

    /**
     * Is airlink available boolean.
     *
     * @return the boolean
     */
    public static boolean isAirlinkAvailable() {
        return isProductModuleAvailable() && (null != DJISampleApplication.getProductInstance().getAirLink());
    }

    /**
     * Is wi fi link available boolean.
     *
     * @return the boolean
     */
    public static boolean isWiFiLinkAvailable() {
        return isAirlinkAvailable() && (null != DJISampleApplication.getProductInstance().getAirLink().getWiFiLink());
    }

    /**
     * Is lightbridge link available boolean.
     *
     * @return the boolean
     */
    public static boolean isLightbridgeLinkAvailable() {
        return isAirlinkAvailable() && (null != DJISampleApplication.getProductInstance()
                .getAirLink()
                .getLightbridgeLink());
    }

    /**
     * Gets accessory aggregation.
     *
     * @return the accessory aggregation
     */
    public static AccessoryAggregation getAccessoryAggregation() {
        Aircraft aircraft = (Aircraft) DJISampleApplication.getProductInstance();

        if (aircraft != null && null != aircraft.getAccessoryAggregation()) {
            return aircraft.getAccessoryAggregation();
        }
        return null;
    }

    /**
     * Gets speaker.
     *
     * @return the speaker
     */
    public static Speaker getSpeaker() {
        Aircraft aircraft = (Aircraft) DJISampleApplication.getProductInstance();

        if (aircraft != null && null != aircraft.getAccessoryAggregation() && null != aircraft.getAccessoryAggregation().getSpeaker()) {
            return aircraft.getAccessoryAggregation().getSpeaker();
        }
        return null;
    }

    /**
     * Gets beacon.
     *
     * @return the beacon
     */
    public static Beacon getBeacon() {
        Aircraft aircraft = (Aircraft) DJISampleApplication.getProductInstance();

        if (aircraft != null && null != aircraft.getAccessoryAggregation() && null != aircraft.getAccessoryAggregation().getBeacon()) {
            return aircraft.getAccessoryAggregation().getBeacon();
        }
        return null;
    }

    /**
     * Gets spotlight.
     *
     * @return the spotlight
     */
    public static Spotlight getSpotlight() {
        Aircraft aircraft = (Aircraft) DJISampleApplication.getProductInstance();

        if (aircraft != null && null != aircraft.getAccessoryAggregation() && null != aircraft.getAccessoryAggregation().getSpotlight()) {
            return aircraft.getAccessoryAggregation().getSpotlight();
        }
        return null;
    }

    /**
     * Gets simulator.
     *
     * @return the simulator
     */
    @Nullable
    public static Simulator getSimulator() {
        Aircraft aircraft = DJISampleApplication.getAircraftInstance();
        if (aircraft != null) {
            FlightController flightController = aircraft.getFlightController();
            if (flightController != null) {
                return flightController.getSimulator();
            }
        }
        return null;
    }

    /**
     * Gets flight controller.
     *
     * @return the flight controller
     */
    @Nullable
    public static FlightController getFlightController() {
        Aircraft aircraft = DJISampleApplication.getAircraftInstance();
        if (aircraft != null) {
            return aircraft.getFlightController();
        }
        return null;
    }

    /**
     * Is mavic 2 product boolean.
     *
     * @return the boolean
     */
    @Nullable
    public static boolean isMavic2Product() {
        BaseProduct baseProduct = DJISampleApplication.getProductInstance();
        if (baseProduct != null) {
            return baseProduct.getModel() == Model.MAVIC_2_PRO || baseProduct.getModel() == Model.MAVIC_2_ZOOM;
        }
        return false;
    }

}
