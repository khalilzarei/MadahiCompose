// ui/login/LoginScreen.kt
package com.khz.madahi.ui.login

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.khz.madahi.R
import com.khz.madahi.data.local.database.AppDatabase
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.data.remote.api.RetrofitClient
import com.khz.madahi.data.remote.repository.AuthRepository
import com.khz.madahi.ui.components.CircularImage
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
// صفحه ورود / ثبت‌نام — سبک شیشه‌ای و سه‌بعدی
// ------------------------------------------------------------
// ساختار دو لایه:
//   LoginScreen       → stateful (ViewModel، جمع‌آوری state)
//   LoginScreenContent → stateless (فقط UI — برای Preview امن است)
// امضای ورودی دقیقاً مثل نسخه قبلی است (NavGraph دست نمی‌خورد)
// ============================================================

@Composable
fun LoginScreen(
    onNavigateToAppSelection: () -> Unit,
    onNavigateToChangePassword: () -> Unit
) {
    val context = LocalContext.current

    var errorMessage by remember { mutableStateOf<String?>(null) }

    // ============ ViewModel (همان نسخه قبلی — بدون تغییر) ============
    val viewModelFactory = remember {
        LoginViewModelFactory(
            preferencesManager = PreferencesManager(context),
            authRepository = AuthRepository(RetrofitClient.apiService),
            networkChecker = NetworkChecker(context),
            appDatabase = AppDatabase.getInstance(context)
        )
    }

    val viewModel: LoginViewModel = viewModel(factory = viewModelFactory)

    // ============ State ============
    val uiState by viewModel.uiState.collectAsState()
    val mobile by viewModel.mobile.collectAsState()
    val fullName by viewModel.fullName.collectAsState()
    val password by viewModel.password.collectAsState()
    val isLoginMode by viewModel.isLoginMode.collectAsState()

    // ============ Effects ============
    LaunchedEffect(uiState) {
        when (uiState) {
            is LoginUiState.Success -> onNavigateToAppSelection()
            is LoginUiState.NeedsPasswordChange -> onNavigateToChangePassword()
            is LoginUiState.Error -> errorMessage = (uiState as LoginUiState.Error).message
            else -> Unit
        }
    }

    // ============ UI (stateless) ============
    LoginScreenContent(
        isLoginMode = isLoginMode,
        fullName = fullName,
        mobile = mobile,
        password = password,
        isLoading = uiState is LoginUiState.Loading,
        errorMessage = errorMessage,
        onFullNameChange = viewModel::updateFullName,
        onMobileChange = viewModel::updateMobile,
        onPasswordChange = viewModel::updatePassword,
        onToggleMode = viewModel::toggleMode,
        onSubmit = viewModel::submit
    )
}

// ============================================================
// UI خالص — بدون ViewModel (برای Preview و تست)
// ============================================================

@Composable
fun LoginScreenContent(
    isLoginMode: Boolean,               // true = ورود ، false = ثبت‌نام
    fullName: String,
    mobile: String,
    password: String,
    isLoading: Boolean,
    errorMessage: String?,
    onFullNameChange: (String) -> Unit,
    onMobileChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onToggleMode: () -> Unit,
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

            Spacer(Modifier.height(36.dp))

            // ============ لوگو (هماهنگ با SplashScreen جدید) ============

            CircularImage(
                image = painterResource(R.drawable.ic_launcher),
                size = 200.dp
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = if (isLoginMode) "ورود" else "ثبت‌نام",
                color = colors.textPrimary,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = if (isLoginMode) "به دفترچه مداحی خوش آمدید" else "همراه ما شوید",
                color = colors.textSecondary,
                fontSize = 14.sp
            )

            Spacer(Modifier.height(28.dp))

            // ============ فرم شیشه‌ای ============
            GlassCard(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.padding(24.dp)
                ) {

                    // نام (فقط در حالت ثبت‌نام)
                    if (!isLoginMode) {
                        Text(
                            text = "نام و نام خانوادگی",
                            color = colors.textSecondary,
                            fontSize = 14.sp
                        )
                        Spacer(Modifier.height(8.dp))
                        GlassTextField(
                            value = fullName,
                            onValueChange = onFullNameChange
                        )
                        Spacer(Modifier.height(16.dp))
                    }

                    Text(
                        text = "شماره موبایل",
                        color = colors.textSecondary,
                        fontSize = 14.sp
                    )

                    Spacer(Modifier.height(8.dp))

                    GlassTextField(
                        value = mobile,
                        onValueChange = onMobileChange,
                        keyboardType = KeyboardType.Number
                    )

                    // ✅ رمز عبور — فقط در حالت ورود
                    if (isLoginMode) {
                        Spacer(Modifier.height(16.dp))

                        Text(
                            text = "رمز عبور",
                            color = colors.textSecondary,
                            fontSize = 14.sp
                        )
                        Spacer(Modifier.height(8.dp))

                        GlassTextField(
                            value = password,
                            onValueChange = onPasswordChange,
                            isPassword = true
                        )

                        Text(
                            text = "رمز عبور اولیهٔ شما همان شمارهٔ موبایل است و پس از اولین ورود باید آن را تغییر دهید.",
                            color = colors.textMuted,
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        // در ثبت‌نام رمز اولیه همان شمارهٔ موبایل است
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = "رمز عبور اولیهٔ شما همان شمارهٔ موبایل است و پس از اولین ورود باید آن را تغییر دهید.",
                            color = colors.textMuted,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

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

                    // دکمه اصلی
                    ThreeDButton(
                        text = if (isLoading) "لطفاً صبر کنید..." else if (isLoginMode) "ورود" else "ثبت‌نام",
                        enabled = !isLoading,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onSubmit
                    )

                    if (isLoading) {
                        Spacer(Modifier.height(12.dp))
                        CircularProgressIndicator(
                            color = colors.gold,
                            modifier = Modifier.size(26.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }

            Spacer(Modifier.height(18.dp))

            // ============ تغییر حالت ورود/ثبت‌نام ============
            Text(
                text = if (isLoginMode) "حساب ندارید؟ ثبت‌نام کنید" else "قبلاً ثبت‌نام کرده‌اید؟ وارد شوید",
                color = colors.textMuted,
                fontSize = 14.sp,
                modifier = Modifier
                    .clickable(onClick = onToggleMode)
                    .padding(8.dp)
            )
        }
    }
}

// ============================================================
// Preview — با داده نمونه (بدون ViewModel → همیشه رندر می‌شود)
// ============================================================

@Preview(showBackground = false)
@Composable
private fun LoginScreenPreview() {
    MadahiThemeGreen(darkTheme = true) {
        LoginScreenContent(
            isLoginMode = true,
            fullName = "",
            mobile = "0912",
            password = "123456",
            isLoading = false,
            errorMessage = null,
            onFullNameChange = {},
            onMobileChange = {},
            onPasswordChange = {},
            onToggleMode = {},
            onSubmit = {})
    }
}

@Preview(showBackground = false)
@Composable
private fun RegisterScreenPreview() {
    MadahiThemeGreen(darkTheme = false) {
        LoginScreenContent(
            isLoginMode = false,
            fullName = "کاربر نمونه",
            mobile = "09123456789",
            password = "",
            isLoading = false,
            errorMessage = "شماره موبایل معتبر نیست",
            onFullNameChange = {},
            onMobileChange = {},
            onPasswordChange = {},
            onToggleMode = {},
            onSubmit = {})
    }
}
