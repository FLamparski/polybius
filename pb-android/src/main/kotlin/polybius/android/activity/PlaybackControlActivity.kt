package polybius.android.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import org.koin.android.architecture.ext.viewModel
import polybius.android.R
import polybius.android.kextensions.observe
import polybius.android.kextensions.tag
import polybius.android.ui.TaskListAdapter
import polybius.android.viewmodel.PlaybackControlViewModel

class PlaybackControlActivity : BaseActivity() {
    private val viewModel by viewModel<PlaybackControlViewModel>()
    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private val listView by lazy { findViewById<RecyclerView>(R.id.taskListView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playback_control)
        setSupportActionBar(toolbar)
        listView.layoutManager = LinearLayoutManager(this)

        viewModel.tasks.observe(this) { tasks ->
            Log.i(tag(this), "Got new tasks: ${tasks.toString()}")
            listView.adapter = TaskListAdapter(tasks!!)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_playback_control, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {
            SettingsActivityStarter.start(this)
            return true
        } else super.onOptionsItemSelected(item)
    }
}
