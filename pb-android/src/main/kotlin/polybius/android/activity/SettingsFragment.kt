package polybius.android.activity

import android.os.Bundle
import android.preference.PreferenceFragment
import polybius.android.R

/**
 * Created by filip on 20/01/18.
 */
class SettingsFragment : PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPreferencesFromResource(R.xml.preferences)
    }
}