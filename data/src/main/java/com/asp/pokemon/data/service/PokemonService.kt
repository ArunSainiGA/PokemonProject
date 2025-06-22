package com.asp.pokemon.data.service

import com.asp.pokemon.data.response.PokemonDetailResponse
import com.asp.pokemon.data.response.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface PokemonService {
    @GET("")
    suspend fun fetchPokemon(@Url url: String): PokemonResponse

    @GET("pokemon/{name}")
    suspend fun fetchPokemonDetail(@Path("name") name: String): PokemonDetailResponse
}