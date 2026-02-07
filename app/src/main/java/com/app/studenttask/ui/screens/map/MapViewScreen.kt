package com.app.studenttask.ui.screens.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.app.studenttask.ui.viewmodel.StudentViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapViewScreen(
    onBack: () -> Unit,
    viewModel: StudentViewModel = hiltViewModel()
) {
    val studentList by viewModel.studentList.collectAsState()
    MapViewContent(
        studentList = studentList,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapViewContent(
    studentList: List<com.app.studenttask.data.model.Student>,
    onBack: () -> Unit,
    isPreview: Boolean = false
) {
    // Center map logic
    val defaultLocation = LatLng(13.0827, 80.2707)
    val initialPos = if (studentList.isNotEmpty()) {
        LatLng(studentList[0].latitude, studentList[0].longitude)
    } else {
        defaultLocation
    }
    
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialPos, 12f)
    }

    var selectedStudentId by remember { mutableStateOf<Long?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Map View", color = MaterialTheme.colorScheme.onPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            if (isPreview) {
                // Map Placeholder for Preview
                Box(
                    modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Google Maps Placeholder", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    onMapClick = { selectedStudentId = null }
                ) {
                    studentList.forEach { student ->
                        val isSelected = selectedStudentId?.toInt() == student.id
                        val photoUri = student.photoUri.takeIf { it.isNotBlank() && it != "null" }

                        MarkerComposable(
                            keys = arrayOf(student.id!!, photoUri ?: "", isSelected),
                            state = rememberMarkerState(position = LatLng(student.latitude, student.longitude)),
                            title = student.name,
                            onClick = {
                                selectedStudentId = if (isSelected) null else student.id.toLong()
                                true
                            }
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (isSelected) {
                                    // Expanded State: Row with Image and Name
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(24.dp))
                                            .padding(4.dp)
                                            .padding(end = 12.dp) // Extra padding for name
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier
                                                .size(50.dp)
                                                .clip(CircleShape)
                                                .background(MaterialTheme.colorScheme.surface)
                                                .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
                                        ) {
                                            if (!photoUri.isNullOrBlank()) {
                                                Image(
                                                    painter = rememberAsyncImagePainter(
                                                        model = coil.request.ImageRequest.Builder(LocalContext.current)
                                                            .data(photoUri)
                                                            .allowHardware(false)
                                                            .crossfade(true)
                                                            .build()
                                                    ),
                                                    contentDescription = null,
                                                    modifier = Modifier
                                                        .size(46.dp)
                                                        .clip(CircleShape),
                                                    contentScale = ContentScale.Crop
                                                )
                                            } else {
                                                Icon(Icons.Default.LocationOn, null, tint = MaterialTheme.colorScheme.primary)
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = student.name,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    // Small Circle Pointer for selected state
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .background(MaterialTheme.colorScheme.primary.copy(alpha=0.6f), CircleShape)
                                            .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape)
                                    )
                                } else {
                                    // Default State: Circular Profile Pic
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(50.dp)
                                            .shadow(2.dp, CircleShape)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.surface)
                                            .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
                                    ) {
                                        if (photoUri != null) {
                                            Image(
                                                painter = rememberAsyncImagePainter(
                                                    model = coil.request.ImageRequest.Builder(LocalContext.current)
                                                        .data(photoUri)
                                                        .allowHardware(false)
                                                        .crossfade(true)
                                                        .build()
                                                    ),
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .size(46.dp)
                                                    .clip(CircleShape),
                                                contentScale = ContentScale.Crop
                                            )
                                        } else {
                                            Icon(Icons.Default.LocationOn, null, tint = MaterialTheme.colorScheme.primary)
                                        }
                                    }
                                    // Triangle Pointer
                                    Box(
                                        modifier = Modifier
                                            .offset(y = (-2).dp)
                                            .size(width = 12.dp, height = 8.dp)
                                            .background(MaterialTheme.colorScheme.primary, shape = TriangleShape)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Custom Triangle Shape for the pin bottom
val TriangleShape = GenericShape { size, _ ->
    moveTo(0f, 0f)
    lineTo(size.width, 0f)
    lineTo(size.width / 2f, size.height)
    close()
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun MapViewScreenPreview() {
    MapViewContent(
        studentList = listOf(
            com.app.studenttask.data.model.Student(
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
            )
        ),
        onBack = {},
        isPreview = true
    )
}
