package com.pp.masterand.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.pp.masterand.R
import com.pp.masterand.game.*
import com.pp.masterand.nav.Screen

class Profile(val login: String, val description: String)

@Composable
fun ProfileCard() {
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
                Text(text = "Login: profile.login")
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Description: profile.description")
            }

        }
    }
}

@Composable
fun ProfileWithScoreTable(navController: NavController) {
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
            ProfileCard()
            Text(
                "Results",
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                "Recent score: 4",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp) // Padding to provide some space from the bottom edge
        ) {
            Button(
                onClick = { navController.navigate(route = Screen.GameScreen.route) }
            ) {
                Text("Restart Game")
            }
            Button(
                onClick = { navController.navigate(route = Screen.LoginScreen.route) }
            ) {
                Text("Logout")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewProfileCard() {
    val navController = rememberNavController()
    ProfileCard(
//        navController = navController
//        profile = Profile(
//            login = "Gentleman",
//            description = "You had my curiosity... but now you have my attention."
//        )
    )
}

@Preview(showBackground = true)
@Composable
fun ProfileWithScoreTablePreview() {
    val navController = rememberNavController()
    ProfileWithScoreTable(navController = navController)
}

