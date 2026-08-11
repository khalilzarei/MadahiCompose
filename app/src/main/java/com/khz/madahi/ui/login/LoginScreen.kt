// ui/login/LoginScreen.kt
package com.khz.madahi.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.khz.madahi.R
import com.khz.madahi.data.local.database.AppDatabase
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.data.remote.api.RetrofitClient
import com.khz.madahi.data.remote.repository.AuthRepository
import com.khz.madahi.ui.views.CustomDialog
import com.khz.madahi.utils.NetworkChecker

@Composable
fun LoginScreen(
    onNavigateToCategory: () -> Unit
) {
    val context = LocalContext.current
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // ============ ViewModel ============
    val viewModelFactory = remember {
        LoginViewModelFactory(
            preferencesManager = PreferencesManager(context),
            authRepository = AuthRepository(RetrofitClient.apiService),
            networkChecker = NetworkChecker(context),
            appDatabase = AppDatabase.getInstance(context)
        )
    }

    val viewModel: LoginViewModel = viewModel(
        factory = viewModelFactory
    )

    // ============ State ============
    val uiState by viewModel.uiState.collectAsState()
    val mobile by viewModel.mobile.collectAsState()
    val fullName by viewModel.fullName.collectAsState()
    val isLoginMode by viewModel.isLoginMode.collectAsState()

    // ============ Effects ============
    LaunchedEffect(uiState) {
        when (uiState) {
            is LoginUiState.Success -> {
                onNavigateToCategory()
            }

            is LoginUiState.Error   -> {
                errorMessage = (uiState as LoginUiState.Error).message
                showErrorDialog = true
            }

            else                    -> Unit
        }
    }

    // ============ Dialog Error ============
    CustomDialog(
        showDialog = showErrorDialog,
        title = "خطا",
        message = errorMessage,
        confirmText = "باشه",
        dismissText = "",
        icon = Icons.Default.Person,
        onDismiss = { showErrorDialog = false },
        onConfirm = {
            showErrorDialog = false
        },
        onDismissAction = {
            showErrorDialog = false
        })

    // ============ UI ============
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo
        Card(
            modifier = Modifier.size(250.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher),
                contentDescription = "App Logo",
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Title
        Text(
            text = if (isLoginMode) "ورود" else "ثبت نام",
            fontSize = 28.sp,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (isLoginMode) "به دفتر مداحی خوش آمدید" else "ثبت نام در دفتر مداحی",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ============ Form Card ============
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Full Name (فقط در حالت ثبت نام)
                if (!isLoginMode) {
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = viewModel::updateFullName,
                        label = { Text("نام و نام خانوادگی") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Mobile
                OutlinedTextField(
                    value = mobile,
                    onValueChange = viewModel::updateMobile,
                    label = { Text("شماره موبایل") },
                    placeholder = { Text("09000000000") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    shape = RoundedCornerShape(12.dp),
                    isError = uiState is LoginUiState.Error
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Submit Button
                Button(
                    onClick = viewModel::submit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = uiState !is LoginUiState.Loading
                ) {
                    if (uiState is LoginUiState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(
                            text = if (isLoginMode) "ورود" else "ثبت نام",
                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Toggle Mode
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(
                        onClick = viewModel::toggleMode
                    ) {
                        Text(
                            if (isLoginMode) "ثبت نام جدید" else "ورود به حساب کاربری"
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ============ Privacy Policy ============
        Text(
            text = "با ورود یا ثبت نام، شرایط و قوانین را می‌پذیرید",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen {}
}