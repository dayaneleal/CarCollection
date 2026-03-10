package com.example.carcollection.ui.screens.details

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carcollection.R
import com.example.carcollection.domain.CarDetails
import com.example.carcollection.ui.components.ErrorDetail
import com.example.carcollection.ui.components.ErrorDisplay
import com.example.carcollection.ui.components.ImageLoad
import com.example.carcollection.ui.theme.AppTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
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

    LaunchedEffect(id) {
        viewModel.fetchCarDetails(id)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.details_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = colors.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_content_description),
                            tint = colors.onSurface
                        )
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
                    ErrorDisplay(
                        modifier = Modifier.align(Alignment.Center),
                        detail = ErrorDetail(
                            title = stringResource(R.string.error_title),
                            icon = Icons.Default.ErrorOutline,
                            description = state.errorMessage
                        ),
                        showBackground = false
                    )
                }

                state.carDetails != null -> {
                    CarDetailsContent(carDetails = state.carDetails!!)
                }
            }
        }
    }
}

@Composable
private fun CarDetailsContent(carDetails: CarDetails) {
    val colors = AppTheme.colors
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        ImageLoad(carDetails.imageUrl)

        Text(
            text = carDetails.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = colors.onSurface
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InfoCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Fingerprint,
                label = stringResource(R.string.id_label),
                value = carDetails.id
            )
            InfoCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.CalendarMonth,
                label = stringResource(R.string.year_label),
                value = carDetails.year
            )
            InfoCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.LocationOn,
                label = stringResource(R.string.license_plate_label),
                value = carDetails.licence
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = colors.primary
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.current_location_label),
                    fontWeight = FontWeight.Bold,
                    color = colors.onSurface
                )
            }

            val carLocation = LatLng(carDetails.place.lat, carDetails.place.long)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(carLocation, 15f)
            }

            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(16.dp)),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = rememberMarkerState(position = carLocation),
                    title = carDetails.name,
                    snippet = stringResource(R.string.car_location_snippet)
                )
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
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = colors.primary
            )
            Text(
                text = label,
                color = colors.onSurfaceVariant,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = value,
                color = colors.onSurface,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
