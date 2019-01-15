package com.example.android.funtravel.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.funtravel.R;


/**
 * Fragment implementation for pretty much all the screen content presented
 * in the {@link ListOffersActivity}.
 *
 * It comes in flavors (paid and free). This is the implementation for the "paid"
 * flavor. This setup allows us to have the same parent activity with fragments
 * that are linked dynamically based on the product flavor.
 */
public class ListOfferFragment extends Fragment {
    public ListOfferFragment() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_offers, container, false);
    }
}
