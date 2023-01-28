package ru.worklight64.shoppinglist.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeManager {
    fun getData():String{
        val formatter = SimpleDateFormat("HH:mm:ss - dd/MM/yy", Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }
}