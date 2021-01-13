package com.mauricio.pokemon.views.bidings

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

object LayoutManagerBiding {

    @BindingAdapter("layout_manager")
    @JvmStatic
    fun setLayoutManager(
        recyclerView: RecyclerView, layout: String) {
        setLayoutManager(
            recyclerView,
            layout,
            1
        )
    }

    @BindingAdapter("layout_manager", "columns")
    @JvmStatic
    fun setLayoutManager(
            recyclerView: RecyclerView,
            layout: String, columns: Int) {

        if ("linear_horizontal".equals(layout, ignoreCase = true)) {
            recyclerView.layoutManager = LinearLayoutManager(
                    recyclerView.context,
                    LinearLayoutManager.HORIZONTAL, false)
        } else if ("linear_vertical".equals(layout, ignoreCase = true)) {
            recyclerView.layoutManager = LinearLayoutManager(
                    recyclerView.context,
                    LinearLayoutManager.VERTICAL, false)
        } else if ("grid".equals(layout, ignoreCase = true)) {
            recyclerView.layoutManager = GridLayoutManager(
                    recyclerView.context, columns)
        }
    }
}
