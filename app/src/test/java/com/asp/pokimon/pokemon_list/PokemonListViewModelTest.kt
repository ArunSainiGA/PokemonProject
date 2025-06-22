package com.asp.pokimon.pokemon_list

import app.cash.turbine.test
import com.asp.pokemon.domain.model.Pokemon
import com.asp.pokemon.domain.model.PokemonCollection
import com.asp.pokemon.domain.usecase.GetPokemonList
import com.asp.pokemon.domain.util.Result
import com.asp.pokimon.ui.pokemon_list.PokemonListIntent
import com.asp.pokimon.ui.pokemon_list.PokemonListUiState
import com.asp.pokimon.ui.pokemon_list.PokemonListViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonListViewModelTest {

    private lateinit var getPokemonList: GetPokemonList
    private lateinit var viewModel: PokemonListViewModel
    private val defaultUrl = PokemonListUiState().url

    @Before
    fun setUp() {
        getPokemonList = mockk()
        // Adding url to get the first next page
        coEvery { getPokemonList(any()) } returns flowOf(Result.Success(PokemonCollection(0, defaultUrl, "", emptyList())))
        viewModel = PokemonListViewModel(getPokemonList)
    }

    @Test
    fun `handleIntent LoadNextPage emits loading then success`() = runTest {
        val pokemons = listOf(Pokemon("bulbasaur", "url1"))
        // Adding url to get the first next page
        val collection = PokemonCollection(0, defaultUrl, "", pokemons)
        coEvery { getPokemonList(any()) } returns flowOf(
            Result.Loading,
            Result.Success(collection)
        )

        viewModel.state.test {
            viewModel.handleIntent(PokemonListIntent.LoadNextPage)

            // Initial state
            assertEquals(PokemonListUiState(), awaitItem())
            // Loading state
            assertEquals(PokemonListUiState(isLoading = true), awaitItem())
            // Success state
            assertEquals(
                PokemonListUiState(
                    pokemonList = pokemons,
                    url = defaultUrl,
                    isLoading = false,
                    error = null
                ),
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `handleIntent LoadNextPage emits loading then error`() = runTest {
        coEvery { getPokemonList(any()) } returns flowOf(
            Result.Loading,
            Result.Error(Exception("Network error"))
        )

        viewModel.state.test {
            viewModel.handleIntent(PokemonListIntent.LoadNextPage)

            assertEquals(PokemonListUiState(), awaitItem())
            assertEquals(PokemonListUiState(isLoading = true), awaitItem())
            assertEquals(
                PokemonListUiState(isLoading = false, error = "Network error"),
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `handleIntent Retry triggers loadNextPage again`() = runTest {
        val pokemons = listOf(Pokemon("pika", "url2"))
        val collection = PokemonCollection(0, defaultUrl, "", pokemons)
        coEvery { getPokemonList(any()) } returns flowOf(
            Result.Loading,
            Result.Success(collection)
        )

        viewModel.state.test {
            viewModel.handleIntent(PokemonListIntent.Retry)

            assertEquals(PokemonListUiState(), awaitItem())
            assertEquals(PokemonListUiState(isLoading = true), awaitItem())
            assertEquals(
                PokemonListUiState(
                    pokemonList = pokemons,
                    url = defaultUrl,
                    isLoading = false,
                    error = null
                ),
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
    }
}