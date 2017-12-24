package com.omarsilva.morsetorch.fragment


import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import com.omarsilva.morsetorch.R.xml.fragment_preferences


class PreferencesFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(fragment_preferences)
    }
}