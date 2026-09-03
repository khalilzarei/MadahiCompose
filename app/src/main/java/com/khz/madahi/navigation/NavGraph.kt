// navigation/NavGraph.kt
package com.khz.madahi.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.khz.madahi.data.local.database.AppDatabase
import com.khz.madahi.helper.GUEST_USER_ID
import com.khz.madahi.models.Category
import com.khz.madahi.models.Content
import com.khz.madahi.ui.about.AboutScreen
import com.khz.madahi.ui.category.CategoryScreen
import com.khz.madahi.ui.common.BottomBarActions
import com.khz.madahi.ui.content.ContentScreen
import com.khz.madahi.ui.contentdetail.ContentDetailScreen
import com.khz.madahi.ui.favorite.FavoritesScreen
import com.khz.madahi.ui.appselection.AppSelectionScreen
import com.khz.madahi.ui.booklet.BookletScreen
import com.khz.madahi.ui.booklet.BookletDetailScreen
import com.khz.madahi.ui.intro.IntroScreen
import com.khz.madahi.ui.login.LoginScreen
import com.khz.madahi.ui.message.MessageScreen
import com.khz.madahi.ui.poems.PoemsScreen
import com.khz.madahi.ui.profile.ProfileScreen
import com.khz.madahi.ui.setting.SettingScreen
import com.khz.madahi.ui.splash.SplashScreen

// ============ Routes ============
sealed class Screen(val route: String) {
    object SplashScreen : Screen("splash")
    object IntroScreen : Screen("intro")
    object LoginScreen : Screen("login")
    object AppSelectionScreen : Screen("appSelection")
    object BookletScreen : Screen("booklet")
    object BookletDetail : Screen("bookletDetail/{sectionId}/{sectionTitle}") {
        fun passSection(sectionId: Int, sectionTitle: String): String {
            return "bookletDetail/$sectionId/$sectionTitle"
        }
    }
    object CategoryScreen : Screen("category")

    // ✅ مسیر Content با categoryId
    object ContentScreen : Screen("content/{categoryId}") {
        fun passCategory(categoryId: Int): String {
            return "content/$categoryId"
        }
    }

    // ✅ مسیر ContentDetail با contentId
    object ContentDetail : Screen("contentDetail/{contentId}") {
        fun passContent(contentId: Int): String {
            return "contentDetail/$contentId"
        }
    }

    object MessageScreen : Screen("message")
    object SettingScreen : Screen("setting")
    object AboutScreen : Screen("about")
    object FavoritesScreen : Screen("favorites")
    object ProfileScreen : Screen("profile")
    object PoemsScreen : Screen("poems")   // 🎧 شعر و سبک (نسخه پرو)
}

