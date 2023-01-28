@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalPermissionsApi::class
)

package com.example.aracua.ui.detail

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aracua.R
import com.example.aracua.stt.SpeechToTextContract
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch

@Composable
fun NoteDetailScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NoteDetailViewModel = viewModel()
) {

    val permissionState = rememberPermissionState(
        permission = Manifest.permission.RECORD_AUDIO
    )
    SideEffect {
        permissionState.launchPermissionRequest()
    }

    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = SpeechToTextContract(stringResource(id = R.string.note_recording)),
        onResult = {
            viewModel.updateBody(it.toString())
        }
    )

    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        DetailsBody(
            uiState = uiState,
            onValueChangeTitle = viewModel::updateTitle,
            onValueChangeBody = viewModel::updateBody
        )

        ActionButtons(
            onRecord = {
                if (permissionState.status.isGranted) {
                    speechRecognizerLauncher.launch(Unit)
                } else {
                    permissionState.launchPermissionRequest()
                }
            },
            onSave = {
                     scope.launch {
                         viewModel.trySave()
                         navigateBack()
                     }
            },
            onDelete = {
                       scope.launch {
                           viewModel.delete()
                           navigateBack()
                       }
            },
            modifier.align(Alignment.BottomEnd)
        )
    }
}

@Composable
fun DetailsBody(
    uiState: NoteDetailUiState,
    onValueChangeTitle: (String) -> Unit,
    onValueChangeBody: (String) -> Unit
) {

    Column {

        OutlinedTextField(
            value = uiState.note.title,
            onValueChange = onValueChangeTitle,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = uiState.isTitleError,
            label = {
                if (uiState.isTitleError) {
                    Text(text = stringResource(id = R.string.note_invalid_value))
                } else {
                    Text(text = stringResource(id = R.string.note_title))
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.note.body,
            onValueChange = onValueChangeBody,
            modifier = Modifier.fillMaxSize(),
            enabled = true,
            isError = uiState.isBodyError,
            label = {
                if (uiState.isBodyError) {
                    Text(text = stringResource(id = R.string.note_invalid_value))
                } else {
                    Text(text = stringResource(id = R.string.note_body))
                }
            }
        )
    }
}

@Composable
fun ActionButtons(
    onRecord: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {

        FloatingActionButton(
            onClick = onRecord,
            modifier = Modifier
                .padding(16.dp)
        ) {

            Icon(
                painterResource(id = R.drawable.baseline_mic),
                contentDescription = stringResource(R.string.note_record)
            )
        }

        FloatingActionButton(
            onClick = onDelete,
            modifier = Modifier
                .padding(16.dp)
        ) {

            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = stringResource(R.string.add_note)
            )
        }

        FloatingActionButton(
            onClick = onSave,
            modifier = Modifier
                .padding(16.dp, bottom = 32.dp)
        ) {

            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = stringResource(R.string.add_note)
            )
        }
    }
}

const val NOTE_ID_ARG = "note_id_arg"
const val NOTE_DETAIL_DESTINATION = "note_details_destination"
const val NOTE_DETAIL_DESTINATION_WITH_ARGS = "${NOTE_DETAIL_DESTINATION}?{$NOTE_ID_ARG}"
