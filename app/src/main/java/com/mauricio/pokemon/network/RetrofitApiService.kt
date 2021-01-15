package com.mauricio.pokemon.network

import com.mauricio.pokemon.pokemondetail.models.PokemonDetailResponse
import com.mauricio.pokemon.pokemon.models.PokemonResponse
import io.reactivex.Observable
import retrofit2.http.*

interface RetrofitApiService {

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("pokemon")
    fun getPokemons(@Query("limit") limit: Int,
                    @Query("offset") offset: Int): Observable<PokemonResponse>

    @GET("pokemon/{code}")
    fun getDetailPokemon(@Path("code") code: Int) : Observable<PokemonDetailResponse>

}