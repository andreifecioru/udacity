package com.example.android.funtravel.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.example.android.funtravel.R;
import com.example.android.funtravel.common.model.Offer;
import com.example.android.funtravel.model.ParcelableOffer;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.funtravel.ui.OfferOverviewFragment.OFFER_ARGS_KEY;
import static com.example.android.funtravel.ui.OfferReviewsFragment.OFFER_ID_ARGS_KEY;


public class OfferDetailsActivity extends BaseActivity {
    private static final String LOG_TAG = OfferDetailsActivity.class.getSimpleName();

    @Nullable @BindView(R.id.toolbar) Toolbar mToolbar;
    @Nullable @BindView(R.id.viewpager) ViewPager mViewPager;
    @Nullable @BindView(R.id.tabs) TabLayout mTabsLayout;

    @BindBool(R.bool.tablet_mode) boolean isTabletModeOn;

    private Offer mOffer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(OFFER_ARGS_KEY)) {
                mOffer = intent.getParcelableExtra(OFFER_ARGS_KEY);
            }
        }

        if (mOffer == null) {
            Log.w(LOG_TAG, "No offer passed as intent extra. Exiting...");
            finish();
        }

        setSupportActionBar(mToolbar);
        // Setup the action-bar and the home button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        if (isTabletModeOn) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                .add(R.id.fl_offer_overview_container, createOfferOverviewFragment())
                .add(R.id.fl_offer_reviews_container, createOfferReviewsFragment())
                .commit();
        } else {
            setupViewPager();
        }
    }

    private void setupViewPager() {
        if (mViewPager == null || mTabsLayout == null) return; // sanity check - fast exit

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(createOfferOverviewFragment(), getString(R.string.tab_title_overview));
        adapter.addFragment(createOfferReviewsFragment(), getString(R.string.tab_title_reviews));
        mViewPager.setAdapter(adapter);
        mTabsLayout.setupWithViewPager(mViewPager);
    }

    private Fragment createOfferOverviewFragment() {
        OfferOverviewFragment fragment = new OfferOverviewFragment();

        Bundle args = new Bundle();
        args.putParcelable(OFFER_ARGS_KEY, (ParcelableOffer) mOffer);
        fragment.setArguments(args);

        return fragment;
    }

    private Fragment createOfferReviewsFragment() {
        OfferReviewsFragment fragment = new OfferReviewsFragment();

        Bundle args = new Bundle();
        args.putLong(OFFER_ID_ARGS_KEY, mOffer.getId());
        fragment.setArguments(args);

        return fragment;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
