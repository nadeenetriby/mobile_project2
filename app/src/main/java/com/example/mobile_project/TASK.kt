package com.example.todolist
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "tasks"
    /*
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE  // Delete tasks when user is deleted
        )
    ]

     */
)
data class TASK(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,


    val description: String = "",

    val isCompleted: Boolean = false,

    val createdDate: Long = System.currentTimeMillis(),

  //  val userId: Int
)