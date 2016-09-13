package com.fyber.ads.ofw.testing.network;

import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import com.android.volley.toolbox.HttpHeaderParser;
import com.bumptech.glide.util.Util;
import com.fyber.ads.ofw.testing.core.SettingManager;
import com.fyber.ads.ofw.testing.model.OfferData;
import com.fyber.ads.ofw.testing.util.Utils;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class MobileOfferManager {

    private static final String TAG = MobileOfferManager.class.getSimpleName();

    private static final String FYBER_MOBILE_OFFER_API = "http://api.fyber.com/feed/v1/offers.json";
    private static final String SIGNATURE_HEADER = "X-Sponsorpay-Response-Signature";

    private static MobileOfferManager mInstance;

    private static String PARAM_APPID = "appid";
    private static String PARAM_UID = "uid";
    private static String PARAM_LOCALE = "locale";
    private static String PARAM_OS_VERSION = "os_version";
    private static String PARAM_TIMESTAMP = "timestamp";
    private static String PARAM_HASHKEY = "hashkey";
    private static String PARAM_GOOGLE_AID = "google_ad_id";
    private static String PARAM_GOOGLE_AID_IS_LAT = "google_ad_id_limited_tracking_enabled";
    private static String PARAM_DEVICE_ID = "device_id";
    private static String PARAM_IP = "ip";
    private static String PARAM_PUB0 = "pub0";
    private static String PARAM_OFFER_TYPES = "offer_types";

    private static String FAKE_LOCALE = "de";
    private static String FAKE_IP = "109.235.143.113";
    private static String FAKE_OFFER_TYPES = "112";

    public static MobileOfferManager getInstance() {
        if (mInstance == null) {
            mInstance = new MobileOfferManager();
        }
        return mInstance;
    }

    public void getOffers(String appid, String uid, String pub0, String apiKey, Listener<OfferData> listener, ErrorListener errorListener) {
        Map<String, String> queryParams = new LinkedHashMap();
        queryParams.put(PARAM_APPID, appid);
        queryParams.put(PARAM_UID, uid);
        queryParams.put(PARAM_LOCALE, FAKE_LOCALE);
        queryParams.put(PARAM_OS_VERSION, android.os.Build.VERSION.RELEASE);
        queryParams.put(PARAM_TIMESTAMP, "" + getTimestamp());
        queryParams.put(PARAM_GOOGLE_AID, SettingManager.getPrefsGoogleAid());
        queryParams.put(PARAM_GOOGLE_AID_IS_LAT, Boolean.toString(SettingManager.getPrefsGoogleAidIsLat()));
        queryParams.put(PARAM_DEVICE_ID, SettingManager.getPrefsGoogleAid());
        queryParams.put(PARAM_IP, FAKE_IP);
        queryParams.put(PARAM_PUB0, pub0);
        queryParams.put(PARAM_OFFER_TYPES, FAKE_OFFER_TYPES);

        getOffers(queryParams, apiKey, listener, errorListener);
    }

    public void getOffers(Map<String, String> queryParams, final String apiKey, Listener<OfferData> listener, ErrorListener errorListener) {
        Uri.Builder uriBuilder =  Uri.parse(FYBER_MOBILE_OFFER_API).buildUpon();
        for (String key : queryParams.keySet()) {
            uriBuilder.appendQueryParameter(key, queryParams.get(key));
        }
        uriBuilder.appendQueryParameter(PARAM_HASHKEY, generateHashkey(queryParams, apiKey));

        String uri = uriBuilder.build().toString();
        Log.i(TAG, "send mobile offer uri=" + uri);
        GsonRequest<OfferData> request = new GsonRequest(Method.GET,uri, OfferData.class, null,
                listener, errorListener) {
            @Override
            protected Response<OfferData> parseNetworkResponse(NetworkResponse response) {
                String signature = response.headers.get(SIGNATURE_HEADER);
                try {
                    String body = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    if (signature.equals(Utils.getSha1Hash(body.concat(apiKey)))) {
                        return super.parseNetworkResponse(response);
                    }
                } catch (UnsupportedEncodingException e) {}

                return Response.error(new AuthFailureError());
            }
        };;
        RequestManager.getRequestQueue().add(request);
    }

    /**
     *
     * @return
     */
    public long getTimestamp() {
        long unixTime = System.currentTimeMillis() / 1000L;
        return unixTime;
    }

    /**
     *
     * @param params
     * @param apiKey
     * @return
     */
    public String generateHashkey(Map<String, String> params, String apiKey) {
        Map<String, String> sortedParams = new TreeMap(params);
        String query = "";
        for (String key : sortedParams.keySet()) {
            if (query.length() != 0) {
                query += "&";
            }
            query += key + "=" + sortedParams.get(key);
        }
        query += "&" + apiKey;
        return Utils.getHash(query, "sha1");
    }
}
