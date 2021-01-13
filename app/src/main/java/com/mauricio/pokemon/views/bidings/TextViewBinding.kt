package com.mauricio.pokemon.views.bidings

import android.util.TypedValue
import android.widget.TextView
import androidx.databinding.BindingAdapter

object TextViewBinding {

    @JvmStatic
    @BindingAdapter("android:textSize")
    fun bindTextSize(textView: TextView, size: Int) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size.toFloat())
    }
}