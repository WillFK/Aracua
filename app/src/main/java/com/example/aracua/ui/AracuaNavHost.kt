package com.example.aracua.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.aracua.ui.detail.NOTE_DETAIL_DESTINATION
import com.example.aracua.ui.detail.NOTE_DETAIL_DESTINATION_WITH_ARGS
import com.example.aracua.ui.detail.NOTE_ID_ARG
import com.example.aracua.ui.detail.NoteDetailScreen
import com.example.aracua.ui.list.NOTE_LIST_DESTINATION
import com.example.aracua.ui.list.NoteListScreen

@ExperimentalMaterial3Api
@Composable
fun AracuaNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = NOTE_LIST_DESTINATION,
        modifier = modifier
    ) {

        composable(route = NOTE_LIST_DESTINATION) {
            NoteListScreen(
                navigateToDetails = { note ->
                    if (note == null) {
                        navController.navigate(NOTE_DETAIL_DESTINATION)
                    } else {
                        navController.navigate("$NOTE_DETAIL_DESTINATION?${note.id}")
                    }
                },
                navigateToNote = {note ->
                    navController.navigate("$NOTE_DETAIL_DESTINATION?${note.id}")
                }
            )
        }

        composable(
            route = NOTE_DETAIL_DESTINATION_WITH_ARGS,
            arguments = listOf(
                navArgument(NOTE_ID_ARG) {
                    nullable = true
                    defaultValue = null
                    type = NavType.StringType

                }
            )
        ) {
            NoteDetailScreen(
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}