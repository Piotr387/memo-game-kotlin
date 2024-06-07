package com.pp.masterand.game

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
    navController: NavController,
    numberOfColorsInPool: Int,
    onLogoutButtonAction: () -> Unit
) {
    val availableColors = listOf(
        Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Cyan,
        Color.Magenta, Color.White, Color.Black, Color.Gray, Color.LightGray
    )

    // State variables to manage game state
    var colorsInPoolAfterLimit by rememberSaveable { mutableStateOf(availableColors.shuffled().take(numberOfColorsInPool).map { it.copy() }) }
    var correctColorsFromPool by rememberSaveable { mutableStateOf(selectRandomColors(colorsInPoolAfterLimit)) }
    val historyRows = rememberSaveable { mutableMapOf<Int, List<Color>>() }

    // Define the reset function
    val resetGame = {
        historyRows.clear()
        colorsInPoolAfterLimit = availableColors.shuffled().take(numberOfColorsInPool).map { it.copy() }
        correctColorsFromPool = selectRandomColors(colorsInPoolAfterLimit)
    }

    // Effect to handle reset when navigating back
    DisposableEffect(Unit) {
        onDispose {
            resetGame()
        }
    }

    // Call the reset function directly if needed
    LaunchedEffect(navController.currentBackStackEntry) {
        val backStackEntry = navController.previousBackStackEntry
        if (backStackEntry?.destination?.route == Screen.ProfileScreen.route) {
            resetGame()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        GameLogic(
            navController = navController,
            colorsInPoolAfterLimit = colorsInPoolAfterLimit,
            correctColorsFromPool = correctColorsFromPool,
            historyRows = historyRows
        )
        Button(
            onClick = onLogoutButtonAction,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 4.dp)
        ) {
            Text("Logout")
        }
    }
}

@Composable
fun GameLogic(
    navController: NavController,
    colorsInPoolAfterLimit: List<Color>,
    correctColorsFromPool: List<Color>,
    historyRows: MutableMap<Int, List<Color>>
) {
    var userSelectedColors by rememberSaveable { mutableStateOf(List(4) { colorsInPoolAfterLimit[it] }) }
    var feedbackForUserAboutPickedColors by rememberSaveable { mutableStateOf(List(4) { Color.Gray }) }
    var score by rememberSaveable { mutableIntStateOf(0) }
    var gameWon by rememberSaveable { mutableStateOf(false) }
    var triggerRecomposition by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = triggerRecomposition) { // Replace key1 with a unique identifier if needed
        triggerRecomposition = !triggerRecomposition  // Toggle the state to trigger recomposition
    }

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

        var listState = rememberLazyListState()
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState
        ) {
            historyRows.forEach { historyRow ->
                item {
                    HistoryRow(historyRow.value, correctColorsFromPool)
                }
            }
            if (gameWon) {
                item {
                    Button(onClick = {
                        historyRows.clear()
                        triggerRecomposition = !triggerRecomposition
                        userSelectedColors = List(4) { colorsInPoolAfterLimit[it] }
                        feedbackForUserAboutPickedColors = List(4) { Color.Gray }
                        gameWon = false
                        navController.navigate(route = Screen.ProfileScreen.route + "/$score")
                    }) {
                        Text("High Score Table")
                    }
                }
            } else {
                item {
                    GameRow(
                        selectedColors = userSelectedColors,
                        feedbackColors = feedbackForUserAboutPickedColors,
                        clickable = !gameWon,
                        onSelectColorClick = { index ->
                            userSelectedColors =
                                selectNextAvailableColor(colorsInPoolAfterLimit, userSelectedColors, index)
                        },
                        onCheckClick = {
                            addToHistoryMap(historyRows = historyRows, colorHistoryToAdd = userSelectedColors)
                            triggerRecomposition = !triggerRecomposition
                            feedbackForUserAboutPickedColors =
                                checkColors(userSelectedColors, correctColorsFromPool, Color.Gray)
                            score = countMatches(userSelectedColors, correctColorsFromPool)
                            gameWon = feedbackForUserAboutPickedColors.all { it == Color.Red }

                        }
                    )
                }
            }
        }
    }
}

// Function to add values to the map with auto-incremented keys
fun addToHistoryMap(historyRows: MutableMap<Int, List<Color>>, colorHistoryToAdd: List<Color>) {
    val newKey = historyRows.size
    historyRows[newKey] = colorHistoryToAdd
}

@Composable
fun HistoryRow(historyRow: List<Color>, correctColorsFromPool: List<Color>) {
    GameRow(
        selectedColors = historyRow,
        feedbackColors = checkColors(historyRow, correctColorsFromPool, Color.Gray),
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
//    val colorsInPoolAfterLimit = listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Cyan, Color.Magenta)
//    val correctColorsFromPool = listOf(
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
//        colorsInPoolAfterLimit = colorsInPoolAfterLimit, //
//        correctColorsFromPool = correctColorsFromPool, //
//        historyRows = historyRows
//    )
//}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    // Create a NavController instance (for preview purposes only)
    val navController = rememberNavController()

    // Call GameScreen with the NavController parameter
    GameScreen(navController = navController, numberOfColorsInPool = 5, onLogoutButtonAction = {})
}


