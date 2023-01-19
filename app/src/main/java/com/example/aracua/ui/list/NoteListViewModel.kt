package com.example.aracua.ui.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aracua.AracuaApplication
import com.example.aracua.data.db.AracuaDatabase
import com.example.aracua.data.repository.NotesRepository
import com.example.aracua.data.repository.NotesRoomRepository
import com.example.aracua.ui.toUi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class NoteListViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * this is definitely not the best way to "inject" a repository to the ViewModel
     * TODO proper DI or AppContainer
     */
    private val repository: NotesRepository by lazy {
        NotesRoomRepository(
            AracuaDatabase
                .getDatabase(
                    this.getApplication<AracuaApplication>().applicationContext
                ).noteDao()
        )
    }

    val uiState: StateFlow<NoteListUiState> =
        repository.getNotes().map { NoteListUiState(notes = it.map { it.toUi() }) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = NoteListUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}