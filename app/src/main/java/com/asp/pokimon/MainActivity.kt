package com.asp.pokimon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.asp.pokimon.ui.common.NavigationGraph
import com.asp.pokimon.ui.common.Screen
import com.asp.pokimon.ui.theme.PokimonProjectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokimonProjectTheme {
                val navController = rememberNavController()
                PokimonProjectTheme {
                    NavigationGraph(navController, startDestination = Screen.List)
                }
            }
        }
    }
}