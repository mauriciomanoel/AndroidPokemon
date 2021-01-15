package com.mauricio.pokemon.network

import android.content.Context
import android.net.Uri
import com.google.gson.GsonBuilder
import com.mauricio.pokemon.main.models.Constant.BASE_URL
import com.mauricio.pokemon.utils.SingletonHolder
import com.mauricio.pokemon.pokemon.models.Pokemon
import com.mauricio.pokemon.pokemondetail.models.PokemonDetailResponse
import com.mauricio.pokemon.pokemon.models.PokemonResponse
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitHelper
/**
 * Instantiates a new Retrofit helper.
 */ private constructor(private val context: Context) {
    
    private val sSingleton by lazy { getInstance(context) }
    
    /**
     * Gets api service.
     *
     * @return the api service
     */
    private fun getApiService(): RetrofitApiService {
        // Add interceptor to OkHttpClient
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        val httpLoggingInterceptorHeaders = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        httpLoggingInterceptorHeaders.level = HttpLoggingInterceptor.Level.HEADERS

        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.addInterceptor(httpLoggingInterceptor)
        httpClientBuilder.addNetworkInterceptor(httpLoggingInterceptorHeaders)
        val okHttpClient = httpClientBuilder
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()
        val gson = GsonBuilder()
                .setLenient()
                .create()
        val factory = GsonConverterFactory.create(gson)
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(factory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
        return retrofit.create(RetrofitApiService::class.java)
    }

    fun getPokemons(limite: Int, offset: Int): Observable<PokemonResponse> {
        return sSingleton.getApiService().getPokemons(limite, offset)
    }

    fun getDetailPokemon(code: Int): Observable<PokemonDetailResponse> {
        return sSingleton.getApiService().getDetailPokemon(code)
    }

    fun getFullPokemons(limite: Int, offset: Int): Observable<Pokemon> {

        return sSingleton.getApiService().getPokemons(limite, offset)
            .flatMap { result -> Observable.fromIterable(result.results) }
            .flatMap { result ->
                Observable.zip(
                    Observable.just(Pokemon(result.name, result.url)),
                    Observable.just(result.url)
                        .flatMap { url ->
                            val a = Uri.parse(url).lastPathSegment
                            sSingleton.getApiService().getDetailPokemon(Uri.parse(url).lastPathSegment!!.toInt())
                        }
                    ,
                    BiFunction<Pokemon, PokemonDetailResponse, Pokemon> { pokemon, detail ->
                        pokemon.detail = detail
                        pokemon
                    }
                )
            }
    }

    companion object: SingletonHolder<RetrofitHelper, Context>(::RetrofitHelper)
}