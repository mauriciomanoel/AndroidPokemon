package com.mauricio.pokemon.network

import com.mauricio.pokemon.models.pokemon.PokemonDetailResponse
import com.mauricio.pokemon.models.pokemon.PokemonResponse
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

interface RetrofitApiService {

    @GET("pokemon")
    fun getPokemons(@Query("limit") limit: Int,
                    @Query("offset") offset: Int): Observable<PokemonResponse>

    @GET("pokemon/{code}")
    fun getDetailPokemon(@Path("code") code: Int) : Observable<PokemonDetailResponse>

}