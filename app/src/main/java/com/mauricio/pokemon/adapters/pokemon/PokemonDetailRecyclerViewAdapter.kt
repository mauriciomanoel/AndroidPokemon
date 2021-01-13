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
import com.mauricio.pokemon.databinding.ItemDetailBaseStatsBinding
import com.mauricio.pokemon.databinding.ItemListPokemonBinding
import com.mauricio.pokemon.databinding.ProgressLoadingBinding
import com.mauricio.pokemon.models.Constant
import com.mauricio.pokemon.models.interfaces.IOnClickEvent
import com.mauricio.pokemon.models.pokemon.Pokemon
import com.mauricio.pokemon.models.pokemon.Stat

class PokemonDetailRecyclerViewAdapter(private val itemStats: ArrayList<Stat?>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var callback: IOnClickEvent
    private var count = 0

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemDetailBaseStatsBinding>(
            LayoutInflater.from(viewGroup.context),
            R.layout.item_detail_base_stats,
            viewGroup,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemStats.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ViewHolder
        if (position < itemCount - 1) {
            itemStats[position]?.let { stat ->
                val progress = stat.baseStat ?: 0
                viewHolder.bind(stat.stat.getNameFormatted(), stat.baseStat.toString(), progress, false)
                count += stat.baseStat
            }
        } else {
            viewHolder.bind("Total", count.toString(), 0, true)
        }
    }

    inner class ViewHolder(var binding: ItemDetailBaseStatsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(name: Any?, baseStat: Any?, progress: Any?, isFinal: Any?) {
            binding.setVariable(BR.name, name)
            binding.setVariable(BR.baseStat, baseStat)
            binding.setVariable(BR.progress, progress)
            binding.setVariable(BR.isFinal, isFinal)
            binding.executePendingBindings()
        }
    }

    companion object {
        val TAG = PokemonDetailRecyclerViewAdapter::class.java.name
    }
}

