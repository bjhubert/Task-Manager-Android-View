package com.example.taskmanager

//data class for a task
data class Task(

    //identifier for each task
    val id: Long,

    //text of the task
    val title: String,

    //whether the task has been completed or not
    var isCompleted: Boolean = false
)