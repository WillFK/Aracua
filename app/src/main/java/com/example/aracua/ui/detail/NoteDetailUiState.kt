package com.example.aracua.ui.detail

import com.example.aracua.ui.NoteUi

data class NoteDetailUiState(
    val note: NoteUi =
        NoteUi(
            id = 0,
            title = "",
            body = "",
            timestamp = System.currentTimeMillis()
        ),
    val isTitleError: Boolean = false,
    val isBodyError: Boolean = false
)