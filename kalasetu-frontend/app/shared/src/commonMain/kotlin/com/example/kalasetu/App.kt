package com.example.kalasetu.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Welcome to Kalasetu",
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Connecting artisans with the world."
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { }
        ) {
            Text("Get Started")
        }
    }
}


@Composable
fun App() {
    MaterialTheme {
        HomeScreen()
    }
}