package com.app.studenttask.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.studenttask.data.model.Student
import com.app.studenttask.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentViewModel @Inject constructor(
    private val repository: StudentRepository
) : ViewModel() {

    private val _studentList = MutableStateFlow<List<Student>>(emptyList())
    val studentList: StateFlow<List<Student>> = _studentList.asStateFlow()

    private val _currentStudent = MutableStateFlow<Student?>(null)
    val currentStudent: StateFlow<Student?> = _currentStudent.asStateFlow()

    private val _addStudentState = MutableStateFlow<StudentUiState>(StudentUiState.Idle)
    val addStudentState: StateFlow<StudentUiState> = _addStudentState.asStateFlow()

    // Form State
    var name = MutableStateFlow("")
    var className = MutableStateFlow("")
    var section = MutableStateFlow("")
    var schoolName = MutableStateFlow("")
    var gender = MutableStateFlow("Male")
    var dob = MutableStateFlow("")
    var bloodGroup = MutableStateFlow("")
    var fatherName = MutableStateFlow("")
    var motherName = MutableStateFlow("")
    var parentContact = MutableStateFlow("")
    var address1 = MutableStateFlow("")
    var address2 = MutableStateFlow("")
    var city = MutableStateFlow("")
    var districtState = MutableStateFlow("")
    var zipCode = MutableStateFlow("")
    var emergencyContact = MutableStateFlow("")
    var latitude = MutableStateFlow<Double?>(null)
    var longitude = MutableStateFlow<Double?>(null)
    var photoUri = MutableStateFlow<Uri?>(null)

    init {
        loadStudents()
    }

    fun loadStudents() {
        viewModelScope.launch {
            _studentList.value = repository.getAllStudents()
        }
    }

    fun getStudent(id: Int) {
        viewModelScope.launch {
            _currentStudent.value = repository.getStudentById(id)
        }
    }

    fun addStudent() {
        viewModelScope.launch {
            _addStudentState.value = StudentUiState.Loading
            try {
                // Validation: Only name, photo, and location are mandatory
                if (name.value.isBlank() || photoUri.value == null || latitude.value == null || longitude.value == null) {
                    _addStudentState.value = StudentUiState.Error("Name, Photo and Location are mandatory")
                    return@launch
                }
                
                val student = Student(
                    name = name.value,
                    className = className.value,
                    section = section.value,
                    schoolName = schoolName.value,
                    gender = gender.value,
                    dob = dob.value,
                    bloodGroup = bloodGroup.value,
                    fatherName = fatherName.value,
                    motherName = motherName.value,
                    parentContact = parentContact.value,
                    address1 = address1.value,
                    address2 = address2.value,
                    city = city.value,
                    state = districtState.value,
                    zipCode = zipCode.value,
                    emergencyContact = emergencyContact.value,
                    latitude = latitude.value ?: 0.0,
                    longitude = longitude.value ?: 0.0,
                    photoUri = photoUri.value.toString()
                )

                val result = repository.addStudent(student)
                if (result) {
                    _addStudentState.value = StudentUiState.Success
                    clearForm()
                    loadStudents() // Refresh list
                } else {
                    _addStudentState.value = StudentUiState.Error("Failed to add student")
                }
            } catch (e: Exception) {
                _addStudentState.value = StudentUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun clearForm() {
        name.value = ""
        className.value = ""
        section.value = ""
        schoolName.value = ""
        gender.value = "Male"
        dob.value = ""
        bloodGroup.value = ""
        fatherName.value = ""
        motherName.value = ""
        parentContact.value = ""
        address1.value = ""
        address2.value = ""
        city.value = ""
        districtState.value = ""
        zipCode.value = ""
        emergencyContact.value = ""
        latitude.value = null
        longitude.value = null
        photoUri.value = null
    }

    fun deleteStudent(id: Int) {
        viewModelScope.launch {
            repository.deleteStudent(id)
            loadStudents()
        }
    }
    
    fun resetAddState() {
        _addStudentState.value = StudentUiState.Idle
    }
}

sealed class StudentUiState {
    object Idle : StudentUiState()
    object Loading : StudentUiState()
    object Success : StudentUiState()
    data class Error(val message: String) : StudentUiState()
}
