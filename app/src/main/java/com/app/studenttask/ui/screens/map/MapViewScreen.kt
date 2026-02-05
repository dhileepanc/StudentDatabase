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
import com.app.studenttask.ui.theme.TealBackground
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Map View", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TealBackground)
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                studentList.forEach { student ->
                    // Custom Marker showing Image and Name always
                    MarkerComposable(
                        keys = arrayOf(student.id, student.photoUri),
                        state = rememberMarkerState(position = LatLng(student.latitude, student.longitude)),
                        title = student.name
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Rounded Image Marker
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(60.dp)
                                    .shadow(4.dp, CircleShape)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .border(2.dp, TealBackground, CircleShape)
                            ) {
                                if (student.photoUri.isNotEmpty()) {
                                    Image(
                                        painter = rememberAsyncImagePainter(
                                            model = coil.request.ImageRequest.Builder(LocalContext.current)
                                                .data(student.photoUri)
                                                .allowHardware(false)
                                                .build()
                                        ),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(54.dp)
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                     // Fallback
                                     Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = TealBackground,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                            // Triangle/Arrow below circle to look like pin
                            Box(
                                modifier = Modifier
                                    .offset(y = (-4).dp)
                                    .size(width = 16.dp, height = 12.dp)
                                    .background(TealBackground, shape = TriangleShape)
                            )
                            
                            Spacer(modifier = Modifier.height(2.dp))
                            
                            // Name Label
                            Card(
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Text(
                                    text = student.name,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
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
