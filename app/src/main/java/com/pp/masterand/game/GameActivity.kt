package com.pp.masterand.game

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class GameRowData(
    var availableColors: List<Color>,
    val correctColors: List<Color>,
    val historyRows: List<List<Color>>
)

@Composable
fun GameScreen() {
    var historyRows: MutableList<List<Color>> = rememberSaveable { mutableListOf() }
    val availableColors = listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Cyan, Color.Magenta)
    val randomlySelectedColor = rememberSaveable { selectRandomColors(availableColors) }

    GameLogic(availableColors, randomlySelectedColor, historyRows)
}

@Composable
fun GameLogic(availableColors: List<Color>, correctColors: List<Color>, historyRows: MutableList<List<Color>>) {

    var selectedColors by rememberSaveable { mutableStateOf(List(4) { availableColors[it] }) }
    var feedbackColors by rememberSaveable { mutableStateOf(List(4) { Color.Gray }) }
    var score by rememberSaveable { mutableStateOf(0) }
    var gameWon by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
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
                    HistoryRow(historyRow, correctColors)
                }
            }
            if (gameWon) {
                item {
                    Button(onClick = {
                        historyRows.clear()
                        selectedColors = List(4) { availableColors[it] }
                        feedbackColors = List(4) { Color.Gray }
                        gameWon = false
                        score = 0
                    }) {
                        Text("Restart Game")
                    }
                }
            } else {
                item {
                    GameRow(
                        selectedColors = selectedColors,
                        feedbackColors = feedbackColors,
                        clickable = !gameWon,
                        onSelectColorClick = { index ->
                            selectedColors = selectNextAvailableColor(availableColors, selectedColors, index)
                        },
                        onCheckClick = {
                            historyRows.apply { add(selectedColors) }
                            feedbackColors = checkColors(selectedColors, correctColors, Color.Gray)
                            score = countMatches(selectedColors, feedbackColors)
                            gameWon = feedbackColors.all { it == Color.Red }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryRow(historyRow: List<Color>, correctColors: List<Color>) {
    GameRow(
        selectedColors = historyRow,
        feedbackColors = checkColors(historyRow, correctColors, Color.Gray),
        clickable = false,
        onSelectColorClick = {},
        onCheckClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun GameLogicPreview() {

    val historyRows: MutableList<List<Color>> = mutableListOf(
        listOf(
            Color.Magenta,
            Color.Cyan,
            Color.Yellow,
            Color.Green
        ),
        listOf(
            Color.Yellow,
            Color.Green,
            Color.Cyan,
            Color.Magenta
        ),
        listOf(
            Color.Yellow,
            Color.Green,
            Color.Cyan,
            Color.Magenta
        )
    )

    val availableColors = listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Cyan, Color.Magenta)
    val randomlySelectedColor = listOf(
        Color.Red,
        Color.Blue,
        Color.Green,
        Color.Yellow
    )

    GameLogic(availableColors, randomlySelectedColor, historyRows)
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    GameScreen()
}