@Composable
fun NavGraph(
    startDestination: String = Screen.SplashScreen.route
) {
    val navController = rememberNavController()
    val context = LocalContext.current

    val bottomBarActions = remember(navController) {
        BottomBarActions(
            onBackClick = {
                navController.popBackStack()
            },
            onHomeClick = {
                navController.navigate(Screen.CategoryScreen.route)
            },
            onCategoryClick = {
                navController.navigate(Screen.CategoryScreen.route)
            },
            onFavoritesClick = {
                navController.navigate(Screen.FavoritesScreen.route)
            },
            onSettingsClick = {
                navController.navigate(Screen.SettingScreen.route)
            },
            onProfileClick = {
                navController.navigate(Screen.ProfileScreen.route)
            },
            onAboutClick = {
                navController.navigate(Screen.AboutScreen.route)
            },
            onMessageClick = {
                navController.navigate(Screen.MessageScreen.route)
            },
            onPoemsClick = {
                navController.navigate(Screen.PoemsScreen.route)
            },
        )
    }


    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // ============ Splash ============
        composable(Screen.SplashScreen.route) {
            SplashScreen(
                onNavigateToIntro = {
                    navController.navigate(Screen.IntroScreen.route) {
                        popUpTo(Screen.SplashScreen.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.LoginScreen.route) {
                        popUpTo(Screen.SplashScreen.route) { inclusive = true }
                    }
                },
                onNavigateToAppSelection = {
                    navController.navigate(Screen.AppSelectionScreen.route) {
                        popUpTo(Screen.SplashScreen.route) { inclusive = true }
                    }
                })
        }

        // ============ Intro ============
        composable(Screen.IntroScreen.route) {
            IntroScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.LoginScreen.route) {
                        popUpTo(Screen.IntroScreen.route) { inclusive = true }
                    }
                },
                onNavigateToAppSelection = {
                    navController.navigate(Screen.AppSelectionScreen.route) {
                        popUpTo(Screen.IntroScreen.route) { inclusive = true }
                    }
                })
        }

        // ============ Login ============
        composable(Screen.LoginScreen.route) {
            LoginScreen(
                onNavigateToAppSelection = {
                    navController.navigate(Screen.AppSelectionScreen.route) {
                        popUpTo(Screen.LoginScreen.route) { inclusive = true }
                    }
                })
        }

        // ============ App Selection ============
        composable(Screen.AppSelectionScreen.route) {
            AppSelectionScreen(
                onNavigateToCategory = {
                    navController.navigate(Screen.CategoryScreen.route) {
                        popUpTo(Screen.AppSelectionScreen.route) { inclusive = false }
                    }
                },
                onNavigateToBooklet = {
                    navController.navigate(Screen.BookletScreen.route) {
                        popUpTo(Screen.AppSelectionScreen.route) { inclusive = false }
                    }
                }
            )
        }

        // ============ Booklet ============
        composable(Screen.BookletScreen.route) {
            BookletScreen(
                onNavigateBack = {
                    navController.popBackStack(Screen.AppSelectionScreen.route, inclusive = false)
                },
                onNavigateToSection = { sectionId, sectionTitle ->
                    navController.navigate(Screen.BookletDetail.passSection(sectionId, sectionTitle))
                }
            )
        }

        // ============ Booklet Detail ============
        composable(
            route = Screen.BookletDetail.route,
            arguments = listOf(
                androidx.navigation.navArgument("sectionId") { type = NavType.IntType },
                androidx.navigation.navArgument("sectionTitle") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val sectionId = backStackEntry.arguments?.getInt("sectionId") ?: 0
            val sectionTitle = backStackEntry.arguments?.getString("sectionTitle") ?: ""

            BookletDetailScreen(
                sectionId = sectionId,
                sectionTitle = sectionTitle,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // ============ Category ============
        composable(Screen.CategoryScreen.route) {
            CategoryScreen(
                bottomBarActions = bottomBarActions,
                onNavigateBack = {
                    navController.popBackStack(Screen.AppSelectionScreen.route, inclusive = false)
                },
                onNavigateToContent = { category ->
                    // ✅ ارسال categoryId به جای کل category
                    navController.navigate(Screen.ContentScreen.passCategory(category.id))
                })
        }

        // ============ Content ============
        composable(
            route = Screen.ContentScreen.route,
            arguments = listOf(
                navArgument("categoryId") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")
                    ?: ""

            val categoryLoadState = produceState<Pair<Boolean, Category?>>(
                initialValue = false to null,
                key1 = categoryId
            ) {
                value = try {
                    true to AppDatabase.getInstance(context)
                        .categoryDAO()
                        .getById(categoryId.toInt())
                } catch (e: Exception) {
                    true to null
                }
            }
            val (isCategoryLoaded, category) = categoryLoadState.value

            if (!isCategoryLoaded) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else {
                ContentScreen(
                    category = category,
                    onNavigateBack = { navController.popBackStack() },
                    bottomBarActions = bottomBarActions,
                    onNavigateToContentDetail = { contentId ->
                        navController.navigate(Screen.ContentDetail.passContent(contentId))
                    },
                )
            }
        }

        // ============ ContentDetail ============
        composable(
            route = Screen.ContentDetail.route,
            arguments = listOf(
                navArgument("contentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val contentId = backStackEntry.arguments?.getString("contentId")
                ?.toInt()
                    ?: GUEST_USER_ID

            val contentLoadState = produceState<Pair<Boolean, Content?>>(
                initialValue = false to null,
                key1 = contentId
            ) {
                value = try {
                    true to AppDatabase.getInstance(context)
                        .contentDAO()
                        .getById(contentId)
                } catch (e: Exception) {
                    true to null
                }
            }
            val (isContentLoaded, detailContent) = contentLoadState.value

            if (!isContentLoaded) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else {
                ContentDetailScreen(
                    content = detailContent,
                    onNavigateBack = { navController.popBackStack() },
                    onFavoriteChanged = {
                        // وقتی علاقه‌مندی تغییر کرد، صفحه‌ی Favorites با
                        // LifecycleEventObserver خودش (ON_RESUME) رفرش می‌شود.
                    },
                )
            }
        }

        // ============ Message ============
        composable(Screen.ProfileScreen.route) {
            ProfileScreen(
                bottomBarActions = bottomBarActions,
                onNavigateToLogin = {
                    navController.navigate(Screen.LoginScreen.route)
                })
        }

        // ============ Message ============
        composable(Screen.MessageScreen.route) {
            MessageScreen(
                bottomBarActions = bottomBarActions,
            )
        }

        // ============ Poems (🎧 شعر و سبک — نسخه پرو) ============
        composable(Screen.PoemsScreen.route) {
            PoemsScreen(
                bottomBarActions = bottomBarActions,
                onNavigateBack = { navController.popBackStack() })
        }

        // ============ Setting ============
        composable(Screen.SettingScreen.route) {
            SettingScreen(
                bottomBarActions = bottomBarActions,
            )

        }

        // ============ About ============
        composable(Screen.AboutScreen.route) {
            AboutScreen(
                bottomBarActions = bottomBarActions,
            )
        }

        // ============ Favorites ============
        composable(Screen.FavoritesScreen.route) {
            FavoritesScreen(
                bottomBarActions = bottomBarActions,
                onNavigateToContentDetail = { content ->
                    navController.navigate(Screen.ContentDetail.passContent(content.id))
                })
        }
    }
}
