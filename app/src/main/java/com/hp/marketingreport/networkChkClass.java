package com.hp.marketingreport;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class networkChkClass {

    public networkChkClass() {
    }

    public static String getConnectionStatus(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info == null || !info.isConnected()) {
            return "NO_CONNECTIVITY";
        } else if (getInternetStatus(info.getType(), info.getSubtype(), context) == 3) {
            return "GOOD_STRENGTH";
        } else if (getInternetStatus(info.getType(), info.getSubtype(), context) >= 2) {
            return "FAIR_STRENGTH";
        } else {
            Toast.makeText(context, "Network strength is poor, Data may be not loaded on Realtime", Toast.LENGTH_LONG).show();
            return "POOR_STRENGTH";
        }
    }

    @SuppressLint("MissingPermission")
    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }


    public static int getInternetStatus(int type, int subType, Context context) {
        if (type == ConnectivityManager.TYPE_WIFI) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            int numberOfLevels = 5;
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
            if (level < 2) {
                return 2; //Fair
            } else {
                return 3; //Good
            }
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return 1; //Poor
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return 2; //Fair
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                case TelephonyManager.NETWORK_TYPE_LTE:
                    return 3; //Good
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    return 0; //No Connection
                default:
                    return 0;
            }
        } else {
            return 0;
        }
    }

    public static boolean chkInternetSpeed(Activity activity) {
        if (getConnectionStatus(activity).equals("NO_CONNECTIVITY")) {
            return false;
        } else {
            return true;
        }
    }
}
