package com.asp.pokemon.data.mapper

import com.asp.pokemon.data.response.PokemonDetailResponse
import com.asp.pokemon.data.response.PokemonResponse
import com.asp.pokemon.domain.model.PokemonCollection
import com.asp.pokemon.domain.model.Pokemon
import com.asp.pokemon.domain.model.PokemonDetail
import javax.inject.Inject

class PokemonResponseMapper @Inject constructor() {
    fun mapToPokemonCollection(response: PokemonResponse): PokemonCollection {
        return PokemonCollection(
            count = response.count,
            next = response.next,
            previous = response.previous,
            results = response.pokemonList.map { response ->
                Pokemon(
                    name = response.name,
                    url = response.url
                )
            }
        )
    }

    fun mapToPokemonDetail(response: PokemonDetailResponse): PokemonDetail {
        return PokemonDetail(
            response.name ?: "",
            response.height ?: 0F,
            listOf(
                response.spirits?.backDefault ?: "",
                response.spirits?.backFemale ?: "",
                response.spirits?.backShiny ?: "",
                response.spirits?.backShinyFemale ?: "",
                response.spirits?.frontDefault ?: "",
                response.spirits?.frontFemale ?: "",
                response.spirits?.frontShiny ?: "",
                response.spirits?.frontShinyFemale ?: "",
            ).filter { it ->
                it.isNotBlank()
            }
        )
    }
}