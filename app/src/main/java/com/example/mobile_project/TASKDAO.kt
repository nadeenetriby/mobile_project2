package com.example.todolist
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
@Dao
interface TASKDAO {

    @Insert
    suspend fun addTask(task: TASK): Long   // returns Long

    // Get all tasks for specific user

  //  @Query("SELECT * FROM tasks WHERE userId = :userId ORDER BY createdDate DESC")
  //  suspend fun getAllTasksForUser(userId: Int): List<TASK>

    @Query("SELECT * FROM tasks")
    suspend fun getAllTasks(): List<TASK>

    @Query("SELECT * FROM tasks WHERE isCompleted = :completed")
    suspend fun getTasksByStatus(completed: Boolean): List<TASK>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: Int): TASK

    @Query("SELECT COUNT(*) FROM tasks")
    suspend fun getTotalTasksCount(): Int

    @Query("SELECT COUNT(*) FROM tasks WHERE isCompleted = 1")
    suspend fun getCompletedTasksCount(): Int

    @Update
    suspend fun updateTask(task: TASK)

    @Delete
    suspend fun deleteTask(task: TASK)


    @Query("DELETE FROM tasks WHERE isCompleted = 1")
    suspend fun deleteCompletedTasks()


    @Query("DELETE FROM tasks")
    suspend fun deleteAllTasks()



   // @Query("DELETE FROM tasks WHERE userId = :userId")
  //  suspend fun deleteAllTasksForUser(userId: Int)

}