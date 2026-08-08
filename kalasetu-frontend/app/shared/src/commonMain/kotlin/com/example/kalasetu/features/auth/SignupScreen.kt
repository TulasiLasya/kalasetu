package com.example.kalasetu.features.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AuthSignupScreen(
    onSignUp: () -> Unit,
    onLogin: () -> Unit,
    onBack: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val canSignUp = email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.Start)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }

            Spacer(Modifier.height(16.dp))

            AuthLogoHeader()

            Spacer(Modifier.height(32.dp))

            Text(
                text = "Create your account",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(32.dp))

            AuthTextField(value = email, onValueChange = { email = it }, placeholder = "Email")
            Spacer(Modifier.height(16.dp))
            AuthTextField(value = password, onValueChange = { password = it }, placeholder = "Password", isPassword = true)
            Spacer(Modifier.height(16.dp))
            AuthTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, placeholder = "Confirm Password", isPassword = true)

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = onSignUp,
                enabled = canSignUp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
            ) {
                Text("Sign Up", fontSize = 16.sp)
            }

            Spacer(Modifier.height(24.dp))

            OrDivider()

            Spacer(Modifier.height(24.dp))

            TextButton(onClick = onLogin) {
                Text("Login to your account", fontSize = 16.sp)
            }
        }
    }
}
