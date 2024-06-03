package com.pp.masterand.login

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.pp.masterand.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter

@Composable
fun ProfileImageWithPicker(profileImageUri: Uri?, selectImageOnClick: () -> Unit) {
    val defaultImage = painterResource(id = R.drawable.ic_baseline_question_mark_24)
    Box {
        if (profileImageUri != null) {
            AsyncImage(
                model = profileImageUri,
                contentDescription = "Profile image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .align(Alignment.Center),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = defaultImage,
                contentDescription = "Profile photo",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .align(Alignment.Center),
                contentScale = ContentScale.Crop
            )
        }

        IconButton(
            onClick = selectImageOnClick,
            modifier = Modifier.size(32.dp)
                .align(Alignment.TopEnd)
        ) {
            Icon(imageVector = Icons.Filled.Search, contentDescription = "Wybierz zdjęcie")
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextFieldWithError(
    modifier: Modifier = Modifier,
    value: MutableState<String>,
    label: String,
    isError: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    supportingText: String = "",
    validator: (String) -> Boolean = { true },
    errorColor: Color = Color.Red // Default error color
) {
    var internalError by remember { mutableStateOf(isError) }
    var shouldShowErrorFirstTime by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier.fillMaxWidth()
            .onFocusChanged { focusState ->
                if (!focusState.isFocused && shouldShowErrorFirstTime) {
                    internalError = !validator(value.value)
                } else {
                    shouldShowErrorFirstTime = true
                }
            },
        value = value.value,
        onValueChange = { newValue ->
            value.value = newValue
            internalError = !validator(newValue)
        },
        label = { Text(label) },
        singleLine = true,
        isError = internalError,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        supportingText = { Text(if (internalError) supportingText else "") },
        trailingIcon = {
            if (internalError) {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_info_24),
                    contentDescription = "Error Icon",
                    colorFilter = ColorFilter.tint(errorColor),
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        },
    )
}

@Composable
fun StartGameButton(onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Start game")
    }
}

@Preview
@Composable
fun ProfileImageWithPickerPreview() {
    ProfileImageWithPicker(profileImageUri = null, selectImageOnClick = {})
}

@Preview
@Composable
fun OutlinedTextFieldWithErrorNumberPreview() {

    val number = rememberSaveable { mutableStateOf("") }

    OutlinedTextFieldWithError(
        value = number,
        label = "Number",
        keyboardType = KeyboardType.Number,
        supportingText = "Number needs to be bigger than one."
    )
}

@Preview
@Composable
fun OutlinedTextFieldWithErrorStringPreview() {

    val email = rememberSaveable { mutableStateOf("") }

    OutlinedTextFieldWithError(
        value = email,
        label = "Number",
        isError = false,
        keyboardType = KeyboardType.Text,
        supportingText = "Number needs to be bigger than one."
    )
}

