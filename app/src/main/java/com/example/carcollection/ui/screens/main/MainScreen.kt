package com.example.carcollection.ui.screens.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carcollection.domain.Car
import com.example.carcollection.ui.components.*
import com.example.carcollection.ui.theme.AppTheme
import com.example.carcollection.ui.theme.CarCollectionTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigate: (id: String) -> Unit,
    viewModel: MainViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val colors = AppTheme.colors

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("CARCOLLECTION", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = Color.White)
                },
                actions = {
                    IconButton(onClick = { /* Logout */ }) {
                        Icon(Icons.AutoMirrored.Filled.Logout, null)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(colors.background)
                .padding(horizontal = 16.dp)
        ) {
            when {
                state.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = colors.primary)
                    }
                }
                state.errorMessage != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        ErrorDisplay(
                            detail = ErrorDetail(
                                title = "Ops! Algo deu errado",
                                icon = Icons.Default.ErrorOutline,
                                description = state.errorMessage
                            ),
                            showBackground = false
                        )
                    }
                }
                else -> {
                    Spacer(modifier = Modifier.height(36.dp))
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(state.cars) { car ->
                            CarCard(car, onNavigate = { id ->
                                onNavigate(id)
                            })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CarCard(car: Car, onNavigate: (id: String) -> Unit) {
    val colors = AppTheme.colors

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
        colors = CardDefaults.cardColors(containerColor = colors.surface)
    ) {
        Column {
            Box(modifier = Modifier
                .height(180.dp)
                .fillMaxWidth()) {
                ImageLoad(car.imageUrl)
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)))
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(car.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = Color.White)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(car.year, color = colors.secondary, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text("LICENSE PLATE", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Text(car.licence, color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    PrimaryButton(
                        text = "DETAILS",
                        icon = Icons.AutoMirrored.Filled.ArrowForward,
                        onClick = {
                            onNavigate(car.id)
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreviewContent() {
    CarCollectionTheme {
        MainScreen(onNavigate = {})
    }
}
