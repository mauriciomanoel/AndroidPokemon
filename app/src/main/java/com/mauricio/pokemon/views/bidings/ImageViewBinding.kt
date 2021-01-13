package com.mauricio.pokemon.views.bidings

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

object ImageViewBinding {

    @JvmStatic
    @BindingAdapter("src_url", "image_width", "image_height")
    fun loadFromURL(image: ImageView, source: String?, image_width:Int, image_height:Int) {
        if (source == null) return
        Glide.with(image.context)
            .load(source)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .override(image_width, image_height)
            .into(image)
    }

    @JvmStatic
    @BindingAdapter("image_resource")
    fun loadImageFromSource(image: ImageView, source: java.lang.String) {
        val id: Int = image.context.resources.getIdentifier(
                image.context.packageName + ":drawable/" + source,
                null,
                null
        )
        image.setImageResource(id)
    }
}