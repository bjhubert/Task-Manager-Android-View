package com.example.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskmanager.ui.theme.TaskManagerTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization

//dad helped me with this: model for a single task, name is task
// , and isCompleted is a boolean
data class Task(val name: String, val isCompleted: Boolean = false)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TaskManagerTheme {
                MainScreen()
            }
        }
    }
}
@Composable
fun MainScreen() {
    //list of tasks, compose automatically changes UI
    val taskList = remember { mutableStateListOf<Task>() }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        //adds a new task to the list
        TaskInputField(onAddTask = { taskName ->
            taskList.add(Task(taskName))
        })

        Spacer(modifier = Modifier.padding(8.dp))

        //displays the list of tasks
        TaskList(
            tasks = taskList,
            onTaskDelete = { task -> taskList.remove(task) },
            onTaskToggle = { task ->
                val index = taskList.indexOf(task)
                if (index != -1) {
                    taskList[index] = task.copy(isCompleted = !task.isCompleted)
                }
            }
        )
    }
}
@Composable
fun TaskInputField(onAddTask: (String) -> Unit) {
    var enterTask by remember { mutableStateOf("") }

    val isInputValid = enterTask.isNotBlank()

    Row(        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = enterTask,
            onValueChange = { enterTask = it },
            modifier = Modifier.weight(1f), // Added weight so it doesn't squish the button
            label = { Text("Enter Task") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.Sentences
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (isInputValid) {
                        onAddTask(enterTask.trim())
                        enterTask = ""
                    }
                }
            ),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Magenta,
                unfocusedIndicatorColor = Color.Magenta,
                focusedLabelColor = Color.Magenta,
                cursorColor = Color.Magenta
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = {
                onAddTask(enterTask.trim())
                enterTask = ""
            },
            enabled = isInputValid, // Button disables if text is empty
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Magenta,
                contentColor = Color.White
            ),
        ) {
            Text("Add Task")
        }
    }
}
@Composable
fun TaskList(
    tasks: List<Task>,
    onTaskDelete: (Task) -> Unit,
    onTaskToggle: (Task) -> Unit
) {
    LazyColumn {
        //displays each task in the list and only one task at a time
        items(tasks) { task ->
            TaskItem(
                task = task,
                onDelete = { onTaskDelete(task) },
                onToggle = { onTaskToggle(task) }
            )
        }
    }
}
@Composable
fun TaskItem(task: Task, onDelete: () -> Unit, onToggle: () -> Unit) {
    //clicking the row helps to check or uncheck the task
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = null,
            colors = CheckboxDefaults.colors(
                checkedColor = Color.Magenta,
                uncheckedColor = Color.Black
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            //helps to show that the checkbox is checked
            text = task.name,
            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
        )
        //used to delete text
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Task"
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun TaskManagerPreview() {
    TaskManagerTheme {
        MainScreen()
    }
}
