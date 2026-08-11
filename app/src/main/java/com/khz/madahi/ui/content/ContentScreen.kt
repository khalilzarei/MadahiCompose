// ui/content/ContentScreen.kt
package com.khz.madahi.ui.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.khz.madahi.data.local.database.AppDatabase
import com.khz.madahi.data.local.preferences.PreferencesManager
import com.khz.madahi.data.remote.api.RetrofitClient
import com.khz.madahi.data.remote.repository.ContentRepository
import com.khz.madahi.models.Category
import com.khz.madahi.models.Content
import com.khz.madahi.ui.common.EmptyContentScreen
import com.khz.madahi.ui.common.ErrorContentScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentScreen(
    category: Category?,
    onNavigateBack: () -> Unit,
    onNavigateToContentDetail: (String) -> Unit
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

    val uiState by viewModel.uiState.collectAsState()
    val contents by viewModel.contents.collectAsState()
    val isDialogVisible by viewModel.isDialogVisible.collectAsState()
    val editingContent by viewModel.editingContent.collectAsState()
    val dialogSubject by viewModel.dialogSubject.collectAsState()
    val dialogAnswer by viewModel.dialogAnswer.collectAsState()
    val dialogContentText by viewModel.dialogContent.collectAsState()
    val isNoheh by viewModel.isNoheh.collectAsState()

    // ============ Scaffold ============
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = viewModel.getCategoryTitle(),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "بازگشت",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = viewModel::showAddDialog) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "افزودن محتوا",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                horizontal = 12.dp,
                                vertical = 8.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(
                                items = contents,
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

                is ContentUiState.Error   -> {
                    ErrorContentScreen(
                        message = (uiState as ContentUiState.Error).message,
                        onRetry = viewModel::refresh
                    )
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

// ============ Preview ============
@Preview(showBackground = true)
@Composable
fun ContentScreenPreview() {
    MaterialTheme {
        ContentScreen(
            category = Category(
                id = "1",
                userId = "1",
                title = "نوحه‌های محرم",
                description = "مجموعه نوحه‌های مناسبتی"
            ),
            onNavigateBack = {},
            onNavigateToContentDetail = {})
    }
}