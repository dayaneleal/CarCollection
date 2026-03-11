package com.example.carcollection.ui.screens.main

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carcollection.R
import com.example.carcollection.domain.CarDetails
import com.example.carcollection.ui.components.*
import com.example.carcollection.ui.theme.AppTheme
import com.example.carcollection.utils.findActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigate: (id: String) -> Unit,
    onLogout: () -> Unit,
    viewModel: MainViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val colors = AppTheme.colors
    val context = LocalContext.current

    val locationPermissionLauncher = rememberLauncherForActivityResult(
         ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.saveUserLocation()
        } else {
            Toast.makeText(context, context.getString(R.string.permission_denied_toast), Toast.LENGTH_SHORT).show()
        }
    }

    val checkLocationPermissionAndRequest = {
        val fineLocation = Manifest.permission.ACCESS_FINE_LOCATION
        val coarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION
        
        val hasFine = ContextCompat.checkSelfPermission(context, fineLocation) == PackageManager.PERMISSION_GRANTED
        val hasCoarse = ContextCompat.checkSelfPermission(context, coarseLocation) == PackageManager.PERMISSION_GRANTED

        val activity = context.findActivity()

        when {
            hasFine && hasCoarse -> {
                viewModel.saveUserLocation()
            }
            activity != null && (ActivityCompat.shouldShowRequestPermissionRationale(activity, fineLocation) || 
                                ActivityCompat.shouldShowRequestPermissionRationale(activity, coarseLocation)) -> {
                locationPermissionLauncher.launch(fineLocation)
            }
            else -> {
                locationPermissionLauncher.launch(fineLocation)
            }
        }
    }

    LaunchedEffect(Unit) {
        checkLocationPermissionAndRequest()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                    )
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = null,
                            tint = colors.onSurface
                        )
                    }
                },
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
                                title = stringResource(R.string.error_title),
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
                        items(state.carDetails) { car ->
                            CarCard(car, onNavigate = onNavigate)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CarCard(carDetails: CarDetails, onNavigate: (id: String) -> Unit) {
    val colors = AppTheme.colors

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, colors.onSurface.copy(alpha = 0.1f)),
        colors = CardDefaults.cardColors(containerColor = colors.surface)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
            ) {
                ImageLoad(carDetails.imageUrl)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colors.scrim.copy(alpha = 0.3f))
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = carDetails.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = colors.onSurface
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = carDetails.year,
                        color = colors.secondary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.license_plate_label),
                            fontSize = 10.sp,
                            color = colors.onSurfaceVariant,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = carDetails.licence,
                            color = colors.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    PrimaryButton(
                        text = stringResource(R.string.details_button),
                        icon = Icons.AutoMirrored.Filled.ArrowForward,
                        onClick = { onNavigate(carDetails.id) }
                    )
                }
            }
        }
    }
}
