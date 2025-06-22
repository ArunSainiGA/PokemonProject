package com.asp.pokemon.domain.repository

import com.asp.pokemon.domain.model.PokemonCollection
import com.asp.pokemon.domain.model.PokemonDetail

interface PokemonRepository {
    suspend fun fetchPokemon(url: String): PokemonCollection
    suspend fun fetchPokemonDetail(name: String): PokemonDetail
}