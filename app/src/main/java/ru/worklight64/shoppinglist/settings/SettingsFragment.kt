package ru.worklight64.shoppinglist.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import ru.worklight64.shoppinglist.R

class SettingsFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting_preference, rootKey)
    }
}