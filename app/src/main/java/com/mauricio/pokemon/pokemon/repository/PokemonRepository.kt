package com.mauricio.pokemon.pokemon.repository

import android.content.Context
import android.util.Log
import com.mauricio.pokemon.utils.Queue
import com.mauricio.pokemon.pokemon.models.Pokemon
import com.mauricio.pokemon.pokemondetail.models.PokemonDetailResponse
import com.mauricio.pokemon.network.RetrofitHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class PokemonRepository private constructor(private val context: Context)  {

    private val disposable = CompositeDisposable()
    private val queue = Queue(mutableListOf<Pokemon>())

    fun getPokemons(
        limit: Int,
        offset: Int,
        process: (value: ArrayList<Pokemon>?, e: Throwable?) -> Unit,
        processFullPokemon: (value: Pokemon) -> Unit
    ) {
        val pokemons = ArrayList<Pokemon>()

        RetrofitHelper.getInstance(context).getPokemons(limit, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()) // Thread that observer will execute
            .subscribe({ pokemonResponse ->
                for (result in pokemonResponse.results) {
                    val pokemon = Pokemon(result.name, result.url)
                    pokemons.add(pokemon)
                    queue.enqueue(pokemon)
                }
                process(pokemons, null)
                callProcessQueue(processFullPokemon)
            }) { e: Throwable? -> process(null, e) }
    }

    fun callProcessQueue(process: (value: Pokemon) -> Unit) {
        while (!queue.isEmpty()) {
            val pokemon = queue.dequeue()
            RetrofitHelper.getInstance(context).getDetailPokemon(pokemon?.getId()!!)
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
                        RetrofitHelper.getInstance(context).getDetailPokemon(pokemon.getId()!!),
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
        RetrofitHelper.getInstance(context).getDetailPokemon(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()) // Thread that observer will execute
            .subscribe({ detail ->
                process(detail, null)
            }, { e-> process(null, e)})
    }

    fun getDetailPokemon(id: Int): PokemonDetailResponse? {
        var detail: PokemonDetailResponse? = null
        RetrofitHelper.getInstance(context).getDetailPokemon(id)
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
        RetrofitHelper.getInstance(context).getFullPokemons(limit, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()) // Thread that observer will execute
            .subscribe({ pokemon ->
                pokemons.add(pokemon)
            }, { e ->
                process(null, e)
            }, { process(pokemons, null) })
    }

    companion object {
        val TAG: String = PokemonRepository::class.java.simpleName
        @Volatile
        private lateinit var sInstance: PokemonRepository
        /**
         * Gets instance.
         *
         * @return the instance
         */
        fun getInstance(context: Context): PokemonRepository {
            synchronized(this) {
                if (!Companion::sInstance.isInitialized) {
                    sInstance = PokemonRepository(context)
                }
                return sInstance
            }
        }
    }
}