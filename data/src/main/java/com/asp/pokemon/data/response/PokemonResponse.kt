package com.asp.pokemon.data.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonResponse(
    val count: Int = 0,
    val next: String?,
    val previous: String?,
    @Json(name = "results") val pokemonList: List<PokemonItemResponse>
)

@JsonClass(generateAdapter = true)
data class PokemonItemResponse(
    @Json(name = "name") val name: String,
    @Json(name = "url") val url: String
)