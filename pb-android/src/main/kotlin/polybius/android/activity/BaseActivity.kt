package polybius.android.activity

import activitystarter.ActivityStarter
import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity



@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityStarter.fill(this, savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        ActivityStarter.save(this, outState)
    }
}