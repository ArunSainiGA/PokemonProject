package com.asp.pokemon.data.repository

import com.asp.pokemon.data.mapper.PokemonResponseMapper
import com.asp.pokemon.data.service.PokemonService
import com.asp.pokemon.domain.model.PokemonCollection
import com.asp.pokemon.domain.model.PokemonDetail
import com.asp.pokemon.domain.repository.PokemonRepository
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val service: PokemonService,
    private val responseMapper: PokemonResponseMapper
): PokemonRepository {

    override suspend fun fetchPokemon(url: String): PokemonCollection {
        return service.fetchPokemon(url).let { response ->
            responseMapper.mapToPokemonCollection(response)
        }
    }

    override suspend fun fetchPokemonDetail(name: String): PokemonDetail {
        return service.fetchPokemonDetail(name).let { response ->
            responseMapper.mapToPokemonDetail(response)
        }
    }

}