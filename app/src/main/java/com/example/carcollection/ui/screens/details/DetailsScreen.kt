package com.example.carcollection.ui.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carcollection.ui.components.ErrorDetail
import com.example.carcollection.ui.components.ErrorDisplay
import com.example.carcollection.ui.components.ImageLoad
import com.example.carcollection.ui.theme.AppTheme
import com.example.carcollection.ui.theme.CarCollectionTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel = viewModel(),
    onBackClick: () -> Unit,
    id: String,
) {
    val state by viewModel.uiState.collectAsState()
    val colors = AppTheme.colors

    // Chama o fetch apenas quando o ID mudar ou a tela abrir
    LaunchedEffect(id) {
        viewModel.fetchCarDetails(id)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vehicle Details", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.background)
            )
        },
        containerColor = colors.background
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = colors.primary
                    )
                }

                state.errorMessage != null -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
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

                state.car != null -> {
                    val car = state.car!!
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        ImageLoad(car.imageUrl)

                        Text(
                            text = car.name,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            InfoCard(
                                modifier = Modifier.weight(1f),
                                icon = Icons.Default.CalendarMonth,
                                label = "YEAR",
                                value = car.year
                            )
                            InfoCard(modifier = Modifier.weight(1f), icon = Icons.Default.LocationOn, label = "LICENCE", value = car.licence)
                            InfoCard(
                                modifier = Modifier.weight(1f),
                                icon = Icons.Default.Fingerprint,
                                label = "ID",
                                value = car.id
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(20.dp), tint = colors.primary)
                                Spacer(Modifier.width(4.dp))
                                Text("Current Location", fontWeight = FontWeight.Bold, color = Color.White)
                            }

                            // Configuração do estado da câmera do Google Maps
                            val carLocation = LatLng(car.place.lat, car.place.long)
                            val cameraPositionState = rememberCameraPositionState {
                                position = CameraPosition.fromLatLngZoom(carLocation, 15f)
                            }

                            // Componente GoogleMap para Compose
                            GoogleMap(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                cameraPositionState = cameraPositionState
                            ) {
                                Marker(
                                    state = rememberMarkerState(position = carLocation),
                                    title = car.name,
                                    snippet = "Vehicle Location"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoCard(modifier: Modifier = Modifier, icon: ImageVector, label: String, value: String) {
    val colors = AppTheme.colors
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = colors.surface
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(icon, null, modifier = Modifier.size(24.dp), tint = colors.primary)
            Text(label, color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text(value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailsPreviewContent() {
    CarCollectionTheme {
        DetailsScreen(onBackClick = {}, id= "001")
    }
}
