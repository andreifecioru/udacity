package com.example.android.funtravel.ui.widget;

import javax.inject.Inject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.funtravel.FunTravelApp;
import com.example.android.funtravel.R;
import com.example.android.funtravel.common.model.Offer;
import com.example.android.funtravel.model.ParcelableOffer;
import com.example.android.funtravel.repo.FunTravelRepository;
import com.example.android.funtravel.utils.OfferUtils;
import com.example.android.funtravel.utils.PreferenceUtils;

import java.util.List;


public class FunTravelRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final String LOG_TAG = FunTravelRemoteViewsFactory.class.getSimpleName();

    private final Context mContext;

    private List<ParcelableOffer> mOffers;

    private FunTravelRepository mRepository;

    @Inject
    public void setRepository(FunTravelRepository repository) {
        mRepository = repository;
    }

    FunTravelRemoteViewsFactory(Context appContext, Intent intent) {
        Log.d(LOG_TAG, "Initializing dataset");

        mContext = appContext;
    }

    @Override
    public void onCreate() {
        FunTravelApp.getInstance().getAppComponent().inject(this);
    }

    @Override
    public void onDataSetChanged() {
        Log.d(LOG_TAG, "Dataset changed");
        mOffers = mRepository.getBestOffers(PreferenceUtils.getWidgetMaxOfferCountSetting(mContext));
    }

    @Override
    public void onDestroy() { }

    @Override
    public int getCount() {
        if (mOffers == null) return 0;
        return mOffers.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (mOffers == null || mOffers.isEmpty() || position >= mOffers.size()) {
            return null;
        }

        Offer offer = mOffers.get(position);

        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);

        // Display the offer-type logo image
        remoteViews.setImageViewResource(R.id.iv_offer_type, OfferUtils.getOfferTypeImageRes(offer));

        // Set the title
        remoteViews.setTextViewText(R.id.tv_offer_title, offer.getTitle());

        // Display the offer price
        remoteViews.setTextViewText(R.id.tv_offer_price,
                mContext.getString(R.string.offer_price, String.valueOf(offer.getPrice())));

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        if (mOffers == null || mOffers.isEmpty() || position >= mOffers.size()) {
            return position;
        }

        return mOffers.get(position).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
