package com.khz.madahi.ui.components

data class MadahiCategory(
    val id: Long,
    val title: String
)

data class MadahiPoem(
    val id: Long,
    val categoryId: Long,
    val title: String,
    val text: String
)

data class MadahiDialog(
    val id: Long,
    val poemId: Long,
    val text: String,
    val beforeVerse: Int,
    val afterVerse: Int
)