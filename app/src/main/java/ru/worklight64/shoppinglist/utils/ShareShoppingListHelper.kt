package ru.worklight64.shoppinglist.utils

import android.content.Intent
import ru.worklight64.shoppinglist.entities.ShoppingListItem

object ShareShoppingListHelper {
    private fun makeShareText(shopList: List<ShoppingListItem>, shopListName: String): String {
        var sBuilder = StringBuilder()
        sBuilder.append("-- $shopListName --\n")
        var counter = 1
        shopList.forEach{
            if (it.itemInfo.isNotEmpty()) sBuilder.append("$counter - ${it.name} [${it.itemInfo}]\n")
            else sBuilder.append("$counter - ${it.name}\n")
            counter++
        }
        return sBuilder.toString()
    }

    fun share(shopList: List<ShoppingListItem>, shopListName: String): Intent{
        var intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.apply {
            putExtra(Intent.EXTRA_TEXT, makeShareText(shopList, shopListName))
        }
        return intent
    }
}