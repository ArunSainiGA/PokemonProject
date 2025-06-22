package com.asp.pokimon.pokemon_detail

import app.cash.turbine.test
import com.asp.pokemon.domain.model.PokemonDetail
import com.asp.pokemon.domain.usecase.GetPokemonDetail
import com.asp.pokemon.domain.util.Result
import com.asp.pokimon.ui.pokemon_detail.PokemonDetailIntent
import com.asp.pokimon.ui.pokemon_detail.PokemonDetailUiState
import com.asp.pokimon.ui.pokemon_detail.PokemonDetailViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonDetailViewModelTest {

    private lateinit var getPokemonDetail: GetPokemonDetail
    private lateinit var viewModel: PokemonDetailViewModel

    @Before
    fun setUp() {
        getPokemonDetail = mockk()
        viewModel = PokemonDetailViewModel(getPokemonDetail)
    }

    @Test
    fun `handleIntent LoadPokemonDetail emits loading then success`() = runTest {
        val detail = PokemonDetail("pikachu", 0.4f, listOf())
        coEvery { getPokemonDetail("pikachu") } returns flowOf(
            Result.Loading,
            Result.Success(detail)
        )

        viewModel.state.test {
            viewModel.handleIntent(PokemonDetailIntent.LoadPokemonDetail("pikachu"))

            assertEquals(PokemonDetailUiState(), awaitItem())
            assertEquals(PokemonDetailUiState(isLoading = true), awaitItem())
            assertEquals(
                PokemonDetailUiState(pokemonDetail = detail, isLoading = false, error = null),
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `handleIntent LoadPokemonDetail emits loading then error`() = runTest {
        coEvery { getPokemonDetail("pikachu") } returns flowOf(
            Result.Loading,
            Result.Error(Exception("Not found"))
        )

        viewModel.state.test {
            viewModel.handleIntent(PokemonDetailIntent.LoadPokemonDetail("pikachu"))

            assertEquals(PokemonDetailUiState(), awaitItem())
            assertEquals(PokemonDetailUiState(isLoading = true), awaitItem())
            assertEquals(
                PokemonDetailUiState(isLoading = false, error = "Not found"),
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `handleIntent Retry triggers loadDetail again`() = runTest {
        val detail = PokemonDetail("pikachu", 0.4f, listOf())
        coEvery { getPokemonDetail("pikachu") } returns flowOf(
            Result.Loading,
            Result.Success(detail)
        )

        viewModel.state.test {
            viewModel.handleIntent(PokemonDetailIntent.Retry("pikachu"))

            assertEquals(PokemonDetailUiState(), awaitItem())
            assertEquals(PokemonDetailUiState(isLoading = true), awaitItem())
            assertEquals(
                PokemonDetailUiState(pokemonDetail = detail, isLoading = false, error = null),
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
    }
}