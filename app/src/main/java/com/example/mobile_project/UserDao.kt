package com.example.todolist


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {


    @Insert
    suspend fun addUser(user: User): Long  // Returns user ID


    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>


    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Int): User?


    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): User?




    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun login(username: String, password: String): User?




    @Query("SELECT COUNT(*) FROM users WHERE username = :username")
    suspend fun isUsernameExists(username: String): Int




    @Update
    suspend fun updateUser(user: User)



    @Query("UPDATE users SET password = :newPassword WHERE id = :userId")
    suspend fun changePassword(userId: Int, newPassword: String)


    @Delete
    suspend fun deleteUser(user: User)


}