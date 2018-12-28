package com.example.android.funtravel.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.android.funtravel.R;

import butterknife.BindBool;
import butterknife.ButterKnife;


/*
 * This is the activity implementing the app's preference panel.
 *
 * This activity is accessed via the "Settings" option in the
 * navigation drawer menu.
 */
public class SettingsActivity extends AppCompatActivity {
    private static final String LOG_TAG = SettingsActivity.class.getSimpleName();

    private static final String EMPTY_STRING = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class PopularMoviesPreferenceFragment
            extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            PreferenceScreen prefScreen = getPreferenceScreen();
            int prefCount = prefScreen.getPreferenceCount();

            for (int i = 0; i < prefCount; i++) {
                Preference preference = prefScreen.getPreference(i);
                setPreferenceSummary(preference);
            }
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            preference.setSummary(newValue.toString());
            return true;
        }

        private void setPreferenceSummary(Preference preference) {
            if (!preferenceHasSummary(preference)) return;

            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), EMPTY_STRING);
            onPreferenceChange(preference, preferenceString);
        }

        private boolean preferenceHasSummary(Preference preference) {
            String prefKey = preference.getKey();

            // Preference categories don't have keys.
            if (prefKey == null) return false;

            // Only a subset of preferences have summaries.
            if (prefKey.equals(getString(R.string.settings_max_offer_count_key))) return true;
            if (prefKey.equals(getString(R.string.settings_widget_max_offer_count_key))) return true;

            return false;
        }
    }
}
