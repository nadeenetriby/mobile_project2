package com.example.mobile_project

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.example.todolist.TASKDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class TaskDetailsActivity : ComponentActivity() {
    private var taskIndex: Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val title = intent.getStringExtra("task_title") ?: ""
        val description = intent.getStringExtra("task_description") ?: ""
        val done = intent.getBooleanExtra("task_done", false)
        taskIndex = intent.getIntExtra("task_index", -1)
        //taskId = intent.getIntExtra("task_id", -1)


        setContent {
            TaskDetailsScreen(
                title = title,
                description = description,
                done = done,
                onBack = { finish() },


                onDelete = ::deletetaskfromDatabase,


                onEdit = {
                    val i = Intent(this, EditTaskActivity::class.java)
                    i.putExtra("task_title", title)
                    i.putExtra("task_description", description)
                    i.putExtra("task_done", done)
                    i.putExtra("task_index", taskIndex)
                    startActivity(i)

                }
            )
        }
    }

    private fun deletetaskfromDatabase() {
        val db = TASKDatabase.getDatabase(this)
        val taskDao = db.taskDao()


        lifecycleScope.launch(Dispatchers.IO) {
            val taskList = taskDao.getAllTasks()
            db.taskDao().deleteTask(taskList[taskIndex])
        }


        val i = Intent(this, HomeActivity::class.java)
        i.putExtra("delete_task", true)
        i.putExtra("task_index", taskIndex)
        startActivity(i)
    }


    private fun edittask(title: String, description: String, done: Boolean, ) {

        val i = Intent(this, EditTaskActivity::class.java)
        i.putExtra("task_title", title)
        i.putExtra("task_description", description)
        i.putExtra("task_done", done)
        i.putExtra("task_index", taskIndex)
        startActivity(i)

    }
}
