package com.app.studenttask.ui.screens.student

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.app.studenttask.data.model.Student
import com.app.studenttask.ui.theme.ButtonColor
import com.app.studenttask.ui.theme.TealBackground
import com.app.studenttask.ui.viewmodel.StudentUiState
import com.app.studenttask.ui.viewmodel.StudentViewModel
import java.util.Calendar

@Composable
fun AddStudentScreen(
    onBack: () -> Unit,
    onNavigateToMap: () -> Unit,
    selectedLat: Double?,
    selectedLng: Double?,
    viewModel: StudentViewModel = hiltViewModel()
) {
    val addState by viewModel.addStudentState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(addState) {
        when (addState) {
            is StudentUiState.Success -> {
                viewModel.resetAddState()
                Toast.makeText(context, "Student Added Successfully", Toast.LENGTH_SHORT).show()
                onBack()
            }
            is StudentUiState.Error -> {
                Toast.makeText(context, (addState as StudentUiState.Error).message, Toast.LENGTH_SHORT).show()
                viewModel.resetAddState()
            }
            else -> {}
        }
    }
    
    AddStudentScreenContent(
        addState = addState,
        onAddStudent = { student -> viewModel.addStudent(student) },
        onBack = onBack,
        onNavigateToMap = onNavigateToMap,
        initialLat = selectedLat,
        initialLng = selectedLng
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStudentScreenContent(
    addState: StudentUiState,
    onAddStudent: (Student) -> Unit,
    onBack: () -> Unit,
    onNavigateToMap: () -> Unit,
    initialLat: Double?,
    initialLng: Double?
) {
    //Form State
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var name by remember { mutableStateOf("") }
    var className by remember { mutableStateOf("") } // Dropdown
    var section by remember { mutableStateOf("") } // Dropdown
    var schoolName by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var dob by remember { mutableStateOf("") }
    var bloodGroup by remember { mutableStateOf("") }
    var fatherName by remember { mutableStateOf("") }
    var motherName by remember { mutableStateOf("") }
    var parentContact by remember { mutableStateOf("") }
    var address1 by remember { mutableStateOf("") }
    var address2 by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("Chennai") }
    var districtState by remember { mutableStateOf("TamilNadu") }
    var zipCode by remember { mutableStateOf("") }
    var emergencyContact by remember { mutableStateOf("") }
    
    // Location State
    var latitude by remember { mutableStateOf(initialLat) }
    var longitude by remember { mutableStateOf(initialLng) }

    LaunchedEffect(initialLat, initialLng) {
        if (initialLat != null && initialLng != null) {
            latitude = initialLat
            longitude = initialLng
        }
    }

    // Dropdown States
    var classExpanded by remember { mutableStateOf(false) }
    var sectionExpanded by remember { mutableStateOf(false) }
    val classes = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")
    val sections = listOf("A", "B", "C", "D")

    // Image Picker
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> uri?.let { photoUri = it } }

    val context = LocalContext.current
    
    // Date Picker Logic would go here (simplified as text input for prototype speed if library missing, or use DatePickerDialog)
    // Using simple text click for now to simulate default behavior or assume DatePicker dialog integration
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Data", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TealBackground)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Photo Upload Section
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .clickable { galleryLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (photoUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(photoUri),
                        contentDescription = "Student Photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "Upload",
                        Modifier.size(40.dp),
                        tint = Color.Gray
                    )
                }
                // Camera Icon Overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .background(TealBackground, CircleShape)
                        .padding(4.dp)
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Fields
            CustomOutlinedTextField(value = name, onValueChange = { name = it }, label = "Name")
            
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                // Class Dropdown
                ExposedDropdownMenuBox(
                    expanded = classExpanded,
                    onExpandedChange = { classExpanded = it },
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    OutlinedTextField(
                        value = className,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Class") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = classExpanded) },
                        modifier = Modifier.menuAnchor(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TealBackground,
                            unfocusedBorderColor = TealBackground
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = classExpanded,
                        onDismissRequest = { classExpanded = false }
                    ) {
                        classes.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = { 
                                    className = option
                                    classExpanded = false
                                }
                            )
                        }
                    }
                }

                // Section Dropdown
               ExposedDropdownMenuBox(
                    expanded = sectionExpanded,
                    onExpandedChange = { sectionExpanded = it },
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                ) {
                    OutlinedTextField(
                        value = section,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Section") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sectionExpanded) },
                        modifier = Modifier.menuAnchor(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TealBackground,
                            unfocusedBorderColor = TealBackground
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = sectionExpanded,
                        onDismissRequest = { sectionExpanded = false }
                    ) {
                        sections.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = { 
                                    section = option
                                    sectionExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            
            CustomOutlinedTextField(value = schoolName, onValueChange = { schoolName = it }, label = "School name")

            // Gender
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Gender : ", color = TealBackground)
                RadioButton(selected = gender == "Male", onClick = { gender = "Male" }, colors = RadioButtonDefaults.colors(selectedColor = TealBackground))
                Text("Male")
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(selected = gender == "Female", onClick = { gender = "Female" }, colors = RadioButtonDefaults.colors(selectedColor = TealBackground))
                Text("Female")
            }

            CustomOutlinedTextField(
                value = dob, 
                onValueChange = { dob = it }, 
                label = "DOB", 
                trailingIcon = Icons.Default.DateRange
            )
            
            CustomOutlinedTextField(value = bloodGroup, onValueChange = { bloodGroup = it }, label = "Blood Group")
            CustomOutlinedTextField(value = fatherName, onValueChange = { fatherName = it }, label = "Father's name")
            CustomOutlinedTextField(value = motherName, onValueChange = { motherName = it }, label = "Mother's name")
            CustomOutlinedTextField(
                value = parentContact, 
                onValueChange = { parentContact = it }, 
                label = "Parent's contact no",
                keyboardType = KeyboardType.Phone
            )
            
            CustomOutlinedTextField(value = address1, onValueChange = { address1 = it }, label = "Address 1")
            CustomOutlinedTextField(value = address2, onValueChange = { address2 = it }, label = "Address 2")
            CustomOutlinedTextField(value = city, onValueChange = { city = it }, label = "City")
            CustomOutlinedTextField(value = districtState, onValueChange = { districtState = it }, label = "State")
            CustomOutlinedTextField(value = zipCode, onValueChange = { zipCode = it }, label = "Zip", keyboardType = KeyboardType.Number)
            
            CustomOutlinedTextField(
                value = emergencyContact, 
                onValueChange = { emergencyContact = it }, 
                label = "Emergency contact no.",
                keyboardType = KeyboardType.Phone
            )

            Spacer(modifier = Modifier.height(16.dp))
            
            // Location Section
            Text("Select Location", color = TealBackground, modifier = Modifier.align(Alignment.Start))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, TealBackground, RoundedCornerShape(8.dp))
            ) {
                 if (latitude != null && longitude != null) {
                    // Show confirmation or lite map if possible (Map logic omitted for stability if no key, just Text or Placeholder)
                    // We'll use a static placeholder with text for stability given instructions on API Key
                     Column(
                        modifier = Modifier.fillMaxSize().background(Color.LightGray),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                     ) {
                         Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Red, modifier = Modifier.size(40.dp))
                         Text("Location Selected!", fontWeight = FontWeight.Bold, color = TealBackground)
                         Text("$latitude, $longitude", fontSize = 12.sp)
                     }
                 } else {
                     Image(
                        painter = rememberAsyncImagePainter(com.app.studenttask.R.drawable.map_view), 
                        contentDescription = "Map Screenshot",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                 }
                
                // Overlay Button
                Box(
                    modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().background(Color.White.copy(alpha = 0.9f)).padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            if(latitude != null) "Selected" else "Select Location",
                            fontSize = 12.sp,
                            color = TealBackground
                        )
                        Button(
                            onClick = onNavigateToMap,
                            colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
                            shape = RoundedCornerShape(4.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("Select Location", fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { 
                    val student = Student(
                        name = name,
                        className = className,
                        section = section,
                        schoolName = schoolName,
                        gender = gender,
                        dob = dob,
                        bloodGroup = bloodGroup,
                        fatherName = fatherName,
                        motherName = motherName,
                        parentContact = parentContact,
                        address1 = address1,
                        address2 = address2,
                        city = city,
                        state = districtState,
                        zipCode = zipCode,
                        emergencyContact = emergencyContact,
                        latitude = latitude ?: 0.0, // Mock lat
                        longitude = longitude ?: 0.0, // Mock lng
                        photoUri = photoUri.toString()
                    )
                    onAddStudent(student)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonColor)
            ) {
                 if (addState is StudentUiState.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("SUBMIT", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    trailingIcon: androidx.compose.ui.graphics.vector.ImageVector? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        trailingIcon = if (trailingIcon != null) { { Icon(trailingIcon, contentDescription = null, tint = TealBackground) } } else null,
         colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = TealBackground,
            unfocusedBorderColor = TealBackground,
            focusedLabelColor = TealBackground,
            unfocusedLabelColor = TealBackground
        )
    )
}

@Preview(showBackground = true)
@Composable
fun AddStudentScreenPreview() {
    AddStudentScreenContent(
        addState = StudentUiState.Idle,
        onAddStudent = {},
        onBack = {},
        onNavigateToMap = {},
        initialLat = null,
        initialLng = null
    )
}
