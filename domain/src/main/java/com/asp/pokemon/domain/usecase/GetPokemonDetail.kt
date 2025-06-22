package com.asp.pokemon.domain.usecase

import com.asp.pokemon.domain.model.PokemonDetail
import com.asp.pokemon.domain.repository.PokemonRepository
import com.asp.pokemon.domain.util.Result
import com.asp.pokemon.domain.util.safeExecuteWithLoading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPokemonDetail @Inject constructor(private val repository: PokemonRepository) {
    operator fun invoke(name: String): Flow<Result<PokemonDetail>> = flow {
        safeExecuteWithLoading<PokemonDetail> {
            repository.fetchPokemonDetail(name)
        }
    }
}