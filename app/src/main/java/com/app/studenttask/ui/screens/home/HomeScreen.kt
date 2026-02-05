package com.app.studenttask.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.studenttask.ui.theme.TealBackground
import com.app.studenttask.ui.theme.LightBlueOverlay
import com.app.studenttask.ui.theme.TealDark

@Composable
fun HomeScreen(
    onAddStudentClick: () -> Unit,
    onViewStudentClick: () -> Unit,
    onMapClick: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TealBackground)
    ) {
        // Top Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f)
                .padding(24.dp)
        ) {
             Column(modifier = Modifier.align(Alignment.CenterStart)) {
                 IconButton(onClick = onLogout) {
                     Icon(Icons.Default.ExitToApp, contentDescription = "Logout", tint = Color.White)
                 }
                 Spacer(modifier = Modifier.height(16.dp))
                 Text(
                     text = "Welcome to",
                     color = Color.White.copy(alpha = 0.7f),
                     fontSize = 16.sp
                 )
                 Text(
                     text = "Student Database App",
                     color = Color.White,
                     fontSize = 24.sp,
                     fontWeight = FontWeight.Bold
                 )
             }
             
             // Circle decoration
             Box(
                 modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(60.dp)
                    .background(Color.White.copy(alpha = 0.8f), shape = androidx.compose.foundation.shape.CircleShape)
             )
        }

        // Bottom Section - Dashboard Grid
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.6f)
                .background(Color.Transparent), // Maintain transparency to see graphical elements if added
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DashboardCard(
                    title = "Add\nStudent", 
                    icon = Icons.Default.Add, 
                    onClick = onAddStudentClick
                )
                DashboardCard(
                    title = "View\nStudent", 
                    icon = Icons.Default.List, 
                    onClick = onViewStudentClick
                )
                DashboardCard(
                    title = "Map\nView", 
                    icon = Icons.Default.Map, 
                    onClick = onMapClick
                )
            }
            
            // Image Placeholder at bottom
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun DashboardCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(140.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = LightBlueOverlay),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(TealDark, shape = androidx.compose.foundation.shape.CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                color = TealDark,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 16.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        onAddStudentClick = {},
        onViewStudentClick = {},
        onMapClick = {},
        onLogout = {}
    )
}
