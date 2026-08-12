// ui/category/CategoryScreen.kt
package com.khz.madahi.ui.category

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.khz.madahi.R
import com.khz.madahi.data.local.database.AppDatabase
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.models.Category
import com.khz.madahi.ui.common.BottomBarActions
import com.khz.madahi.ui.common.BottomBarView
import com.khz.madahi.ui.common.BottomTab
import com.khz.madahi.ui.common.EmptyContentScreen
import com.khz.madahi.ui.common.ErrorContentScreen
import com.khz.madahi.ui.theme.PrimaryGreenDark
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    onNavigateToContent: (Category) -> Unit,
    bottomBarActions: BottomBarActions,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val viewModel: CategoryViewModel = viewModel(
        factory = CategoryViewModelFactory(
            preferencesManager = PreferencesManager(context),
            appDatabase = AppDatabase.getInstance(context)
        )
    )

    val uiState by viewModel.uiState.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val isDialogVisible by viewModel.isDialogVisible.collectAsState()
    val editingCategory by viewModel.editingCategory.collectAsState()
    val dialogTitle by viewModel.dialogTitle.collectAsState()
    val dialogDescription by viewModel.dialogDescription.collectAsState()
    val listState = rememberLazyListState()

    // ============ Dialog State ============
    var showDeleteDialog by remember { mutableStateOf(false) }
    var categoryToDelete by remember { mutableStateOf<Category?>(null) }

    // ============ Scaffold ============
    Scaffold(
        topBar = {
            CategoryTopBar(
                onMessagesClick = bottomBarActions.onMessageClick
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = viewModel::showAddCategoryDialog,
                containerColor = Color(0xFF074634),
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .size(70.dp)
                    .border(
                        width = 3.dp,
                        color = Color(0xFFD4AF37),  // طلایی برای حاشیه
                        shape = CircleShape
                    )
            ) {
                Image(
                    painter = painterResource(
                        R.drawable.ic_fab_border // ✅ تصویر دارک
                    ),
                    contentDescription = "Splash Background",
                    modifier = Modifier.size(60.dp),
                    contentScale = ContentScale.FillBounds

                )
                Icon(
                    imageVector = Icons.Default.Add  // 🤍 خالی
                    ,
                    contentDescription = "افزودن به علاقه‌مندی‌ها",
                    modifier = Modifier.size(36.dp),
                    tint = Color.White
                )
            }
        },
        bottomBar = {
            BottomBarView(
                selectedTab = BottomTab.CATEGORY,
                actions = bottomBarActions,
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF07130c).copy(0.9f),
                            PrimaryGreenDark.copy(0.5f),
                            Color(0xFF07130c).copy(0.5f),
                        )
                    )
                )
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is CategoryUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                is CategoryUiState.Success -> {
                    if (categories.isEmpty()) {
                        // ✅ استفاده از فایل مشترک EmptyContentScreen
                        EmptyContentScreen(
                            title = "هیچ دسته‌بندی وجود ندارد",
                            subtitle = "اولین دسته‌بندی خود را اضافه کنید",
                            buttonText = "افزودن دسته‌بندی جدید",
                            onAddClick = viewModel::showAddCategoryDialog
                        )
                    } else {
                        val distinctCategories = categories.distinctBy { it.id }
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    top = 5.dp,
                                    bottom = 5.dp
                                ),
                            contentPadding = PaddingValues(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            state = listState,
                        ) {
                            items(
                                items = distinctCategories,
                                key = { it.id }) { category ->
                                CategoryItem(
                                    category = category,
                                    onClick = {
                                        coroutineScope.launch {
                                            onNavigateToContent(category)
                                        }
                                    },
                                    onEditClick = {
                                        viewModel.showEditCategoryDialog(category)
                                    },
                                    onDeleteClick = {
                                        categoryToDelete = category
                                        showDeleteDialog = true
                                    })
                            }
                        }

                        LaunchedEffect(categories) {
                            if (categories.isNotEmpty()) {
                                listState.scrollToItem(0)
                            }
                        }
                    }
                }

                is CategoryUiState.Error   -> {
                    // ✅ استفاده از فایل مشترک ErrorContentScreen
                    ErrorContentScreen(
                        message = (uiState as CategoryUiState.Error).message,
                        onRetry = viewModel::loadCategories
                    )
                }
            }
        }
    }

    // ============ Add/Edit Dialog ============
    if (isDialogVisible) {
        CategoryDialog(
            title = dialogTitle,
            description = dialogDescription,
            isEditMode = editingCategory != null,
            onTitleChange = viewModel::updateDialogTitle,
            onDescriptionChange = viewModel::updateDialogDescription,
            onDismiss = viewModel::hideDialog,
            onConfirm = {
                if (editingCategory != null) {
                    viewModel.editCategory()
                } else {
                    viewModel.addCategory()
                }
            })
    }

    // ============ Delete Dialog ============
    if (showDeleteDialog && categoryToDelete != null) {
        DeleteCategoryDialog(
            category = categoryToDelete!!,
            onDelete = {
                viewModel.deleteCategory(categoryToDelete!!)
                showDeleteDialog = false
                categoryToDelete = null
            },
            onDismiss = {
                showDeleteDialog = false
                categoryToDelete = null
            })
    }
}

//@Preview(
//    showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_NO,
//)
//@Composable
//fun CategoryScreenPreview() {
//    MadahiTheme(darkTheme = false) {
//        CategoryScreen(
//            onNavigateToContent = {},
//            onNavigateToMessage = {},
//            onNavigateToSetting = {},
//            onNavigateToFavorites = {},
//            onNavigateToAbout = {},
//        )
//    }
//}