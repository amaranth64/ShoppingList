package ru.worklight64.shoppinglist.activities

import android.app.Application
import ru.worklight64.shoppinglist.db.MainDataBase

class MainApp: Application(){
    val database by lazy { MainDataBase.getDataBase(this)}
}