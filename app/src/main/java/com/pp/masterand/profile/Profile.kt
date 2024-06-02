package com.pp.masterand.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pp.masterand.R

class Profile(val login: String, val description: String)

@Composable
fun ProfileCard(profile: Profile) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val profileImage: Painter = painterResource(id = R.drawable.ic_launcher_foreground)
            Image(
                painter = profileImage,
                contentDescription = "Profile Image",
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = profile.login)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = profile.description)
            }

        }
    }
}

@Composable
fun ProfileScreenInitial() {
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
                    if (IntRange(5,10).contains(num)) {
                        return@OutlinedTextFieldWithError true // Valid number
                    }
                } catch (e: NumberFormatException) {
                    return@OutlinedTextFieldWithError true // Valid number
                }
                return@OutlinedTextFieldWithError false // Invalid number
            }
        )

        StartGameButton {

        }
    }
}

@Preview
@Composable
fun ProfileScreenInitialPreview() {
    ProfileScreenInitial()
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileCard() {
    ProfileCard(
        profile = Profile(
            login = "Gentleman",
            description = "You had my curiosity... but now you have my attention."
        )
    )
}

