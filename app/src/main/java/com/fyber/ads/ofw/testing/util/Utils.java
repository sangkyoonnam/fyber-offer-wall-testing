package com.fyber.ads.ofw.testing.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Utils {

    public static final String TAG = Utils.class.getSimpleName();

    /**
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        String deviceId = null;
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                deviceId = tm.getDeviceId();
            }
        } catch (Exception e) {
            Log.e(TAG, "error=" + e.getMessage());
        }
        return deviceId;
    }

    /**
     *
     * @param source
     * @return
     */
    public static String getSha1Hash(String source) {
        return getHash(source, "sha1");
    }

    /**
     *
     * @param source
     * @param hashType
     * @return
     */
    public static String getHash(String source, String hashType) {
        String hash = null;
        try {
            MessageDigest md = MessageDigest.getInstance(hashType);
            md.update(source.getBytes());
            byte byteData[] = md.digest();
            StringBuffer sb = new StringBuffer();
            for(int i = 0 ; i < byteData.length ; i++){
                sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            }
            hash = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "error=" + e.getMessage());
        }
        return hash;
    }
}
