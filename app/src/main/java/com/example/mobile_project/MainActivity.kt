package com.example.mobile_project

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.mobile_project.ui.theme.Mobile_projectTheme
import com.example.todolist.TASK
import com.example.todolist.TASKDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this, HomeActivity::class.java))
        }
    }


        @Composable
fun AddTaskScreen(
    savebutton: (String, String) -> Unit,
    cancelbutton: () -> Unit
)
{
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var titleError by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        item {

            // ===== Screen Title =====
            Text(
                text = "Add New Task",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // ===== Task Title Field =====
            Column {
                Text(text = "Task Title")
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        titleError = false
                    },
                    isError = titleError,
                    modifier = Modifier.fillMaxWidth()
                )

                if (titleError) {
                    Text(
                        text = "Title cannot be empty",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ===== Task Description Field =====
            Column {
                Text(text = "Task Description")
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 5
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // ===== Buttons =====
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                OutlinedButton(
                    onClick = { cancelbutton() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        if (title.isBlank()) {
                            titleError = true
                        } else {
                            savebutton(title, description)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Save")
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AddTaskScreenPreview() {
    AddTaskScreen(
        savebutton = { _, _ -> },
        cancelbutton = { }
    )
}


@Composable
fun HomeScreen(
    taskList: List<TASK>,
    onAddClick: () -> Unit,
    onTaskClick: (TASK, Int) -> Unit,
    ontaskchecked: (Int, Boolean) -> Unit
) {
  
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {

        Text("My To-Do List", fontSize = 22.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {

            items(taskList.size) { index ->
                val task = taskList[index]

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Gray)
                        .padding(12.dp)
                        .clickable { onTaskClick(task, index) }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked =  done,
                            onCheckedChange = {checked ->
                                ontaskchecked(task, checked)
                               }
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = task.title,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(task.description)
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(onClick = onAddClick) {
                Text("Add Task")
            }
        }
    }
}



@Composable
fun TaskDetailsScreen(
    title: String,
    description: String,
    done: Boolean,
    onBack: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit
)
{

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Text("Task Details", fontSize = 22.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(20.dp))

        Text("Title", fontWeight = FontWeight.Bold)
        Text(title)

        Spacer(modifier = Modifier.height(12.dp))

        Text("Description", fontWeight = FontWeight.Bold)
        Text(description)

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = done, onCheckedChange = null)
            Text("Completed")
        }

        Spacer(modifier = Modifier.height(30.dp))

        Row(modifier = Modifier.fillMaxWidth()) {

            OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) {
                Text("Back")
            }

            Spacer(modifier = Modifier.width(10.dp))

            Button(onClick = onEdit, modifier = Modifier.weight(1f)) {
                Text("Edit")
            }

            Spacer(modifier = Modifier.width(10.dp))

            Button(
                onClick = onDelete,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Delete")
            }
        }
    }
}


@Composable
fun EditTaskScreen(
    oldTitle: String,
    oldDescription: String,
    oldDone: Boolean,
    onSave: (String, String, Boolean) -> Unit,
    onCancel: () -> Unit
) {
    var title by remember { mutableStateOf(oldTitle) }
    var description by remember { mutableStateOf(oldDescription) }
    var done by remember { mutableStateOf(oldDone) }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        item {
            Text("Edit Task", fontSize = 22.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = done, onCheckedChange = { done = it })
                Text("Completed")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth()) {

                OutlinedButton(onClick = onCancel, modifier = Modifier.weight(1f)) {
                    Text("Cancel")
                }

                Spacer(modifier = Modifier.width(10.dp))

                Button(onClick = { onSave(title, description, done) }, modifier = Modifier.weight(1f)) {
                    Text("Save")
                }
            }
        }
    }
}

