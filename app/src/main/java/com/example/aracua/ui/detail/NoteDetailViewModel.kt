package com.example.aracua.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.aracua.AracuaApplication
import com.example.aracua.data.db.AracuaDatabase
import com.example.aracua.data.repository.NotesRepository
import com.example.aracua.data.repository.NotesRoomRepository
import com.example.aracua.ui.toModel
import com.example.aracua.ui.toUi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NoteDetailViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
): AndroidViewModel(application) {

    private val id: Long? = savedStateHandle.get<String>(NOTE_ID_ARG)?.toLongOrNull()

    private var _uiState = MutableStateFlow(NoteDetailUiState())
    val uiState = _uiState.asStateFlow()

    private var _events = MutableSharedFlow<NoteDetailEvent>()
    val events = _events.asSharedFlow()

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

    init {
        viewModelScope.launch {
            _uiState.value = loadState()
        }
    }

    private suspend fun loadState(): NoteDetailUiState {
        Log.d("FKZ details VM", "id: $id")
        return id?.let { noteId ->
            repository.getNote(noteId)
                .filterNotNull()
                .map {
                    it.toUi()
                }
                .first().let { NoteDetailUiState(it) }
        } ?: NoteDetailUiState()
    }

    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(note = _uiState.value.note.copy(title = title))
    }

    fun updateBody(body: String) {
        _uiState.value = _uiState.value.copy(note = _uiState.value.note.copy(body = body))
    }
    
    suspend fun trySave() {

        val note = _uiState.value.note
        // validate
        var valid = true

        // Optimizable
        if (note.title.isEmpty()) {
            _uiState.value = _uiState.value.copy(isTitleError = true)
            valid = false
        } else {
            _uiState.value = _uiState.value.copy(isTitleError = false)
        }

        if (note.body.isEmpty()) {
            _uiState.value = _uiState.value.copy(isBodyError = true)
            valid = false
        } else {
            _uiState.value = _uiState.value.copy(isBodyError = false)
        }

        if (valid) {
            if (note.id == 0L) {
                repository.insert(note = _uiState.value.note.toModel())
            } else {
                repository.update(note = _uiState.value.note.toModel())
            }
            _events.emit(NoteDetailEvent.SavingSuccess)
        } else {
            _events.emit(NoteDetailEvent.SavingFailure)
        }
    }

    suspend fun delete() {
        val note = _uiState.value.note
        if (note.id != 0L) {

            repository.delete(note.toModel())
        }
    }
}