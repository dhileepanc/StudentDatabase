package com.app.studenttask.ui.screens.student

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Expand
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.app.studenttask.R
import com.app.studenttask.data.model.Student
import com.app.studenttask.ui.theme.TealBackground
import com.app.studenttask.ui.viewmodel.StudentViewModel

@Composable
fun StudentDetailScreen(
    studentId: Int,
    onBack: () -> Unit,
    viewModel: StudentViewModel = hiltViewModel()
) {
    LaunchedEffect(studentId) {
        viewModel.getStudent(studentId)
    }

    val student by viewModel.currentStudent.collectAsState()

    student?.let {
        StudentDetailContent(student = it, onBack = onBack)
    } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = TealBackground)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDetailContent(
    student: Student,
    onBack: () -> Unit
) {
    Scaffold(
        containerColor = TealBackground // Top background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // -- Header Section --
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile Image
                    if (student.photoUri.isNotEmpty()) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(student.photoUri)
                                    .build()
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .border(4.dp, Color.White, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(4.dp, Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(60.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = student.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = "${student.className} Standard \"${student.section}\" Section",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Text(
                        text = student.schoolName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            // -- Stats Cards --
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoCard(label = "Gender", value = student.gender, modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                InfoCard(label = "DOB", value = student.dob, modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                InfoCard(label = "Blood", value = student.bloodGroup, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // -- White Container for Details --
            // We use a Surface/Card with top rounded corners to mimic the bottom sheet look if desired, 
            // or just standard cards as per the flat design screenshot which looks like cards on white bg.
            // But wait, the screenshot has a white rounded container holding the rest.
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color.White, 
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                    )
                    .padding(16.dp)
            ) {
                // Parents Details
                DetailSectionCard(title = "Parents Details") {
                    DetailRow("Father's name", student.fatherName)
                    DetailRow("Mother's name", student.motherName)
                    DetailRow("Contact no.", student.parentContact, isLink = true)
                    DetailRow("Emergency contact no.", student.emergencyContact, isLink = true)
                }
                
                Spacer(modifier = Modifier.height(16.dp))

                // Residential Details
                DetailSectionCard(title = "Residential Details") {
                    DetailRow("Address 1", student.address1)
                    DetailRow("Address 2", student.address2.ifEmpty { "-" })
                    DetailRow("City", student.city, isLink = true) // Color match design
                    DetailRow("State", student.state, isLink = true)
                    DetailRow("Zip", student.zipCode, isLink = true)
                }
                
                Spacer(modifier = Modifier.height(16.dp))

                // Location
                Text(
                    text = "Location",
                    style = MaterialTheme.typography.titleMedium,
                    color = TealBackground,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Box(
                     modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                        .background(Color.LightGray) // Placeholder color
                ) {
                     // In a real app, use LiteMode GoogleMap here.
                     // For now, use the map resource or placeholder icon
                     Image(
                        painter = painterResource(id = R.drawable.map_view), // Use existing drawable
                        contentDescription = "Map Preview",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                     )
                     
                     // Expand Icon overlay
                     Box(
                         modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(Color.White, RoundedCornerShape(4.dp))
                            .padding(4.dp)
                     ) {
                         Icon(Icons.Default.Expand, contentDescription = "Expand", tint = TealBackground, modifier = Modifier.size(16.dp))
                     }
                }
                
                Spacer(modifier = Modifier.height(40.dp)) // Bottom padding
            }
        }
    }
}

@Composable
fun InfoCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(70.dp), // Fixed height from look
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White) 
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label, 
                style = MaterialTheme.typography.bodyMedium, 
                color = TealBackground,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value, 
                style = MaterialTheme.typography.bodySmall, 
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun DetailSectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFADD8E6)) // Light Blue Border
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                content = content
            )
        }
        // Title Overlay
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF7CA7B8), // Muted Blue/Teal
            modifier = Modifier
                .padding(start = 12.dp)
                .background(Color.White)
                .padding(horizontal = 4.dp)
        )
    }
}

@Composable
fun DetailRow(label: String, value: String, isLink: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.LightGray,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isLink) TealBackground else Color(0xFF546E7A), // Teal for links, GreyBlue for text
            fontWeight = if (isLink) FontWeight.Medium else FontWeight.Normal,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End
        )
    }
}
