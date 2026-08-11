/**
 * Bottom Navigation With Docked FAB in Jetpack Compose
 * -----------------------------------------------------
 * وابستگی‌های لازم (build.gradle - ماژول app):
 *
 * implementation "androidx.navigation:navigation-compose:2.7.7"
 * implementation "androidx.compose.material:material:1.6.8"   // برای BottomAppBar با cutoutShape
 * implementation "androidx.compose.material:material-icons-extended:1.6.8"
 *
 * نکته: قابلیت cutoutShape داخل androidx.compose.material.BottomAppBar وجود دارد
 * (نه در Material3)، چون خودش داخلی از Canvas + Shape برای رسم فرورفتگی استفاده می‌کند.
 */

package com.khz.madahi.ui.views

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.khz.madahi.ui.theme.MadahiTheme
import com.khz.madahi.ui.theme.PrimaryGreenDark

// -------------------------------------------------------------------
// 1) مقصدهای صفحه (Screen destinations)
// -------------------------------------------------------------------
sealed class Screen(
    val route: String,
    val icon: ImageVector,
    val title: String
) {
    object Home : Screen(
        "home",
        Icons.Default.Home,
        "خانه"
    )

    object Search : Screen(
        "search",
        Icons.Default.Search,
        "جستجو"
    )

    object Notifications : Screen(
        "notifications",
        Icons.Default.Notifications,
        "اعلان‌ها"
    )

    object Profile : Screen(
        "profile",
        Icons.Default.Person,
        "پروفایل"
    )
}

// لیست آیتم‌هایی که در BottomNavigation نمایش داده می‌شوند
val bottomNavItems = listOf(
    Screen.Home,
    Screen.Search,
    Screen.Notifications,
    Screen.Profile
)

// -------------------------------------------------------------------
// 2) صفحه اصلی: Scaffold که همه چیز را کنار هم می‌چیند
// -------------------------------------------------------------------
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            AppBottomBar(navController = navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // اکشن دلخواه دکمه وسط، مثلا ناوبری به صفحه افزودن
                },
                backgroundColor = MaterialTheme.colors.secondary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "افزودن"
                )
            }
        },
        isFloatingActionButtonDocked = true,               // FAB داخل فرورفتگی می‌نشیند
        floatingActionButtonPosition = FabPosition.Center   // وسط BottomBar
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Search.route,
            modifier = Modifier
                .padding(innerPadding)
                .background(PrimaryGreenDark)
        ) {
            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.Search.route) { SearchScreen() }
            composable(Screen.Notifications.route) { NotificationsScreen() }
            composable(Screen.Profile.route) { ProfileScreen() }
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun MainScreenPreview() {
    MadahiTheme(darkTheme = false) {
        MainScreen()
    }
}


@Composable
fun AppBottomBar(navController: NavController) {
    BottomAppBar(
        cutoutShape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50)), // شکل دایره‌ای برای فرورفتگی
        backgroundColor = PrimaryGreenDark,
        elevation = 8.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        BottomNavigation(
            backgroundColor = Color.Transparent,
            elevation = 0.dp
        ) {
            bottomNavItems.forEachIndexed { index, screen ->
                // دقیقا وسط لیست، یک فاصله خالی برای جا دادن FAB می‌گذاریم
                if (index == 2) {
                    Spacer(
                        modifier = Modifier.weight(
                            1f,
                            true
                        )
                    )
                }

                BottomNavigationItem(
                    icon = {
                        Icon(
                            screen.icon,
                            contentDescription = screen.title
                        )
                    },
                    label = { Text(text = screen.title) },
                    selected = currentRoute == screen.route,
                    selectedContentColor = MaterialTheme.colors.secondary,
                    unselectedContentColor = Color.Gray,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    })
            }
        }
    }
}

// -------------------------------------------------------------------
// 4) صفحات نمونه (Placeholder Screens)
// -------------------------------------------------------------------
@Composable
fun HomeScreen() {
    Text(
        text = "صفحه خانه",
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun SearchScreen() {
    Text(
        text = "صفحه جستجو",
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun NotificationsScreen() {
    Text(
        text = "صفحه اعلان‌ها",
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun ProfileScreen() {
    Text(
        text = "صفحه پروفایل",
        modifier = Modifier.padding(16.dp)
    )
}