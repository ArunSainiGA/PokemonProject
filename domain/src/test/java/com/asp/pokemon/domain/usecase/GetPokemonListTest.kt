package com.asp.pokemon.domain.usecase

import app.cash.turbine.test
import com.asp.pokemon.domain.model.PokemonCollection
import com.asp.pokemon.domain.repository.PokemonRepository
import com.asp.pokemon.domain.util.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetPokemonListTest {
    private lateinit var repository: PokemonRepository
    private lateinit var getPokemonList: GetPokemonList

    @Before
    fun setUp() {
        repository = mockk()
        getPokemonList = GetPokemonList(repository)
    }

    @Test
    fun `invoke emits success when repository returns list`() = runTest {
        val collection = PokemonCollection(
            0, null, null, emptyList()
        )
        coEvery { repository.fetchPokemon("url") } returns collection

        getPokemonList("url").test {
            val loading = awaitItem()
            assert(loading is Result.Loading)
            val success = awaitItem()
            assert(success is Result.Success && success.data == collection)
            awaitComplete()
        }
    }

    @Test
    fun `invoke emits error when repository throws exception`() = runTest {
        coEvery { repository.fetchPokemon("url") } throws RuntimeException("error")

        getPokemonList("url").test {
            val loading = awaitItem()
            assert(loading is Result.Loading)
            val error = awaitItem()
            assert(error is Result.Error)
            awaitComplete()
        }
    }
}