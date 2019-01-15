package com.example.android.funtravel.ui;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.fragment.app.FragmentActivity;

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

    LiveData<Resource<Long>> fetchOffers(int maxOfferCount, int flags) {
        return mRepository.fetchOffers(maxOfferCount, flags);
    }

    LiveData<ParcelableOffer> getOfferAtPosition(long position) {
        return mRepository.getOfferAtPosition(position);
    }

    void deleteAllOffers(Runnable onComplete) {
        mRepository.deleteAllOffers(onComplete);
    }
}
