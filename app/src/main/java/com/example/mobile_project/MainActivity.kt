package com.example.mobile_project

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
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
import io.flutter.embedding.android.FlutterActivity
import androidx.compose.foundation.layout.Box
import com.google.firebase.FirebaseApp
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

        }
        }
    }


@Composable
fun AddTaskScreen(
    savebutton: (String, String) -> Unit,
    cancelbutton: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var titleError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F1FF))
            .padding(20.dp)
    ) {

        // ===== Top Bar =====
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Cancel",
                color = Color(0xFF4E3CCB),
                fontSize = 16.sp,
                modifier = Modifier.clickable { cancelbutton() }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (title.isBlank()) {
                        titleError = true
                    } else {
                        savebutton(title, description)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6A5AE0)
                ),
                shape = CircleShape,
                contentPadding = PaddingValues(
                    horizontal = 22.dp,
                    vertical = 12.dp
                )
            ) {
                Text(
                    "Save",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // ===== Title =====
        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
                titleError = false
            },
            placeholder = {
                Text(
                    "Title",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF9A90E8)
                )
            },
            textStyle = LocalTextStyle.current.copy(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D2D2D)
            ),
            isError = titleError,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                errorBorderColor = Color.Transparent
            )
        )

        if (titleError) {
            Text(
                text = "Title cannot be empty",
                color = Color.Red,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ===== Description =====
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            placeholder = {
                Text(
                    "Start typing...",
                    fontSize = 16.sp,
                    color = Color(0xFF9A90E8)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            textStyle = LocalTextStyle.current.copy(
                fontSize = 16.sp,
                color = Color(0xFF3A3A3A)
            ),
            maxLines = Int.MAX_VALUE,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent
            )
        )
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
    onTaskCheckedChange: (Int, Boolean) -> Unit,
    onOpenFlutterClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F1FF))
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "My Tasks",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4E3CCB),
                modifier = Modifier.weight(1f)
            )

        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {

            items(taskList.size) { index ->
                val task = taskList[index]

                val barColor = listOf(
                    Color(0xFF6A5AE0),
                    Color(0xFF4CAF50),
                    Color(0xFF03A9F4),
                    Color(0xFFFF7043)
                )[index % 4]

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { onTaskClick(task,index) }
                ) {

                    // Left color bar
                    Box(
                        modifier = Modifier
                            .width(6.dp)
                            .height(90.dp)
                            .background(barColor)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    // Card content
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFFEDEAFF), shape = MaterialTheme.shapes.large)

                            .padding(16.dp)
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = task.isCompleted,
                                onCheckedChange = { newValue ->
                                    onTaskCheckedChange(index, newValue)
                                },
                                colors = androidx.compose.material3.CheckboxDefaults.colors(
                                    checkedColor = Color(0xFF6A5AE0),
                                    uncheckedColor = Color(0xFF9A90E8),
                                    checkmarkColor = Color.White
                                )
                            )


                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = task.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2D2D2D)
                            )

                        }

                        if (task.description.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = task.description,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }


            // First button
            Button(
                onClick = onAddClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6A5AE0)
                )
            ) {
                Text(
                    "Add Task",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            // Second button
            Button(
                onClick = onOpenFlutterClick,
                modifier = Modifier.fillMaxWidth().testTag("OpenFlutterButton")
            ) {
                Text("Open Flutter")
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
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F1FF))
            .padding(16.dp)
    ) {

        /* ---------- TOP BAR ---------- */
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Cancel",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp,
                modifier = Modifier.clickable { onBack() }
            )

            Row {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit"
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        /* ---------- TITLE ---------- */
        Text(
            text = "Title",
            fontSize = 14.sp,
            color = Color(0xFF8E8E93),
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6C63FF)
        )

        Spacer(modifier = Modifier.height(32.dp))

        /* ---------- DESCRIPTION ---------- */
        Text(
            text = "Description",
            fontSize = 14.sp,
            color = Color(0xFF8E8E93),
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (description.isBlank()) "No description" else description,
            fontSize = 16.sp,
            color = Color.DarkGray,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        /* ---------- STATUS ---------- */
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = done,
                onCheckedChange = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (done) "Completed" else "Not Completed",
                fontSize = 16.sp
            )
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F1FF))
            .padding(16.dp)
    ) {

        /* ---------- TOP BAR ---------- */
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = onCancel) {
                Icon(Icons.Default.Close, contentDescription = "Cancel")
            }
            Text(
                text = "Edit Task",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(
                onClick = { onSave(title, description, done) }
            ) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "Save",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        /* ---------- TITLE ---------- */
        Text(
            text = "Title",
            fontSize = 14.sp,

            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )

        BasicTextField(
            value = title,
            onValueChange = { title = it },
            textStyle = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6C63FF)
            ),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { inner ->
                if (title.isEmpty()) {
                    Text("Start typing...", color = Color.Gray)
                }
                inner()
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        /* ---------- DESCRIPTION ---------- */
        Text(
            text = "Description",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )

        BasicTextField(
            value = description,
            onValueChange = { description = it },
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { inner ->
                if (description.isEmpty()) {
                    Text("Add details...", color = Color.Gray)
                }
                inner()
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        /* ---------- STATUS ---------- */
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = done, onCheckedChange = { done = it })
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (done) "Completed" else "Not Completed")
        }
    }
}

