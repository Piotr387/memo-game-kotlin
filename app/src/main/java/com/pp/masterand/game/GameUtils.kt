package com.pp.masterand.game

import androidx.compose.ui.graphics.Color

fun selectNextAvailableColor(
    availableColors: List<Color>,
    selectedColors: List<Color>,
    buttonIndex: Int
): List<Color> {
    val currentColor = selectedColors[buttonIndex]
    val nextColorIndex = (availableColors.indexOf(currentColor) + 1) % availableColors.size
    var nextColor = availableColors[nextColorIndex]

    // Find the next available color that is not already selected
    while (nextColor in selectedColors) {
        val nextIndex = (availableColors.indexOf(nextColor) + 1) % availableColors.size
        nextColor = availableColors[nextIndex]
    }

    // Create a new list with the updated color for the specific button
    return selectedColors.toMutableList().apply {
        this[buttonIndex] = nextColor
    }
}

fun selectRandomColors(availableColors: List<Color>): List<Color> {
    return availableColors.shuffled().take(4)
}

fun countMatches(selectedColors: List<Color>, feedbackColors: List<Color>): Int {
    return feedbackColors.count { it in selectedColors }
}

fun checkColors(
    selectedColors: List<Color>,
    correctColors: List<Color>,
    notFoundColor: Color
): List<Color> {
    val feedback = MutableList(4) { notFoundColor }

    // To keep track of which colors have already been matched
    val matchedIndices = mutableSetOf<Int>()

    // First pass: Check for correct color and correct position
    for (i in selectedColors.indices) {
        if (selectedColors[i] == correctColors[i]) {
            feedback[i] = Color.Red
            matchedIndices.add(i)
        }
    }

    // Second pass: Check for correct color in the wrong position
    for (i in selectedColors.indices) {
        if (feedback[i] == Color.Red) continue // Skip already matched colors
        for (j in correctColors.indices) {
            if (j !in matchedIndices && selectedColors[i] == correctColors[j]) {
                feedback[i] = Color.Yellow
                matchedIndices.add(j)
                break
            }
        }
    }

    return feedback
}

