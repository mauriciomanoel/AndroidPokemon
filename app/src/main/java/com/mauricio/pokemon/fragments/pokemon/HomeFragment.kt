package com.mauricio.pokemon.fragments.pokemon

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mauricio.pokemon.R
import com.mauricio.pokemon.databinding.FragmentHomeBinding
import com.mauricio.pokemon.adapters.pokemon.PokemonRecyclerViewAdapter
import com.mauricio.pokemon.models.views.RecyclerViewLoadMoreScroll
import com.mauricio.pokemon.models.interfaces.IOnClickEvent
import com.mauricio.pokemon.models.interfaces.OnLoadMoreListener
import com.mauricio.pokemon.models.pokemon.Pokemon
import com.mauricio.pokemon.viewmodel.pokemon.PokemonViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var pokemonAdapter: PokemonRecyclerViewAdapter
    private val viewModel: PokemonViewModel by lazy { ViewModelProviders.of(this).get(PokemonViewModel::class.java) }
    private lateinit var recyclerViewLoadMoreScroll: RecyclerViewLoadMoreScroll
    private val listPokemon: ArrayList<Pokemon?> = ArrayList()
    private lateinit var callback: IOnClickEvent

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as IOnClickEvent
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = this

        initPokemonAdapter()
        initScrollListener()
        initObservers()
        viewModel.getPokemons()

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun initPokemonAdapter() {
        pokemonAdapter = PokemonRecyclerViewAdapter(listPokemon)
        pokemonAdapter.callback = callback
        binding.pokemonAdapter = pokemonAdapter
    }

    private  fun initScrollListener() {
        val mLayoutManager = LinearLayoutManager(activity)
        recyclerViewLoadMoreScroll = RecyclerViewLoadMoreScroll(mLayoutManager)
        recyclerViewLoadMoreScroll.setOnLoadMoreListener(object: OnLoadMoreListener{
            override fun onLoadMore() {
                loadMoreData()
            }
        })
        binding.pokemonRecyclerView.addOnScrollListener(recyclerViewLoadMoreScroll)
    }

    override fun onDestroy() {
        super.onDestroy()
        unSubscribeObservers()
    }

    private fun initObservers() {
        viewModel.pokemons.observe(this, Observer { pokemons ->
            listPokemon.addAll(pokemons)
            pokemonAdapter.notifyDataSetChanged()
        })

        viewModel.morePokemons.observe(this, Observer { morePokemons ->
            //Remove the Loading View
            pokemonAdapter.removeLoadingView()
            //We adding the data to our main ArrayList
            listPokemon.addAll(morePokemons)
            pokemonAdapter.notifyDataSetChanged()
            //Change the boolean isLoading to false
            recyclerViewLoadMoreScroll.setLoaded()
            //Update the recyclerView in the main thread
            binding.pokemonRecyclerView.post {
                pokemonAdapter.notifyDataSetChanged()
            }
        })

        viewModel.fullPokemon.observe(this, Observer { fullPokemon ->
            val position = listPokemon.indexOf(fullPokemon)
            Log.v(TAG, "${fullPokemon.getId()} - ${fullPokemon.detail.toString()}")
            if (position != -1) {
                listPokemon[position] = fullPokemon
                pokemonAdapter.notifyItemChanged(position)
            }
        })
    }

    private fun unSubscribeObservers() {
        if (viewModel.pokemons.hasObservers()) {
            viewModel.pokemons.removeObservers(this)
        }
        if (viewModel.morePokemons.hasObservers()) {
            viewModel.morePokemons.removeObservers(this)
        }
    }

    private fun loadMoreData() {
        //Add the Loading View
        pokemonAdapter.addLoadingView()
        viewModel.getMorePokemons()
    }

    companion object {
        val TAG = HomeFragment::class.java.name
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}