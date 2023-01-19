@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.aracua.ui.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aracua.ui.NoteUi

@Composable
fun NoteCard(
    note: NoteUi,
    onCardClicked: (NoteUi) -> Unit
) {
    Card(
        onClick = {
                  onCardClicked(note)
        },
        modifier = Modifier.padding(24.dp)
    ) {
        Column {
            Text(
                text = note.title,
                maxLines = 1,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Text(
                text = note.body,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}