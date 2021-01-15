package com.mauricio.pokemon.pokemon.repository

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.gson.GsonBuilder
import com.mauricio.pokemon.main.models.Constant.BASE_URL
import com.mauricio.pokemon.network.RetrofitApiService
import com.mauricio.pokemon.utils.Queue
import com.mauricio.pokemon.pokemon.models.Pokemon
import com.mauricio.pokemon.pokemondetail.models.PokemonDetailResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class PokemonTestRepository @Inject constructor(private val application: Application, private val retrofitApiService: RetrofitApiService)  {
//    class PokemonRepository @Inject constructor(private val context: Context) {

    private val disposable = CompositeDisposable()
    private val queue = Queue(mutableListOf<Pokemon>())

//    @VisibleForTesting
//    constructor(application: Application, test: Boolean): this(application)  {
//        this.application = application
//    }

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

    fun getPokemons(
        limit: Int,
        offset: Int,
        process: (value: ArrayList<Pokemon>?, e: Throwable?) -> Unit,
        processFullPokemon: (value: Pokemon) -> Unit
    ) {
        val values:ArrayList<Pokemon> = ArrayList<Pokemon>()

        getApiService().getPokemons(limit, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()) // Thread that observer will execute
            .subscribe({ pokemonResponse ->
                for (result in pokemonResponse.results) {
                    val pokemon = Pokemon(result.name, result.url)
                    values.add(pokemon)
                    queue.enqueue(pokemon)
                }
                process(values, null)
                callProcessQueue(processFullPokemon)
            }) { e: Throwable? -> process(null, e) }
    }

    fun callProcessQueue(process: (value: Pokemon) -> Unit) {
        while (!queue.isEmpty()) {
            val pokemon = queue.dequeue()
            retrofitApiService.getDetailPokemon(pokemon?.getId()!!)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread()) // Thread that observer will execute
                .subscribe({
                    pokemon.detail = it
                    process(pokemon)
                })
        }
    }

    fun getDetailPokemon(values: ArrayList<Pokemon>?, process: (value: Pokemon) -> Unit) {
        values?.let { pokemons ->
            Log.v(TAG, "${pokemons.toString()}")
           val a = Observable.fromIterable(pokemons)
            a.subscribeOn(Schedulers.single())
                .subscribeOn(AndroidSchedulers.mainThread())
                .concatMap { pokemon ->
                    Observable.zip(
                        Observable.just(pokemon),
                        retrofitApiService.getDetailPokemon(pokemon.getId()!!),
                        { pokemon, detail ->
                            pokemon.detail = detail
                            Log.v(TAG, "${pokemon.getId()}")

                            pokemon
                        }
                    )
                }.subscribe({it ->
                    it.apply(process)
                }, {})
            disposable.add(a.subscribe())
        }
    }

    fun getDetailPokemon(id: Int, process: (value: PokemonDetailResponse?, e: Throwable?) -> Unit) {
        retrofitApiService.getDetailPokemon(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()) // Thread that observer will execute
            .subscribe({ detail ->
                process(detail, null)
            }, { e-> process(null, e)})
    }

    fun getDetailPokemon(id: Int): PokemonDetailResponse? {
        var detail: PokemonDetailResponse? = null
        retrofitApiService.getDetailPokemon(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()) // Thread that observer will execute
            .subscribe({
                detail = it
            })
        return detail
    }


    fun getFullPokemons(
        limit: Int,
        offset: Int,
        process: (value: ArrayList<Pokemon>?, e: Throwable?) -> Unit
    ) {
        val pokemons = ArrayList<Pokemon>()
        retrofitApiService.getPokemons(limit, offset)
            .flatMap { result -> Observable.fromIterable(result.results) }
            .flatMap { result ->
                Observable.zip(
                    Observable.just(Pokemon(result.name, result.url)),
                    Observable.just(result.url)
                        .flatMap { url ->
                            val a = Uri.parse(url).lastPathSegment
                            retrofitApiService.getDetailPokemon(Uri.parse(url).lastPathSegment!!.toInt())
                        }
                    ,
                    { pokemon, detail ->
                        pokemon.detail = detail
                        pokemon
                    }
                )
            }
            .subscribe({ pokemon ->
                pokemons.add(pokemon)
            }, { e ->
                process(null, e)
            }, { process(pokemons, null) })
    }

//    fun getFullPokemons(
//        limit: Int,
//        offset: Int,
//        process: (value: ArrayList<Pokemon>?, e: Throwable?) -> Unit
//    ) {
//        val pokemons = ArrayList<Pokemon>()
//        retrofitApiService.getFullPokemons(limit, offset)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread()) // Thread that observer will execute
//            .subscribe({ pokemon ->
//                pokemons.add(pokemon)
//            }, { e ->
//                process(null, e)
//            }, { process(pokemons, null) })
//    }

    companion object {
        val TAG: String = PokemonTestRepository::class.java.simpleName
    }
}