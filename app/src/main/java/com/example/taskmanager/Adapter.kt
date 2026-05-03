package com.example.taskmanager

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//adapter class for a view that displays a list of tasks
class TaskAdapter(
    private val tasks: MutableList<Task>,
    private val onDelete: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    //viewholder that holds references to the views in the layout
    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.taskCheckBox)
        val titleText: TextView = itemView.findViewById(R.id.taskTitleText)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
    }
    //called when recycleview needs another row
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
        return TaskViewHolder(view)
    }
    //binds data to a row
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.titleText.text = task.title
        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = task.isCompleted
        updateStrikeThrough(holder.titleText, task.isCompleted)
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            task.isCompleted = isChecked
            updateStrikeThrough(holder.titleText, isChecked)
        }
        holder.deleteButton.setOnClickListener { onDelete(task) }
    }
    //gives the strike through affect after a task is checked off
    private fun updateStrikeThrough(textView: TextView, isCompleted: Boolean) {
        if (isCompleted) {
            textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }
    //returns the number of items in the list
    override fun getItemCount(): Int = tasks.size

    //adds a task to the list
    fun add(task: Task) {
        tasks.add(0, task)
        notifyItemInserted(0)
    }

    //removes task
    fun remove(task: Task) {
        val idx = tasks.indexOfFirst { it.id == task.id }
        if (idx >= 0) {
            tasks.removeAt(idx)
            notifyItemRemoved(idx)
        }
    }
}
