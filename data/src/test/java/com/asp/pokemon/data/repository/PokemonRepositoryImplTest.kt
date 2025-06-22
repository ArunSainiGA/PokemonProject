package com.asp.pokemon.data.repository

import com.asp.pokemon.data.mapper.PokemonResponseMapper
import com.asp.pokemon.data.response.PokemonDetailResponse
import com.asp.pokemon.data.response.PokemonResponse
import com.asp.pokemon.data.service.PokemonService
import com.asp.pokemon.domain.model.PokemonCollection
import com.asp.pokemon.domain.model.PokemonDetail
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonRepositoryImplTest {

    private lateinit var service: PokemonService
    private lateinit var mapper: PokemonResponseMapper
    private lateinit var repository: PokemonRepositoryImpl

    @Before
    fun setUp() {
        service = mockk()
        mapper = mockk()
        repository = PokemonRepositoryImpl(service, mapper)
    }

    @Test
    fun `fetchPokemon returns mapped collection`() = runTest {
        val url = "url"
        val response = PokemonResponse(
            count = 0,
            next = null,
            previous = null,
            pokemonList = listOf()
        )
        val collection = PokemonCollection(
            0, null, null, emptyList()
        )

        coEvery { service.fetchPokemon(url) } returns response
        coEvery { mapper.mapToPokemonCollection(response) } returns collection

        val result = repository.fetchPokemon(url)

        assert(result == collection)
        coVerify { service.fetchPokemon(url) }
        verify { mapper.mapToPokemonCollection(response) }
    }

    @Test
    fun `fetchPokemonDetail returns mapped detail`() = runTest {
        val name = "pikachu"
        val response = PokemonDetailResponse(0.0F, "Pika")
        val detail = PokemonDetail(name, 0.4f, listOf())

        coEvery { service.fetchPokemonDetail(name) } returns response
        coEvery { mapper.mapToPokemonDetail(response) } returns detail

        val result = repository.fetchPokemonDetail(name)

        assert(result == detail)
        coVerify { service.fetchPokemonDetail(name) }
        verify { mapper.mapToPokemonDetail(response) }
    }
}