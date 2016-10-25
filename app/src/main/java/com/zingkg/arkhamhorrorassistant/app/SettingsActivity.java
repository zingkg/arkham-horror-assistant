package com.zingkg.arkhamhorrorassistant.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.widget.Toolbar;

/**
 * This activity governs the settings. These settings will be read from the XML file and generated
 * accordingly.
 */
public class SettingsActivity
    extends AppCompatActivity
    implements PreferenceFragmentCompat.OnPreferenceStartScreenCallback {
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (savedInstanceState == null) {
            // Create the fragment only when the activity is created for the first time.
            // ie. not after orientation changes
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(
                SettingsFragment.FRAGMENT_TAG
            );
            if (fragment == null) {
                fragment = new SettingsFragment();
            }

            getSupportFragmentManager().beginTransaction().replace(
                R.id.frame_settings,
                fragment,
                SettingsFragment.FRAGMENT_TAG
            ).commit();
        }
    }

    @Override
    public boolean onPreferenceStartScreen(
        PreferenceFragmentCompat preferenceFragmentCompat,
        PreferenceScreen preferenceScreen
    ) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT, preferenceScreen.getKey());
        fragment.setArguments(args);
        ft.replace(R.id.frame_settings, fragment, preferenceScreen.getKey());
        ft.addToBackStack(preferenceScreen.getKey());
        ft.commit();
        mToolbar.setTitle(preferenceScreen.getTitle());
        return true;
    }

    /**
     * The fragment that is meant to be embedded into the SettingsActivity.
     */
    public static class SettingsFragment extends PreferenceFragmentCompat {
        public static final String FRAGMENT_TAG = "settings_fragment";

        @Override
        public void onCreatePreferences(Bundle bundle, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);
        }
    }
}
