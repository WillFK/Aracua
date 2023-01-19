@file:OptIn(ExperimentalPermissionsApi::class)

package com.example.aracua.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aracua.R
import com.example.aracua.ui.NoteUi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@Composable
fun NoteListScreen(
    modifier: Modifier = Modifier,
    navigateToDetails: (NoteUi?) -> Unit = {},
    navigateToNote: (NoteUi) -> Unit = {},
    viewModel: NoteListViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier.fillMaxSize()
    ) {

        if (uiState.notes.isEmpty()) {
            Text(
                text = stringResource(R.string.no_notes_yet),
                fontSize = 24.sp,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        } else {
            NoteListBody(
                items = uiState.notes,
                onCardClicked = navigateToNote
            )
        }

        FloatingActionButton(
            onClick = {
                      scope.launch {
                          navigateToDetails(null)
                      }
            },
            modifier = Modifier
                .padding(16.dp, bottom = 32.dp)
                .align(Alignment.BottomCenter)
        ) {

            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(R.string.add_note)
            )
        }
    }
}

@Composable
fun NoteListBody(
    items: List<NoteUi>,
    onCardClicked: (NoteUi) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        items(items = items, key = { it.id }) {note ->
            NoteCard(
                note = note,
                onCardClicked = onCardClicked
            )
            Divider()
        }
    }
}

@Preview(showBackground = false)
@Composable
fun Preview() {
    NoteCard(
        note = NoteUi(
            id = 0,
            title = "Card title",
            body = """
                    Minha terra tem palmeiras
                    Onde canta o Sabiá,
                    As aves, que aqui gorjeiam,
                    Não gorjeiam como lá.
                """.trimIndent(),
            timestamp = 1673894023494
        ),
        onCardClicked = {}
    )
}

const val NOTE_LIST_DESTINATION = "note_list"
