package com.asp.pokemon.domain.usecase

import app.cash.turbine.test
import com.asp.pokemon.domain.model.PokemonDetail
import com.asp.pokemon.domain.repository.PokemonRepository
import com.asp.pokemon.domain.util.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetPokemonDetailTest {

    private lateinit var repository: PokemonRepository
    private lateinit var getPokemonDetail: GetPokemonDetail

    @Before
    fun setUp() {
        repository = mockk()
        getPokemonDetail = GetPokemonDetail(repository)
    }

    @Test
    fun `invoke emits success when repository returns detail`() = runTest {
        val detail = PokemonDetail(name = "pikachu", height = 0.4f, images = listOf())
        coEvery { repository.fetchPokemonDetail("pikachu") } returns detail

        getPokemonDetail("pikachu").test {
            val loading = awaitItem()
            val result = awaitItem()
            assert(loading is Result.Loading)
            assert(result is Result.Success && result.data == detail)
            awaitComplete()
        }
    }

    @Test
    fun `invoke emits error when repository throws exception`() = runTest {
        coEvery { repository.fetchPokemonDetail("pikachu") } throws RuntimeException("error")

        getPokemonDetail("pikachu").test {
            val loading = awaitItem()
            val result = awaitItem()
            assert(loading is Result.Loading)
            assert(result is Result.Error)
            awaitComplete()
        }
    }
}