package com.zingkg.arkhamhorrorboard.secondedition.app

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.PreferenceScreen
import android.support.v7.widget.Toolbar
import com.zingkg.arkhamhorrorboard.secondedition.app.R

class SettingsActivity : AppCompatActivity(), PreferenceFragmentCompat.OnPreferenceStartScreenCallback {
    private var mToolbar: Toolbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        if (savedInstanceState == null) {
            // Create the fragment only when the activity is created for the first time.
            // ie. not after orientation changes
            mToolbar = findViewById(R.id.toolbar)
            setSupportActionBar(mToolbar)
            supportActionBar?.setDisplayShowTitleEnabled(true)
            var fragment: Fragment? = supportFragmentManager.findFragmentByTag(
                SettingsFragment.FragmentTag
            )
            if (fragment == null) {
                fragment = SettingsFragment()
            }

            supportFragmentManager.beginTransaction().replace(
                R.id.frame_settings,
                fragment,
                SettingsFragment.FragmentTag
            ).commit()
        }
    }

    override fun onPreferenceStartScreen(
        preferenceFragmentCompat: PreferenceFragmentCompat?,
        preferenceScreen: PreferenceScreen?
    ): Boolean {
        val ft = supportFragmentManager.beginTransaction()
        val fragment = SettingsFragment()
        val args = Bundle()
        args.putString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT, preferenceScreen?.key)
        fragment.arguments = args
        ft.replace(R.id.frame_settings, fragment, preferenceScreen?.key)
        ft.addToBackStack(preferenceScreen?.key)
        ft.commit()
        mToolbar?.title = preferenceScreen?.title
        return true
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(bundle: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)
        }

        companion object {
            val FragmentTag = "settings_fragment"
        }
    }
}
