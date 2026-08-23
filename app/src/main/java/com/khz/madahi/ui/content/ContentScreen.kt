// ui/content/ContentScreen.kt
package com.khz.madahi.ui.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.khz.madahi.data.local.database.AppDatabase
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.data.remote.api.RetrofitClient
import com.khz.madahi.data.remote.repository.ContentRepository
import com.khz.madahi.models.Category
import com.khz.madahi.models.Content
import com.khz.madahi.ui.common.BottomBarActions
import com.khz.madahi.ui.common.BottomTab
import com.khz.madahi.ui.common.EmptyContentScreen
import com.khz.madahi.ui.common.ErrorContentScreen
import com.khz.madahi.ui.components.BaseScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentScreen(
    category: Category?,
    bottomBarActions: BottomBarActions,
    onNavigateBack: () -> Unit,
    onNavigateToContentDetail: (Int) -> Unit
) {
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }
    var contentToDelete by remember { mutableStateOf<Content?>(null) }

    // ============ ViewModel ============
    val viewModel: ContentViewModel = viewModel(
        factory = ContentViewModelFactory(
            preferencesManager = PreferencesManager(context),
            contentRepository = ContentRepository(
                apiService = RetrofitClient.apiService,
                contentDao = AppDatabase.getInstance(context)
                    .contentDAO(),
                favoriteDao = AppDatabase.getInstance(context)
                    .favoriteDAO()
            ),
            appDatabase = AppDatabase.getInstance(context),
            category = category
        )
    )

    // ✅ وقتی به صفحه برمی‌گردیم (مثلاً بعد از حذف محتوا از صفحه جزئیات)، لیست را رفرش کن
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refresh()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val uiState by viewModel.uiState.collectAsState()
    val contents by viewModel.contents.collectAsState()
    val isDialogVisible by viewModel.isDialogVisible.collectAsState()
    val editingContent by viewModel.editingContent.collectAsState()
    val dialogSubject by viewModel.dialogSubject.collectAsState()
    val dialogAnswer by viewModel.dialogAnswer.collectAsState()
    val dialogContentText by viewModel.dialogContent.collectAsState()
    val isNoheh by viewModel.isNoheh.collectAsState()
    val subjectError by viewModel.subjectError.collectAsState()
    val contentError by viewModel.contentError.collectAsState()
    val dialogMessage by viewModel.dialogMessage.collectAsState()

    // ============ Scaffold ============

//    (
//        bottomBarActions = BottomBarActions(),
//        title = viewModel.getCategoryTitle(),
//        subtitle = "",
//        selectedBottomTab = BottomTab.CATEGORY,
//        onHeaderBottonClicked = { onNavigateBack() },
//    )
    BaseScreen(
        bottomBarActions = bottomBarActions,
        title = viewModel.getCategoryTitle(),
        subtitle = "",
        selectedBottomTab = BottomTab.CATEGORY,
        onHeaderBottonClicked = { onNavigateBack() },
        onFabAddBottonClicked = {
            viewModel.showAddDialog()
        },
    ) {
        Column {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                when (uiState) {
                    is ContentUiState.Loading -> {
                        LoadingContentScreen()
                    }

                    is ContentUiState.Success -> {
                        if (contents.isEmpty()) {
                            EmptyContentScreen(
                                onAddClick = viewModel::showAddDialog
                            )
                        } else {
                            Column {
                                val distinctContents = contents.distinctBy { it.id }
                                    .sortedByDescending { it.id }
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .padding(horizontal = 30.dp),
                                    contentPadding = PaddingValues(horizontal = 30.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(
                                        items = distinctContents,
                                        key = { "${it.id}_${it.idContent}" }) { content ->
                                        ContentItem(
                                            content = content,
                                            onClick = {
                                                onNavigateToContentDetail(content.id)
                                            },
                                            onEditClick = { viewModel.showEditDialog(content) },
                                            onDeleteClick = {
                                                contentToDelete = content
                                                showDeleteDialog = true
                                            })
                                    }
                                }
                            }
                        }
                    }

                    is ContentUiState.Error   -> {
                        ErrorContentScreen(
                            message = (uiState as ContentUiState.Error).message,
                            onRetry = viewModel::refresh
                        )
                    }
                }
            }
        }
    }

    // ============ Add/Edit Dialog ============
    if (isDialogVisible) {
        ContentDialog(
            subject = dialogSubject,
            answer = dialogAnswer,
            contentText = dialogContentText,
            isNoheh = isNoheh,
            isEditMode = editingContent != null,
            subjectError = subjectError,
            contentError = contentError,
            errorMessage = dialogMessage,
            onSubjectChange = viewModel::updateSubject,
            onAnswerChange = viewModel::updateAnswer,
            onContentChange = viewModel::updateContent,
            onContentTypeChange = viewModel::updateContentType,
            onDismiss = viewModel::hideDialog,
            onConfirm = {
                if (editingContent != null) {
                    viewModel.editContent()
                } else {
                    viewModel.addContent()
                }
            })
    }

    // ============ Delete Dialog ============
    if (showDeleteDialog && contentToDelete != null) {
        DeleteContentDialog(
            content = contentToDelete!!,
            onDelete = {
                viewModel.deleteContent(contentToDelete!!)
                showDeleteDialog = false
                contentToDelete = null
            },
            onDismiss = {
                showDeleteDialog = false
                contentToDelete = null
            })
    }
}

//// ============ Preview ============
//@Preview(showBackground = true)
//@Composable
//fun ContentScreenPreview() {
//    MaterialTheme {
//        ContentScreen(
//            category = Category(
//                id = "1",
//                userId = "1",
//                title = "نوحه‌های محرم",
//                description = "مجموعه نوحه‌های مناسبتی"
//            ),
//            onNavigateBack = {},
//            onNavigateToContentDetail = {})
//    }
//}