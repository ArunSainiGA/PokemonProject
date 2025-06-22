package com.asp.pokimon.ui.pokemon_detail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.asp.pokemon.domain.model.PokemonDetail
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PokemonDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadingState_showsLoadingIndicator() {
        val state = PokemonDetailUiState(isLoading = true)
        composeTestRule.setContent {
            LoadPokemonDetail(
                name = "pikachu",
                state = state
            )
        }
        composeTestRule.onNode(hasTestTag("CircularProgressIndicator")).assertExists()
    }

    @Test
    fun errorState_showsErrorScreen() {
        val state = PokemonDetailUiState(isLoading = false, error = "Network error")
        composeTestRule.setContent {
            LoadPokemonDetail(
                name = "pikachu",
                state = state
            )
        }
        composeTestRule.onNodeWithText("Network error").assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry", substring = true).assertExists()
    }

    @Test
    fun successState_showsPokemonDetail() {
        val detail = PokemonDetail(
            name = "pikachu",
            height = 0.4f,
            images = listOf("https://test.com/pikachu.png")
        )
        val state = PokemonDetailUiState(isLoading = false, pokemonDetail = detail)
        composeTestRule.setContent {
            LoadPokemonDetail(
                name = "pikachu",
                state = state
            )
        }
        composeTestRule.onNodeWithText("Pikachu").assertIsDisplayed()
    }
}