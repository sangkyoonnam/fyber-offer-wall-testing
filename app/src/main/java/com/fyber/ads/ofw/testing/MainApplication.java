package com.fyber.ads.ofw.testing;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.fyber.ads.ofw.testing.core.SettingManager;
import com.fyber.ads.ofw.testing.network.MobileOfferManager;
import com.fyber.ads.ofw.testing.network.RequestManager;
import com.fyber.ads.ofw.testing.util.Utils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

public class MainApplication extends Application {

    public static final String TAG = MainApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    private void init() {
        SettingManager.init(this);
        RequestManager.init(this);

        new Thread(new GetGAID(this)).start();
    }

    private class GetGAID implements Runnable {
        private final WeakReference<Context> weakContext;
        private String googleAid;
        private boolean isLat = false;

        public GetGAID(Context context) {
            weakContext = new WeakReference<Context>(context);
        }

        public void run() {
            try {
                Class<?>[] adIdMethodParams = new Class[1];
                adIdMethodParams[0] = Context.class;

                Method adIdMethod = Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient").getDeclaredMethod("getAdvertisingIdInfo", Context.class);
                Object adInfo = adIdMethod.invoke(null, new Object[] { weakContext.get() });

                Method getIdMethod = Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient$Info").getDeclaredMethod("getId");
                googleAid = (String) getIdMethod.invoke(adInfo);

                Method getLATMethod = Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient$Info").getDeclaredMethod("isLimitAdTrackingEnabled");
                isLat = ((Boolean) getLATMethod.invoke(adInfo)).booleanValue();

                setGoogleAid(googleAid);
                setGoogleAdTrackingLimited(isLat);
            } catch (Exception e) {
                Log.e(TAG, "error=" + e.getMessage());
            }
        }
    }

    public void setGoogleAid(String googleAid) {
        SettingManager.setPrefsGoogleAid(googleAid);
    }

    public void setGoogleAdTrackingLimited(boolean isLat) {
        SettingManager.setPrefsGoogleAidIsLat(isLat);
    }
}
