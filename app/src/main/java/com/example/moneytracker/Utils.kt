package com.example.moneytracker

import java.text.SimpleDateFormat
import java.util.Locale

object Utils {
    fun formatDateToReadableForm(dateInMillis:Long): String{
        val dateFormatter = SimpleDateFormat("dd-MM-YYYY", Locale.getDefault())
        return dateFormatter.format(dateInMillis)
    }

}