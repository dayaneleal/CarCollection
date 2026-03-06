package com.example.carcollection.ui.screens.login

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carcollection.AppTheme
import com.example.carcollection.ui.components.PrimaryButton
import com.example.carcollection.ui.components.SecondaryButton
import com.example.carcollection.ui.components.TextFieldInput

@Composable
fun LoginScreen(
    onNavigate: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    var phoneNumber by remember { mutableStateOf("") }
    var otpCode by remember { mutableStateOf("") }
    val colors = AppTheme.colors
    val context = LocalContext.current

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
                .padding(innerPadding)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, colors.primary.copy(alpha = 0.2f), RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(32.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LoginHeader()

                    Spacer(modifier = Modifier.height(32.dp))

                    TextFieldInput(
                        label = "PHONE NUMBER",
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        placeholder = "+1 (555) 000-0000",
                        icon = Icons.Default.PhoneAndroid
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    PrimaryButton(
                        text = "SEND SMS",
                        icon = Icons.AutoMirrored.Filled.Send
                    ) {
                        if (context is Activity) {
                            viewModel.sendVerificationCode(context, phoneNumber)
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = Color.Gray.copy(alpha = 0.3f)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    TextFieldInput(
                        label = "VERIFICATION CODE",
                        value = otpCode,
                        onValueChange = { otpCode = it },
                        placeholder = "ENTER CODE",
                        icon = Icons.Default.VpnKey
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    SecondaryButton(
                        text = "VALIDATE CODE",
                        icon = Icons.Default.Verified
                    ) {
                        onNavigate()
                    }
                }
            }
        }
    }
}

@Composable
fun LoginHeader() {
    val colors = AppTheme.colors
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(colors.primary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Speed, null, tint = colors.onPrimary, modifier = Modifier.size(32.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "VELOCE",
            style = MaterialTheme.typography.headlineMedium,
            color = colors.onSurface,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Ignite your passion for performance.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}


