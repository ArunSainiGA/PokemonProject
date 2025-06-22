package com.asp.pokimon.ui.pokemon_list

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.*
import androidx.compose.foundation.layout.PaddingValues
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.asp.pokemon.domain.model.Pokemon
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PokemonListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun pokemonList_displaysAllItems() {
        val pokemons = listOf(
            Pokemon(name = "bulbasaur", url = "url1"),
            Pokemon(name = "charmander", url = "url2")
        )
        composeTestRule.setContent {
            PokemonListScreen(
                padding = PaddingValues(),
                onPokemonClick = {},
                pokemonList = pokemons,
                isLoading = false,
                loadNext = {}
            )
        }
        composeTestRule.onNodeWithText("Bulbasaur").assertIsDisplayed()
        composeTestRule.onNodeWithText("Charmander").assertIsDisplayed()
    }

    @Test
    fun pokemonListItem_click_triggersCallback() {
        var clicked = false
        val pokemons = listOf(Pokemon(name = "pikachu", url = "url"))
        composeTestRule.setContent {
            PokemonListScreen(
                padding = PaddingValues(),
                onPokemonClick = { clicked = true },
                pokemonList = pokemons,
                isLoading = false,
                loadNext = {}
            )
        }
        composeTestRule.onNodeWithText("Pikachu").performClick()
        assert(clicked)
    }

    @Test
    fun loadingIndicator_isDisplayed_whenLoading() {
        composeTestRule.setContent {
            PokemonListScreen(
                padding = PaddingValues(),
                onPokemonClick = {},
                pokemonList = emptyList(),
                isLoading = true,
                loadNext = {}
            )
        }
        composeTestRule.onNodeWithText("Pokemon 1").assertDoesNotExist()
        composeTestRule.onNode(hasTestTag("LinearProgressIndicator")).assertExists()
    }
}