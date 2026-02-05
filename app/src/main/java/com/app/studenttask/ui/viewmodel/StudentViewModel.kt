package com.app.studenttask.ui.viewmodel

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

    fun addStudent(student: Student) {
        viewModelScope.launch {
            _addStudentState.value = StudentUiState.Loading
            try {
                if (student.name.isBlank() || student.className.isBlank() || student.section.isBlank()) {
                     _addStudentState.value = StudentUiState.Error("Name, Class and Section are required")
                     return@launch
                }
                
                val result = repository.addStudent(student)
                if (result) {
                    _addStudentState.value = StudentUiState.Success
                    loadStudents() // Refresh list
                } else {
                    _addStudentState.value = StudentUiState.Error("Failed to add student")
                }
            } catch (e: Exception) {
                _addStudentState.value = StudentUiState.Error(e.message ?: "Unknown error")
            }
        }
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
