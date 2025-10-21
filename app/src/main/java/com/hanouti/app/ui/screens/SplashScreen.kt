package com.hanouti.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hanouti.app.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2500)
        onFinished()
    }

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF2196F3), // Orange
            Color(0xFF009688)  // Red
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_shop_logo),
                contentDescription = "App Logo",
                modifier = Modifier.height(96.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "7ANOUTI",
                color = Color.White,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 40.sp,
                    letterSpacing = 0.5.sp,
                    fontFamily = FontFamily.Monospace
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
            CircularProgressIndicator(color = Color.White)
        }
    }
}
