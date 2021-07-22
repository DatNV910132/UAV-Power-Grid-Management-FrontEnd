package com.dji.sdk.sample.internal.utils;

import dji.common.error.DJIError;
import dji.common.util.CommonCallbacks;

/**
 * Created by dji on 15/12/18.
 */
public class GeneralUtils {
    /**
     * The constant ONE_METER_OFFSET.
     */
    public static final double ONE_METER_OFFSET = 0.00000899322;
    private static long lastClickTime;

    /**
     * Is fast double click boolean.
     *
     * @return the boolean
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * Check gps coordinate boolean.
     *
     * @param latitude  the latitude
     * @param longitude the longitude
     * @return the boolean
     */
    public static boolean checkGpsCoordinate(double latitude, double longitude) {
        return (latitude > -90 && latitude < 90 && longitude > -180 && longitude < 180) && (latitude != 0f
            && longitude != 0f);
    }

    /**
     * To radian double.
     *
     * @param x the x
     * @return the double
     */
    public static double toRadian(double x) {
        return x * Math.PI / 180.0;
    }

    /**
     * To degree double.
     *
     * @param x the x
     * @return the double
     */
    public static double toDegree(double x) {
        return x * 180 / Math.PI;
    }

    /**
     * Cos for degree double.
     *
     * @param degree the degree
     * @return the double
     */
    public static double cosForDegree(double degree) {
        return Math.cos(degree * Math.PI / 180.0f);
    }

    /**
     * Calc longitude offset double.
     *
     * @param latitude the latitude
     * @return the double
     */
    public static double calcLongitudeOffset(double latitude) {
        return ONE_METER_OFFSET / cosForDegree(latitude);
    }

    /**
     * Add line to sb.
     *
     * @param sb    the sb
     * @param name  the name
     * @param value the value
     */
    public static void addLineToSB(StringBuffer sb, String name, Object value) {
        if (sb == null) return;
        sb.
              append(name == null ? "" : name + ": ").
              append(value == null ? "" : value + "").
              append("\n");
    }

    /**
     * Gets common completion callback.
     *
     * @return the common completion callback
     */
    public static CommonCallbacks.CompletionCallback getCommonCompletionCallback() {
        return new CommonCallbacks.CompletionCallback() {
            @Override
            public void onResult(DJIError djiError) {
                ToastUtils.setResultToToast(djiError == null ? "Succeed!" : "failed!" + djiError.getDescription());
            }
        };
    }
}
