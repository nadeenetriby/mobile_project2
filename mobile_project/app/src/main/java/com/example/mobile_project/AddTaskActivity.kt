package com.example.mobile_project

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.example.mobile_project.TASK
import com.example.mobile_project.TASKDatabase
import com.example.mobile_project.TASKDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddTaskActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AddTaskScreen(
                savebutton = ::saveTaskToDatabase,
                cancelbutton = {
                    finish()
                }
            )
        }
    }
    private fun saveTaskToDatabase(title: String, description: String) {
        val db = TASKDatabase.getDatabase(this)
        val taskDao = db.taskDao()

        lifecycleScope.launch(Dispatchers.IO)  {
            val task = TASK(title=title, description =  description, isCompleted = false)
            taskDao.addTask(task)
        }
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("task_title", title)
        intent.putExtra("task_description", description)

        startActivity(intent)
    }



}