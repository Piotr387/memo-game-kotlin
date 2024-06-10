package com.pp.masterand.game

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import com.pp.masterand.data.GameViewModel
import com.pp.masterand.nav.Screen

@Composable
fun GameScreen(
    navController: NavController,
    numberOfColorsInPool: Int,
    onLogoutButtonAction: () -> Unit,
    gameViewModel: GameViewModel,
    playerId: Long
) {
    val availableColors = listOf(
        Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Cyan,
        Color.Magenta, Color.White, Color.Black, Color.Gray, Color.LightGray
    )

    var colorsInPoolAfterLimit by rememberSaveable {
        mutableStateOf(
            availableColors.shuffled().take(numberOfColorsInPool).map { it.copy() })
    }
    var correctColorsFromPool by rememberSaveable { mutableStateOf(selectRandomColors(colorsInPoolAfterLimit)) }
    val historyRows = rememberSaveable { mutableMapOf<Int, List<Color>>() }
    var userSelectedColors by rememberSaveable { mutableStateOf(List(4) { colorsInPoolAfterLimit[it] }) }

    val resetGame = {
        historyRows.clear()
        colorsInPoolAfterLimit = availableColors.shuffled().take(numberOfColorsInPool).map { it.copy() }
        correctColorsFromPool = selectRandomColors(colorsInPoolAfterLimit)
        userSelectedColors = List(4) { colorsInPoolAfterLimit[it] }
    }

    DisposableEffect(Unit) {
        onDispose {
            resetGame()
        }
    }

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
            historyRows = historyRows,
            userSelectedColors = userSelectedColors,
            setUserSelectedColors = { userSelectedColors = it },
            gameViewModel = gameViewModel,
            playerId = playerId
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameLogic(
    navController: NavController,
    colorsInPoolAfterLimit: List<Color>,
    correctColorsFromPool: List<Color>,
    historyRows: MutableMap<Int, List<Color>>,
    userSelectedColors: List<Color>,
    setUserSelectedColors: (List<Color>) -> Unit,
    gameViewModel: GameViewModel,
    playerId: Long
) {
    var feedbackForUserAboutPickedColors by rememberSaveable { mutableStateOf(List(4) { Color.Gray }) }
    var score by rememberSaveable { mutableIntStateOf(0) }
    var gameWon by rememberSaveable { mutableStateOf(false) }
    var triggerRecomposition by remember { mutableStateOf(false) }
    var list by remember { mutableStateOf(mutableListOf<Int>()) }

    val listState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 64.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Your score: $score",
            style = MaterialTheme.typography.headlineMedium
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState
        ) {
            items(list, key = { it }) { index ->
                AnimatedVisibility(
                    visible = true,
                    enter = expandVertically(
                        animationSpec = tween(
                            durationMillis = 2000,
                            easing = LinearOutSlowInEasing,
                        )
                    ) + fadeIn(
                        animationSpec = tween(
                            durationMillis = 2000,
                            easing = LinearOutSlowInEasing,
                        )
                    ),
                    exit = shrinkVertically(
                        animationSpec = tween(
                            durationMillis = 2000,
                            easing = LinearOutSlowInEasing,
                        )
                    ) + fadeOut(
                        animationSpec = tween(
                            durationMillis = 2000,
                            easing = LinearOutSlowInEasing,
                        )
                    ),
                    modifier = Modifier.animateItemPlacement(
                        animationSpec = tween(
                            durationMillis = 2000,
                            easing = LinearOutSlowInEasing,
                        )
                    )
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .animateItemPlacement(
                                animationSpec = tween(
                                    durationMillis = 2000,
                                    easing = LinearOutSlowInEasing,
                                )
                            )
                    ) {
                        HistoryRow(historyRows[index]!!, correctColorsFromPool)
                    }
                }
            }
            if (gameWon) {
                item {
                    Button(onClick = {
                        historyRows.clear()
                        list = historyRows.keys.toMutableList()
                        triggerRecomposition = !triggerRecomposition
                        setUserSelectedColors(List(4) { colorsInPoolAfterLimit[it] })
                        feedbackForUserAboutPickedColors = List(4) { Color.Gray }
                        gameWon = false
                        navController.navigate(route = Screen.ProfileScreen.route + "/$score/$playerId")
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
                            setUserSelectedColors(
                                selectNextAvailableColor(colorsInPoolAfterLimit, userSelectedColors, index)
                            )
                        },
                        onCheckClick = {
                            addToHistoryMap(historyRows = historyRows, colorHistoryToAdd = userSelectedColors)
                            list = historyRows.keys.toMutableList()
                            feedbackForUserAboutPickedColors =
                                checkColors(userSelectedColors, correctColorsFromPool, Color.Gray)
                            score = list.size
                            gameWon = feedbackForUserAboutPickedColors.all { it == Color.Red }
                            if (gameWon) {
                                val difficulty = colorsInPoolAfterLimit.size - 4
                                gameViewModel.addScore(playerId, score.toLong(), difficultyLevel = difficulty.toLong())
                                Log.d("GameLogic", "Score added: $difficulty for playerId: $playerId")
                            }
                        }
                    )
                }
            }
        }

        // Scroll to the top when a new item is added
        LaunchedEffect(list.size) {
            listState.animateScrollToItem(list.size)
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

//@Preview(showBackground = true)
//@Composable
//fun GameScreenPreview() {
//    // Create a NavController instance (for preview purposes only)
//    val navController = rememberNavController()
//
//    // Call GameScreen with the NavController parameter
//    GameScreen(navController = navController, numberOfColorsInPool = 5, onLogoutButtonAction = {})
//}


