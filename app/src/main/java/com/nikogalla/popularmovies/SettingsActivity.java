package com.nikogalla.popularmovies;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;





/**
 * Created by Nicola on 2016-03-08.
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // Load the preferences from an XML resource
        getFragmentManager().beginTransaction()
                .add(android.R.id.content, new SettingsFragment())
                .commit();
    }


    public static class SettingsFragment extends PreferenceFragment {
        final String TAG = "SettingsFragment";
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
        }
    }

}
