package com.pp.masterand.game

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.pp.masterand.nav.Screen

data class GameRowData(
    var availableColors: List<Color>,
    val correctColors: List<Color>,
    val historyRows: List<List<Color>>
)

@Composable
fun GameScreen(
    navController: NavController
) {
    val availableColors = listOf(
        Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Cyan,
        Color.Magenta, Color.White, Color.Black, Color.Gray, Color.LightGray
    )

    val userSelectedNumberOfColor = rememberSaveable { availableColors.shuffled().take(6).map { it.copy() } }
    val correctColorsRandomlySelected = rememberSaveable { selectRandomColors(userSelectedNumberOfColor) }
    val historyRows = rememberSaveable { mutableMapOf<Int, List<Color>>() }

    GameLogic( //
        navController = navController, //
        userSelectedNumberOfColor = userSelectedNumberOfColor, //
        correctColorsRandomlySelected = correctColorsRandomlySelected, //
        historyRows = historyRows
    )
}

@Composable
fun GameLogic(
    navController: NavController,
    userSelectedNumberOfColor: List<Color>,
    correctColorsRandomlySelected: List<Color>,
    historyRows: MutableMap<Int, List<Color>>
) {
    var selectedColors by rememberSaveable { mutableStateOf(List(4) { userSelectedNumberOfColor[it] }) }
    var feedbackColors by rememberSaveable { mutableStateOf(List(4) { Color.Gray }) }
    var score by rememberSaveable { mutableIntStateOf(0) }
    var gameWon by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 64.dp), // Space for the Logout button
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Your score: $score",
                style = MaterialTheme.typography.headlineMedium
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                historyRows.forEach { historyRow ->
                    item {
                        HistoryRow(historyRow.value, correctColorsRandomlySelected)
                    }
                }
                if (gameWon) {
                    item {
                        Button(onClick = {
                            historyRows.clear()
                            selectedColors = List(4) { userSelectedNumberOfColor[it] }
                            feedbackColors = List(4) { Color.Gray }
                            gameWon = false
                            score = 0
                            navController.navigate(route = Screen.ProfileScreen.route)
                        }) {
                            Text("High Score Table")
                        }
                    }
                } else {
                    item {
                        GameRow(
                            selectedColors = selectedColors,
                            feedbackColors = feedbackColors,
                            clickable = !gameWon,
                            onSelectColorClick = { index ->
                                selectedColors =
                                    selectNextAvailableColor(userSelectedNumberOfColor, selectedColors, index)
                            },
                            onCheckClick = {
                                addToHistoryMap(historyRows = historyRows, colorHistoryToAdd = selectedColors)
                                feedbackColors = checkColors(selectedColors, correctColorsRandomlySelected, Color.Gray)
                                score = countMatches(selectedColors, feedbackColors)
                                gameWon = feedbackColors.all { it == Color.Red }
                            }
                        )
                    }
                }
            }
        }

        Button(
            onClick = { navController.navigate(route = Screen.LoginScreen.route) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 4.dp)
        ) {
            Text("Logout")
        }
    }
}

// Function to add values to the map with auto-incremented keys
fun addToHistoryMap(historyRows: MutableMap<Int, List<Color>>, colorHistoryToAdd: List<Color>) {
    val newKey = historyRows.size
    historyRows[newKey] = colorHistoryToAdd
}

@Composable
fun HistoryRow(historyRow: List<Color>, correctColorsRandomlySelected: List<Color>) {
    GameRow(
        selectedColors = historyRow,
        feedbackColors = checkColors(historyRow, correctColorsRandomlySelected, Color.Gray),
        clickable = false,
        onSelectColorClick = {},
        onCheckClick = {}
    )
}

//@Preview(showBackground = true)
//@Composable
//fun GameLogicPreview() {
//
//    val historyRows = rememberSaveable { mutableStateOf(mutableListOf<List<Color>>()) }
//    historyRows.value.add(listOf(Color.Magenta, Color.Cyan, Color.Yellow, Color.Green))
//    historyRows.value.add(listOf(Color.Magenta, Color.Cyan, Color.Yellow, Color.Green))
//    historyRows.value.add(listOf(Color.Yellow, Color.Green, Color.Cyan, Color.Magenta))
//    historyRows.value.add(listOf(Color.Yellow, Color.Green, Color.Cyan, Color.Magenta))
//    historyRows.value.add(listOf(Color.Yellow, Color.Green, Color.Cyan, Color.Magenta))
//    historyRows.value.add(listOf(Color.Yellow, Color.Green, Color.Cyan, Color.Magenta))
//    historyRows.value.add(listOf(Color.Yellow, Color.Green, Color.Cyan, Color.Magenta))
//    historyRows.value.add(listOf(Color.Yellow, Color.Green, Color.Cyan, Color.Magenta))
//    historyRows.value.add(listOf(Color.Yellow, Color.Green, Color.Cyan, Color.Magenta))
//    historyRows.value.add(listOf(Color.Yellow, Color.Green, Color.Cyan, Color.Magenta))
//
//    val userSelectedNumberOfColor = listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Cyan, Color.Magenta)
//    val correctColorsRandomlySelected = listOf(
//        Color.Red,
//        Color.Blue,
//        Color.Green,
//        Color.Yellow
//    )
//
//    // Create a NavController instance (for preview purposes only)
//    val navController = rememberNavController()
//
//    GameLogic( //
//        navController = navController, //
//        userSelectedNumberOfColor = userSelectedNumberOfColor, //
//        correctColorsRandomlySelected = correctColorsRandomlySelected, //
//        historyRows = historyRows
//    )
//}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    // Create a NavController instance (for preview purposes only)
    val navController = rememberNavController()

    // Call GameScreen with the NavController parameter
    GameScreen(navController = navController)
}


