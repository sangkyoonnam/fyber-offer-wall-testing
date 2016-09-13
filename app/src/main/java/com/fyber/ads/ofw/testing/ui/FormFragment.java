package com.fyber.ads.ofw.testing.ui;


import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fyber.ads.ofw.testing.MainApplication;
import com.fyber.ads.ofw.testing.R;
import com.fyber.ads.ofw.testing.core.SettingManager;
import com.fyber.ads.ofw.testing.model.OfferData;
import com.fyber.ads.ofw.testing.network.MobileOfferManager;

public class FormFragment extends Fragment {

    public static final String TAG = FormFragment.class.getSimpleName();

    protected MainApplication mApplication;
    protected Activity mParentActivity;

    protected ViewGroup mRootView;

    private TextInputLayout mUidLayout;
    private EditText mUidText;
    private TextInputLayout mApiKeyLayout;
    private EditText mApiKeyText;
    private TextInputLayout mAppidLayout;
    private EditText mAppidText;
    private TextInputLayout mPub0Layout;
    private EditText mPub0Text;

    private Button mSubmitButton;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);

        mParentActivity = getActivity();
        mApplication = (MainApplication) mParentActivity.getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_form, container, false);
        initLayout();

        return mRootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        prepareToLoad();
    }

    public void initLayout() {
        retrieveUiObjRefs();
        registerUiActionHandler();
    }

    public void retrieveUiObjRefs() {
        mUidLayout = (TextInputLayout) mRootView.findViewById(R.id.lay_uid);
        mUidText = (EditText) mRootView.findViewById(R.id.txt_uid);
        mUidText.addTextChangedListener(new FormTextWatcher(mUidText));

        mApiKeyLayout = (TextInputLayout) mRootView.findViewById(R.id.lay_api_key);
        mApiKeyText = (EditText) mRootView.findViewById(R.id.txt_api_key);
        mApiKeyText.addTextChangedListener(new FormTextWatcher(mApiKeyText));

        mAppidLayout = (TextInputLayout) mRootView.findViewById(R.id.lay_appid);
        mAppidText = (EditText) mRootView.findViewById(R.id.txt_appid);
        mAppidText.addTextChangedListener(new FormTextWatcher(mAppidText));

        mPub0Layout = (TextInputLayout) mRootView.findViewById(R.id.lay_pub0);
        mPub0Text = (EditText) mRootView.findViewById(R.id.txt_pub0);
//        mPub0Text.addTextChangedListener(new FormTextWatcher(mPub0Text));

        mSubmitButton = (Button) mRootView.findViewById(R.id.btn_submit);
    }

    public void registerUiActionHandler() {
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
    }

    public void prepareToLoad() {
        setStoredSettings();
    }

    public void setStoredSettings() {
        mUidText.setText(SettingManager.getPrefsUid());
        mApiKeyText.setText(SettingManager.getPrefsApiKey());
        mAppidText.setText(SettingManager.getPrefsAppid());
        mPub0Text.setText(SettingManager.getPrefsPub0());
    }

    public void saveSettings() {
        SettingManager.setPrefsUid(mUidText.getText().toString());
        SettingManager.setPrefsApiKey(mApiKeyText.getText().toString());
        SettingManager.setPrefsAppid(mAppidText.getText().toString());
        SettingManager.setPrefsPub0(mPub0Text.getText().toString());
    }

    public void loadDefaults() {
        SettingManager.clear();
        setStoredSettings();
    }

    public void submitForm() {
        if (!validateUid()) {
            return;
        }
        if (!validateApiKey()) {
            return;
        }
        if (!validateAppid()) {
            return;
        }
        // pub0 is not mandatory
//        if (!validatePub0()) {
//            return;
//        }

        sendRequest();
    }

    public void sendRequest() {
        String uid = mUidText.getText().toString();
        String apiKey = mApiKeyText.getText().toString();
        String appid = mAppidText.getText().toString();
        String pub0 = mPub0Text.getText().toString();

        final ProgressDialog dialog = ProgressDialog.show(mParentActivity, null, getString(R.string.lbl_progress), true, true);
        MobileOfferManager.getInstance().getOffers(appid, uid, pub0, apiKey,
                new Response.Listener<OfferData>() {
                    @Override
                    public void onResponse(OfferData response) {
                        dialog.dismiss();

                        if (response.code.equals("OK")) {
                            saveSettings();
                            ((MainActivity) mParentActivity).showOfferList(response.getOffers());
                        } else {
                            if (!TextUtils.isEmpty(response.message)) {
                                Toast.makeText(mParentActivity, response.message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();

                        if (error instanceof AuthFailureError) {
                            Toast.makeText(mParentActivity, getString(R.string.err_msg_invalid_signature),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mParentActivity, getString(R.string.err_msg_failed_to_load),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    private boolean validateUid() {
        if (mUidText.getText().toString().trim().isEmpty()) {
            mUidLayout.setError(getString(R.string.err_msg_uid));
            requestFocus(mUidText);
            return false;
        } else {
            mUidLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateApiKey() {
        if (mApiKeyText.getText().toString().trim().isEmpty()) {
            mApiKeyLayout.setError(getString(R.string.err_msg_api_key));
            requestFocus(mApiKeyText);
            return false;
        } else {
            mApiKeyLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateAppid() {
        if (mAppidText.getText().toString().trim().isEmpty()) {
            mAppidLayout.setError(getString(R.string.err_msg_appid));
            requestFocus(mAppidText);
            return false;
        } else {
            mAppidLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePub0() {
        if (mPub0Text.getText().toString().trim().isEmpty()) {
            mPub0Layout.setError(getString(R.string.err_msg_pub0));
            requestFocus(mPub0Text);
            return false;
        } else {
            mPub0Layout.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            mParentActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class FormTextWatcher implements TextWatcher {

        private View mView;

        private FormTextWatcher(View view) {
            this.mView = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (mView.getId()) {
                case R.id.txt_uid:
                    validateUid();
                    break;
                case R.id.txt_api_key:
                    validateApiKey();
                    break;
                case R.id.txt_appid:
                    validateAppid();
                    break;
//                case R.id.txt_pub0:
//                    validatePub0();
//                    break;
            }
        }
    }
}
