package com.pp.masterand.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.pp.masterand.R
import com.pp.masterand.data.Score
import com.pp.masterand.nav.Screen

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
fun ProfileWithScoreTable(
    navController: NavController,
    scoreNumber: Int,
    onButtonClicked: () -> Unit,
    onLogoutButtonAction: () -> Unit,
    scores: List<Score>
) {
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
                "Recent score: $scoreNumber",
                style = MaterialTheme.typography.headlineMedium
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(scores) { score ->
                    ScoreItem(score)
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp) // Padding to provide some space from the bottom edge
        ) {
            Button(
                onClick = onButtonClicked
            ) {
                Text("Restart Game")
            }
            Button(
                onClick = onLogoutButtonAction
            ) {
                Text("Logout")
            }
        }
    }
}

@Composable
fun ScoreItem(score: Score) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Score ID: ${score.scoreId}")
            Text(text = "Score: ${score.scoreNumber}")
            Text(text = "Difficulty: ${score.difficultyLevel}")
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

//@Preview(showBackground = true)
//@Composable
//fun ProfileWithScoreTablePreview() {
//    val navController = rememberNavController()
//    ProfileWithScoreTable(navController = navController, scoreNumber = 4, onButtonClicked = {}, onLogoutButtonAction = {})
//}

