// ui/splash/SplashScreen.kt
package com.khz.madahi.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.khz.madahi.R
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.data.remote.api.RetrofitClient
import com.khz.madahi.data.remote.repository.AppInfoRepository
import com.khz.madahi.ui.views.UpdateDialog
import com.khz.madahi.utils.NetworkChecker

@Composable
fun SplashScreen(
    onNavigateToIntro: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToAppSelection: () -> Unit
) {
    val context = LocalContext.current
    var showUpdateDialog by remember { mutableStateOf(false) }
    var updateUrl by remember { mutableStateOf("") }


    val preferencesManager = PreferencesManager(context)

    val isDarkTheme = preferencesManager.isNightMode

    val viewModelFactory = remember {
        SplashViewModelFactory(
            preferencesManager = PreferencesManager(context),
            appInfoRepository = AppInfoRepository(RetrofitClient.apiService),
            networkChecker = NetworkChecker(context)
        )
    }

    val viewModel: SplashViewModel = viewModel(
        factory = viewModelFactory
    )

    val state by viewModel.state.collectAsState()

    // بررسی وضعیت برای نمایش Dialog
    LaunchedEffect(state) {
        when (state) {
            is SplashState.UpdateRequired -> {
                showUpdateDialog = true
                updateUrl = (state as SplashState.UpdateRequired).appInfo.appUrl ?: ""
            }
            is SplashState.NavigateToIntro -> onNavigateToIntro()
            is SplashState.NavigateToLogin -> onNavigateToLogin()
            is SplashState.NavigateToCategory -> onNavigateToAppSelection()
            else -> Unit
        }
    }

    // ============ Dialog آپدیت ============
    UpdateDialog(
        showDialog = showUpdateDialog,
        updateUrl = updateUrl,
        onDismiss = { showUpdateDialog = false },
        onUpdate = {
            showUpdateDialog = false
        },
        onLater = {
            showUpdateDialog = false
            when {
                PreferencesManager(context).isFirstTimeLaunch -> onNavigateToIntro()
                !PreferencesManager(context).isLoggedIn -> onNavigateToLogin()
                else -> onNavigateToAppSelection()
            }
        }
    )

    // ============ محتوای صفحه Splash ============
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // ✅ بک‌گراند بر اساس تم
        Image(
            painter = painterResource(
                id = if (isDarkTheme) {
                    R.drawable.bg_splash_dark  // ✅ تصویر دارک
                } else {
                    R.drawable.bg_splash_light  // ✅ تصویر لایت
                }
            ),
            contentDescription = "Splash Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )



        // ============ محتوای اصلی ============
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Loading
            if (state is SplashState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Error
            if (state is SplashState.Error) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = (state as SplashState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "در حال انتقال به صفحه اصلی...",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }


    }
}

@Preview(showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(
        {},
        {},
        {},
    )
}