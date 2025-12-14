package com.example.mobile_project

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.lifecycleScope
import com.example.mobile_project.ui.theme.Mobile_projectTheme
import com.example.todolist.TASK
import com.example.todolist.TASKDatabase
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
                    onTaskClick = { task, index ->
                        val intent = Intent(this, TaskDetailsActivity::class.java)
                        intent.putExtra("task_title", task.title)
                        intent.putExtra("task_description", task.description)
                        intent.putExtra("task_done", task.isCompleted)
                        intent.putExtra("task_index", index)
                        startActivity(intent)
                    }, ontaskchecked = ::ontaskchecked
                )
            }
        }
    }

    private fun loadAllTasks() {
        lifecycleScope.launch(Dispatchers.IO) {
            val tasks = db.taskDao().getAllTasks()
            taskList.clear()
            taskList.addAll(tasks)
        }
    }

    private fun ontaskchecked(task: TASK, checked: Boolean) {
        lifecycleScope.launch(Dispatchers.IO) {
            db.taskDao().updateTask(task.copy(isCompleted = checked))
            loadAllTasks()

        }
    }

        override fun onResume() {
            super.onResume()
            loadAllTasks()
        }

    }

