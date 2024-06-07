package com.pp.masterand.login

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.pp.masterand.nav.Screen
import com.pp.masterand.profile.Profile


@Composable
fun LoginActivity(
    navController: NavController
) {
    val profileImageUri = rememberSaveable { mutableStateOf<Uri?>(null) }
    val name = rememberSaveable { mutableStateOf("") }
    val email = rememberSaveable { mutableStateOf("") }
    val number = rememberSaveable { mutableStateOf("") }

    val isNameValid = rememberSaveable { mutableStateOf(false) }
    val isEmailValid = rememberSaveable { mutableStateOf(false) }
    val isNumberValid = rememberSaveable { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "MasterAnd",
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier.padding(bottom = 48.dp)
                .scale(scale)
        )

        val imagePicker = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { selectedUri ->
                if (selectedUri != null) {
                    profileImageUri.value = selectedUri
                }
            })

        ProfileImageWithPicker(profileImageUri = profileImageUri.value, selectImageOnClick = {
            imagePicker.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        })

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextFieldWithError(
            value = name,
            label = "Name",
            supportingText = "Name can't be empty",
            validator = {
                val isValid = it.isNotEmpty()
                isNameValid.value = isValid
                isValid
            }
        )

        OutlinedTextFieldWithError(
            value = email,
            label = "Email",
            supportingText = "Email can't be empty",
            validator = {
                val isValid = it.isNotEmpty()
                isEmailValid.value = isValid
                isValid
            }
        )

        OutlinedTextFieldWithError(
            value = number,
            label = "Number",
            keyboardType = KeyboardType.Number,
            supportingText = "Must be in range <5,10>", // Clearer message
            validator = { text ->
                try {
                    val num = text.toIntOrNull() // Handle non-numeric input
                    if (IntRange(5, 10).contains(num)) {
                        isNumberValid.value = true
                        return@OutlinedTextFieldWithError true // Valid number
                    }
                } catch (e: NumberFormatException) {
                    isNumberValid.value = false
                    return@OutlinedTextFieldWithError false // Invalid number
                }
                isNumberValid.value = false
                return@OutlinedTextFieldWithError false // Invalid number
            }
        )
        if (isNameValid.value && isEmailValid.value && isNumberValid.value) {
            StartGameButton {
                navController.navigate(Screen.GameScreen.route + "/${number.value.toInt()}")
            }
        } else {
            StartGameButton {
                navController.navigate(Screen.GameScreen.route + "/${5}")
            }
        }

    }
}

@Preview
@Composable
fun ProfileScreenInitialPreview() {
    val navController = rememberNavController()
    LoginActivity(navController = navController)
}