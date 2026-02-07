package com.app.studenttask.ui.screens.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectLocationScreen(
    onLocationSelected: (Double, Double) -> Unit,
    onBack: () -> Unit
) {

    val defaultLocation = LatLng(13.0827, 80.2707)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 15f)
    }


    var centerLocation by remember { mutableStateOf(defaultLocation) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    
    LaunchedEffect(Unit) {
        try {
            val location = fusedLocationClient.lastLocation.await()
            location?.let {
                val userLatLng = LatLng(it.latitude, it.longitude)
                cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f))
                centerLocation = userLatLng
            }
        } catch (e: Exception) {
            // Permission might not be granted yet, fallback to default
        }
    }
    
    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            centerLocation = cameraPositionState.position.target
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Select Location",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(zoomControlsEnabled = false)
            )

            // Center Pin
            Icon(
                imageVector = Icons.Default.GpsFixed,
                contentDescription = "Center Pin",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.Center).size(36.dp)
            )

            // Search Bar Overlay
            Card(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu", tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Try gas stations, ATMs",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.weight(1f),
                        fontSize = 16.sp
                    )
                    IconButton(onClick = {}) { 
                        Icon(Icons.Default.Mic, contentDescription = "Voice", tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    }
                }
            }

            // Bottom Controls Area
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 16.dp, end = 16.dp, start = 16.dp),
                horizontalAlignment = Alignment.End
            ) {

                // My Location FAB
                SmallFloatingActionButton(
                    onClick = { 
                        scope.launch {
                            try {
                                val location = fusedLocationClient.lastLocation.await()
                                location?.let {
                                    cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 15f))
                                }
                            } catch (e: Exception) {}
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    shape = androidx.compose.foundation.shape.CircleShape,
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(Icons.Default.GpsFixed, contentDescription = "My Location", modifier = Modifier.size(24.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))


                SmallFloatingActionButton(
                    onClick = {  },
                    containerColor = Color(0xFF4285F4), // Google Blue
                    contentColor = Color.White,
                    shape = androidx.compose.foundation.shape.CircleShape,
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(
                        painter = androidx.compose.ui.res.painterResource(android.R.drawable.ic_menu_directions), 
                        contentDescription = "Directions",
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ADD Button
                Button(
                    onClick = {
                        onLocationSelected(centerLocation.latitude, centerLocation.longitude)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        "ADD",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun SelectLocationScreenPreview() {
    SelectLocationScreen(
        onLocationSelected = { _, _ -> },
        onBack = {}
    )
}
