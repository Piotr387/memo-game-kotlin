package com.pp.masterand.game

import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import com.pp.masterand.R
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun GameRow(
    selectedColors: List<Color>,
    feedbackColors: List<Color>,
    clickable: Boolean,
    onSelectColorClick: (Int) -> Unit,
    onCheckClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        SelectableColorsRow(
            colors = selectedColors,
            onClick = { index ->
                if (clickable) {
                    onSelectColorClick(index)
                }
            }
        )
        Spacer(modifier = Modifier.width(16.dp))
        IconButton(
            onClick = { onCheckClick() },
            enabled = clickable,
            modifier = Modifier.size(50.dp).clip(CircleShape),
            colors = IconButtonDefaults.filledIconButtonColors()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_check_circle_24),
                contentDescription = "Check"
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        if (!clickable) {
            FeedbackCircles(colors = feedbackColors)
        }
    }
}

@Composable
private fun CircularButton(onClick: () -> Unit, color: Color) {
    var currentColor by remember { mutableStateOf(color) }
    var animateColorChange by remember { mutableStateOf(false) }
    var secondaryAnimationFinished by remember { mutableStateOf(false) }

    val intermediateColor = currentColor.copy(alpha = 0.5f)

    val animatedColor by animateColorAsState(
        targetValue = if (animateColorChange) intermediateColor else currentColor,
        animationSpec = if (animateColorChange) {
            repeatable(
                iterations = 3,
                animation = tween(1000),
                repeatMode = RepeatMode.Reverse
            )
        } else {
            tween(durationMillis = 500) // Duration for smooth transition back
        },
        finishedListener = {
            if (animateColorChange) {
                animateColorChange = false
                secondaryAnimationFinished = false
            } else {
                secondaryAnimationFinished = true
            }
        }
    )

    LaunchedEffect(color) {
        if (currentColor != color) {
            animateColorChange = true
            secondaryAnimationFinished = false
            currentColor = color
        }
    }

    Button(
        onClick = {
            onClick()
            animateColorChange = true // Trigger the animation on click
            secondaryAnimationFinished = false
            currentColor = color
        },
        modifier = Modifier
            .size(50.dp)
            .background(color = MaterialTheme.colorScheme.background),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = animatedColor,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground)
    ) {
        // Empty content, as it's just a circular button
    }

    // Reset the secondaryAnimationFinished state after the secondary animation completes
    LaunchedEffect(animatedColor) {
        if (secondaryAnimationFinished && animatedColor == currentColor) {
            secondaryAnimationFinished = false
        }
    }
}



@Composable
private fun SelectableColorsRow(
    colors: List<Color>,
    onClick: (Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp)
    ) {
        colors.forEachIndexed { index, color ->
            CircularButton(
                onClick = { onClick(index) },
                color = color
            )
        }
    }
}

@Composable
private fun SmallCircle(
    color: Color
) {
    Box(
        modifier = Modifier
            .size(16.dp)
            .clip(CircleShape)
            .background(color = color)
            .border(2.dp, MaterialTheme.colorScheme.onBackground, CircleShape)
    ) {
        // Empty content, as it's just a small colored circle
    }
}

@Composable
fun FeedbackCircles(
    colors: List<Color>,
    itemsPerRow: Int = 2
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        colors.chunked(itemsPerRow).forEach { rowColors ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                rowColors.forEach { color ->
                    SmallCircle(color = color)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GameRowPreview() {
    val selectedColors = listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow)
    val feedbackColors = listOf(Color.Red, Color.Magenta, Color.Yellow, Color.Blue)

    GameRow(
        selectedColors = selectedColors,
        feedbackColors = feedbackColors,
        clickable = true,
        onSelectColorClick = {},
        onCheckClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun GameRowClickableFalsePreview() {
    val selectedColors = listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow)
    val feedbackColors = listOf(Color.Red, Color.Magenta, Color.Yellow, Color.Blue)

    GameRow(
        selectedColors = selectedColors,
        feedbackColors = feedbackColors,
        clickable = false,
        onSelectColorClick = {},
        onCheckClick = {}
    )
}