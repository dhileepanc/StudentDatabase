package com.app.studenttask.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.studenttask.data.model.User
import com.app.studenttask.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: StudentRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val loginState: StateFlow<AuthUiState> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val registerState: StateFlow<AuthUiState> = _registerState.asStateFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = AuthUiState.Loading
            try {
                if (username.isBlank() || password.isBlank()) {
                    _loginState.value = AuthUiState.Error("Please fill all fields")
                    return@launch
                }
                
                val result = repository.loginUser(username, password)
                if (result) {
                    _loginState.value = AuthUiState.Success
                } else {
                    _loginState.value = AuthUiState.Error("Invalid credentials")
                }
            } catch (e: Exception) {
                _loginState.value = AuthUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun register(user: User) {
        viewModelScope.launch {
            _registerState.value = AuthUiState.Loading
            try {
                 if (user.username.isBlank() || user.password.isBlank()) {
                    _registerState.value = AuthUiState.Error("Please fill all fields")
                    return@launch
                }
                
                if (repository.isUserExists(user.username)) {
                    _registerState.value = AuthUiState.Error("User already exists")
                    return@launch
                }

                val result = repository.registerUser(user)
                if (result) {
                    _registerState.value = AuthUiState.Success
                } else {
                    _registerState.value = AuthUiState.Error("Registration failed")
                }
            } catch (e: Exception) {
                _registerState.value = AuthUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
    
    fun resetState() {
        _loginState.value = AuthUiState.Idle
        _registerState.value = AuthUiState.Idle
    }
}

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    object Success : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}
