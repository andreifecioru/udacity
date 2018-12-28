package com.example.android.funtravel.ui;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.example.android.funtravel.R;
import com.example.android.funtravel.common.model.Offer;
import com.example.android.funtravel.model.ParcelableOffer;
import com.example.android.funtravel.repo.FunTravelRepository;
import com.example.android.funtravel.utils.OfferUtils;
import com.example.android.funtravel.utils.ui.DynamicHeightNetworkImageView;

/**
 * RecyclerView adapter for the {@link Offer} class. Fuels the grid-view
 * which displays the list of offers.
 */
public class OfferAdapter
        extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder> {
    private static final String LOG_TAG = OfferAdapter.class.getSimpleName();

    private final Context mContext;
    private final OnOfferClick mOnOfferClick;
    private final ListOffersViewModel mViewModel;
    private final LifecycleOwner mOwner;

    private long mOfferCount = 0;

    /** Produces an instance of the {@link OfferAdapter} class (constructor)
     *
     *
     * @param context An instance of {@link Context} (allows us to access app's resources)
     * @param owner An instance of {@link LifecycleOwner} (allows us to observe {@link LiveData} objects.
     * @param viewModel An instance of {@link ListOffersViewModel} (provides access to {@link FunTravelRepository}).
     * @param onOfferClick Implementation of the {@link OnOfferClick} interface
     *                      where you can specify custom logic for clicking a article item.
     */
    OfferAdapter(Context context,
                 LifecycleOwner owner,
                 ListOffersViewModel viewModel,
                 OnOfferClick onOfferClick) {
        mContext = context;
        mOwner = owner;
        mViewModel = viewModel;
        mOnOfferClick = onOfferClick;
    }

    /**
     * Forces an update of the adapter view.
     *
     * @param offerCount The number of offers to be displayed by this adapter.
     */
    public void notifyChanged(long offerCount) {
        Log.d(LOG_TAG, "Updating the offer adapter (" + offerCount + " items)");
        mOfferCount = offerCount;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for the article item.
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(R.layout.offer_grid_item, parent, false);

        // Return the view holder
        return new OfferViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull final OfferViewHolder holder, int position) {
        final int pos = position;
        // Get the article at position
        mViewModel.getOfferAtPosition(position).observe(mOwner, new Observer<ParcelableOffer>() {
            @Override
            public void onChanged(@Nullable ParcelableOffer offer) {
                // Bind data to the view-holder
                holder.bindOffer(offer, pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (int) mOfferCount;
    }

    // Implements the view-holder pattern.
    class OfferViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {


        @BindView(R.id.iv_thumbnail) DynamicHeightNetworkImageView mThumbImageView;
        @BindView(R.id.tv_offer_title) TextView mOfferTitleTextView;
        @BindView(R.id.iv_offer_type) ImageView mOfferTypeImageView;
        @BindView(R.id.tv_offer_price) TextView mOfferPriceTextView;
        @BindView(R.id.ll_rating_container) LinearLayout mRatingContainerLinearLayout;

        private Offer mOffer;
        private int mPosition;

        OfferViewHolder(@NonNull View root) {
            super(root);

            ButterKnife.bind(this, root);

            // Install on-click handler
            root.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnOfferClick.onOfferClick(mOffer, mPosition);
        }

        // Binding method.
        void bindOffer(Offer offer, int position) {
            // Sanity check: fast exit
            if (offer == null) return;

            mOffer = offer;
            mPosition = position;

            // Load the thumbnail image
            mThumbImageView.loadImage(mOffer.getPhotoUrl());
            mThumbImageView.setAspectRatio(mOffer.getAspectRatio());

            // Display the offer-type logo image
            mOfferTypeImageView.setImageResource(OfferUtils.getOfferTypeImageRes(mOffer));

            // Display the offer title
            mOfferTitleTextView.setText(mOffer.getTitle());

            // Display the avg. rating
            OfferUtils.showRatingStars(mRatingContainerLinearLayout, mOffer.getAvgRating());

            // Display the offer price
            mOfferPriceTextView.setText(mContext.getString(R.string.offer_price, String.valueOf(mOffer.getPrice())));
        }
    }

    /**
     * Protocol for providing custom logic for handling user taps on article items.
     */
    interface OnOfferClick {
        void onOfferClick(Offer offer, int position);
    }
}

