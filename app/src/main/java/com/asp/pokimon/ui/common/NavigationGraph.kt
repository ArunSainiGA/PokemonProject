package com.asp.pokimon.ui.common

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.asp.pokimon.ui.pokemon_detail.LoadPokemonDetail
import com.asp.pokimon.ui.pokemon_detail.PokemonDetailIntent
import com.asp.pokimon.ui.pokemon_detail.PokemonDetailViewModel
import com.asp.pokimon.ui.pokemon_list.LoadPokemonList
import com.asp.pokimon.ui.pokemon_list.PokemonListIntent
import com.asp.pokimon.ui.pokemon_list.PokemonListViewModel
import com.asp.pokimon.util.capitalizeFirstLetter

@Composable
fun NavigationGraph(navController: NavHostController, startDestination: Screen) {
    NavHost(navController, startDestination = startDestination.route) {
        composable(Screen.List.route) {
            val viewModel: PokemonListViewModel = hiltViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()
            LoadPokemonList(
                onPokemonClick = { pokemon ->
                    navController.navigate(Screen.Detail.route + "?name=${pokemon.name}")
                },
                loadNext = {
                    viewModel.handleIntent(PokemonListIntent.LoadNextPage)
                },
                retry = {
                    viewModel.handleIntent(PokemonListIntent.Retry)
                },
                state = state,
                buffer = 3
            )
        }
        composable(
            route = Screen.Detail.route + "?name={name}",
            arguments = listOf(
                navArgument("name") {
                    type = NavType.StringType
                    nullable = false
                })
        ) { destination ->
            val detailViewModel: PokemonDetailViewModel = hiltViewModel()
            val name = destination.arguments?.getString("name") ?: ""
            LaunchedEffect(name) {
                detailViewModel.handleIntent(PokemonDetailIntent.LoadPokemonDetail(name))
            }
            val state by detailViewModel.state.collectAsStateWithLifecycle()
            LoadPokemonDetail(
                name = name.capitalizeFirstLetter(),
                state = state,
                onBack = { navController.popBackStack() },
                retry = {
                    detailViewModel.handleIntent(PokemonDetailIntent.LoadPokemonDetail(name))
                }
            )
        }
    }
}

sealed class Screen(val route: String) {
    object List: Screen("list_screen")
    object Detail: Screen("detail_screen")
}