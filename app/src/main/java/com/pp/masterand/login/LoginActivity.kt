package com.pp.masterand.login

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            validator = { it.isNotEmpty() }
        )

        OutlinedTextFieldWithError(
            value = email,
            label = "Email",
            supportingText = "Email can't be empty",
            validator = { it.isNotEmpty() }
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
                        return@OutlinedTextFieldWithError true // Valid number
                    }
                } catch (e: NumberFormatException) {
                    return@OutlinedTextFieldWithError true // Valid number
                }
                return@OutlinedTextFieldWithError false // Invalid number
            }
        )

        StartGameButton { navController.navigate(route = Screen.GameScreen.route) }
    }
}

@Preview
@Composable
fun ProfileScreenInitialPreview() {
    val navController = rememberNavController()
    LoginActivity(navController = navController)
}