package com.example.aracua.ui

import com.example.aracua.data.Note

data class NoteUi(
    val id: Long,
    val title: String,
    val body: String,
    val timestamp: Long
)

fun Note.toUi() = NoteUi(
    id = this.id,
    title = this.title,
    body = this.body,
    timestamp = this.timestamp
)

fun NoteUi.toModel() = Note(
    id = this.id,
    title = this.title,
    body = this.body,
    timestamp = this.timestamp
)

