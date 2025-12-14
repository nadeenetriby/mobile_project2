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
import io.flutter.embedding.android.FlutterActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeActivity : ComponentActivity() {

    private lateinit var taskService: TaskService
    private val taskList = mutableStateListOf<TASK>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = TASKDatabase.getDatabase(this)
        taskService = TaskService(db.taskDao())


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
                    onTaskCheckedChange = ::onTaskChecked,
                    onOpenFlutterClick = {
                        startActivity(
                            FlutterActivity.createDefaultIntent(this@HomeActivity)
                        )
                    }
                )
            }
        }
    }

    private fun onTaskChecked(index: Int, newState: Boolean) {
        lifecycleScope.launch(Dispatchers.IO) {
            val task = taskList[index]
            val updated = task.copy(isCompleted = newState)
            taskService.updateTask(updated.id, updated.title, updated.description, updated.isCompleted)
            taskList[index] = updated
        }
    }

    private fun loadTasks() {
        lifecycleScope.launch(Dispatchers.IO) {
            val tasks = taskService.getAllTasks()
            taskList.clear()
            taskList.addAll(tasks)
        }
    }

    override fun onResume() {
        super.onResume()
        loadTasks()
    }
    }

