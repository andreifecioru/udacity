package com.example.android.funtravel.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;

import com.example.android.funtravel.model.ParcelableOffer;
import com.example.android.funtravel.model.ParcelableReview;
import com.example.android.funtravel.repo.FunTravelRepository;
import com.example.android.funtravel.utils.Resource;

import javax.inject.Inject;


/**
 * A {@link ViewModel} implementation used by the {@link OfferDetailsActivity}.
 *
 * Provides access to the {@link FunTravelRepository} services.
 */
public class ListReviewsViewModel extends ViewModel {
    private FunTravelRepository mRepository;

    public static ListReviewsViewModel create (FragmentActivity activity) {
        return ViewModelProviders.of(activity).get(ListReviewsViewModel.class);
    }

    @Inject
    public void setRepository(FunTravelRepository repository) {
        mRepository = repository;
    }

    LiveData<Resource<Long>> fetchReviewsForOffer(long offerId) {
        return mRepository.getReviewsForOffer(offerId);
    }

    LiveData<ParcelableReview> getReviewAtPositionForOffer(long position, long offerId) {
        return mRepository.getReviewAtPositionForOffer(position, offerId);
    }

    public void deleteAllReviewsForOffer(long offerId, Runnable onComplete) {
        mRepository.deleteAllReviewsForOffer(offerId, onComplete);
    }
}
