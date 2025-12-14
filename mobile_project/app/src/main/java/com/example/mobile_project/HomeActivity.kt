package com.example.mobile_project

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.lifecycleScope
import com.example.mobile_project.ui.theme.Mobile_projectTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeActivity : ComponentActivity() {

    private lateinit var db: TASKDatabase
    private val taskList = mutableStateListOf<TASK>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = TASKDatabase.getDatabase(this)

        setContent {
            Mobile_projectTheme {

                HomeScreen(
                    taskList = taskList,
                    onAddClick = {
                        startActivity(Intent(this, AddTaskActivity::class.java))
                    },
                    onTaskClick = { task ,index->
                        val intent = Intent(this, TaskDetailsActivity::class.java)
                        intent.putExtra("task_title", task.title)
                        intent.putExtra("task_description", task.description)
                        intent.putExtra("task_done", task.isCompleted)
                        intent.putExtra("task_index", index)
                        startActivity(intent)
                    },
                    onTaskCheckedChange = ::onTaskChecked
                )
            }
        }
    }


        override fun onResume() {
        super.onResume()
        loadTasks()
    }
/*
    fun changeAppBackgroundColor() {
        // Example: cycle between colors
        appBackgroundColor = when (appBackgroundColor) {
            Color(0xFFF4F8FB) -> Color(0xFFFFF3E0) // Light Orange
            Color(0xFFFFF3E0) -> Color(0xFFE0F7FA) // Light Cyan
            Color(0xFFE0F7FA) -> Color(0xFFFCE4EC) // Light Pink
            else -> Color(0xFFF4F8FB)              // Back to default
                }
    }

 */
private fun onTaskChecked(index: Int, newState: Boolean) {
    lifecycleScope.launch(Dispatchers.IO) {
        val task = taskList[index]
        val updated = task.copy(isCompleted = newState)
        db.taskDao().updateTask(updated)
        taskList[index] = updated
    }
}

    private fun loadTasks() {
        lifecycleScope.launch(Dispatchers.IO) {
            val tasks = db.taskDao().getAllTasks()
            taskList.clear()
            taskList.addAll(tasks)
        }
    }
}
