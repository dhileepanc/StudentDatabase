package com.app.studenttask.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.studenttask.R
import com.app.studenttask.ui.theme.TealBackground
import com.app.studenttask.ui.theme.ButtonColor
import com.app.studenttask.ui.theme.LightBlueOverlay
import com.app.studenttask.ui.viewmodel.AuthUiState
import com.app.studenttask.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val loginState by viewModel.loginState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(loginState) {
        when (loginState) {
            is AuthUiState.Success -> {
                viewModel.resetState()
                onLoginSuccess()
            }
            is AuthUiState.Error -> {
                 Toast.makeText(context, (loginState as AuthUiState.Error).message, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    LoginScreenContent(
        loginState = loginState,
        onLoginClick = { u, p -> viewModel.login(u, p) },
        onNavigateToSignUp = onNavigateToSignUp
    )
}

@Composable
fun LoginScreenContent(
    loginState: AuthUiState,
    onLoginClick: (String, String) -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TealBackground)
    ) {
        // Top Section with Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(TealBackground),
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                painter = painterResource(id = R.drawable.signin_logo),
                contentDescription = "Login Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 0.dp) 
            )
        }

        // Bottom Section - Form
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.5f)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(LightBlueOverlay)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "SIGN IN",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = ButtonColor,
                modifier = Modifier.align(Alignment.Start).padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("User Name") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ButtonColor,
                    unfocusedBorderColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else
                        Icons.Filled.VisibilityOff

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = null)
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ButtonColor,
                    unfocusedBorderColor = Color.Gray
                )
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Forget Password ?",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.End).clickable { /* TODO */ }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onLoginClick(username, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonColor)
            ) {
                 if (loginState is AuthUiState.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("LOG IN", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row {
                Text("Don't have an account? ", color = Color.Gray)
                Text(
                    "SignUp", 
                    color = ButtonColor, 
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToSignUp() }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreenContent(
        loginState = AuthUiState.Idle,
        onLoginClick = { _, _ -> },
        onNavigateToSignUp = {}
    )
}
