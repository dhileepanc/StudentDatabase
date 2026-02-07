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
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDetailContent(
    student: Student,
    onBack: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background // Base background
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
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(bottom = 16.dp)
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

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
                                .size(120.dp)
                                .clip(CircleShape)
                                .border(4.dp, MaterialTheme.colorScheme.onPrimary, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface)
                                .border(4.dp, MaterialTheme.colorScheme.onPrimary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(60.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = student.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = "${student.className} Standard \"${student.section}\" Section",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                    )
                    Text(
                        text = student.schoolName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                    )
                }
            }


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


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.background, 
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                    )
                    .padding(16.dp)
            ) {

                DetailSectionCard(title = "Parents Details") {
                    DetailRow("Father's name", student.fatherName)
                    DetailRow("Mother's name", student.motherName)
                    DetailRow("Contact no.", student.parentContact, isLink = true)
                    DetailRow("Emergency contact no.", student.emergencyContact, isLink = true)
                }
                
                Spacer(modifier = Modifier.height(16.dp))

                DetailSectionCard(title = "Residential Details") {
                    DetailRow("Address 1", student.address1)
                    DetailRow("Address 2", student.address2.ifEmpty { "-" })
                    DetailRow("City", student.city, isLink = true)
                    DetailRow("State", student.state, isLink = true)
                    DetailRow("Zip", student.zipCode, isLink = true)
                }
                
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Location",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha=0.5f), RoundedCornerShape(8.dp))
                ) {
                    val studentLocation = com.google.android.gms.maps.model.LatLng(student.latitude, student.longitude)
                    val cameraPositionState = com.google.maps.android.compose.rememberCameraPositionState {
                        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(studentLocation, 15f)
                    }
                    
                    com.google.maps.android.compose.GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        uiSettings = com.google.maps.android.compose.MapUiSettings(
                            zoomControlsEnabled = false,
                            scrollGesturesEnabled = false,
                            zoomGesturesEnabled = false,
                            tiltGesturesEnabled = false,
                            rotationGesturesEnabled = false
                        )
                    ) {
                        com.google.maps.android.compose.Marker(
                            state = com.google.maps.android.compose.MarkerState(position = studentLocation),
                            title = student.name
                        )
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f), RoundedCornerShape(4.dp))
                            .padding(4.dp)
                    ) {
                        Icon(Icons.Default.Expand, contentDescription = "Expand", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
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
        modifier = modifier.height(70.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface) 
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label, 
                style = MaterialTheme.typography.bodyMedium, 
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value, 
                style = MaterialTheme.typography.bodySmall, 
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
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
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha=0.3f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                content = content
            )
        }

        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary.copy(alpha=0.7f),
            modifier = Modifier
                .padding(start = 12.dp)
                .background(MaterialTheme.colorScheme.background)
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
            color = MaterialTheme.colorScheme.onSurface.copy(alpha=0.5f),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isLink) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (isLink) FontWeight.Medium else FontWeight.Normal,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End
        )
    }
}
@Preview(showBackground = true)
@Composable
fun StudentDetailPreview() {
    StudentDetailContent(
        student = Student(
            id = 1,
            name = "John Doe",
            className = "10",
            section = "A",
            schoolName = "Greenwood High",
            gender = "Male",
            dob = "01/01/2000",
            bloodGroup = "O+",
            fatherName = "James Doe",
            motherName = "Jane Doe",
            parentContact = "1234567890",
            address1 = "123 Main St",
            address2 = "",
            city = "Chennai",
            state = "Tamil Nadu",
            zipCode = "600001",
            emergencyContact = "0987654321",
            latitude = 13.0827,
            longitude = 80.2707,
            photoUri = ""
        ),
        onBack = {}
    )
}
