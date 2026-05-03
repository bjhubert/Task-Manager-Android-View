package com.example.taskmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    //stores tasks in memory
    private val tasks = mutableListOf<Task>()

    //adapter for the recyclerview
    private lateinit var adapter: TaskAdapter

    //simple counter to give each task a unique id
    private var nextId: Long = 1L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity)
        val recyclerView = findViewById<RecyclerView>(R.id.tasksRecyclerView)
        val addButton = findViewById<Button>(R.id.addTaskButton)
        adapter = TaskAdapter(tasks) { task ->
            adapter.remove(task)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        addButton.setOnClickListener {
            showAddTaskDialog()
        }
    }
    //popup dialog for adding a task
    private fun showAddTaskDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.addtask, null)
        val input = dialogView.findViewById<EditText>(R.id.taskTitleInput)
        AlertDialog.Builder(this)
            .setTitle("Add Task")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val title = input.text?.toString()?.trim().orEmpty()
                if (title.isNotBlank()) {
                    val task = Task(id = nextId++, title = title)
                    adapter.add(task)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
