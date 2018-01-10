package com.example.android.miwok;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;


class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        if (viewPager != null) {
            PagerAdapter pagerAdapter = new MiwokTabsPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(pagerAdapter);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.pager_tabs);
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
