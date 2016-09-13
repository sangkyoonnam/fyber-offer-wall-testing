package com.fyber.ads.ofw.testing.core;

import android.content.Context;

import com.android.volley.RequestQueue;

public class SettingManager {

    private static PreferenceDelegate mPrefs;

    public static final String PREFS_STORE_NAME = "settings";

    public static final String PREFS_GOOGLE_AID= "google_aid";
    public static final String PREFS_GOOGLE_AID_IS_LAT = "google_aid_is_lat";

    public static final String PREFS_UID = "uid";
    public static final String PREFS_API_KEY = "api_key";
    public static final String PREFS_APPID = "appid";
    public static final String PREFS_PUB0 = "pub0";

    public static final String PREFS_DEFAULT_UID = "spiderman";
    public static final String PREFS_DEFAULT_API_KEY = "1c915e3b5d42d05136185030892fbb846c278927";
    public static final String PREFS_DEFAULT_APPID = "2070";
    public static final String PREFS_DEFAULT_PUB0 = "campaign2";

    public static void init(Context context) {
        if (mPrefs == null) {
            mPrefs = new PreferenceDelegate(context, PREFS_STORE_NAME);
        }
    }

    public static void setPrefsGoogleAid(String value) {
        mPrefs.putString(PREFS_GOOGLE_AID, value);
    }

    public static String getPrefsGoogleAid() {
        return mPrefs.getString(PREFS_GOOGLE_AID, null);
    }

    public static void setPrefsGoogleAidIsLat(boolean value) {
        mPrefs.putBoolean(PREFS_GOOGLE_AID_IS_LAT, value);
    }

    public static boolean getPrefsGoogleAidIsLat() {
        return mPrefs.getBoolean(PREFS_GOOGLE_AID_IS_LAT, false);
    }

    public static void setPrefsUid(String value) {
        mPrefs.putString(PREFS_UID, value);
    }

    public static String getPrefsUid() {
        return mPrefs.getString(PREFS_UID, PREFS_DEFAULT_UID);
    }

    public static void setPrefsApiKey(String value) {
        mPrefs.putString(PREFS_API_KEY, value);
    }

    public static String getPrefsApiKey() {
        return mPrefs.getString(PREFS_API_KEY, PREFS_DEFAULT_API_KEY);
    }

    public static void setPrefsAppid(String value) {
        mPrefs.putString(PREFS_APPID, value);
    }

    public static String getPrefsAppid() {
        return mPrefs.getString(PREFS_APPID, PREFS_DEFAULT_APPID);
    }

    public static void setPrefsPub0(String value) {
        mPrefs.putString(PREFS_PUB0, value);
    }

    public static String getPrefsPub0() {
        return mPrefs.getString(PREFS_PUB0, PREFS_DEFAULT_PUB0);
    }

    public static void clear() {
        mPrefs.clear();
    }

    /**
     *
     * @return
     */
    public static PreferenceDelegate getPreference() {
        if (mPrefs != null) {
            return mPrefs;
        } else {
            throw new IllegalStateException("Not initialized");
        }
    }
}
