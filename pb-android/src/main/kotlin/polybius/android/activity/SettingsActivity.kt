package polybius.android.activity

import activitystarter.MakeActivityStarter
import android.os.Bundle

@MakeActivityStarter
class SettingsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentManager.beginTransaction()
                .replace(android.R.id.content, SettingsFragment())
                .commit()
    }
}