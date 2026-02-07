package com.app.studenttask.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.studenttask.R

@Composable
fun HomeScreen(
    onAddStudentClick: () -> Unit,
    onViewStudentClick: () -> Unit,
    onMapClick: () -> Unit,
    onLogout: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Top sectionn
            Image(
                painter = painterResource(id = R.drawable.dashboard_top),
                contentDescription = "Dashboard Background",
                contentScale = ContentScale.FillWidth,
                alignment = Alignment.TopCenter,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            // Center Logo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.dash_logo),
                    contentDescription = "Dashboard Logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
            }
        }

        // 2. Content Overlay
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Top Header Content
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 16.dp)
            ) {
                 Column(modifier = Modifier.align(Alignment.CenterStart)) {
                     IconButton(onClick = onLogout) {
                         Icon(
                             Icons.Default.GridView, 
                             contentDescription = "Menu", 
                             tint = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary else Color.White
                         )
                     }
                     Spacer(modifier = Modifier.height(24.dp))
                     Text(
                         text = "Welcome to",
                         color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f) else Color.White.copy(alpha = 0.8f),
                         fontSize = 18.sp
                     )
                     Text(
                         text = "Student Database App",
                         color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary else Color.White,
                         fontSize = 28.sp,
                         fontWeight = FontWeight.Bold
                     )
                 }

                 Box(
                     modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(60.dp)
                        .background(
                            color = MaterialTheme.colorScheme.onPrimary,
                            shape = androidx.compose.foundation.shape.CircleShape
                        )
                 )
            }
            
            Spacer(modifier = Modifier.height(60.dp)) // Spacing to push grid down to overlap/sit below curve

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DashboardCard(
                    title = "Add\nStudent", 
                    iconRes = R.drawable.add_student,
                    onClick = onAddStudentClick
                )
                DashboardCard(
                    title = "View\nStudent", 
                    iconRes = R.drawable.view_student, 
                    onClick = onViewStudentClick
                )
                DashboardCard(
                    title = "Map\nView", 
                    iconRes = R.drawable.map_view, 
                    onClick = onMapClick
                )
            }
        }
    }
}

@Composable
fun DashboardCard(
    title: String,
    iconRes: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(140.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
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
