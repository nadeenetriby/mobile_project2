package com.example.mobile_project
import com.example.todolist.TASK

import com.example.todolist.TASKDAO

class TaskService (private val taskDAO: TASKDAO){
    suspend fun addTask(title: String, description: String): Boolean {
        return try {
            val task = TASK(
                title = title,
                description = description,
                isCompleted = false
            )
            taskDAO.addTask(task)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getAllTasks(): List<TASK> {
        return taskDAO.getAllTasks()
    }

    suspend fun updateTask(taskId: Int, title: String, description: String, isCompleted: Boolean): Boolean {
        return try {
            val updatedTask = TASK(
                id = taskId,
                title = title,
                description = description,
                isCompleted = isCompleted
            )
            taskDAO.updateTask(updatedTask)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteTask(task: TASK): Boolean {
        return try {
            taskDAO.deleteTask(task)
            true
        } catch (e: Exception) {
            false
        }
    }

 /*   suspend fun markTaskAsCompleted(task: TASK): Boolean {
        return try {
            val completedTask = task.copy(isCompleted = true)
            taskDAO.updateTask(completedTask)
            true
        } catch (e: Exception) {
            false
        }
    }*/

    suspend fun getCompletedTasksCount(): Int {
        return taskDAO.getCompletedTasksCount()
    }

    suspend fun getTotalTasksCount(): Int {
        return taskDAO.getTotalTasksCount()
    }

    suspend fun getTasksByStatus(isCompleted: Boolean): List<TASK> {
        return taskDAO.getTasksByStatus(isCompleted)
    }
    suspend fun getTaskById(Id: Int): TASK {
        return taskDAO.getTaskById(Id)
    }
}