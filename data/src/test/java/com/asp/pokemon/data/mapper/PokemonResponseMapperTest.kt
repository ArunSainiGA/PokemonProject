package com.asp.pokemon.data.mapper

import com.asp.pokemon.data.response.PokemonDetailResponse
import com.asp.pokemon.data.response.PokemonDetailSpiritsResponse
import com.asp.pokemon.data.response.PokemonItemResponse
import com.asp.pokemon.data.response.PokemonResponse
import com.asp.pokemon.domain.model.Pokemon
import com.asp.pokemon.domain.model.PokemonCollection
import com.asp.pokemon.domain.model.PokemonDetail
import org.junit.Assert.assertEquals
import org.junit.Test

class PokemonResponseMapperTest {

    private val mapper = PokemonResponseMapper()

    @Test
    fun `mapToPokemonCollection maps response correctly`() {
        val response = PokemonResponse(
            count = 2,
            next = "nextUrl",
            previous = "prevUrl",
            pokemonList = listOf(
                PokemonItemResponse(name = "poki 1", url = "url1"),
                PokemonItemResponse(name = "poki 2", url = "url2")
            )
        )

        val expected = PokemonCollection(
            count = 2,
            next = "nextUrl",
            previous = "prevUrl",
            results = listOf(
                Pokemon(name = "poki 1", url = "url1"),
                Pokemon(name = "poki 2", url = "url2")
            )
        )

        val result = mapper.mapToPokemonCollection(response)
        assertEquals(expected, result)
    }

    @Test
    fun `mapToPokemonDetail maps response with all null sprites`() {
        val spirits = PokemonDetailSpiritsResponse()
        val response = PokemonDetailResponse(
            name = "pikachu",
            height = 0.4f,
            spirits = spirits
        )

        val expected = PokemonDetail(
            name = "pikachu",
            height = 0.4f,
            images = emptyList<String>()
        )

        val result = mapper.mapToPokemonDetail(response)
        assertEquals(expected, result)
    }

    @Test
    fun `mapToPokemonDetail filters blank sprites and handles nulls`() {
        val spirits = PokemonDetailSpiritsResponse(
            backDefault = "",
            backFemale = null,
            backShiny = "back3",
            backShinyFemale = "",
            frontDefault = null,
            frontFemale = "",
            frontShiny = "front3",
            frontShinyFemale = null
        )
        val response = PokemonDetailResponse(
            name = "pikachu",
            height = 0.5F,
            spirits = spirits
        )

        val expected = PokemonDetail(
            name = "pikachu",
            height = 0.5F,
            images = listOf("back3", "front3")
        )

        val result = mapper.mapToPokemonDetail(response)
        assertEquals(expected, result)
    }
}