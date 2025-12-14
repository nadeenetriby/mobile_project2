package com.example.mobile_project

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.example.todolist.TASKDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditTaskActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val oldTitle = intent.getStringExtra("task_title") ?: ""
        val oldDescription = intent.getStringExtra("task_description") ?: ""
        val oldDone = intent.getBooleanExtra("task_done", false)

        setContent {
            EditTaskScreen(
                oldTitle,
                oldDescription,
                oldDone,
                onSave =::updatefun,
                onCancel = {  }
            )
        }
    }
    private fun updatefun( newTitle:String,  newDescription:String, newDone:Boolean){
        val db = TASKDatabase.getDatabase(this)
        val index = intent.getIntExtra("task_index", -1)
        if (index == -1) return

        lifecycleScope.launch(Dispatchers.IO) {
            val taskDao = db.taskDao()
            val allTasks = taskDao.getAllTasks()
            val updatedTask = allTasks[index].copy(
                    title = newTitle,
                    description = newDescription,
                    isCompleted = newDone
                )
                taskDao.updateTask(updatedTask)

                launch(Dispatchers.Main) {
                    val i = Intent(this@EditTaskActivity, HomeActivity::class.java)
                    i.putExtra("task_title", newTitle)
                    i.putExtra("task_description", newDescription)
                    i.putExtra("task_done", newDone)
                    i.putExtra("task_index", index)
                    startActivity(i)
                }

        }
    }
}
