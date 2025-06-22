package com.asp.pokemon.domain.usecase

import com.asp.pokemon.domain.model.PokemonCollection
import com.asp.pokemon.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.asp.pokemon.domain.util.Result
import com.asp.pokemon.domain.util.safeExecuteWithLoading
import javax.inject.Inject

class GetPokemonList @Inject constructor(private val repository: PokemonRepository) {
    operator fun invoke(url: String): Flow<Result<PokemonCollection>> = flow {
        safeExecuteWithLoading<PokemonCollection> {
            repository.fetchPokemon(url)
        }
    }
}