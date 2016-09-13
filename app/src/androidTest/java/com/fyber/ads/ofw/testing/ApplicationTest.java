package com.fyber.ads.ofw.testing;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fyber.ads.ofw.testing.core.SettingManager;
import com.fyber.ads.ofw.testing.model.OfferData;
import com.fyber.ads.ofw.testing.network.MobileOfferManager;
import com.fyber.ads.ofw.testing.network.RequestManager;
import com.fyber.ads.ofw.testing.ui.MainActivity;

import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    public static final String TAG = ApplicationTest.class.getSimpleName();

    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        createApplication();
    }

    @SmallTest
    public void testRequestManagerCreation() throws Exception {
        RequestManager.init(getContext());
        assertNotNull(RequestManager.getRequestQueue());
    }

    @SmallTest
    public void testSettingManagerCreation() throws Exception {
        SettingManager.init(getContext());
        assertNotNull(SettingManager.getPreference());
    }

    @MediumTest
    public void testSendRequestSuccess() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        SettingManager.clear();
        String appid = SettingManager.PREFS_DEFAULT_APPID;
        String uid = SettingManager.PREFS_DEFAULT_UID;
        String pub0 = SettingManager.PREFS_DEFAULT_PUB0;
        String apiKey = SettingManager.PREFS_DEFAULT_API_KEY;

        MobileOfferManager.getInstance().getOffers(appid, uid, pub0, apiKey,
                new Response.Listener<OfferData>() {
                    @Override
                    public void onResponse(OfferData response) {
                        assertEquals("OK", response.code);
                        assertTrue(response.getOffers().size() > 0);
                        signal.countDown();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        assertNotNull(null);
                        signal.countDown();
                    }
                }
        );

        try {
            signal.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(0, signal.getCount());
    }

    @MediumTest
    public void testSendRequestInvalidSignature() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        SettingManager.clear();
        String appid = SettingManager.PREFS_DEFAULT_APPID;
        String uid = SettingManager.PREFS_DEFAULT_UID;
        String pub0 = SettingManager.PREFS_DEFAULT_PUB0;
        String apiKey = SettingManager.PREFS_DEFAULT_API_KEY + "1";

        MobileOfferManager.getInstance().getOffers(appid, uid, pub0, apiKey,
                new Response.Listener<OfferData>() {
                    @Override
                    public void onResponse(OfferData response) {
                        assertNotNull(null);
                        signal.countDown();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof AuthFailureError) {
                            assertNull(null);
                        } else {
                            assertNotNull(null);
                        }
                        signal.countDown();
                    }
                }
        );

        try {
            signal.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(0, signal.getCount());
    }

    @MediumTest
    public void testSendRequestFailedToLoad() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        SettingManager.clear();
        String appid = SettingManager.PREFS_DEFAULT_APPID + "1";
        String uid = SettingManager.PREFS_DEFAULT_UID;
        String pub0 = SettingManager.PREFS_DEFAULT_PUB0;
        String apiKey = SettingManager.PREFS_DEFAULT_API_KEY;

        MobileOfferManager.getInstance().getOffers(appid, uid, pub0, apiKey,
                new Response.Listener<OfferData>() {
                    @Override
                    public void onResponse(OfferData response) {
                        assertNull(null);
                        signal.countDown();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof AuthFailureError) {
                            assertNotNull(null);
                        } else {
                            assertNull(null);
                        }
                        signal.countDown();
                    }
                }
        );

        try {
            signal.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(0, signal.getCount());
    }
}