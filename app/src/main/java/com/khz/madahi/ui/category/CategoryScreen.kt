// ui/category/CategoryScreen.kt
package com.khz.madahi.ui.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.khz.madahi.data.local.database.AppDatabase
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.models.Category
import com.khz.madahi.ui.common.BottomBarActions
import com.khz.madahi.ui.common.BottomTab
import com.khz.madahi.ui.common.EmptyContentScreen
import com.khz.madahi.ui.common.ErrorContentScreen
import com.khz.madahi.ui.components.BaseScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    onNavigateToContent: (Category) -> Unit,
    bottomBarActions: BottomBarActions,
    onNavigateBack: () -> Unit = {},
    onNavigateHome: () -> Unit = onNavigateBack,
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
    val titleError by viewModel.titleError.collectAsState()
    val descriptionError by viewModel.descriptionError.collectAsState()
    val dialogMessage by viewModel.dialogMessage.collectAsState()
    val listState = rememberLazyListState()

    // ============ Dialog State ============
    var showDeleteDialog by remember { mutableStateOf(false) }
    var categoryToDelete by remember { mutableStateOf<Category?>(null) }

    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    BaseScreen(
        bottomBarActions = bottomBarActions,
        title = "دسته بندی ها",
        subtitle = "",
        selectedBottomTab = BottomTab.CATEGORY,
        onHeaderBottonClicked = { onNavigateBack() },
        onHeaderHomeClicked = onNavigateHome,
        isCategory = true,
        onFabAddBottonClicked = {
            viewModel.showAddCategoryDialog()
        },
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
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        val distinctCategories = categories.distinctBy { it.id }
                            .sortedByDescending { it.id }
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 15.dp),
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
                                        // کلیک روی سه نقطه -> باز شدن CategoryDialog
                                        viewModel.showEditCategoryDialog(category)

                                    },
                                )
                            }
                        }

                        LaunchedEffect(categories) {
                            if (categories.isNotEmpty()) {
                                listState.scrollToItem(0)
                            }
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

    // ============ Add/Edit Dialog ============
    if (isDialogVisible) {
        CategoryDialog(
            title = dialogTitle,
            description = dialogDescription,
            isEditMode = editingCategory != null,
            titleError = titleError,
            descriptionError = descriptionError,
            errorMessage = dialogMessage,
            onTitleChange = viewModel::updateDialogTitle,
            onDescriptionChange = viewModel::updateDialogDescription,
            onDismiss = viewModel::hideDialog,
            onConfirm = {
                if (editingCategory != null) {
                    viewModel.editCategory()
                } else {
                    viewModel.addCategory()
                }
            },
            onDelete = {
                // ۱. ابتدا دسته فعلی را ذخیره می‌کنیم
                val targetCategory = editingCategory

                // ۲. دیالوگ ویرایش را می‌بندیم
                viewModel.hideDialog()

                // ۳. دیالوگ تأیید حذف را باز می‌کنیم
                if (targetCategory != null) {
                    categoryToDelete = targetCategory
                    showDeleteConfirmDialog = true
                }
            })
    }

    // ============ Delete Dialog ============
    if (showDeleteConfirmDialog && categoryToDelete != null) {
        DeleteCategoryDialog(
            category = categoryToDelete!!,
            onDelete = {
                viewModel.deleteCategory(categoryToDelete!!)
                showDeleteConfirmDialog = false
                categoryToDelete = null
            },
            onDismiss = {
                showDeleteConfirmDialog = false
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
//    MadahiThemeGreen(darkTheme = false) {
//        CategoryScreen(
//            onNavigateToContent = {},
//            bottomBarActions = BottomBarActions(),
//        )
//    }
//}
//
//@Preview(
//    showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//)
//@Composable
//fun CategoryScreenPreviewDark() {
//    MadahiThemeGreen(darkTheme = true) {
//        CategoryScreen(
//            onNavigateToContent = {},
//            bottomBarActions = BottomBarActions(),
//        )
//    }
//}
