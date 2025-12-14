package com.example.todolist

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(
    entities = [TASK::class, User::class],
    version = 2,
    exportSchema = false
)
    abstract class TASKDatabase: RoomDatabase() {

        abstract fun taskDao(): TASKDAO
        abstract fun userDao(): UserDao

    companion object {
            @Volatile
            private var INSTANCE: TASKDatabase? = null

            fun getDatabase(context: Context): TASKDatabase {
                return INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        TASKDatabase::class.java,
                        "task_database"
                    )
                        .build()
                    INSTANCE = instance
                    instance
                }
            }
        }
    }
