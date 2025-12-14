package com.example.mobile_project

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.example.todolist.TASK
import com.example.todolist.TASKDAO
import com.example.todolist.TASKDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddTaskActivity : ComponentActivity() {

    private lateinit var taskService: TaskService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = TASKDatabase.getDatabase(this)
        taskService = TaskService(db.taskDao())

        setContent {
            AddTaskScreen(
                savebutton = ::savetask,
                cancelbutton = {
                    finish()
                }
            )
        }
    }
    private  fun savetask(title: String, description: String) {


        lifecycleScope.launch(Dispatchers.IO) {
            val success = taskService.addTask(title, description)


            if (success) {
                val intent = Intent(this@AddTaskActivity, HomeActivity::class.java)
                intent.putExtra("task_title", title)
                intent.putExtra("task_description", description)

                startActivity(intent)
            }
        }
    }



}
