package com.mauricio.pokemon.utils

import kotlin.math.roundToInt

object ConverterUtils {

    @JvmStatic
    fun meterToFeetFormatted(value: Float): String {
        val ft = value.times(3.28)
        val doubleAsString = ft.toString()
        val indexOfDecimal = doubleAsString.indexOf(".")
        val decimalPart = doubleAsString.substring(indexOfDecimal).toFloat()
        val valueIn = String.format("%02d", decimalPart.times(12).roundToInt())
        return "${ft.toInt()}′${valueIn}″"
    }

    fun kilogramsToPounds(value: Float): String {
        val lbs = value.times(2.20462262)
        val valueIn = String.format("%.1f", lbs)
        return "${valueIn} lbs"
    }
}