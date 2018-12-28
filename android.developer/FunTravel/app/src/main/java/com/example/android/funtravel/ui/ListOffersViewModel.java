package com.example.android.funtravel.ui;

import java.util.List;

import javax.inject.Inject;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;

import com.example.android.funtravel.model.ParcelableOffer;
import com.example.android.funtravel.repo.FunTravelRepository;
import com.example.android.funtravel.utils.Resource;


/**
 * A {@link ViewModel} implementation used by the {@link ListOffersActivity}.
 *
 * Provides access to the {@link FunTravelRepository} services.
 */
public class ListOffersViewModel extends ViewModel {
    private FunTravelRepository mRepository;

    public static ListOffersViewModel create (FragmentActivity activity) {
        return ViewModelProviders.of(activity).get(ListOffersViewModel.class);
    }

    @Inject
    public void setRepository(FunTravelRepository repository) {
        mRepository = repository;
    }

    LiveData<Resource<Long>> fetchOffers(int maxOfferCount) {
        return mRepository.fetchOffers(maxOfferCount);
    }

    LiveData<ParcelableOffer> getOfferAtPosition(long position) {
        return mRepository.getOfferAtPosition(position);
    }

//    LiveData<List<ParcelableOffer>> getBestOffersAsync(int count) {
//        return mRepository.getBestOffersAsync(count);
//    }

    void deleteAllOffers(Runnable onComplete) {
        mRepository.deleteAllOffers(onComplete);
    }
}
