package com.dikin.assignment4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.dikin.assignment4.composables.TaskListScreen
import com.dikin.assignment4.db.AppDatabase
import com.dikin.assignment4.db.TaskRepository
import com.dikin.assignment4.ui.theme.Assignment4Theme
import com.dikin.assignment4.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assignment4Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    StartApp(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun StartApp(modifier: Modifier) {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val taskRepository = TaskRepository(database.taskDao())
    val taskViewModel = TaskViewModel(taskRepository)

    TaskListScreen(
        modifier = modifier,
        taskViewModel = taskViewModel
    )
}
