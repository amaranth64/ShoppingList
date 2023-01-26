package ru.worklight64.shoppinglist.utils

import android.text.Html
import android.text.Spanned

object HtmlManager {
    fun getFromHtml(text:String):Spanned{
        return if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.N){
            Html.fromHtml(text)
        } else {
            Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
        }
    }

    fun toHtml(spanText: Spanned):String{
        return if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.N){
            Html.toHtml(spanText)
        } else {
            Html.toHtml(spanText, Html.FROM_HTML_MODE_COMPACT)
        }
    }

}