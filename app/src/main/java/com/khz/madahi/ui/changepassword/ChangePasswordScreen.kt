// ui/changepassword/ChangePasswordScreen.kt
package com.khz.madahi.ui.changepassword

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.data.remote.api.RetrofitClient
import com.khz.madahi.data.remote.repository.AuthRepository
import com.khz.madahi.helper.PASSWORD_MIN_LENGTH
import com.khz.madahi.ui.components.GlassCard
import com.khz.madahi.ui.components.GlassTextField
import com.khz.madahi.ui.components.MadahiBackground
import com.khz.madahi.ui.components.ThreeDButton
import com.khz.madahi.ui.theme.LocalMadahiColors
import com.khz.madahi.ui.theme.MadahiThemeGreen
import com.khz.madahi.ui.theme.delete
import com.khz.madahi.ui.theme.gold
import com.khz.madahi.ui.theme.textMuted
import com.khz.madahi.ui.theme.textPrimary
import com.khz.madahi.ui.theme.textSecondary
import com.khz.madahi.utils.NetworkChecker

// ============================================================
// صفحهٔ تغییر اجباری رمز عبور
// ------------------------------------------------------------
// بعد از ورود با رمز اولیه (= شمارهٔ موبایل) نمایش داده می‌شود.
// سرور تا وقتی رمز عوض نشود بقیهٔ APIها را می‌بندد، پس این صفحه
// قابل رد شدن نیست و دکمهٔ «بعداً» ندارد.
// ============================================================

@Composable
fun ChangePasswordScreen(
    onPasswordChanged: () -> Unit
) {
    val context = LocalContext.current

    var errorMessage by remember { mutableStateOf<String?>(null) }

    val viewModelFactory = remember {
        ChangePasswordViewModelFactory(
            preferencesManager = PreferencesManager(context),
            authRepository = AuthRepository(RetrofitClient.apiService),
            networkChecker = NetworkChecker(context)
        )
    }

    val viewModel: ChangePasswordViewModel = viewModel(factory = viewModelFactory)

    val uiState by viewModel.uiState.collectAsState()
    val currentPassword by viewModel.currentPassword.collectAsState()
    val newPassword by viewModel.newPassword.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()

    LaunchedEffect(uiState) {
        when (uiState) {
            is ChangePasswordUiState.Success -> onPasswordChanged()
            is ChangePasswordUiState.Error   -> errorMessage = (uiState as ChangePasswordUiState.Error).message
            else                             -> Unit
        }
    }

    ChangePasswordScreenContent(
        currentPassword = currentPassword,
        newPassword = newPassword,
        confirmPassword = confirmPassword,
        isLoading = uiState is ChangePasswordUiState.Loading,
        errorMessage = errorMessage,
        onCurrentPasswordChange = viewModel::updateCurrentPassword,
        onNewPasswordChange = viewModel::updateNewPassword,
        onConfirmPasswordChange = viewModel::updateConfirmPassword,
        onSubmit = viewModel::submit
    )
}

// ============================================================
// UI خالص — بدون ViewModel (برای Preview و تست)
// ============================================================

@Composable
fun ChangePasswordScreenContent(
    currentPassword: String,
    newPassword: String,
    confirmPassword: String,
    isLoading: Boolean,
    errorMessage: String?,
    onCurrentPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    val colors = LocalMadahiColors.current

    MadahiBackground {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(26.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(48.dp))

            Text(
                text = "تغییر رمز عبور",
                color = colors.textPrimary,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "برای امنیت حساب خود، رمز عبور اولیه را تغییر دهید.",
                color = colors.textSecondary,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(24.dp))

            GlassCard(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.padding(24.dp)
                ) {

                    Text(
                        text = "رمز عبور فعلی",
                        color = colors.textSecondary,
                        fontSize = 14.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    GlassTextField(
                        value = currentPassword,
                        onValueChange = onCurrentPasswordChange,
                        isPassword = true,
                        hint = "همان شمارهٔ موبایل"
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "رمز عبور جدید",
                        color = colors.textSecondary,
                        fontSize = 14.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    GlassTextField(
                        value = newPassword,
                        onValueChange = onNewPasswordChange,
                        isPassword = true,
                        hint = "حداقل $PASSWORD_MIN_LENGTH کاراکتر"
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "تکرار رمز عبور جدید",
                        color = colors.textSecondary,
                        fontSize = 14.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    GlassTextField(
                        value = confirmPassword,
                        onValueChange = onConfirmPasswordChange,
                        isPassword = true
                    )

                    // پیام خطا
                    errorMessage?.let { msg ->
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = msg,
                            color = colors.delete,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(Modifier.height(22.dp))

                    ThreeDButton(
                        text = if (isLoading) "لطفاً صبر کنید..." else "ثبت رمز جدید",
                        enabled = !isLoading,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onSubmit
                    )

                    if (isLoading) {
                        Spacer(Modifier.height(12.dp))
                        CircularProgressIndicator(
                            color = colors.gold,
                            modifier = Modifier
                                .size(26.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }

            Spacer(Modifier.height(18.dp))

            Text(
                text = "تا زمانی که رمز عبور تغییر نکند، دسترسی به بخش‌های دیگر اپ ممکن نیست.",
                color = colors.textMuted,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

// ============================================================
// Preview
// ============================================================

@Preview(showBackground = false)
@Composable
private fun ChangePasswordScreenPreview() {
    MadahiThemeGreen(darkTheme = true) {
        ChangePasswordScreenContent(
            currentPassword = "",
            newPassword = "",
            confirmPassword = "",
            isLoading = false,
            errorMessage = null,
            onCurrentPasswordChange = {},
            onNewPasswordChange = {},
            onConfirmPasswordChange = {},
            onSubmit = {})
    }
}

@Preview(showBackground = false)
@Composable
private fun ChangePasswordErrorPreview() {
    MadahiThemeGreen(darkTheme = false) {
        ChangePasswordScreenContent(
            currentPassword = "123456",
            newPassword = "123",
            confirmPassword = "123",
            isLoading = false,
            errorMessage = "رمز عبور جدید باید حداقل ۶ کاراکتر باشد",
            onCurrentPasswordChange = {},
            onNewPasswordChange = {},
            onConfirmPasswordChange = {},
            onSubmit = {})
    }
}
