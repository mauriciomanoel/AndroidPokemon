package com.mauricio.pokemon.pokemon.repository

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import com.mauricio.pokemon.network.RetrofitApiService
import com.mauricio.pokemon.utils.Queue
import com.mauricio.pokemon.pokemon.models.Pokemon
import com.mauricio.pokemon.pokemondetail.models.PokemonDetailResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class PokemonRepository @Inject constructor(private val apiService: RetrofitApiService)  {

    private val disposable = CompositeDisposable()
    private val queue = Queue(mutableListOf<Pokemon>())

    fun getPokemons(
        limit: Int,
        offset: Int,
        process: (value: ArrayList<Pokemon>?, e: Throwable?) -> Unit,
        processFullPokemon: (value: Pokemon) -> Unit
    ) {
        val values = ArrayList< Pokemon>()
        apiService.getPokemons(limit, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()) // Thread that observer will execute
            .subscribe({ pokemonResponse ->
                for (result in pokemonResponse.results) {
                    val pokemon = Pokemon(result.name, result.url)
                    values.add(pokemon)
                    queue.enqueue(pokemon)
                }
            }, {e: Throwable? -> process(null, e)}, {
                process(values, null)
                callProcessQueue(processFullPokemon)
            })
    }

    fun callProcessQueue(process: (value: Pokemon) -> Unit) {
        while (!queue.isEmpty()) {
            val pokemon = queue.dequeue()
            pokemon?.getId()?.let { id ->
                apiService.getDetailPokemon(id)
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread()) // Thread that observer will execute
                    .subscribe({
                        Log.v(TAG, it.id.toString())
                        pokemon.detail = it
                        process(pokemon)
                    }, {
                        e -> Log.v(TAG, e.message)
                    })
            }

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
                        apiService.getDetailPokemon(pokemon.getId()!!),
                        BiFunction<Pokemon, PokemonDetailResponse, Pokemon> { pokemon, detail ->
                            // here we get both the results at a time.
                            return@BiFunction pokemon
                        }
                    )
                }.subscribe({ it ->
                    it.apply(process)
                }, {})
            disposable.add(a.subscribe())
        }
    }

    fun getDetailPokemon(id: Int, process: (value: PokemonDetailResponse?, e: Throwable?) -> Unit) {
        apiService.getDetailPokemon(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()) // Thread that observer will execute
            .subscribe({ detail ->
                process(detail, null)
            }, { e-> process(null, e)})
    }

    fun getDetailPokemon(id: Int): PokemonDetailResponse? {
        var detail: PokemonDetailResponse? = null
        apiService.getDetailPokemon(id)
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
        apiService.getPokemons(limit, offset)
            .flatMap { result -> Observable.fromIterable(result.results) }
            .flatMap { result ->
                Observable.zip(
                    Observable.just(Pokemon(result.name, result.url)),
                    Observable.just(result.url)
                        .flatMap { url ->
                            val a = Uri.parse(url).lastPathSegment
                            apiService.getDetailPokemon(Uri.parse(url).lastPathSegment!!.toInt())
                        }
                    ,
                    BiFunction<Pokemon, PokemonDetailResponse, Pokemon> { pokemon, detail ->
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

    companion object {
        val TAG: String = PokemonRepository::class.java.simpleName
    }
}