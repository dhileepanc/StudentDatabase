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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.app.studenttask.R
import com.app.studenttask.data.model.Student
import com.app.studenttask.ui.theme.ButtonColor
import com.app.studenttask.ui.theme.TealBackground
import com.app.studenttask.ui.utils.LocationUtils
import com.app.studenttask.ui.viewmodel.StudentUiState
import com.app.studenttask.ui.viewmodel.StudentViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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

    // Collect all VM states
    val name by viewModel.name.collectAsState()
    val className by viewModel.className.collectAsState()
    val section by viewModel.section.collectAsState()
    val schoolName by viewModel.schoolName.collectAsState()
    val gender by viewModel.gender.collectAsState()
    val dob by viewModel.dob.collectAsState()
    val bloodGroup by viewModel.bloodGroup.collectAsState()
    val fatherName by viewModel.fatherName.collectAsState()
    val motherName by viewModel.motherName.collectAsState()
    val parentContact by viewModel.parentContact.collectAsState()
    val address1 by viewModel.address1.collectAsState()
    val address2 by viewModel.address2.collectAsState()
    val city by viewModel.city.collectAsState()
    val districtState by viewModel.districtState.collectAsState()
    val zipCode by viewModel.zipCode.collectAsState()
    val emergencyContact by viewModel.emergencyContact.collectAsState()
    val latitude by viewModel.latitude.collectAsState()
    val longitude by viewModel.longitude.collectAsState()
    val photoUri by viewModel.photoUri.collectAsState()


    LaunchedEffect(selectedLat, selectedLng) {
        if (selectedLat != null && selectedLng != null) {
            viewModel.latitude.value = selectedLat
            viewModel.longitude.value = selectedLng
        }
    }

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
        onAddStudent = { viewModel.addStudent() },
        onBack = onBack,
        onNavigateToMap = onNavigateToMap,
        name = name,
        onNameChange = { viewModel.name.value = it },
        className = className,
        onClassNameChange = { viewModel.className.value = it },
        section = section,
        onSectionChange = { viewModel.section.value = it },
        schoolName = schoolName,
        onSchoolNameChange = { viewModel.schoolName.value = it },
        gender = gender,
        onGenderChange = { viewModel.gender.value = it },
        dob = dob,
        onDobChange = { viewModel.dob.value = it },
        bloodGroup = bloodGroup,
        onBloodGroupChange = { viewModel.bloodGroup.value = it },
        fatherName = fatherName,
        onFatherNameChange = { viewModel.fatherName.value = it },
        motherName = motherName,
        onMotherNameChange = { viewModel.motherName.value = it },
        parentContact = parentContact,
        onParentContactChange = { viewModel.parentContact.value = it },
        address1 = address1,
        onAddress1Change = { viewModel.address1.value = it },
        address2 = address2,
        onAddress2Change = { viewModel.address2.value = it },
        city = city,
        onCityChange = { viewModel.city.value = it },
        districtState = districtState,
        onDistrictStateChange = { viewModel.districtState.value = it },
        zipCode = zipCode,
        onZipCodeChange = { viewModel.zipCode.value = it },
        emergencyContact = emergencyContact,
        onEmergencyContactChange = { viewModel.emergencyContact.value = it },
        latitude = latitude,
        longitude = longitude,
        photoUri = photoUri,
        onPhotoUriChange = { viewModel.photoUri.value = it }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStudentScreenContent(
    addState: StudentUiState,
    onAddStudent: () -> Unit,
    onBack: () -> Unit,
    onNavigateToMap: () -> Unit,
    name: String,
    onNameChange: (String) -> Unit,
    className: String,
    onClassNameChange: (String) -> Unit,
    section: String,
    onSectionChange: (String) -> Unit,
    schoolName: String,
    onSchoolNameChange: (String) -> Unit,
    gender: String,
    onGenderChange: (String) -> Unit,
    dob: String,
    onDobChange: (String) -> Unit,
    bloodGroup: String,
    onBloodGroupChange: (String) -> Unit,
    fatherName: String,
    onFatherNameChange: (String) -> Unit,
    motherName: String,
    onMotherNameChange: (String) -> Unit,
    parentContact: String,
    onParentContactChange: (String) -> Unit,
    address1: String,
    onAddress1Change: (String) -> Unit,
    address2: String,
    onAddress2Change: (String) -> Unit,
    city: String,
    onCityChange: (String) -> Unit,
    districtState: String,
    onDistrictStateChange: (String) -> Unit,
    zipCode: String,
    onZipCodeChange: (String) -> Unit,
    emergencyContact: String,
    onEmergencyContactChange: (String) -> Unit,
    latitude: Double?,
    longitude: Double?,
    photoUri: Uri?,
    onPhotoUriChange: (Uri?) -> Unit
) {
    var locationAddress by remember { mutableStateOf("") }
    val context = LocalContext.current
    

    LaunchedEffect(latitude, longitude) {
        if (latitude != null && longitude != null) {
            locationAddress = LocationUtils.getAddressFromLocation(context, latitude!!, longitude!!)
        }
    }
 
    // Permission Launchers
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onNavigateToMap()
        } else {
            Toast.makeText(context, "Location permission is required to select location", Toast.LENGTH_LONG).show()
        }
    }

    // Camera Logic
    var tempPhotoUri by remember { mutableStateOf<Uri?>(null) }
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            onPhotoUriChange(tempPhotoUri)
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val uri = createTempImageUri(context)
            tempPhotoUri = uri
            takePictureLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Camera permission is required to take photo", Toast.LENGTH_LONG).show()
        }
    }

    var classExpanded by remember { mutableStateOf(false) }
    var sectionExpanded by remember { mutableStateOf(false) }
    val classes = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")
    val sections = listOf("A", "B", "C", "D")

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = Date(millis)
                        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        onDobChange(formatter.format(date))
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    headlineContentColor = MaterialTheme.colorScheme.primary,
                    navigationContentColor = MaterialTheme.colorScheme.primary,
                    yearContentColor = MaterialTheme.colorScheme.primary,
                    currentYearContentColor = MaterialTheme.colorScheme.primary,
                    selectedYearContentColor = MaterialTheme.colorScheme.onPrimary,
                    selectedYearContainerColor = MaterialTheme.colorScheme.primary,
                    dayContentColor = MaterialTheme.colorScheme.onSurface,
                    selectedDayContentColor = MaterialTheme.colorScheme.onPrimary,
                    selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                    todayContentColor = MaterialTheme.colorScheme.primary,
                    todayDateBorderColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Data", color = MaterialTheme.colorScheme.onPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.background)
                    .navigationBarsPadding()
            ) {
                Button(
                    onClick = onAddStudent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                     if (addState is StudentUiState.Loading) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                    } else {
                        Text("SUBMIT", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clickable { 
                        val permission = android.Manifest.permission.CAMERA
                        if (androidx.core.content.ContextCompat.checkSelfPermission(context, permission) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                            val uri = createTempImageUri(context)
                            tempPhotoUri = uri
                            takePictureLauncher.launch(uri)
                        } else {
                            cameraPermissionLauncher.launch(permission)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (photoUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(photoUri)
                                .allowHardware(false)
                                .crossfade(true)
                                .build()
                        ),
                        contentDescription = "Student Photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                } else {
                     Image(
                        painter = painterResource(id = R.drawable.upload_gallery),
                        contentDescription = "Upload",
                        modifier = Modifier.fillMaxSize()
                    )
                }
                

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 8.dp, end = 8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.upload_camera), 
                        contentDescription = null, 
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))


            CustomOutlinedTextField(value = name, onValueChange = onNameChange, label = "Name *")
            
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

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
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.primary
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
                                    onClassNameChange(option)
                                    classExpanded = false
                                }
                            )
                        }
                    }
                }


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
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.primary
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
                                    onSectionChange(option)
                                    sectionExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            
            CustomOutlinedTextField(value = schoolName, onValueChange = onSchoolNameChange, label = "School name")


            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Gender : ", color = MaterialTheme.colorScheme.primary, fontSize = 16.sp)
                Spacer(modifier = Modifier.width(16.dp))
                
                Text("Male", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                RadioButton(
                    selected = gender == "Male", 
                    onClick = { onGenderChange("Male") }, 
                    colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary, unselectedColor = MaterialTheme.colorScheme.primary)
                )
                
                Spacer(modifier = Modifier.width(24.dp))
                
                Text("Female", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                RadioButton(
                    selected = gender == "Female", 
                    onClick = { onGenderChange("Female") }, 
                    colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary, unselectedColor = MaterialTheme.colorScheme.primary)
                )
            }

            CustomOutlinedTextField(
                value = dob, 
                onValueChange = onDobChange, 
                label = "DOB", 
                trailingIconId = R.drawable.calendar,
                modifier = Modifier
                    .clickable { showDatePicker = true }
                    .onFocusChanged { 
                        if (it.isFocused) {
                            showDatePicker = true
                        }
                    },
                readOnly = true
            )
            
            CustomOutlinedTextField(value = bloodGroup, onValueChange = onBloodGroupChange, label = "Blood Group")
            CustomOutlinedTextField(value = fatherName, onValueChange = onFatherNameChange, label = "Father's name")
            CustomOutlinedTextField(value = motherName, onValueChange = onMotherNameChange, label = "Mother's name")
            CustomOutlinedTextField(
                value = parentContact, 
                onValueChange = onParentContactChange, 
                label = "Parent's contact no",
                keyboardType = KeyboardType.Phone
            )
            
            CustomOutlinedTextField(value = address1, onValueChange = onAddress1Change, label = "Address 1")
            CustomOutlinedTextField(value = address2, onValueChange = onAddress2Change, label = "Address 2")
            CustomOutlinedTextField(value = city, onValueChange = onCityChange, label = "City")
            CustomOutlinedTextField(value = districtState, onValueChange = onDistrictStateChange, label = "State")
            CustomOutlinedTextField(value = zipCode, onValueChange = onZipCodeChange, label = "Zip", keyboardType = KeyboardType.Number)
            
            CustomOutlinedTextField(
                value = emergencyContact, 
                onValueChange = onEmergencyContactChange, 
                label = "Emergency contact no.",
                keyboardType = KeyboardType.Phone
            )

            Spacer(modifier = Modifier.height(16.dp))
            

            Text("Select Location *", color = MaterialTheme.colorScheme.primary, modifier = Modifier.align(Alignment.Start))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
            ) {
                 if (latitude != null && longitude != null) {
                     val location = LatLng(latitude, longitude)
                     val cameraPositionState = rememberCameraPositionState {
                         position = CameraPosition.fromLatLngZoom(location, 15f)
                     }
                     
                     GoogleMap(
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
                         Marker(
                             state = MarkerState(position = location),
                             title = "Selected Location"
                         )
                     }
                 } else {
                     Image(
                        painter = painterResource(id = R.drawable.map_view), 
                        contentDescription = "Map Placeholder",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                 }
                

                Box(
                    modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)).padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                if(latitude != null) "Selected Location" else "Location not selected",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            if (locationAddress.isNotBlank()) {
                                Text(
                                    locationAddress,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                )
                            }
                        }
                        
                        Button(
                            onClick = { 
                                val permission = android.Manifest.permission.ACCESS_FINE_LOCATION
                                if (androidx.core.content.ContextCompat.checkSelfPermission(context, permission) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                                    onNavigateToMap()
                                } else {
                                    locationPermissionLauncher.launch(permission)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(if(latitude != null) "Change" else "Select Location", fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

private fun createTempImageUri(context: android.content.Context): Uri {
    val storageDir = File(context.filesDir, "students")
    if (!storageDir.exists()) storageDir.mkdirs()
    val imageFile = File.createTempFile("student_", ".jpg", storageDir)
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        imageFile
    )
}

@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    trailingIcon: ImageVector? = null,
    trailingIconId: Int? = null,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth().padding(vertical = 4.dp),
        readOnly = readOnly,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        trailingIcon = {
            if (trailingIconId != null) {
                 Image(painter = painterResource(id = trailingIconId), contentDescription = null, modifier = Modifier.size(24.dp))
            } else if (trailingIcon != null) {
                 Icon(trailingIcon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
        },
        shape = RoundedCornerShape(8.dp),
         colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.primary,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
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
        name = "John Doe",
        onNameChange = {},
        className = "10",
        onClassNameChange = {},
        section = "A",
        onSectionChange = {},
        schoolName = "Greenwood High",
        onSchoolNameChange = {},
        gender = "Male",
        onGenderChange = {},
        dob = "01/01/2000",
        onDobChange = {},
        bloodGroup = "O+",
        onBloodGroupChange = {},
        fatherName = "James Doe",
        onFatherNameChange = {},
        motherName = "Jane Doe",
        onMotherNameChange = {},
        parentContact = "1234567890",
        onParentContactChange = {},
        address1 = "123 Main St",
        onAddress1Change = {},
        address2 = "",
        onAddress2Change = {},
        city = "Chennai",
        onCityChange = {},
        districtState = "Tamil Nadu",
        onDistrictStateChange = {},
        zipCode = "600001",
        onZipCodeChange = {},
        emergencyContact = "0987654321",
        onEmergencyContactChange = {},
        latitude = 13.0827,
        longitude = 80.2707,
        photoUri = null,
        onPhotoUriChange = {}
    )
}
