package com.mauricio.pokemon.pokemondetail.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mauricio.pokemon.BR
import com.mauricio.pokemon.R
import com.mauricio.pokemon.databinding.ItemTypePokemonBinding
import com.mauricio.pokemon.pokemondetail.models.Type
import com.mauricio.pokemon.pokemon.models.TypePokemonEnum

class PokemonTypeRecyclerViewAdapter(private val itemTypes: ArrayList<Type>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemTypePokemonBinding>(
            LayoutInflater.from(viewGroup.context),
            R.layout.item_type_pokemon,
            viewGroup,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemTypes.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ViewHolder
        itemTypes[position]?.let { type ->
            val typeEnum = TypePokemonEnum.getByCode(type.type.name)
            val image = "ic_badge_${type.type.name}"

            val drawable = GradientDrawable()
            drawable.cornerRadius = 20f
            drawable.setColor(Color.parseColor(typeEnum.color))

            viewHolder.bind(typeEnum.title, image, drawable)
        }

    }

    inner class ViewHolder(var binding: ItemTypePokemonBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(name: Any?, icon: Any?, drawable: Any?) {
            binding.setVariable(BR.name, name)
            binding.setVariable(BR.icon, icon)
            binding.setVariable(BR.drawable, drawable)
            binding.executePendingBindings()
        }
    }
    companion object {
        val TAG = PokemonTypeRecyclerViewAdapter::class.java.name
    }
}

