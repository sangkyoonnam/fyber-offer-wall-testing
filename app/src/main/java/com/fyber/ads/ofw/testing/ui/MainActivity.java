package com.fyber.ads.ofw.testing.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.fyber.ads.ofw.testing.R;
import com.fyber.ads.ofw.testing.model.Offer;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private Activity mActivity;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = this;

        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        initLayout();
    }

    @Override
    protected void onStart() {
        super.onStart();
        prepareToLoad();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Fragment fragment = getCurrentFragment();
        if (fragment != null
                && fragment.getTag() == FormFragment.TAG) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_load_defaults) {
            Fragment fragment = getCurrentFragment();
            if (fragment != null
                    && fragment.getTag() == FormFragment.TAG) {
                ((FormFragment) fragment).loadDefaults();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initLayout() {
        retrieveUiObjRefs();
        registerUiActionHandler();
    }

    public Fragment getCurrentFragment() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.view_content);
        return fragment;
    }

    public void retrieveUiObjRefs() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.view_content, new FormFragment(), FormFragment.TAG);
        ft.setTransition(FragmentTransaction.TRANSIT_NONE);
        ft.commit();
    }

    public void registerUiActionHandler() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                } else {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                }
                mActivity.invalidateOptionsMenu();
            }
        });
    }

    public void prepareToLoad() {
        // do nothing yet
    }

    public void showOfferList(ArrayList<Offer> offers) {
        replaceFragment(OfferListFragment.newInstance(offers), OfferListFragment.TAG);
    }

    private void replaceFragment(Fragment fragment, String tag){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.view_content, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(tag);
        ft.commit();
    }
}
