package com.example.android.miwok;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MiwokTabsPagerAdapter extends FragmentPagerAdapter {
    public MiwokTabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new NumbersFragment();
            case 1: return new ColorsFragment();
            case 2: return new FamilyFragment();
            case 3: return new PhrasesFragment();
            default: return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return "NUMBERS";
            case 1: return "COLORS";
            case 2: return "FAMILY";
            case 3: return "PHRASES";
            default: return null;
        }
    }
}
