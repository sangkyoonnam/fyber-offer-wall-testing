package com.fyber.ads.ofw.testing.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fyber.ads.ofw.testing.MainApplication;
import com.fyber.ads.ofw.testing.R;
import com.fyber.ads.ofw.testing.model.Offer;

import java.util.ArrayList;
import java.util.List;

public class OfferListFragment extends Fragment {

    public static final String TAG = OfferListFragment.class.getSimpleName();

    protected MainApplication mApplication;
    protected Activity mParentActivity;

    protected ViewGroup mRootView;
    private RecyclerView mRecyclerView;
    private View mErrorView;

    private ArrayList<Offer> mOffers = new ArrayList();

    public static OfferListFragment newInstance(ArrayList<Offer> data) {
        OfferListFragment fragment = new OfferListFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("offers", data);
        fragment.setArguments(bundle);

        return fragment;
    }

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
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_offer_list, container, false);
        initLayout();

        return mRootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Offer> offers = getArguments().getParcelableArrayList("offers");
        mOffers.clear();
        mOffers.addAll(offers);
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
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.view_list);
        mRecyclerView.setHasFixedSize(true);
        LayoutManager layoutManager = new LinearLayoutManager(mParentActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(new OfferListAdapter());

        mErrorView = mRootView.findViewById(R.id.view_error);
        mErrorView.setVisibility(View.INVISIBLE);
    }

    public void registerUiActionHandler() {
    }

    public void prepareToLoad() {
        if (mOffers.size() == 0) {
            mRecyclerView.setVisibility(View.INVISIBLE);
            mErrorView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mErrorView.setVisibility(View.INVISIBLE);
        }
    }

    class OfferListAdapter extends RecyclerView.Adapter<OfferListAdapter.ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView mThumbnailImage;
            public TextView mTitleLabel;
            public TextView mTeaserLabel;
            public TextView mPayoutLabel;
            public ViewHolder(View view) {
                super(view);
                mThumbnailImage = (ImageView) view.findViewById(R.id.img_thumbnail);
                mTitleLabel = (TextView) view.findViewById(R.id.lbl_title);
                mTeaserLabel = (TextView) view.findViewById(R.id.lbl_teaser);
                mPayoutLabel = (TextView) view.findViewById(R.id.lbl_payout);
            }
        }

        @Override
        public OfferListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_offer_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Offer offer = mOffers.get(position);
            try {
                Glide.with(mParentActivity).load(offer.thumbnail.hires)
                        .into(holder.mThumbnailImage);
                holder.mTitleLabel.setText(offer.title);
                holder.mTeaserLabel.setText(offer.teaser);
                holder.mPayoutLabel.setText("Payout: " + offer.payout);
            } catch (Exception e) {
                Log.e(TAG, "error=" + e.getMessage());
            }
        }

        @Override
        public int getItemCount() {
            return mOffers.size();
        }
    }
}
