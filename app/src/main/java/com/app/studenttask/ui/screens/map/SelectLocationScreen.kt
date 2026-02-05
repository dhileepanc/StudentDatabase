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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.studenttask.ui.theme.ButtonColor
import com.app.studenttask.ui.theme.TealBackground
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun SelectLocationScreen(
    onLocationSelected: (Double, Double) -> Unit,
    onBack: () -> Unit
) {
    // Default location (e.g., Chennai)
    val defaultLocation = LatLng(13.0827, 80.2707)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 15f)
    }
    
    // Track center of screen as selected location
    var centerLocation by remember { mutableStateOf(defaultLocation) }
    
    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            centerLocation = cameraPositionState.position.target
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = false)
        ) {
            // Optional: Show existing choice if any
        }

        // Center Pin (Fixed in middle of screen)
        Icon(
            imageVector = Icons.Default.GpsFixed,
            contentDescription = "Center Pin",
            tint = ButtonColor,
            modifier = Modifier.align(Alignment.Center).size(32.dp)
        )

        // Top Search Bar (UI Mock)
        Card(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 48.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.Gray)
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Try gas stations, ATMs",
                    color = Color.Gray,
                    modifier = Modifier.weight(1f)
                )
                Icon(Icons.Default.Mic, contentDescription = "Voice", tint = Color.Gray)
            }
        }
        
        // Back Button (Top Left - acting as Close based on Screenshot)
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 8.dp, start = 8.dp)
                .background(TealBackground, shape = androidx.compose.foundation.shape.CircleShape)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
        }

        // ADD Button
        Button(
            onClick = { 
                onLocationSelected(centerLocation.latitude, centerLocation.longitude) 
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp, start = 32.dp, end = 32.dp)
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ButtonColor)
        ) {
            Text("ADD", fontSize = 16.sp, color = Color.White)
        }
        
        // My Location Button
        FloatingActionButton(
            onClick = { /* Reset to my location logic */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 100.dp, end = 16.dp),
            containerColor = Color.White,
            contentColor = ButtonColor
        ) {
             Icon(Icons.Default.GpsFixed, contentDescription = "My Location")
        }
    }
}
