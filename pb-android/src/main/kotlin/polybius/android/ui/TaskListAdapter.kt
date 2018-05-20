package polybius.android.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import polybius.android.R
import polybius.common.models.Task

class TaskListAdapter(var tasks : List<Task> = listOf()) : RecyclerView.Adapter<TaskListAdapter.ViewHolder>() {
    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val titleView by lazy { view.findViewById<TextView>(R.id.taskTitle) }
        val urlView by lazy { view.findViewById<TextView>(R.id.taskUrl) }
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemCount() = tasks.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val taskView = inflater.inflate(R.layout.task_list_item, parent, false)
        return ViewHolder(taskView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks[position]
        holder.titleView.text = task.title
        holder.urlView.text = task.url
    }

    override fun getItemId(position: Int): Long {
        val taskId = tasks[position].id
        return taskId!!.hashCode().toLong()
    }
}