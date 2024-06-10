package com.pp.masterand

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.pp.masterand.ui.theme.MasterAndTheme
import androidx.navigation.compose.rememberNavController
import com.pp.masterand.data.AppContainer
import com.pp.masterand.data.AppDataContainer
import com.pp.masterand.login.LoginActivity
import com.pp.masterand.nav.SetupNavGraph
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    lateinit var navController: NavHostController
    lateinit var container: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //container = AppDataContainer(this)
        setContent {
            MasterAndTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    navController = rememberNavController()
                    //Funkcja odpowiedziana za powiązania między kolejnymi ekranami
                    SetupNavGraph(
                        navController = navController //
                    //    , appContainer = container
                    )
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun MainActivityPreview() {
//    // Create a NavController instance (for preview purposes only)
//    val navController = rememberNavController()
//    LoginActivity(navController = navController)
//}