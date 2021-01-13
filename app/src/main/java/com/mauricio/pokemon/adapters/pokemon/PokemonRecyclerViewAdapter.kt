package com.mauricio.pokemon.adapters.pokemon

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mauricio.pokemon.BR
import com.mauricio.pokemon.R
import com.mauricio.pokemon.databinding.ItemListPokemonBinding
import com.mauricio.pokemon.databinding.ProgressLoadingBinding
import com.mauricio.pokemon.models.Constant
import com.mauricio.pokemon.models.interfaces.IOnClickEvent
import com.mauricio.pokemon.models.pokemon.Pokemon

class PokemonRecyclerViewAdapter(private val itensPokemon: ArrayList<Pokemon?>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var callback: IOnClickEvent

    fun addLoadingView() {
        //Add loading item
        Handler(Looper.getMainLooper()).post {
            itensPokemon.add(null)
            notifyItemInserted(itensPokemon.size - 1)
        }
    }

    fun removeLoadingView() {
        //Remove loading item
        if (itensPokemon.size != 0) {
            itensPokemon.removeAt(itensPokemon.size - 1)
            notifyItemRemoved(itensPokemon.size)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        return if (i == Constant.VIEW_TYPE_ITEM) {
            val binding = DataBindingUtil.inflate<ItemListPokemonBinding>(
                LayoutInflater.from(viewGroup.context),
                R.layout.item_list_pokemon,
                viewGroup,
                false
            )
            ViewHolder(binding)
        } else {
            val binding = DataBindingUtil.inflate<ProgressLoadingBinding>(
                LayoutInflater.from(viewGroup.context),
                R.layout.progress_loading,
                viewGroup,
                false
            )
            LoadingViewHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (itensPokemon[position] == null) {
            Constant.VIEW_TYPE_LOADING
        } else {
            Constant.VIEW_TYPE_ITEM
        }
    }

    override fun getItemCount(): Int {
        return itensPokemon.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == Constant.VIEW_TYPE_ITEM) {
            val viewHolder = holder as ViewHolder
            itensPokemon[position]?.let { pokemon ->
                Log.v(TAG, "${pokemon.getId()} - ${pokemon.detail.toString()}")
                viewHolder.binding.linearLayout.setOnClickListener {
                    callback.onClickDetailPokemon(pokemon)
                }
                pokemon.detail?.let { detail ->
                    val adapter = PokemonTypeRecyclerViewAdapter(ArrayList(detail.types))
                    viewHolder.bind(pokemon, adapter)
                } ?: run {
                    viewHolder.bind(pokemon)
                }
            }
        } else {
            val viewHolder = holder as LoadingViewHolder
            viewHolder.binding.executePendingBindings()
        }
    }

    inner class ViewHolder(var binding: ItemListPokemonBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pokemon: Any?, adapter: Any?) {
            binding.setVariable(BR.typeAdapter, adapter)
            binding.setVariable(BR.pokemon, pokemon)
            binding.executePendingBindings()
        }
        fun bind(pokemon: Any?) {
            binding.setVariable(BR.pokemon, pokemon)
            binding.executePendingBindings()
        }
    }

    inner class LoadingViewHolder(var binding: ProgressLoadingBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        val TAG = PokemonRecyclerViewAdapter::class.java.name
    }
}