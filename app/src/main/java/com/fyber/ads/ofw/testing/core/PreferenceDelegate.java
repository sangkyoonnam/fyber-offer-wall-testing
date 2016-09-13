package com.fyber.ads.ofw.testing.core;


import android.content.Context;
import android.content.SharedPreferences;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class PreferenceDelegate {

    private SharedPreferences mPrefs;

    public PreferenceDelegate(Context context, String name) {
        this.mPrefs = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    /**
     *
     * @param key
     * @param value
     */
    public synchronized void putString(String key, String value) {
        mPrefs.edit().putString(key, value).apply();
    }

    /**
     *
     * @param key
     * @param value
     */
    public synchronized void putInt(String key, int value) {
        mPrefs.edit().putInt(key, value).apply();
    }

    /**
     *
     * @param key
     * @param value
     */
    public synchronized void putLong(String key, long value) {
        mPrefs.edit().putLong(key, value).apply();
    }

    /**
     *
     * @param key
     * @param value
     */
    public synchronized void putFloat(String key, float value) {
        mPrefs.edit().putFloat(key, value).apply();
    }

    /**
     *
     * @param key
     * @param value
     */
    public synchronized void putStringSet(String key, Set<String> value) {
        mPrefs.edit().putStringSet(key, value).apply();
    }

    /**
     *
     * @param key
     * @param value
     */
    public synchronized void putBoolean(String key, boolean value) {
        mPrefs.edit().putBoolean(key, value).apply();
    }

    /**
     *
     * @param key
     * @return
     */
    public synchronized String getString(String key) {
        return getString(key, null);
    }

    /**
     *
     * @param key
     * @param defValue
     * @return
     */
    public synchronized String getString(String key, String defValue) {
        try {
            return mPrefs.getString(key, defValue);
        } catch (ClassCastException e) {
            return defValue;
        }
    }

    /**
     *
     * @param key
     * @return
     */
    public synchronized int getInt(String key) {
        return getInt(key, 0);
    }

    /**
     *
     * @param key
     * @param defValue
     * @return
     */
    public synchronized int getInt(String key, int defValue) {
        try {
            return mPrefs.getInt(key, defValue);
        } catch (ClassCastException e) {
            return defValue;
        }
    }

    /**
     *
     * @param key
     * @return
     */
    public synchronized long getLong(String key) {
        return getLong(key, 0);
    }

    /**
     *
     * @param key
     * @param defValue
     * @return
     */
    public synchronized long getLong(String key, int defValue) {
        try {
            return mPrefs.getLong(key, defValue);
        } catch (ClassCastException e) {
            return defValue;
        }
    }

    /**
     *
     * @param key
     * @return
     */
    public synchronized float getFloat(String key) {
        return getFloat(key, 0.f);
    }

    /**
     *
     * @param key
     * @param defValue
     * @return
     */
    public synchronized float getFloat(String key, float defValue) {
        try {
            return mPrefs.getFloat(key, defValue);
        } catch (ClassCastException e) {
            return defValue;
        }
    }

    /**
     *
     * @param key
     * @return
     */
    public synchronized boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     *
     * @param key
     * @param defValue
     * @return
     */
    public synchronized boolean getBoolean(String key, boolean defValue) {
        try {
            return mPrefs.getBoolean(key, defValue);
        } catch (ClassCastException e) {
            return defValue;
        }
    }

    /**
     *
     * @param key
     * @return
     */
    public Set<String> getStringSet(String key) {
        return getStringSet(key, Collections.<String>emptySet());
    }

    /**
     *
     * @param key
     * @param defValue
     * @return
     */
    public Set<String> getStringSet(String key, Set<String> defValue) {
        try {
            return mPrefs.getStringSet(key, defValue);
        } catch (ClassCastException e) {
            return defValue;
        }
    }

    public synchronized boolean contains(String key) {
        return mPrefs.contains(key);
    }

    public synchronized void remove(String key) {
        mPrefs.edit().remove(key).apply();
    }

    public synchronized void clear() {
        mPrefs.edit().clear().apply();
    }

    public synchronized Map<String, ?> getAll() {
        return mPrefs.getAll();
    }

}
