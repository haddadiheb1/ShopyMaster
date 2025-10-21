package com.hanouti.app.ui.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object Formatters {
    private val symbols = DecimalFormatSymbols(Locale.US).apply {
        groupingSeparator = ','
        decimalSeparator = '.'
    }
    private val amountDf = DecimalFormat("#,##0.00", symbols)

    fun amount(value: Double): String = amountDf.format(value)
}


