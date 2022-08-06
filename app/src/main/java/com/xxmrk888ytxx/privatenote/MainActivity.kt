package com.xxmrk888ytxx.privatenote

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.xxmrk888ytxx.privatenote.Screen.EditNoteScreen.EditNoteScreen
import com.xxmrk888ytxx.privatenote.Screen.MainScreen.MainScreen
import com.xxmrk888ytxx.privatenote.Screen.Screen
import com.xxmrk888ytxx.privatenote.Screen.SplashScreen.SplashScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var LifecycleState: MutableStateFlow<LifeCycleState>
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            Scaffold() {
                NavHost(navController = navController, startDestination = Screen.SplashScreen.route) {
                    composable(Screen.SplashScreen.route) {SplashScreen(navController)}
                    composable(Screen.MainScreen.route) {MainScreen(navController = navController)}
                    composable(Screen.EditNoteScreen.route) {EditNoteScreen(navController = navController)}
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch{
            LifecycleState.emit(LifeCycleState.onResume)
        }
    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.launch{
            LifecycleState.emit(LifeCycleState.onPause)
        }
    }
}

