package com.app.studenttask.ui.screens.student

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.app.studenttask.data.model.Student
import com.app.studenttask.ui.viewmodel.StudentViewModel

@Composable
fun StudentListScreen(
    onBack: () -> Unit,
    onStudentClick: (Int) -> Unit, // Add Callback
    viewModel: StudentViewModel = hiltViewModel()
) {
    val studentList by viewModel.studentList.collectAsState()

    StudentListScreenContent(
        studentList = studentList,
        onBack = onBack,
        onStudentClick = onStudentClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentListScreenContent(
    studentList: List<Student>,
    onBack: () -> Unit,
    onStudentClick: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("View Data", color = MaterialTheme.colorScheme.onPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            items(studentList) { student ->
                StudentItem(
                    student = student,
                    onClick = { onStudentClick(student.id) }
                )
            }
        }
    }
}

@Composable
fun StudentItem(
    student: Student,
    onClick: () -> Unit
) {
    val outlineColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar
                if (student.photoUri.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(student.photoUri)
                                .allowHardware(false)
                                .crossfade(true)
                                .build()
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .border(2.dp, outlineColor, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                     Box(
                         modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                            .border(2.dp, outlineColor, CircleShape),
                         contentAlignment = Alignment.Center
                     ) {
                         Image(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                        )
                     }
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Info
                Column {
                    Text(
                        text = student.name, 
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${student.className} Std - ${student.section} / ${student.schoolName}", 
                        style = MaterialTheme.typography.bodyMedium, 
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            
            // Right Arrow
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.ArrowForward, 
                    contentDescription = "Details", 
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StudentListScreenPreview() {
    val sampleStudents = listOf(
        Student(
            id = 1, name = "Michael", className = "6", section = "A", schoolName = "K.G School",
            gender = "Male", dob = "01/01/2000", bloodGroup = "O+", fatherName = "Joseph", motherName = "Janet",
            parentContact = "123", address1 = "Add1", address2 = "Add2", city = "Chennai", state = "TN", zipCode = "600",
            emergencyContact = "100"
        ),
        Student(
            id = 2, name = "Amirtha", className = "8", section = "B", schoolName = "K.G School",
            gender = "Female", dob = "01/01/2000", bloodGroup = "O+", fatherName = "Father", motherName = "Mother",
            parentContact = "123", address1 = "Add1", address2 = "Add2", city = "Chennai", state = "TN", zipCode = "600",
            emergencyContact = "100"
        )
    )
    StudentListScreenContent(
        studentList = sampleStudents,
        onBack = {},
        onStudentClick = {}
    )
}
