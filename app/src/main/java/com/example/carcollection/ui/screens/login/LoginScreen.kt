package com.example.carcollection.ui.screens.login

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carcollection.R
import com.example.carcollection.ui.components.ButtonSize
import com.example.carcollection.ui.theme.AppTheme
import com.example.carcollection.ui.components.PrimaryButton
import com.example.carcollection.ui.components.SecondaryButton
import com.example.carcollection.ui.components.TextFieldInput
import com.example.carcollection.utils.findActivity
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    onNavigate: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val colors = AppTheme.colors
    val context = LocalContext.current
    val codeSentMsg = stringResource(R.string.verification_code_sent)

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is LoginUiEvent.ShowError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
                is LoginUiEvent.CodeSent -> {
                    Toast.makeText(context, codeSentMsg, Toast.LENGTH_SHORT).show()
                }
                is LoginUiEvent.NavigateToMain -> {
                    onNavigate()
                }
            }
        }
    }


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
                        label = stringResource(R.string.phone_label),
                        value = uiState.phoneNumber,
                        onValueChange = { viewModel.onPhoneNumberChange(it) },
                        placeholder = stringResource(R.string.phone_placeholder),
                        icon = Icons.Default.PhoneAndroid
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    PrimaryButton(
                        text = if (uiState.isLoading && uiState.verificationId.isEmpty()) {
                            stringResource(R.string.sending)
                        } else {
                            stringResource(R.string.send_sms)
                        },
                        icon = Icons.AutoMirrored.Filled.Send,
                        size = ButtonSize.LARGE,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !uiState.isLoading
                    ) {
                        val activity = context.findActivity()
                        if (activity != null) {
                            viewModel.sendVerificationCode(activity)
                        }
                    }

                    AnimatedVisibility(
                        visible = uiState.verificationId.isNotEmpty(),
                        enter = fadeIn() + slideInVertically { it / 2 }
                    ) {
                        Column {
                            Spacer(modifier = Modifier.height(32.dp))

                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth(),
                                thickness = 1.dp,
                                color = colors.onSurfaceVariant.copy(alpha = 0.3f)
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            TextFieldInput(
                                label = stringResource(R.string.verification_code_label),
                                value = uiState.otpCode,
                                onValueChange = { viewModel.onOtpCodeChange(it) },
                                placeholder = stringResource(R.string.verification_code_placeholder),
                                icon = Icons.Default.VpnKey
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            SecondaryButton(
                                text = if (uiState.isLoading && uiState.verificationId.isNotEmpty()) {
                                    stringResource(R.string.verifying)
                                } else {
                                    stringResource(R.string.validate_code)
                                },
                                icon = Icons.Default.Verified
                            ) {
                                viewModel.verifyCode()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoginHeader() {
    val colors = AppTheme.colors
    val typography = MaterialTheme.typography
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
            text = stringResource(R.string.app_title),
            style = typography.headlineMedium,
            color = colors.onSurface,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.app_subtitle),
            style = typography.bodySmall,
            color = colors.onSurfaceVariant
        )
    }
}
